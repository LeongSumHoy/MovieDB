package ot.sh.com.moviedb;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brandon on 19/12/2015.
 */

public class Trailer implements Parcelable {
    String id;
    String iso_639_1;
    String key;
    String name;
    String site;
    String size;
    String type;

    public Trailer(String id, String iso_639_1, String key, String name, String site, String size, String type) {
        this.id = id;
        this.iso_639_1 = iso_639_1;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    private Trailer(Parcel in) {
        id = in.readString();
        iso_639_1 = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int i) {
        p.writeString(id);
        p.writeString(iso_639_1);
        p.writeString(key);
        p.writeString(name);
        p.writeString(site);
        p.writeString(size);
        p.writeString(type);
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel p) {
            return new Trailer(p);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}