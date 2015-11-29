package ot.sh.com.moviedb;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by brandon on 03/11/2015.
 */
public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    boolean mBindingPreference;
    private final String LOG_TAG = SettingsActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_key)));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        mBindingPreference = true;
        preference.setOnPreferenceChangeListener(this);

        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
        mBindingPreference = false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if ( !mBindingPreference ) {
            if (preference.getKey().equals(getString(R.string.pref_sort_key))) {
                MovieFragment.FetchMoviesTask moviesTask = new MovieFragment.FetchMoviesTask();
                String sortOption = value.toString();
                moviesTask.execute(sortOption);
            }
        }
        return true;
    }
}
