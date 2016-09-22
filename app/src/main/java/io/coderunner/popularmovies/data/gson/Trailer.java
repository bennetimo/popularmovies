package io.coderunner.popularmovies.data.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * GSON class representing a single trailer in TMDB API
 */
public class Trailer implements Parcelable {
    public String id;
    public String key;
    public String name;
    public String site;
    public double size;
    public String type;

    protected Trailer(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readDouble();
        type = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeDouble(size);
        dest.writeString(type);
    }
}
