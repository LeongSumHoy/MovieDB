package ot.sh.com.moviedb.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import ot.sh.com.moviedb.Movie;
import ot.sh.com.moviedb.R;

/**
 * Created by brandon on 28/12/2015.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;


    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public MovieSyncAdapter(Context context, boolean autoInitialize, boolean allowParellelSync) {
        super(context, autoInitialize, allowParellelSync);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String discoverMoviesStr = "";
        String key = "028c2b9fc540ec7b951493fec02350dc";
        String sort_order = "popularity.desc";

        try {
            final String DISCOVER_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(DISCOVER_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sort_order)
                    .appendQueryParameter(KEY_PARAM, key)
                    .build();
            URL url = new URL(builtUri.toString());
            Log.d(LOG_TAG, builtUri.toString());

            // Create request to movieDB.org
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();
            StringBuffer sb = new StringBuffer();
            /*
            if (in == null) {
                return null;
            } */
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            /*
            if (sb.length() == 0) {
                return null;
            } */
            discoverMoviesStr = sb.toString();
            Log.d(LOG_TAG, discoverMoviesStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
            // return null;
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

        //
        final String M_ID = "id";
        final String M_RESULTS = "results";
        final String M_ORG_TITLE = "original_title";
        final String M_POSTER_PATH = "poster_path";
        final String M_BACKDROP_PATH = "backdrop_path";
        final String M_OVERVIEW = "overview";
        final String M_VOTE_AVG = "vote_average";
        final String M_RELEASE_DATE = "release_date";

        try {
            JSONObject movieJson = new JSONObject(discoverMoviesStr);
            JSONArray movieArray = movieJson.getJSONArray(M_RESULTS);
            ArrayList<Movie> movieList = new ArrayList<Movie>();

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

                movieList.add(new Movie(id, BASE_URL + PIC_SIZE + poster_path, original_title, overview, vote_average, release_date));
            }
        } catch(final JSONException je){
            Log.e(LOG_TAG, String.valueOf(je));
        }

    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
    * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
    * onAccountCreated method so we can initialize things.
    *
    * @param context The context used to access the account service
    * @return a fake account.
    */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
        */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
        /*
         * If you don't set android:syncable="true" in
         * in your <provider> element in the manifest
         * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
         * here.
         */
            onAccountCreated(newAccount, context);
         }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
        * Since we've created an account
        */
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

}
