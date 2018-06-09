package com.example.hp_lap.popmovies.activitiesanddatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

class Contract extends SQLiteOpenHelper {

    static final String TABLE_NAME = "movies_table";
    static final String COLUMN_DATA_MOVIE_ID = "id";
    static final String COLUMN_DATA_TITLE = "title";
    static final String COLUMN_DATA_OVERVIEW = "overview";
    static final String COLUMN_DATA_POSTER_PATH = "poster_path";
    static final String COLUMN_DATA_AVERAGE = "average";
    static final String COLUMN_DATA_RELEASE_DATE = "release_date";

    static final String DATA_AUTHORITY = "com.example.hp_lap.popmovies";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + DATA_AUTHORITY);

    static final Uri DATA_CONTENT =
            BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    Contract(Context context) {
        super(context, "favorite_Movies.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " ("
                + COLUMN_DATA_MOVIE_ID + " INTEGER PRIMARY KEY , "
                + COLUMN_DATA_TITLE + " VARCHAR(400) , "
                + COLUMN_DATA_OVERVIEW + " VARCHAR(400) , "
                + COLUMN_DATA_POSTER_PATH + " VARCHAR(400) , "
                + COLUMN_DATA_AVERAGE + " VARCHAR(400) , "
                + COLUMN_DATA_RELEASE_DATE + " VARCHAR(400) );";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
}