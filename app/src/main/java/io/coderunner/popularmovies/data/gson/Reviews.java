package io.coderunner.popularmovies.data.gson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.coderunner.popularmovies.data.MovieData;

/**
 * GSON class representing the full movie reviews retrieved from TMDB API
 */
public class Reviews implements MovieData<Review> {
    public int page;
    @SerializedName("results")
    public List<Review> reviews = new ArrayList<Review>();

    @Override
    public String toString() {
        return "Reviews{" +
                "page=" + page +
                ", reviews=" + reviews +
                '}';
    }

    @Override
    public List<Review> getData() {
        return reviews;
    }
}
