package ot.sh.com.moviedb;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by brandon on 07/11/2015.
 */
public class Util {
    private final static String LOG_TAG = Util.class.getSimpleName();

    public static String getSortPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_default));
    }

    public static String getSavedPref(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.pref_saved_key), Context.MODE_PRIVATE);
        return prefs.getString(context.getString(R.string.pref_saved_order), context.getString(R.string.pref_sort_default));
    }

    public static void setSavedPref(Context context, String sortKey) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.pref_saved_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_saved_order), sortKey);
        editor.commit();
    }

}
