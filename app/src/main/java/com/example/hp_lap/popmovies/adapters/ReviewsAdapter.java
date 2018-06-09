package com.example.hp_lap.popmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp_lap.popmovies.activitiesanddatabase.DetailsActivity;
import com.example.hp_lap.popmovies.R;
import com.example.hp_lap.popmovies.models.ReviewsModel;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    private List<ReviewsModel.Review> reviewsModels;
    private Context context;

    public ReviewsAdapter(DetailsActivity detailsActivity, List<ReviewsModel.Review> reviewsList) {
        this.reviewsModels =  reviewsList;
        this.context = detailsActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.reviews_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.MyViewHolder holder, int position) {

        // Picasso.with(context)
        //        .load("http://image.tmdb.org/t/p/w185/" + reviewsModels.get(position).)
        //      .into(holder.imageView);

        holder.author.setText(reviewsModels.get(position).getReviewAuthor());
        holder.Content.setText(reviewsModels.get(position).getReviewContent());

    }

    @Override
    public int getItemCount() {
        return reviewsModels.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView author, Content;

        MyViewHolder(View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.author);
            Content = itemView.findViewById(R.id.content);

        }
    }
}
