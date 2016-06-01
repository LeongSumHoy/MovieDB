package ot.sh.com.moviedb;

import android.content.Context;
import android.content.Intent;
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
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();
    ArrayList<Trailer> mDataset = new ArrayList<Trailer>();
    Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder (View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.trailerImage);
        }
    }

    public TrailerAdapter(Context context, ArrayList<Trailer> dataset) {
        mContext = context;
        mDataset = dataset;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        Picasso.with(mContext).load("http://img.youtube.com/vi/"+mDataset.get(position).key+"/maxresdefault.jpg" ).into(viewHolder.mImageView);
        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }


    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "getItemCount : " + String.valueOf(mDataset.size()));
        return mDataset.size();
    }

}
