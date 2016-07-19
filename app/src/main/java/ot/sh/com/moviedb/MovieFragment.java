package ot.sh.com.moviedb;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {
    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    // private static RecyclerAdapter mRecyclerAdapter;
    private ArrayList<Movie> movieList = new ArrayList<Movie>();
    public static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate "+ Util.getSavedPref(getActivity()));

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState : " + String.valueOf(movieList.size()));
       // outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        // spinner
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.pref_sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Log.d(LOG_TAG, String.valueOf(spinner.getSelectedItemId()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "onItemSelected : " + position);
                if (parent.getSelectedItem().equals(getString(R.string.pref_sort_label_popular))) {
                    Util.setSavedPref(getActivity(), getString(R.string.pref_sort_popularity));
                } else if (parent.getSelectedItem().equals(getString(R.string.pref_sort_label_rate))) {
                    Util.setSavedPref(getActivity(), getString(R.string.pref_sort_rating));
                }
                updateMovies(Util.getSavedPref(getActivity()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // RecyclerView Grid Layout.
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_grid_view);
        mRecyclerView.setHasFixedSize(true);
        // user Grid layout manager
        mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Load adapter
        mAdapter = new RecyclerAdapter(getActivity(), movieList);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void updateMovies(String sortOrder) {
        Log.d(LOG_TAG, "updateMovies : " + sortOrder);
        FetchMoviesTask movieTask = new FetchMoviesTask();
        movieTask.execute(sortOrder);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "onDetach");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "onAttach");
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>  > {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private Activity aContext;
        private String type;

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

        @Override
        protected void onPostExecute(ArrayList<Movie> dataset) {
            Log.d(LOG_TAG, "onPostExecute : " + String.valueOf(dataset.size()));

            if (dataset != null) {
                mAdapter = new RecyclerAdapter(getActivity(), dataset);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

    }

}