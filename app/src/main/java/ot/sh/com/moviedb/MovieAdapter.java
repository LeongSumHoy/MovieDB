package ot.sh.com.moviedb;

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
    private Movie[] movieList;

    public MovieAdapter(Activity context, Movie[] movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    public int getCount() {
        if (movieList == null) {
            return 0;
        }
        return movieList.length;
    }

    public Object getItem(int position) {
        return movieList[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public void setDataChange(Movie[] movieList) {
        if (movieList == null) { Log.d(LOG_TAG, "movieList is null !"); }
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        // prepare imageview
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        //load values
        Picasso.with(context).load(movieList[position].url).into(imageView);

        return imageView;
    }

}
