package io.coderunner.popularmovies.data.gson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.coderunner.popularmovies.data.MovieData;

/**
 * GSON class representing the trailers retrieved from TMDB API
 */
public class Trailers implements MovieData<Trailer> {
    @SerializedName("results")
    public List<Trailer> trailers = new ArrayList<Trailer>();

    @Override
    public String toString() {
        return "Trailers{" +
                ", reviews=" + trailers +
                '}';
    }

    @Override
    public List<Trailer> getData() {
        return trailers;
    }
}
