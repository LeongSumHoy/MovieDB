package ot.sh.com.moviedb;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {
    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    private MovieAdapter movieAdapter;
    private String[] url = {"http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "http://image.tmdb.org/t/p/w185//bHarw8xrmQeqf3t8HpuMY7zoK4x.jpg"};
    private List<Movies> movieList = new ArrayList<Movies>();


    public MovieFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        updateMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        // gridview
        GridView gridview = (GridView) rootView.findViewById(R.id.gridView);
        movieAdapter = new MovieAdapter(getActivity(), movieList);
        gridview.setAdapter(movieAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), " " + position, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG_TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.moviefragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onOptionsItemSelected");
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMovies() {
        Log.d(LOG_TAG, "updateMovies");
        FetchMoviesTask movieTask = new FetchMoviesTask();
        movieTask.execute();
    }

/* */
    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movies>  > {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private Activity context;

        public FetchMoviesTask() {   }

        private List<Movies> getMovieDataFromJSON(String discoverMoviesStr) throws JSONException {
        final String M_RESULTS = "results";
        final String M_ORG_TITLE = "original_title";
        final String M_POSTER_PATH = "poster_path";
        final String M_BACKDROP_PATH = "backdrop_path";
        final String M_OVERVIEW = "overview";
        final String M_VOTE_AVG = "vote_average";
        final String M_RELEASE_DATE = "release_date";
        Log.d(LOG_TAG, discoverMoviesStr);
            JSONObject movieJson = new JSONObject(discoverMoviesStr);
            JSONArray movieArray = movieJson.getJSONArray(M_RESULTS);
            Movies[] m = new Movies[movieArray.length()];
            Log.d(LOG_TAG, String.valueOf(movieArray.length()));
            for (int i = 0; i < movieArray.length(); i++) {
                String original_title;
                String backdrop_path;
                String overview;
                String vote_average;
                String release_date;
                String poster_path;
                String BASE_URL = "http://image.tmdb.org/t/p";
                JSONObject movie = movieArray.getJSONObject(i);
                original_title = movie.getString(M_ORG_TITLE);
                backdrop_path = movie.getString(M_BACKDROP_PATH);
                poster_path = movie.getString(M_POSTER_PATH);
                overview = movie.getString(M_OVERVIEW);
                vote_average = movie.getString(M_VOTE_AVG);
                release_date = movie.getString(M_RELEASE_DATE);
                Log.d(LOG_TAG, original_title + ", " + release_date + ", " + poster_path + ", " + backdrop_path);
  //               movieList[i] =  original_title+ ", "+release_date+ ", "+vote_average+ ", "+overview+", "+backdrop_path;

                m[i].url = BASE_URL+backdrop_path;
                m[i].title = original_title;
                m[i].plot = overview;
                m[i].rating = vote_average;
                m[i].release_date = release_date;
                movieList.add(i, m[i]);

                Log.d(LOG_TAG, movieList.get(i).url);
        }

        return movieList;
    }
    @Override
    protected List<Movies> doInBackground(String... params) {
        // form url
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // JSON reply string
        String discoverMoviesStr;
        String sort_order = "popularity.desc";
        String key = "028c2b9fc540ec7b951493fec02350dc";
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
    @Override
    protected void onPostExecute(List<Movies> movies) {
        Log.d(LOG_TAG, "onPostExecute");

        movieList = movies;
        movieAdapter.setDataChange(movieList);

    }
}

/* */
}
