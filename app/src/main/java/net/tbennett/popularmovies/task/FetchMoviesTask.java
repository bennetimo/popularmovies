package net.tbennett.popularmovies.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import net.tbennett.popularmovies.data.ImageAdapter;
import net.tbennett.popularmovies.data.gson.Movie;
import net.tbennett.popularmovies.data.gson.Movies;
import net.tbennett.popularmovies.util.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            //Connect to The Movie DB
            URL url = new URL(Utility.buildMovieDBUri(mContext, sortBy, page).toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Retrieve the results
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            //Empty input
            if(inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0) {
                Log.d(LOG_TAG, "The response from TMDB was empty");
                return null;
            }

            String retrievedJson = buffer.toString();

            Gson gson = new Gson();
            Movies movies = gson.fromJson(retrievedJson, Movies.class);
            return movies;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: ", e);
            return null;
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }
}