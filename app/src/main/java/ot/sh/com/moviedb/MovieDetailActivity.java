package ot.sh.com.moviedb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by brandon on 09/11/2015.
 */
public class MovieDetailActivity extends ActionBarActivity {
    private final String LOG_TAG = MovieDetailActivity.class.getCanonicalName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieDetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MovieDetailFragment extends Fragment {
        private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
        private Movie movieDetail;

        public MovieDetailFragment() { setHasOptionsMenu(true); }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

            Intent intent = getActivity().getIntent();

            if (intent != null) {
                movieDetail = intent.getExtras().getParcelable("movie");

                ((TextView) rootView.findViewById(R.id.title_textview)).setText(movieDetail.title);
                ((TextView) rootView.findViewById(R.id.plot_textview)).setText(movieDetail.plot);
                ((TextView) rootView.findViewById(R.id.rate_textview)).setText(formatFloat(movieDetail.rating)+"/10");
                ((TextView) rootView.findViewById(R.id.release_date_textview)).setText(convertDateFormat(movieDetail.release_date));
                Picasso.with(getActivity()).load(movieDetail.url).into((ImageView) rootView.findViewById(R.id.detailImageViewer));
            }
            return rootView;
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
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.moviefragment_menu, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
