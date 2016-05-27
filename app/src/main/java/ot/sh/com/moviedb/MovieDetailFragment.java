package ot.sh.com.moviedb;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class MovieDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private Movie movieInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            movieInfo = intent.getExtras().getParcelable("movie");
            ((TextView) view.findViewById(R.id.title_textview)).setText(movieInfo.title);
            ((TextView) view.findViewById(R.id.plot_textview)).setText(movieInfo.plot);
            ((TextView) view.findViewById(R.id.rate_textview)).setText(formatFloat(movieInfo.rating)+"/10");
            ((TextView) view.findViewById(R.id.release_date_textview)).setText(convertDateFormat(movieInfo.release_date));
            Picasso.with(getActivity()).load(movieInfo.url).into((ImageView) view.findViewById(R.id.detailImageViewer));
        }
        return view;
    }

    public String formatFloat(String rate) {

        try {
            return Integer.toString( (int) (long) Math.round(Double.parseDouble(rate)) );
        } catch (Exception e) { return "0"; }

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


}
