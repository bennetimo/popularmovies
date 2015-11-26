package net.tbennett.popularmovies.data.gson;

import com.google.gson.annotations.SerializedName;

/**
 * GSON class representing a single movie in TMDB API
 */
public class Movie {
    public long id;
    @SerializedName("poster_path")
    public String posterPath;
    public double popularity;
    public String title;

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", posterPath='" + posterPath + '\'' +
                ", popularity=" + popularity +
                ", title='" + title + '\'' +
                '}';
    }
}
