package io.coderunner.popularmovies.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import io.coderunner.popularmovies.data.MovieData;
import io.coderunner.popularmovies.util.Utility;

/**
 * Generic class to detailed data for a specific type T (trailers, reviews etc)
 * @param <T> individual item type, e.g. Movie
 * @param <U> collection type, holding T's, e.g. Movies
 */
public class FetchMovieDataTask<T, U extends MovieData<T>> extends AsyncTask<String, Void, U> {

    private final String LOG_TAG = Utility.getLogTag(this.getClass());
    private Context mContext;
    private ArrayAdapter<T> mAdapter;
    private Class<U> mClazz;

    public FetchMovieDataTask(ArrayAdapter<T> adapter, Context context, Class<U> clazz) {
        mAdapter = adapter;
        mContext = context;
        mClazz = clazz;
    }

    @Override
    protected void onPostExecute(U items) {
        if(items != null) {
            for (T item : items.getData()) {
                mAdapter.add(item);
            }
        }
    }

    @Override
    protected U doInBackground(String... params) {
        final String movieID = params[0];
        final String movieEndpoint = params[1];
        try {
            //Connect to The Movie DB
            URL url = new URL(Utility.buildMovieEndpointUri(mContext, movieID, movieEndpoint).toString());
            String retrievedJson = Utility.retrieveData(LOG_TAG, url);
            Log.d(LOG_TAG, retrievedJson);
            Gson gson = new Gson();
            U items = gson.fromJson(retrievedJson, mClazz);
            return items;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: ", e);
            return null;
        }
    }
}