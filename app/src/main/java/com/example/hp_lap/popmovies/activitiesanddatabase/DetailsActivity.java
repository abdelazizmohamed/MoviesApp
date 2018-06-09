package com.example.hp_lap.popmovies.activitiesanddatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp_lap.popmovies.adapters.ReviewsAdapter;
import com.example.hp_lap.popmovies.adapters.TrailerAdapter;
import com.example.hp_lap.popmovies.models.MovieModel;
import com.example.hp_lap.popmovies.models.ReviewsModel;
import com.example.hp_lap.popmovies.models.TrailerModel;
import com.example.hp_lap.popmovies.GetApi;
import com.example.hp_lap.popmovies.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {

    ImageView imageView;
    TextView title, date, rate, description;

    List<ReviewsModel.Review> reviewsList;
    private RecyclerView reviewRecycler;
    ReviewsAdapter reviewsAdapter;
    GetApi ReciewsAPI;
    int ID_R;

    List<TrailerModel> trailerList;
    private RecyclerView trailerRecycler;
    TrailerAdapter trailerAdapter;
    GetApi TrailerAPI;
    int ID_T;

    //MovieModel movie;
    MovieModel movie;
    TextView star;
    Controller controller;
    boolean inFavoriteList = false;

    NestedScrollView scrollView;
    Parcelable parcelableTrailer;
    Parcelable parcelableReview;
    int Star;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        movie = new MovieModel();
        controller = new Controller(DetailsActivity.this);
        scrollView = findViewById(R.id.DetailScroll);
        star = findViewById(R.id.star_fav);


        imageView = findViewById(R.id.Image_poster);
        rate = findViewById(R.id.Rate);
        title = findViewById(R.id.Header_title);
        date = findViewById(R.id.Release_date);
        description = findViewById(R.id.Description);

        title.setText(getIntent().getExtras().getString("title"));
        description.setText(getIntent().getExtras().getString("overview"));
        date.setText(getIntent().getExtras().getString("release_date"));
        rate.setText(String.valueOf(getIntent().getExtras().getDouble("vote_average")) + "/10");

        Picasso.with(DetailsActivity.this)
                .load("http://image.tmdb.org/t/p/w185/" + getIntent()
                        .getExtras()
                        .getString("poster_path"))
                .into(imageView);

        //  Start ReviewsModel code
        Intent intentR = getIntent();
        ID_R = intentR.getIntExtra("id", 0);
        reviewsAdapter = new ReviewsAdapter(DetailsActivity.this, reviewsList);
        reviewRecycler = findViewById(R.id.RecReview);
        reviewRecycler.setNestedScrollingEnabled(false);
        reviewRecycler.setLayoutManager(new LinearLayoutManager(this));
        openCallReviews();

        //  Start Trailer code
        ID_T = intentR.getIntExtra("id", 0);
        trailerAdapter = new TrailerAdapter(DetailsActivity.this, trailerList);
        trailerRecycler = findViewById(R.id.RecTrial);
        trailerRecycler.setNestedScrollingEnabled(false);
        trailerRecycler.setLayoutManager(new LinearLayoutManager(this));

        if (savedInstanceState == null) {
            trailerList = new ArrayList<>();
            reviewsList = new ArrayList<>();
        } else {

            trailerList = (List<TrailerModel>) savedInstanceState.getSerializable("trailerList");
            trailerRecycler.setAdapter(new TrailerAdapter(DetailsActivity.this, trailerList));
            trailerRecycler.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
            trailerAdapter.notifyDataSetChanged();
            if (parcelableTrailer != null) {
                trailerRecycler.getLayoutManager().onRestoreInstanceState(parcelableTrailer);
            }

            reviewsList = (List<ReviewsModel.Review>) savedInstanceState.getSerializable("reviewList");
            reviewRecycler.setAdapter(new ReviewsAdapter(DetailsActivity.this, reviewsList));
            reviewRecycler.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
            reviewsAdapter.notifyDataSetChanged();
            if (parcelableReview != null) {
                reviewRecycler.getLayoutManager().onRestoreInstanceState(parcelableReview);
            }
        }
        openCallTrailer();
        openCallReviews();
        if (controller.isFavorite(ID_R)) {
            star.setTextColor(Color.parseColor("#ffdc40"));
        } else {
            inFavoriteList = true;
            star.setTextColor(Color.parseColor("#ffffff"));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("trailer", trailerRecycler.getLayoutManager().onSaveInstanceState());
        outState.putParcelable("review", reviewRecycler.getLayoutManager().onSaveInstanceState());
        outState.putSerializable("trailerList", (Serializable) trailerList);
        outState.putSerializable("reviewList", (Serializable) reviewsList);


        outState.putIntArray("SCROLL_POSITION",
                new int[]{scrollView.getScrollX(), scrollView.getScrollY()});
    }

    //Restore them on onRestoreInstanceState
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            parcelableReview = (savedInstanceState).getParcelable("review");
            parcelableTrailer = (savedInstanceState).getParcelable("trailer");
        }
        int[] position = savedInstanceState.getIntArray("SCROLL_POSITION");
        if (position != null)
            scrollView.post(() -> scrollView.scrollTo(position[0], position[1]));
    }

    private void openCallReviews() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetApi.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ReciewsAPI = retrofit.create(GetApi.class);
        Call<ReviewsModel> ReviewReCall = ReciewsAPI.getApiReviews(ID_R);
        ReviewReCall.enqueue(new Callback<ReviewsModel>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsModel> call, @NonNull Response<ReviewsModel> response) {
                reviewsList = response.body().getResults();
                reviewRecycler.setAdapter(new ReviewsAdapter(DetailsActivity.this, reviewsList));
                reviewRecycler.smoothScrollToPosition(0);
                reviewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<ReviewsModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void openCallTrailer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetApi.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TrailerAPI = retrofit.create(GetApi.class);
        Call<TrailerModel> TrailerReCall = TrailerAPI.getApiTrailer(ID_T);
        TrailerReCall.enqueue(new Callback<TrailerModel>() {
            @Override
            public void onResponse(@NonNull Call<TrailerModel> call, @NonNull Response<TrailerModel> response) {

                trailerList = response.body().getResults();
                trailerRecycler.setAdapter(new TrailerAdapter(DetailsActivity.this, trailerList));
                trailerRecycler.smoothScrollToPosition(0);
                trailerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<TrailerModel> call, @NonNull Throwable t) {
            }
        });
    }


    public void onToggleStar(View view) {
        if (inFavoriteList) {

            star.setTextColor(Color.parseColor("#ffdc40"));
            Snackbar.make(view, "Added to Favorite", Snackbar.LENGTH_SHORT).show();
            ContentValues contentValues = new ContentValues();

            contentValues.put(Contract.COLUMN_DATA_MOVIE_ID, ID_R);
            contentValues.put(Contract.COLUMN_DATA_AVERAGE, String.valueOf(getIntent().getExtras().getDouble("vote_average")) + "/10");
            contentValues.put(Contract.COLUMN_DATA_OVERVIEW, getIntent().getExtras().getString("overview"));
            contentValues.put(Contract.COLUMN_DATA_POSTER_PATH, getIntent().getExtras().getString("poster_path"));
            contentValues.put(Contract.COLUMN_DATA_RELEASE_DATE, getIntent().getExtras().getString("release_date"));
            contentValues.put(Contract.COLUMN_DATA_TITLE, getIntent().getExtras().getString("title"));

            if (DetailsActivity.this != null && DetailsActivity.this.getContentResolver() != null) {
                getContentResolver().insert(Contract.DATA_CONTENT, contentValues);
                inFavoriteList = !inFavoriteList;

            }
        } else {

            star.setTextColor(Color.parseColor("#ffffff"));
            Snackbar.make(view, "Removed From Favorite", Snackbar.LENGTH_SHORT).show();

            if (DetailsActivity.this != null && DetailsActivity.this.getContentResolver() != null)
                DetailsActivity.this.getContentResolver().delete(
                        Contract.DATA_CONTENT,
                        Contract.COLUMN_DATA_MOVIE_ID + " = " + ID_R,
                        null);
            inFavoriteList = !inFavoriteList;

        }
    }

}
