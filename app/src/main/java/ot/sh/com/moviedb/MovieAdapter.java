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
    private Movies[] movieList;

    public MovieAdapter() {}

    public MovieAdapter(Activity context, Movies[] movieList) {
        Log.d(LOG_TAG,"MovieAdapter");
        this.context = context;
        //this.url = url;
         this.movieList = movieList;
    }

    public int getCount() {
        Log.d(LOG_TAG, "getCount");

        if (movieList == null) {
            Log.d(LOG_TAG, "count 0");
            return 0;
        }
        Log.d(LOG_TAG, String.valueOf(movieList.length) );
        return movieList.length;
    }

    public Object getItem(int position) {
        Log.d(LOG_TAG, "getItem");
        return movieList[position];
    }

    public long getItemId(int position) {
        Log.d(LOG_TAG, "getItemId");
        return 0;
    }

    public void setDataChange(Movies[] movieList) {
        Log.d(LOG_TAG, "setDataChage");

        if (movieList == null) { Log.d(LOG_TAG, "movieList is null !"); }
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        Log.d(LOG_TAG, "getView");
        // prepare imageview
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(450, 450));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        //load values
        Picasso.with(context).load(movieList[position].url).into(imageView);

        return imageView;
    }

}
