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

    @Override
    public String toString() {
        return "Movies{" +
                "page=" + page +
                ", movies=" + movies +
                '}';
    }
}
