package io.coderunner.popularmovies.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import io.coderunner.popularmovies.data.ImageAdapter;
import io.coderunner.popularmovies.data.gson.Movie;
import io.coderunner.popularmovies.data.gson.Movies;
import io.coderunner.popularmovies.util.Utility;

import java.io.IOException;
import java.net.URL;

public class FetchMoviesTask extends AsyncTask<String, Void, Movies> {

    private final String LOG_TAG = Utility.getLogTag(this.getClass());
    private ImageAdapter mImageAdapter;
    private Context mContext;

    public FetchMoviesTask(ImageAdapter adapter, Context context) {
        mImageAdapter = adapter;
        mContext = context;
    }

    @Override
    protected void onPostExecute(Movies movies) {
        if(movies != null) {
            for (Movie movie : movies.movies) {
                mImageAdapter.add(movie);
            }
        }
    }

    protected Movies doInBackground(String... params) {
        if(params.length != 2) {
            Log.d(LOG_TAG, "FetchMoviesTask was called without the sort order or page specified");
        }
        final String sortBy = params[0];
        final String page = params[1];

        try {
            //Connect to The Movie DB
            URL url = new URL(Utility.buildDiscoverUri(mContext, sortBy, page).toString());
            String retrievedJson = Utility.retrieveData(LOG_TAG, url);
            Gson gson = new Gson();
            Movies movies = gson.fromJson(retrievedJson, Movies.class);
            return movies;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: ", e);
            return null;
        }
    }
}