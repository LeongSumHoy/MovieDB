package ot.sh.com.moviedb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by brandon on 20/03/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();
    ArrayList<Review> mDataset = new ArrayList<Review>();
    Context mContext;
    private final int REVIEW = 0;

    public static class ViewHolder_Review extends RecyclerView.ViewHolder {
        public TextView tvAuthor;
        public TextView tvContent;

        public ViewHolder_Review (View v) {
            super(v);
            tvAuthor = (TextView) v.findViewById(R.id.author_textview);
            tvContent = (TextView) v.findViewById(R.id.content_textview);
        }
    }

    public ReviewAdapter(Context context, ArrayList<Review> dataset) {
        mContext = context;
        mDataset = dataset;
    }

    @Override
    public ReviewAdapter.ViewHolder_Review onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_view, parent, false);
        ViewHolder_Review vh = new ViewHolder_Review(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        Log.d(LOG_TAG, "onBindViewHolder");

        switch (viewHolder.getItemViewType()) {
            case REVIEW :
                ViewHolder_Review v_review = (ViewHolder_Review) viewHolder;
                configureViewHolder_Review(v_review, position);
                break;
        }
    }

    private void configureViewHolder_Review(ViewHolder_Review vh_review, final int position) {
        Log.d(LOG_TAG, "configureViewHolder_Review");
        Review r = (Review) mDataset.get(position);

        if (r != null) {
            for(int i=0; i <= mDataset.size(); i++) {
                vh_review.tvAuthor.setText(mDataset.get(position).author);
                vh_review.tvContent.setText(mDataset.get(position).content);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "getItemCount : " + String.valueOf(mDataset.size()));
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(LOG_TAG, "getItemViewType");

        if (mDataset.get(position) instanceof Review) {
            return REVIEW;
        }
        return -1;
    }

}
