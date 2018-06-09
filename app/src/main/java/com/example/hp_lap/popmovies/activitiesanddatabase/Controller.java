package com.example.hp_lap.popmovies.activitiesanddatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;

import com.example.hp_lap.popmovies.models.MovieModel;

import java.util.ArrayList;
import java.util.List;


public class Controller {

    private Context context;
    private static Contract Helper;
    private List<MovieModel> FavoriteList = new ArrayList<>();

    public Controller(Context context) {
        this.context = context;
        this.Helper = new Contract(context);
    }


    public List<MovieModel> getFavouriteMovie() {
        Cursor cursor = null;
        MovieModel Movie;
        if (context != null && context.getContentResolver() != null)
            cursor = context.getContentResolver().query(Contract.DATA_CONTENT,
                    null,
                    "Select * FROM " + Helper.TABLE_NAME,
                    null,
                    null);

        if (FavoriteList != null) FavoriteList.clear();
        for (; cursor != null && cursor.moveToNext(); ) {
            Movie = new MovieModel();
            Movie.setId(cursor.getInt(0));
            Movie.setTitle(cursor.getString(1));
            Movie.setOverview(cursor.getString(2));
            Movie.setPosterPath(cursor.getString(3));
            Movie.setVoteAverage(cursor.getDouble(4));
            Movie.setReleaseDate(cursor.getString(5));

            FavoriteList.add(Movie);
        }
        if (cursor != null) cursor.close();
        return FavoriteList;
    }

    public long addToFavorite(MovieModel movie) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Helper.COLUMN_DATA_MOVIE_ID, movie.getId());
        contentValues.put(Helper.COLUMN_DATA_TITLE, movie.getTitle());
        contentValues.put(Helper.COLUMN_DATA_OVERVIEW, movie.getOverview());
        contentValues.put(Helper.COLUMN_DATA_POSTER_PATH, movie.getPosterPath());
        contentValues.put(Helper.COLUMN_DATA_AVERAGE, movie.getVoteAverage());
        contentValues.put(Helper.COLUMN_DATA_RELEASE_DATE, movie.getReleaseDate());

        Uri uri = null;
        if (context != null && context.getContentResolver() != null)
            uri = context.getContentResolver().insert(Contract.DATA_CONTENT, contentValues);

        return uri == null ? -1 : 1;
    }

    public int deleteFromFavourite(int movieId) {
        if (context != null && context.getContentResolver() != null)
            return context.getContentResolver().delete(
                    Contract.DATA_CONTENT,
                    Contract.COLUMN_DATA_MOVIE_ID + " = " + movieId,
                    null);
        return 5;
    }

    public boolean isFavorite(int movieId) {
        Cursor cursor = context.getContentResolver()
                .query(Contract.DATA_CONTENT.buildUpon().appendEncodedPath(String.valueOf(movieId)).build(),
                        null,
                        "Select * FROM " + Helper.TABLE_NAME + " WHERE " + Helper.COLUMN_DATA_MOVIE_ID + " = " + movieId + "",
                        null,
                        null);

        return cursor != null && cursor.moveToNext();
    }
}
