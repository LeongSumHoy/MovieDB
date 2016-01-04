package ot.sh.com.moviedb;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context context;
    private String[] url;
    private ArrayList<Movie> movieList;

    public MovieAdapter(Activity context, ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView movieIV;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(LOG_TAG, "ViewHolder");
            movieIV = (ImageView)itemView.findViewById(R.id.movie_ImageViewer);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        Log.d(LOG_TAG, "MovieAdapter.ViewHolder");
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_movies, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(LOG_TAG, "onBindViewHolder");
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // holder.mTextView.setText(mDataset[position]);
        holder.movieIV.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.movieIV.setLayoutParams(new GridView.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 600));
        // holder.movieIV.setImageResource(movieList.get(position));
        Picasso.with(context).load(movieList.get(position).url).into(holder.movieIV);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "getItemCount : " + String.valueOf(movieList.size()));
        return movieList.size();
    }


    public void setDataChange(ArrayList<Movie> movieList) {

        if (movieList == null) { Log.d(LOG_TAG, "movieList is null !"); }
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    /*
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        // Movie movie = getItem(position);
        // prepare imageview
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new GridView.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 600));
        } else {
            imageView = (ImageView) convertView;
        }

        //load values
        Picasso.with(context).load(movieList.get(position).url).into(imageView);

        return imageView;
    }
*/
}
