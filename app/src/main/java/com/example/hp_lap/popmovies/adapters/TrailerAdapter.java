package com.example.hp_lap.popmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp_lap.popmovies.models.TrailerModel;
import com.example.hp_lap.popmovies.R;

import java.util.List;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

    private Context context;
    private List<TrailerModel> trailerList;


    public TrailerAdapter(Context mContext, List<TrailerModel> trailerList) {
        this.context = mContext;
        this.trailerList = trailerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.title.setText(trailerList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView th;
        public View view;
        public MyViewHolder(final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTrial);
            th= itemView.findViewById(R.id.imageTrailer);
            view = itemView.findViewById(R.id.trailer);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                        String id = trailerList.get(getAdapterPosition()).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/watch?v=" + id));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id", id);
                        context.startActivity(intent);
                       Snackbar.make(itemView, "You clicked " + trailerList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
