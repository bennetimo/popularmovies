package net.tbennett.popularmovies.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import net.tbennett.popularmovies.activity.SettingsActivity;
import net.tbennett.popularmovies.data.ImageAdapter;
import net.tbennett.popularmovies.R;
import net.tbennett.popularmovies.data.InfiniteListScroller;
import net.tbennett.popularmovies.util.Utility;
import net.tbennett.popularmovies.activity.MovieDetailActivity;
import net.tbennett.popularmovies.data.gson.Movie;
import net.tbennett.popularmovies.data.gson.Movies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MoviesFragment extends Fragment {

    private final String LOG_TAG = Utility.getLogTag(this.getClass());
    private ImageAdapter mImageAdapter;
    private SharedPreferences mSharedPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_movies, container, false);

        mImageAdapter = new ImageAdapter(getActivity(), R.layout.movie_tile);
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        GridView movieView = (GridView) fragmentView.findViewById(R.id.grid_movies);
        movieView.setAdapter(mImageAdapter);
        movieView.setOnScrollListener(new InfiniteListScroller() {
            @Override
            public boolean onLoadMore(int page) {
                Log.v(LOG_TAG, "Retrieving movie page: #" + page);
                retrieveMovies(page);
                return true;
            }
        });

        movieView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Launch the movie detail activity for this movie
                Movie thisMovie = mImageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class).putExtra(Intent.EXTRA_TEXT, thisMovie);
                startActivity(intent);
            }
        });

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Initially populate the movies
        retrieveMovies(1);
    }

    /**
     * Retrieve movie data from TMDB using the sort order specified in the preferences
     */
    private void retrieveMovies(int page) {
        String sortOrder = mSharedPref.getString(getString(R.string.pref_key_sort), getString(R.string.pref_default_sort));
        new FetchMoviesTask().execute(sortOrder, "" + page);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                retrieveMovies(1);
                return true;
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movies> {

        private final String LOG_TAG = Utility.getLogTag(this.getClass());

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
                URL url = new URL(Utility.buildMovieDBUri(getContext(), sortBy, page).toString());
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
                Log.d(LOG_TAG, "Retrieved: " + retrievedJson);

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

}
