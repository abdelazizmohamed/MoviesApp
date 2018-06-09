package com.example.hp_lap.popmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class ReviewsModel implements Parcelable {
    @SerializedName("results")
    private List<Review> Results;

    protected ReviewsModel(Parcel in) {
    }

    public static final Creator<ReviewsModel> CREATOR = new Creator<ReviewsModel>() {
        @Override
        public ReviewsModel createFromParcel(Parcel in) {
            return new ReviewsModel(in);
        }

        @Override
        public ReviewsModel[] newArray(int size) {
            return new ReviewsModel[size];
        }
    };

    public List<Review> getResults() {
        return Results;
    }

    public void setResults(List<Review> Results) {
        this.Results = Results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public class Review implements Parcelable {
        @SerializedName("author")
        private String reviewAuthor;
        @SerializedName("content")
        private String reviewContent;

        protected Review(Parcel in) {
            reviewAuthor = in.readString();
            reviewContent = in.readString();
        }

        public final Creator<Review> CREATOR = new Creator<Review>() {
            @Override
            public Review createFromParcel(Parcel in) {
                return new Review(in);
            }

            @Override
            public Review[] newArray(int size) {
                return new Review[size];
            }
        };

        public String getReviewAuthor() {
            return reviewAuthor;
        }

        public void setReviewAuthor(String reviewAuthor) {
            this.reviewAuthor = reviewAuthor;
        }

        public String getReviewContent() {
            return reviewContent;
        }

        public void setReviewContent(String reviewContent) {
            this.reviewContent = reviewContent;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(reviewAuthor);
            parcel.writeString(reviewContent);
        }
    }

}
