package ot.sh.com.moviedb;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

/**
 * Created by brandon on 09/11/2015.
 */
public class MovieDetailActivity extends ActionBarActivity {
    private final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
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
        private String movieDetail;
        private char delimiter = '|';

        public MovieDetailFragment() { setHasOptionsMenu(true); }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

            // The detail Activity called via intent.  Inspect the intent for forecast data.
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                movieDetail = intent.getStringExtra(Intent.EXTRA_TEXT);
                TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter(delimiter);
                splitter.setString(movieDetail);
                int i=0;
                for (String s : splitter) {
                    switch (i) {
                        case 0:
                            Picasso.with(getActivity()).load(s).into((ImageView) rootView.findViewById(R.id.detailImageViewer));
                        case 1:
                            ((TextView) rootView.findViewById(R.id.title_textview)).setText(s);
                            break;
                        case 2:
                            ((TextView) rootView.findViewById(R.id.plot_textview)).setText(s);
                            break;
                        case 3:
                            Float new_rate = Float.parseFloat(s)/10*5;
                            ((RatingBar) rootView.findViewById(R.id.rating_bar)).setRating(new_rate);
                        case 4:
                            ((TextView) rootView.findViewById(R.id.release_date_textview)).setText(s);
                        default: break;
                    }
                    Log.d(LOG_TAG, i+s);
                    i++;
                }
/*
                ((TextView) rootView.findViewById(R.id.title_textview))
                        .setText(splitter[1]);
                ((TextView) rootView.findViewById(R.id.plot_textview))
                        .setText(splitter[2]);
                        */
            }
            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.moviefragment_menu, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    }
}
