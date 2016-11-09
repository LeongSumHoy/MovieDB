package com.sh.ot.moviedb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sh.ot.moviedb.db.MovieContract.MovieEntry;

/**
 * Created by Brandon on 16/9/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE "+MovieEntry.TABLE_NAME+" ("+
                MovieEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieEntry.COLUMN_MOVIE_ID+" INTEGER NOT NULL, "+
                MovieEntry.COLUMN_URL+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_RATING+" INTEGER, " +
                MovieEntry.COLUMN_RELEASE_DATE+" INTEGER NOT NULL, "+
                MovieEntry.COLUMN_TITLE+" TEXT, "+
                MovieEntry.COLUMN_PLOT+" TEXT, "+
                " UNIQUE ("+ MovieEntry.COLUMN_MOVIE_ID+") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
