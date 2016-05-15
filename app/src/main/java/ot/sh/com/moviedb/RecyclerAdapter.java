package ot.sh.com.moviedb;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by brandon on 20/03/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String LOG_TAG = RecyclerAdapter.class.getCanonicalName();
    ArrayList<Movie> mDataset = new ArrayList<Movie>();
    Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder (View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.movie_image);
        }
    }

    public RecyclerAdapter(Context context, ArrayList<Movie> dataset) {
        mContext = context;
        mDataset = dataset;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_image, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(LOG_TAG, "onBindViewHolder");
        viewHolder.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        viewHolder.mImageView.setLayoutParams(new GridView.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 600));
        Picasso.with(mContext).load(mDataset.get(position).url).into(viewHolder.mImageView);
        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                intent.putExtra("movie", mDataset.get(position));
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "getItemCount : " + String.valueOf(mDataset.size()));
        return mDataset.size();
    }

}
