package ot.sh.com.moviedb;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brandon on 30/10/2015.
 */

public class Review implements Parcelable {
    String id;
    String author;
    String content;
    String url;


    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.url = url;
        this.author = author;
        this.content = content;
    }

    private Review(Parcel in) {
        id = in.readString();
        url = in.readString();
        content = in.readString();
        author = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int i) {
        p.writeString(id);
        p.writeString(url);
        p.writeString(author);
        p.writeString(content);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel p) {
            return new Review(p);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}

