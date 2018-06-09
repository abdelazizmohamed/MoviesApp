package com.example.hp_lap.popmovies;

import com.example.hp_lap.popmovies.models.MovieModel;
import com.example.hp_lap.popmovies.models.ReviewsModel;
import com.example.hp_lap.popmovies.models.TrailerModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetApi {

    String API_URL = "http://api.themoviedb.org";

    @GET("/3/movie/popular?api_key=04d0b7696c38e4a4dc47f5087bf083c6")
    Call<MovieModel> getApiPopular();

    @GET("/3/movie/top_rated?api_key=04d0b7696c38e4a4dc47f5087bf083c6")
    Call<MovieModel> getApiTopRated();

    @GET("/3/movie/{id}/videos?api_key=04d0b7696c38e4a4dc47f5087bf083c6")
    Call<TrailerModel> getApiTrailer(@Path("id") int ID_T);

    @GET("/3/movie/{id}/reviews?api_key=04d0b7696c38e4a4dc47f5087bf083c6")
    Call<ReviewsModel> getApiReviews(@Path("id") int ID_R);

}
