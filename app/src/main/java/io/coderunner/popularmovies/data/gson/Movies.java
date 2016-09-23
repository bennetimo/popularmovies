package io.coderunner.popularmovies.data.gson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * GSON class representing the full movie results retrieved from TMDB API
 */
public class Movies {
    public int page;
    @SerializedName("results")
    public List<Movie> movies = new ArrayList<Movie>();

    public Movies(List<Movie> movies, int page) {
        this.movies = movies;
        this.page = page;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "page=" + page +
                ", movies=" + movies +
                '}';
    }
}
