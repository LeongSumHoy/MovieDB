package com.sh.ot.moviedb;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Brandon on 13/9/16.
 */
public class Util {

    public static int getSavedOpt(Context context) {
        String LOG_TAG = Util.class.getSimpleName();

        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        return prefs.getInt(context.getString(R.string.saved_spinner_state), 0 );
    }

    public static void saveSortOpt(Context context, int sortKey) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(context.getString(R.string.saved_spinner_state), sortKey);
        editor.commit();
    }


}
