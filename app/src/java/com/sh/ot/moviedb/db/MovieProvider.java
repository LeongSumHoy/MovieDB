package com.sh.ot.moviedb.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Brandon on 16/9/16.
 */
public class MovieProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MovieDbHelper mOpenHelper;

    static {
        sUriMatcher.addURI("com.sh.ot.moviedb.db.provider.movies", "movies", 1);
    }

    private static final String sMovieOrderByDateDesc = MovieContract.MovieEntry.TABLE_NAME+"."+
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        switch (UriMatcher.match(uri)) {
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    public boolean onCreate() {
        mOpenHelper = new MainDatabaseHelper(getContext(), dbname, null, 1);

        return true;
    }

    public Cursor insert(Uri uri, ContentValues values) {
        db = mOpenHelper.getWritableDatabase();
    }

    private static final String SQL_CREATE_MAIN = "CREATE TABLE " +
            "main " +
            "(" +
            " _ID INTEGER PRIMARY KEY, " +
            ""
}
