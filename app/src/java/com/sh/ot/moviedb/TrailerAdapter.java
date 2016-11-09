package com.sh.ot.moviedb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Brandon on 13/9/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();
    ArrayList<Trailer> mDataset = new ArrayList<Trailer>();
    Context mContext;
    private final int TRAILER = 0;

    public static class ViewHolder_Trailer extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder_Trailer(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.trailer_image);
        }
    }

    public TrailerAdapter(Context context, ArrayList<Trailer> dataset) {
        mContext = context;
        mDataset = dataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        Log.d(LOG_TAG, "onCreateViewHolder");

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case TRAILER:
                View vt = inflater.inflate(R.layout.trailer_image, viewGroup, false);
                viewHolder = new ViewHolder_Trailer(vt);
                break;
            default:
                View vd = inflater.inflate(R.layout.fragment_detail, viewGroup, false);
                viewHolder = new ViewHolder_Trailer(vd);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        //      Log.d(LOG_TAG, "onBindViewHolder");
        //      Log.d(LOG_TAG, "vh.getItemViewType : "+ String.valueOf(viewHolder.getItemViewType()));
        switch (viewHolder.getItemViewType()) {
            case TRAILER:
                ViewHolder_Trailer v_trailer = (ViewHolder_Trailer) viewHolder;
                configureViewHolder_Trailer(v_trailer, position);
                break;
        }

    }

    private void configureViewHolder_Trailer(ViewHolder_Trailer vh_trailer, final int position) {
//        Log.d(LOG_TAG, "configureViewHolder_Trailer");
        Trailer t = (Trailer) mDataset.get(position);

        if (t != null) {
            for (int i = 0; i <= mDataset.size(); i++) {
                Picasso.with(mContext).load("http://img.youtube.com/vi/" + mDataset.get(position).key + "/" + i + ".jpg").into(vh_trailer.mImageView);
            }
        }
        vh_trailer.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + mDataset.get(position).key)));
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //       Log.d(LOG_TAG, "onAttachedToRecyclerView" );
    }

    @Override
    public int getItemCount() {
//        Log.d(LOG_TAG, "getItemCount : " + String.valueOf(mDataset.size()));
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
//        Log.d(LOG_TAG, "getItemViewType");
        if (mDataset.get(position) instanceof Trailer) {
            return TRAILER;
        }
        return -1;
    }
}