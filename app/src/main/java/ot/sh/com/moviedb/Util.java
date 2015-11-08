package ot.sh.com.moviedb;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by brandon on 07/11/2015.
 */
public class Util {

    public static String getSortPref(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.pref_sort_key), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_default) );
    }
}
