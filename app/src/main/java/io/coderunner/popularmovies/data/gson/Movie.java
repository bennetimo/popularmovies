package io.coderunner.popularmovies.data.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * GSON class representing a single movie in TMDB API
 */
public class Movie implements Parcelable {
    @SerializedName("backdrop_path")
    public String backdropPath;
    public long id;
    @SerializedName("original_language")
    public String originalLanguage;
    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("overview")
    public String plotSynopsis;
    @SerializedName("release_date")
    public String releaseDate;
    @SerializedName("poster_path")
    public String posterPath;
    public double popularity;
    @SerializedName("vote_average")
    public double voteAverage;
    public String title;

    protected Movie(Parcel in) {
        backdropPath = in.readString();
        id = in.readLong();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        plotSynopsis = in.readString();
        releaseDate = in.readString();
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(backdropPath);
        dest.writeLong(id);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(plotSynopsis);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeDouble(popularity);
        dest.writeDouble(voteAverage);
        dest.writeString(title);
    }
}
