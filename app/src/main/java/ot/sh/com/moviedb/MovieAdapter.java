package ot.sh.com.moviedb;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by brandon on 28/10/2015.
 */
public class MovieAdapter extends BaseAdapter {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context context;
    private String[] url;
    private ArrayList<Movie> movieList;

    public MovieAdapter(Activity context, ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    public int getCount() {
        if (movieList == null) {
            return 0;
        }
        return movieList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
    }

    public void setDataChange(ArrayList<Movie> movieList) {
        if (movieList == null) { Log.d(LOG_TAG, "movieList is null !"); }
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        // Movie movie = getItem(position);
        // prepare imageview
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new GridView.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 600));
         //   imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;
        }

        //load values
        Picasso.with(context).load(movieList.get(position).url).into(imageView);

        return imageView;
    }

}
