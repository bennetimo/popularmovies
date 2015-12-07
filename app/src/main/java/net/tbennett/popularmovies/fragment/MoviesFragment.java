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
import net.tbennett.popularmovies.task.FetchMoviesTask;
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

/**
 * Displays all of the movies in a grid
 */
public class MoviesFragment extends Fragment {

    private final String LOG_TAG = Utility.getLogTag(this.getClass());
    private ImageAdapter mImageAdapter;
    private SharedPreferences mSharedPref;
    private String mCurrentSortOrder;
    private InfiniteListScroller mScroller;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); //Retain this fragment on rotation etc
        setHasOptionsMenu(true);
        mImageAdapter = new ImageAdapter(getActivity(), R.layout.movie_tile);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_movies, container, false);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mCurrentSortOrder = getCurrentSortOrder();

        GridView movieView = (GridView) fragmentView.findViewById(R.id.grid_movies);
        movieView.setAdapter(mImageAdapter);

        mScroller = new InfiniteListScroller() {
            @Override
            public boolean onLoadMore(int page) {
                retrieveMovies(page);
                return true;
            }
        };
        movieView.setOnScrollListener(mScroller);

        movieView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Launch the movie detail activity for this movie
                Movie thisMovie = mImageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class).putExtra(Intent.EXTRA_TEXT, thisMovie);
                startActivity(intent);
            }
        });

        if(savedInstanceState == null){
            //Initially populate the movies
            retrieveMovies(1);
        }

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Check to see if the sort order has changed, and if it has retrieve the movies again
        if(mSharedPref != null){
            String sortOrder = getCurrentSortOrder();
            if(mCurrentSortOrder != sortOrder){
                mCurrentSortOrder = sortOrder;
                mImageAdapter.clear();
                mScroller.resetPreviousTotalItemCount();
            }
        }
    }

    //Retrieves the current sorting preference
    private String getCurrentSortOrder(){
        if(mSharedPref != null) {
            return mSharedPref.getString(getString(R.string.pref_key_sort), getString(R.string.pref_default_sort));
        }
        return null;
    }

    /**
     * Retrieve movie data from TMDB using the sort order specified in the preferences
     */
    private void retrieveMovies(int page) {
        String sortOrder = getCurrentSortOrder();
        Log.d(LOG_TAG, "Retrieving movies page: " + page);
        new FetchMoviesTask(mImageAdapter, getContext()).execute(sortOrder, "" + page);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
