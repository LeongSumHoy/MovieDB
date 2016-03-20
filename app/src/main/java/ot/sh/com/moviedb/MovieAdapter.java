package ot.sh.com.moviedb;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by brandon on 28/10/2015.
 */
public class MovieAdapter extends CursorAdapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private Cursor mCursor;

    public MovieAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
        mCursor = cursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView movieIV;

        public ViewHolder(View itemView) {
            super(itemView);
            movieIV = (ImageView)itemView.findViewById(R.id.movie_ImageViewer);
        }
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        Log.d(LOG_TAG, "== onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_movies, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        Log.d(LOG_TAG, "=== onBindViewHolder ");

        viewHolder.movieIV.setScaleType(ImageView.ScaleType.FIT_XY);
        viewHolder.movieIV.setLayoutParams(new GridView.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 600));
        Picasso.with(mContext).load(cursor.getString(MovieFragment.COL_URL)).into(viewHolder.movieIV);
        viewHolder.movieIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                mContext.startActivity(intent);
            }
        });
    }


}
