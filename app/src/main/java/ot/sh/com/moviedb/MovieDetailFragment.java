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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 */
public class MovieDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private Movie movieInfo;
    private ArrayList<Trailer> trailerList = new ArrayList<Trailer>();
    public static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            movieInfo = intent.getExtras().getParcelable("movie");
            ((TextView) view.findViewById(R.id.title_textview)).setText(movieInfo.title);
            ((TextView) view.findViewById(R.id.plot_textview)).setText(movieInfo.plot);
            ((TextView) view.findViewById(R.id.rate_textview)).setText(formatFloat(movieInfo.rating)+"/10");
            ((TextView) view.findViewById(R.id.release_date_textview)).setText(convertDateFormat(movieInfo.release_date));
            Picasso.with(getActivity()).load(movieInfo.url).into((ImageView) view.findViewById(R.id.detailImageViewer));
           //  Picasso.with(getActivity()).load("http://img.youtube.com/vi/PfBVIHgQbYk/maxresdefault.jpg").into((ImageView) view.findViewById(R.id.trailerImage));

            // RecyclerView Grid Layout.
            mRecyclerView = (RecyclerView) view.findViewById(R.id.trailerImage);
            mRecyclerView.setHasFixedSize(true);
            // user Grid layout manager
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            // Load adapter
            mAdapter = new TrailerAdapter(getActivity(), trailerList);
            mRecyclerView.setAdapter(mAdapter);

            getTrailers(movieInfo.id);

        }
        return view;
    }

    private void getTrailers(String id) {
        FetchTrailersTask trailerTask = new FetchTrailersTask();
        trailerTask.execute(id);
    }

    public String formatFloat(String rate) {

        try {
            return Integer.toString( (int) (long) Math.round(Double.parseDouble(rate)) );
        } catch (Exception e) { return "0"; }

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


    public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<Trailer>  > {
        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
        private Activity aContext;

        private ArrayList<Trailer> getTrailerDataFromJSON(String discoverTrailerStr) throws JSONException {
            Log.d(LOG_TAG, "getTrailerDataFromJSON");

            final String M_RESULTS = "results";
            final String M_ID = "id";
            final String M_ISO_639_1 = "iso_639_1";
            final String M_ISO_3166_1 = "iso_3166_1";
            final String M_KEY = "key";
            final String M_NAME = "name";
            final String M_SITE = "site";
            final String M_SIZE = "size";
            final String M_TYPE = "type";

            JSONObject trailerJson = new JSONObject(discoverTrailerStr);
            JSONArray trailerArray = trailerJson.getJSONArray(M_RESULTS);
            ArrayList<Trailer> trailerList = new ArrayList<Trailer>();

            for (int i = 0; i < trailerArray.length(); i++) {
                String id;
                String iso_639_1;
                String iso_3166_1;
                String key;
                String name;
                String site;
                String size;
                String type;
                String BASE_URL = "https://www.youtube.com/watch?v=";


                JSONObject trailer = trailerArray.getJSONObject(i);
                id = trailer.getString(M_ID);
                iso_639_1 = trailer.getString(M_ISO_639_1);
                iso_3166_1 = trailer.getString(M_ISO_3166_1);
                key = trailer.getString(M_KEY);
                name = trailer.getString(M_NAME);
                site = trailer.getString(M_SITE);
                size = trailer.getString(M_SIZE);
                type = trailer.getString(M_TYPE);

                Log.d(LOG_TAG, BASE_URL+key);
                trailerList.add(new Trailer(id, iso_639_1, iso_3166_1, key, name, site, size, type));
            }
            return trailerList;
        }

        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {
            Log.d(LOG_TAG, "doInBackground");

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String discoverTrailerStr;
            String key = "028c2b9fc540ec7b951493fec02350dc";

            if (params.length == 0) {
                return null;
            }
            String id = params[0];

            try {
                final String DISCOVER_BASE_URL = "http://api.themoviedb.org/3/movie/"+id+"/videos?";
                final String KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(DISCOVER_BASE_URL).buildUpon()
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
                discoverTrailerStr = sb.toString();
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
                return getTrailerDataFromJSON(discoverTrailerStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> dataset) {
            Log.d(LOG_TAG, "onPostExecute : " + String.valueOf(dataset.size()));

            if (dataset != null) {
                mAdapter = new TrailerAdapter(getActivity(), dataset);
                mRecyclerView.setAdapter(mAdapter);
            }

        }

    }


}
