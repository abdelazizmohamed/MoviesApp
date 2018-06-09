package com.example.hp_lap.popmovies.activitiesanddatabase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Content_Provider extends ContentProvider {


    private Contract Helper;
    public static final int DATA_MOVIES = 100;
    public static final int DATA_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(Contract.DATA_AUTHORITY, Contract.TABLE_NAME, DATA_MOVIES);
        uriMatcher.addURI(Contract.DATA_AUTHORITY, Contract.TABLE_NAME + "/#", DATA_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        Helper = new Contract(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Cursor reCursor = null;
        switch (uriMatcher.match(uri)) {
            case DATA_MOVIES:
                reCursor = Helper.getReadableDatabase().rawQuery(selection, selectionArgs);
                //cursor = db.rawQuery("SELECT * FROM "+Contract.TABLE_NAME,null);
                //  cursor=db.query(Contract.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);

                break;
            case DATA_ID:
                // cursor = db.rawQuery("SELECT * FROM "+Contract.TABLE_NAME,null);
                //  cursor=db.query(Contract.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                reCursor = Helper.getReadableDatabase().rawQuery(selection, selectionArgs);
        }

        //  if (cursor != null && getContext() != null)
        reCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return reCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      @Nullable ContentValues contentValues) {

        Uri RUri = null;
        switch (uriMatcher.match(uri)) {
            case DATA_MOVIES:

                long id = Helper.getWritableDatabase().insert(Contract.TABLE_NAME, null, contentValues);
                if (id > 0)
                    RUri = ContentUris.withAppendedId(Contract.DATA_CONTENT, id);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return RUri;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        int DELETED_ROWS = 0;
        switch (uriMatcher.match(uri)) {
            case DATA_MOVIES:
                DELETED_ROWS = Helper.getWritableDatabase().delete(Contract.TABLE_NAME, selection, null);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return DELETED_ROWS;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues contentValues,
                      @Nullable String s,
                      @Nullable String[] strings) {
        return 0;
    }
}
