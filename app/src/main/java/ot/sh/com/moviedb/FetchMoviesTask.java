package ot.sh.com.moviedb;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import ot.sh.com.moviedb.data.MovieContract;

/**
 * Created by brandon on 01/02/2016.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, Void> {
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private Context mContext;

    public FetchMoviesTask(Context context) {
        mContext = context;
    }

    private void getDataFromJSON(String urlStr) throws JSONException {
        Log.d(LOG_TAG, "getDataFromJSON");
        // Movie
        final String M_ID = "id";
        final String M_RESULTS = "results";
        final String M_ORG_TITLE = "original_title";
        final String M_POSTER_PATH = "poster_path";
        final String M_OVERVIEW = "overview";
        final String M_VOTE_AVG = "vote_average";
        final String M_RELEASE_DATE = "release_date";

        JSONObject movieJson = new JSONObject(urlStr);
        JSONArray movieArray = movieJson.getJSONArray(M_RESULTS);
        ArrayList<Movie> movieList = new ArrayList<Movie>();

        Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

        for (int i = 0; i < movieArray.length(); i++) {
            String id;
            String original_title;
            String overview;
            String vote_average;
            String release_date;
            String poster_path;
            String BASE_URL = "http://image.tmdb.org/t/p/";
            String PIC_SIZE = "w500";

            JSONObject movie = movieArray.getJSONObject(i);
            id = movie.getString(M_ID);
            original_title = movie.getString(M_ORG_TITLE);
            poster_path = movie.getString(M_POSTER_PATH);
            overview = movie.getString(M_OVERVIEW);
            vote_average = movie.getString(M_VOTE_AVG);
            release_date = movie.getString(M_RELEASE_DATE);
             Log.d(LOG_TAG, id+"|"+original_title+"|"+poster_path+"|"+overview+"|"+vote_average+"|"+release_date);

            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieContract.MovieEntry.COLUMN_ID, id);
            movieValues.put(MovieContract.MovieEntry.COLUMN_URL, BASE_URL + PIC_SIZE + poster_path);
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, original_title);
            movieValues.put(MovieContract.MovieEntry.COLUMN_PLOT, overview);
            movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, vote_average);
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, release_date);

            cVVector.add(movieValues);

            // movieList.add(new Movie(id, BASE_URL + PIC_SIZE + poster_path, original_title, overview, vote_average, release_date));
        }

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            Log.d(LOG_TAG, "Uri : " + MovieContract.MovieEntry.CONTENT_URI);
            mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
        }
    }

    @Override
    protected Void doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String urlStr;
        String key = "028c2b9fc540ec7b951493fec02350dc";

        if (params.length == 0) { return null; }
        String sort_key = params[0];

        final String BASE_URL = "http://api.themoviedb.org/3/";
        final String DISCOVER_STR = "discover/movie?";
        final String SORT_PARAM = "sort_by";
        final String KEY_PARAM = "api_key";
        final String MOVIE_STR = "movie/";
        final String VIDEO_STR = "video?";
        final URL url;
        final Uri builtUri;

        try {
            builtUri = Uri.parse(BASE_URL+DISCOVER_STR).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sort_key)
                    .appendQueryParameter(KEY_PARAM, key)
                    .build();

            Log.d(LOG_TAG, builtUri.toString());
            url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();
            StringBuffer sb = new StringBuffer();
            if (in == null) {
                Log.d(LOG_TAG, "string buffer null, exit");
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            if (sb.length() == 0) { return null; }
            urlStr = sb.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            getDataFromJSON(urlStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

}