package net.tbennett.popularmovies.data.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * GSON class representing a single movie in TMDB API
 */
public class Movie implements Parcelable {
    public long id;
    @SerializedName("poster_path")
    public String posterPath;
    public double popularity;
    @SerializedName("vote_average")
    public double voteAverage;
    public String title;

    protected Movie(Parcel in) {
        id = in.readLong();
        posterPath = in.readString();
        popularity = in.readDouble();
        voteAverage = in.readDouble();
        title = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", posterPath='" + posterPath + '\'' +
                ", popularity=" + popularity +
                ", voteAverage=" + voteAverage +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(posterPath);
        dest.writeDouble(popularity);
        dest.writeDouble(voteAverage);
        dest.writeString(title);
    }
}
