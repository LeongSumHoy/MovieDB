package ot.sh.com.moviedb;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {
    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private Movie movieDetail;
    private ArrayList<Trailer> trailerList = new ArrayList<Trailer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Log.d(LOG_TAG, "=== onSaveInstanceState " + movieList.size());
        // outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            movieDetail = intent.getExtras().getParcelable("movie");
            ((TextView) rootView.findViewById(R.id.title_textview)).setText(movieDetail.title);
            ((TextView) rootView.findViewById(R.id.plot_textview)).setText(movieDetail.plot);
            ((TextView) rootView.findViewById(R.id.rate_textview)).setText(formatFloat(movieDetail.rating)+"/10");
            ((TextView) rootView.findViewById(R.id.release_date_textview)).setText(convertDateFormat(movieDetail.release_date));
            Picasso.with(getActivity()).load(movieDetail.url).into((ImageView) rootView.findViewById(R.id.detailImageViewer));

            updateTrailers(movieDetail.id);
            new FetchTrailersTask().execute(movieDetail.id);
     //       ((TextView) rootView.findViewById(R.id.trailer_textView)).setText();

        }
        return rootView;
    }

    private void updateTrailers(String movie_id) {
        Log.d(LOG_TAG, "updateTrailers ");
        new FetchTrailersTask().execute(movie_id);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public String formatFloat(String rate) {
        final String ten = "10.0";
        try {
            if (rate.equals(ten)) { return "10"; }
        } catch (Exception e) {
            Log.e(LOG_TAG, String.valueOf(e));
            return "0";
        }
        return rate;
    }

    public String convertDateFormat(String date) {
        Date d = null;
        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            d = oldFormat.parse(date);
        } catch (Exception e) {
            Log.e(LOG_TAG, String.valueOf(e));
            return "N/A";
        }
        return newFormat.format(d).toString();
    }

}