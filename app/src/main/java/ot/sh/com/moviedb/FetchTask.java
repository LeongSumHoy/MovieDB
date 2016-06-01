package ot.sh.com.moviedb;

import android.app.Activity;
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

/**
 * Created by Brandon on 28/5/16.
 */
public class FetchTask extends AsyncTask<String, Void, ArrayList<Movie>  > {
    private final String LOG_TAG = FetchTask.class.getSimpleName();
    private Activity aContext;
    private String type;

    public FetchTask(String type) {
        this.type = type;
    }
    private ArrayList<Movie> getMovieDataFromJSON(String discoverMoviesStr) throws JSONException {
        Log.d(LOG_TAG, "getMovieDataFromJSON");

        final String M_RESULTS = "results";
        final String M_ORG_TITLE = "original_title";
        final String M_POSTER_PATH = "poster_path";
        final String M_ID = "id";
        final String M_OVERVIEW = "overview";
        final String M_VOTE_AVG = "vote_average";
        final String M_RELEASE_DATE = "release_date";

        JSONObject movieJson = new JSONObject(discoverMoviesStr);
        JSONArray movieArray = movieJson.getJSONArray(M_RESULTS);
        ArrayList<Movie> movieList = new ArrayList<Movie>();

        for (int i = 0; i < movieArray.length(); i++) {
            String original_title;
            String id;
            String overview;
            String vote_average;
            String release_date;
            String poster_path;
            String BASE_URL = "http://image.tmdb.org/t/p/";
            String PIC_SIZE = "w500";

            JSONObject movie = movieArray.getJSONObject(i);
            original_title = movie.getString(M_ORG_TITLE);
            id = movie.getString(M_ID);
            poster_path = movie.getString(M_POSTER_PATH);
            overview = movie.getString(M_OVERVIEW);
            vote_average = movie.getString(M_VOTE_AVG);
            release_date = movie.getString(M_RELEASE_DATE);

            Log.d(LOG_TAG, id+"|"+original_title+"|"+poster_path);
            movieList.add(new Movie(id, BASE_URL + PIC_SIZE + poster_path, original_title, overview, vote_average, release_date));
        }
        return movieList;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        Log.d(LOG_TAG, "doInBackground");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String discoverMoviesStr;
        String key = "028c2b9fc540ec7b951493fec02350dc";

        if (params.length == 0) {
            return null;
        }


        String sort_order = params[0];

        try {
            final String DISCOVER_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(DISCOVER_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sort_order)
                    .appendQueryParameter(KEY_PARAM, key)
                    .build();
            URL url = new URL(builtUri.toString());
            Log.d(LOG_TAG, url.toString());

            // Create request to movieDB.org
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();
            StringBuffer sb = new StringBuffer();
            if (in == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            if (sb.length() == 0) {
                return null;
            }
            discoverMoviesStr = sb.toString();
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
            return getMovieDataFromJSON(discoverMoviesStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

/*
    @Override
    protected void onPostExecute(ArrayList<Movie> dataset) {
        Log.d(LOG_TAG, "onPostExecute : " + String.valueOf(dataset.size()));

        if (dataset != null) {
            mAdapter = new RecyclerAdapter(getActivity(), dataset);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
*/

}