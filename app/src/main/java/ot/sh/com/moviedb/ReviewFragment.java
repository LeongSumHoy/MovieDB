package ot.sh.com.moviedb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
\ * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {
    private final String LOG_TAG = ReviewFragment.class.getSimpleName();
    private String MOVIE_ID;
    private ArrayList<Review> reviewList = new ArrayList<Review>();
    public static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;


    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void getReviews(String id) {
        FetchReviewsTask reviewTask = new FetchReviewsTask();
        reviewTask.execute(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            MOVIE_ID = intent.getExtras().getString("movie_id");
            getReviews(MOVIE_ID);
            // RecyclerView Linear Layout.
            mRecyclerView = (RecyclerView) view.findViewById(R.id.review_card_view);
            mRecyclerView.setHasFixedSize(true);
            // user Grid layout manager
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            // Load adapter
            mAdapter = new ReviewAdapter(getActivity(), reviewList);
            mRecyclerView.setAdapter(mAdapter);
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */



    public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<Review>  > {
        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();
        private Activity aContext;

        private ArrayList<Review> getReviewDataFromJSON(String discoverReviewStr) throws JSONException {
            Log.d(LOG_TAG, "getReviewDataFromJSON");

            final String M_RESULTS = "results";
            final String M_ID = "id";
            final String M_AUTHOR = "author";
            final String M_CONTENT = "content";
            final String M_URL = "url";

            JSONObject reviewJson = new JSONObject(discoverReviewStr);
            JSONArray reviewArray = reviewJson.getJSONArray(M_RESULTS);
            ArrayList<Review> reviewList = new ArrayList<Review>();

            for (int i = 0; i < reviewArray.length(); i++) {
                String id;
                String author;
                String content;
                String url;
                String BASE_URL = "https://www.youtube.com/watch?v=";

                JSONObject review = reviewArray.getJSONObject(i);
                id = review.getString(M_ID);
                author = review.getString(M_AUTHOR);
                content = review.getString(M_CONTENT);
                url = review.getString(M_URL);

                Log.d(LOG_TAG, BASE_URL+id);
                reviewList.add(new Review(id, author, content, url));
            }
            return reviewList;
        }

        @Override
        protected ArrayList<Review> doInBackground(String... params) {
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
                final String DISCOVER_BASE_URL = "http://api.themoviedb.org/3/movie/"+id+"/reviews?";
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
                return getReviewDataFromJSON(discoverTrailerStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Review> dataset) {
            Log.d(LOG_TAG, "onPostExecute : " + String.valueOf(dataset.size()));

            if (dataset != null) {
                reviewList = dataset;
                mAdapter = new ReviewAdapter(getActivity(), dataset);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

    }

}
