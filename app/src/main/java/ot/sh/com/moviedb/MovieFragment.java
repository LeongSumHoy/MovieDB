package ot.sh.com.moviedb;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import java.util.ArrayList;

import ot.sh.com.moviedb.data.MovieContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    private ArrayList<Movie> movieList = new ArrayList<Movie>();
    RecyclerView rv;
    RecyclerView.Adapter arv;
    RecyclerView.LayoutManager lmrv;
    private MovieAdapter movieAdapter;

    private static final int URL_LOADER = 0;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_ID,
            MovieContract.MovieEntry.COLUMN_URL,
            MovieContract.MovieEntry.COLUMN_PLOT,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE
    };

    public static final int COL_ID = 0;
    public static final int COL_URL = 1;
    public static final int COL_PLOT = 2;
    public static final int COL_TITLE = 3;
    public static final int COL_RATING = 4;
    public static final int COL_RELEASE_DATE = 5;


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        getLoaderManager().restartLoader(URL_LOADER, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState : " + outState.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");

        // Initialize CursorLoader
        movieAdapter = new MovieAdapter(getContext(), null);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        // spinner
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.pref_sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "onItemSelected");
                Log.d(LOG_TAG, "  Position : " + String.valueOf(position));
                Log.d(LOG_TAG, "  Id : " + String.valueOf(id));
                switch (position) {
                    case 0 :
                        Log.d(LOG_TAG, " --- sort popularity");
                        Util.setSavedPref(getActivity(), getString(R.string.pref_sort_popularity));
                        break;
                    case 1 :
                        Log.d(LOG_TAG, " --- sort rating");
                        Util.setSavedPref(getActivity(), getString(R.string.pref_sort_rating));
                        break;
                }
                updateMovies(Util.getSavedPref(getActivity()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // RecyclerView
        rv = (RecyclerView) rootView.findViewById(R.id.movie_grid_rv);
        rv.setHasFixedSize(true);
        lmrv = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lmrv);
     //   arv = new MovieAdapter(getContext(), null);
        rv.setAdapter(movieAdapter);

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
        return super.onOptionsItemSelected(item);
    }

    private void updateMovies(String sortOrder) {
        Log.d(LOG_TAG, "updateMovies : " + sortOrder);
        new FetchMoviesTask(getActivity()).execute(sortOrder);
        
    }

    /*
     *  Loaders abstract methods
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        movieAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onActivityCreated");
        getLoaderManager().initLoader(URL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        String sortOrder = MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " ASC";
        Uri movieUri = MovieContract.MovieEntry.CONTENT_URI;

        switch(id) {
            case URL_LOADER:
                return new CursorLoader(
                        getActivity(),
                        movieUri,
                        MOVIE_COLUMNS,
                        null,
                        null,
                        sortOrder
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
        movieAdapter.changeCursor(data);
    }


}