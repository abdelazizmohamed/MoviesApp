package com.example.hp_lap.popmovies.activitiesanddatabase;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.hp_lap.popmovies.adapters.MovieAdapterP;
import com.example.hp_lap.popmovies.adapters.MovieAdapterT;
import com.example.hp_lap.popmovies.GetApi;
import com.example.hp_lap.popmovies.models.MovieModel;
import com.example.hp_lap.popmovies.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    int Select;
    GetApi moviesAPI;
    GetApi API;

    List<MovieModel> listT;
    List<MovieModel> listP;

    private RecyclerView recyclerViewT, recyclerViewP;
    private MovieAdapterT movieAdapterT;
    private MovieAdapterP movieAdapterP;

    Parcelable parcelableT, parcelableP;

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_t);

        recyclerViewT = findViewById(R.id.recyclerViewT);
        recyclerViewP = findViewById(R.id.recyclerViewT);
        recyclerViewT.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        recyclerViewP.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

        controller = new Controller(MainActivity.this);

        if (savedInstanceState == null) {
            listT = new ArrayList<>();
            listP = new ArrayList<>();
            Select = 1;
            openCallP();

        } else {

            Select = (int) savedInstanceState.getSerializable("select");
            if (Select == 2) {

                listT = (List<MovieModel>) savedInstanceState.getSerializable("list");
                movieAdapterT = new MovieAdapterT(listT, MainActivity.this);
                recyclerViewT.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                recyclerViewT.setAdapter(movieAdapterT);
                movieAdapterT.notifyDataSetChanged();
                if (parcelableT != null) {
                    recyclerViewT.getLayoutManager().onRestoreInstanceState(parcelableT);
                }
            } else if (Select == 1) {

                listP = (List<MovieModel>) savedInstanceState.getSerializable("listP");
                movieAdapterP = new MovieAdapterP(listP, MainActivity.this);
                recyclerViewP.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                recyclerViewP.setAdapter(movieAdapterP);
                movieAdapterP.notifyDataSetChanged();
                if (parcelableP != null) {
                    recyclerViewP.getLayoutManager().onRestoreInstanceState(parcelableP);
                }
            } else if (Select == 3) {
                if (controller.getFavouriteMovie() != null && controller.getFavouriteMovie().size() > 0) {
                    if (listT != null) {
                        listT.clear();
                        listT.addAll(controller.getFavouriteMovie());
                    }
                    if (movieAdapterT == null) {
                        movieAdapterT = new MovieAdapterT(controller.getFavouriteMovie(), MainActivity.this);
                        recyclerViewT.setAdapter(movieAdapterT);
                    } else {
                        movieAdapterT.notifyDataSetChanged();
                        recyclerViewT.setAdapter(movieAdapterT);
                    }
                    // recyclerViewT.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                }
            }
        }
    }

    private void openCallP() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetApi.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API = retrofit.create(GetApi.class);
        Call<MovieModel> PopularReCall = API.getApiPopular();
        PopularReCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                listP = response.body().getResults();
                movieAdapterP = new MovieAdapterP(listP, MainActivity.this);
                recyclerViewP.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                recyclerViewP.setAdapter(movieAdapterP);
                movieAdapterP.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Snackbar.make(recyclerViewP,
                        " Check internet connection! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openCallT() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetApi.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        moviesAPI = retrofit.create(GetApi.class);
        Call<MovieModel> TopRateReCall = moviesAPI.getApiTopRated();
        TopRateReCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {

                listT = response.body().getResults();
                movieAdapterT = new MovieAdapterT(listT, MainActivity.this);
                recyclerViewT.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                recyclerViewT.setAdapter(movieAdapterT);
                movieAdapterT.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Snackbar.make(recyclerViewT,
                        " Check internet connection! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.Pouplar) {
            Select = 1;
            openCallP();
        }
        if (id == R.id.TopRate) {
            Select = 2;
            openCallT();
        }
        if (id == R.id.Favourit) {
            Select = 3;

            if (controller.getFavouriteMovie() != null && controller.getFavouriteMovie().size() > 0) {
                if (listT != null) {
                    listT.clear();
                    listT.addAll(controller.getFavouriteMovie());
                }
                if (movieAdapterT == null) {
                    movieAdapterT = new MovieAdapterT(controller.getFavouriteMovie(), MainActivity.this);
                    recyclerViewT.setAdapter(movieAdapterT);
                } else {
                    movieAdapterT.notifyDataSetChanged();
                    recyclerViewT.setAdapter(movieAdapterT);
                }

                // recyclerViewT.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

            } else {
                Snackbar.make(recyclerViewT, "Favorite is Empty!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    //save value on onSaveInstanceState
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("list", (Serializable) listT);
        outState.putSerializable("listP", (Serializable) listP);

        outState.putSerializable("select", Select);

        outState.putParcelable("positionT", recyclerViewT.getLayoutManager().onSaveInstanceState());
        outState.putParcelable("positionP", recyclerViewP.getLayoutManager().onSaveInstanceState());
    }

    //Restore them on onRestoreInstanceState
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        parcelableT = savedInstanceState.getParcelable("positionT");
        parcelableP = savedInstanceState.getParcelable("positionP");

    }
}

