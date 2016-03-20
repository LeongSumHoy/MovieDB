package ot.sh.com.moviedb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ot.sh.com.moviedb.data.MovieContract.MovieEntry;
import ot.sh.com.moviedb.data.MovieContract.TrailerEntry;

/**
 * Created by brandon on 03/02/2016.
 */
public class MovieDB extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private final String LOG_TAG = MovieDB.class.getSimpleName();

    public static final String DATABASE_NAME = "movie.db";

    public MovieDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(LOG_TAG, "onCreate");
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_URL + " TEXT, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_PLOT + " TEXT , " +
                MovieEntry.COLUMN_RATING + " TEXT, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT  " +
//                "UNIQUE (" + MovieEntry.COLUMN_ID +") ON CONFLICT IGNORE"+
                " );";

        Log.d(LOG_TAG, SQL_CREATE_MOVIE_TABLE);

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                TrailerEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TrailerEntry.COLUMN_ISO_639_1 + " INTEGER NOT NULL, " +
                TrailerEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_SITE + " INTEGER NOT NULL," +
                TrailerEntry.COLUMN_SIZE + " REAL NOT NULL, " +
                TrailerEntry.COLUMN_TYPE + " REAL NOT NULL, " +
                " FOREIGN KEY (" + TrailerEntry.COLUMN_KEY + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_ID + "), " +
                " UNIQUE (" + TrailerEntry.COLUMN_ISO_639_1 + ", " +
                TrailerEntry.COLUMN_KEY + ") ON CONFLICT REPLACE);";

        Log.d(LOG_TAG, SQL_CREATE_TRAILER_TABLE);

        try {
            sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
            Log.d(LOG_TAG, "Create table MOVIE success");
        } catch (SQLiteException e) { Log.d(LOG_TAG, "ERROR " + e.toString()); }
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "onUpgrade old ver " + String.valueOf(oldVersion) + " new Ver " + String.valueOf(newVersion));

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
