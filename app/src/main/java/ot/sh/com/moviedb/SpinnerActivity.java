package ot.sh.com.moviedb;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by brandon on 08/11/2015.
 */
public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = SpinnerActivity.class.getSimpleName();

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Toast.makeText(parent.getContext(), parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, String.valueOf(parent.getSelectedItemId()));
    }

    public void onNothingSelected(AdapterView<?> parent) {}
}
