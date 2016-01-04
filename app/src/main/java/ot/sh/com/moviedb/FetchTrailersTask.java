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
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by brandon on 20/12/2015.
 */
public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<Trailer>> {
    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
    private Activity aContext;

    FetchTrailersTask() {}

    private ArrayList<Trailer> getTrailersFromJSON(String trailersStr) throws JSONException {
        final String M_RESULTS = "results";
        final String M_ID = "id";
        final String M_ISO = "iso_639_1";
        final String M_KEY = "key";
        final String M_NAME = "name";
        final String M_SITE = "site";
        final String M_SIZE = "size";
        final String M_TYPE = "type";

        JSONObject trailerJson = new JSONObject(trailersStr);
        JSONArray trailerArray = trailerJson.getJSONArray(M_RESULTS);
        ArrayList<Trailer> trailerList = new ArrayList<Trailer>();

        for (int i = 0; i < trailerArray.length(); i++) {
            String id;
            String iso_639_1;
            String key;
            String name;
            String site;
            String size;
            String type;
            String BASE_URL = "http://www.youtube.com/watch?v=";

            JSONObject t = trailerArray.getJSONObject(i);
            id = t.getString(M_ID);
            iso_639_1 = t.getString(M_ISO);
            key = t.getString(M_KEY);
            name = t.getString(M_NAME);
            site = t.getString(M_SITE);
            size = t.getString(M_SIZE);
            type = t.getString(M_TYPE);
            Log.d(LOG_TAG, id+"|"+key+"|"+name+"|"+site);
            trailerList.add(new Trailer(id, iso_639_1, key, name, site, size, type));
        }
        return trailerList;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String trailersStr;
        String key = "028c2b9fc540ec7b951493fec02350dc";

        if (params.length == 0) { return null; }
        String trailer_id = params[0];
        Log.d(LOG_TAG, trailer_id);

        try {
            final String BASEURL_VIDEO = "http://api.themoviedb.org/3/movie/";
            final String STR_VIDEO = "/videos?";
            final String URL = BASEURL_VIDEO + trailer_id + STR_VIDEO;
            final String KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(URL).buildUpon()
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
            trailersStr = sb.toString();
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
            return getTrailersFromJSON(trailersStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

}
