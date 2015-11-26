package net.tbennett.popularmovies.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

    private ImageAdapter mImageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_movies, container, false);

        mImageAdapter = new ImageAdapter(getActivity(), R.layout.movie_tile);

        GridView movieView = (GridView) fragmentView.findViewById(R.id.grid_movies);
        movieView.setAdapter(mImageAdapter);

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
                new FetchMoviesTask().execute("popularity.desc");
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
                mImageAdapter.clear();
                for (Movie movie : movies.movies) {
                    mImageAdapter.add(movie);
                }
            }
        }

        protected Movies doInBackground(String... params) {
            if(params.length != 1) {
                Log.d(LOG_TAG, "FetchMoviesTask was called without the sort order specified");
            }
            final String sortBy = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                //Connect to The Movie DB
                URL url = new URL(Utility.buildMovieDBUri(getContext(), sortBy).toString());
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

                Log.d(LOG_TAG, "Built GSON: " + movies);
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
