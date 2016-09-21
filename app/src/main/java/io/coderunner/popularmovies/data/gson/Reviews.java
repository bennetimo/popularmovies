package io.coderunner.popularmovies.data.gson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * GSON class representing the full movie reviews retrieved from TMDB API
 */
public class Reviews {
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
}
