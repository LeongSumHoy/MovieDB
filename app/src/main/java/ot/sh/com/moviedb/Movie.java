package ot.sh.com.moviedb;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by brandon on 30/10/2015.
 */

public class Movie implements Parcelable {
    String id;
    String url;
    String title;
    String plot;
    String rating;
    String release_date;

    public Movie(String id, String url, String title, String plot, String rating, String release_date) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.plot = plot;
        this.rating = rating;
        this.release_date = release_date;
    }

    private Movie(Parcel in) {
        id = in.readString();
        url = in.readString();
        title = in.readString();
        plot = in.readString();
        rating = in.readString();
        release_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int i) {
        p.writeString(id);
        p.writeString(url);
        p.writeString(title);
        p.writeString(plot);
        p.writeString(rating);
        p.writeString(release_date);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public  Movie createFromParcel(Parcel p) {
            return new Movie(p);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}