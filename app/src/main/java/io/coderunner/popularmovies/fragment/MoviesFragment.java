package io.coderunner.popularmovies.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
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

import io.coderunner.popularmovies.activity.SettingsActivity;
import io.coderunner.popularmovies.data.adapter.ImageAdapter;
import io.coderunner.popularmovies.R;
import io.coderunner.popularmovies.data.InfiniteListScroller;
import io.coderunner.popularmovies.task.DisplayFavouritesTask;
import io.coderunner.popularmovies.task.FetchMoviesTask;
import io.coderunner.popularmovies.util.Utility;
import io.coderunner.popularmovies.activity.MovieDetailActivity;
import io.coderunner.popularmovies.data.gson.Movie;

/**
 * Displays all of the movies in a grid
 */
public class MoviesFragment extends Fragment {

    private final String LOG_TAG = Utility.getLogTag(this.getClass());
    private ImageAdapter mImageAdapter;
    private SharedPreferences mSharedPref;
    private String mCurrentSortOrder;
    private InfiniteListScroller mScroller;

    public interface Callback {
        void onItemSelected(Movie movie);
        void loadFirstItem(Movie movie);
    }

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

        final GridView movieView = (GridView) fragmentView.findViewById(R.id.grid_movies);
        movieView.setAdapter(mImageAdapter);

        // Select the first movie if we're starting and nothing is selected
        mImageAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(movieView.getSelectedItem() == null && mImageAdapter.getCount() > 0) {
                    Movie thisMovie = mImageAdapter.getItem(0);
                    ((Callback) getActivity()).loadFirstItem(thisMovie);
                }
            }
        });

        mScroller = new InfiniteListScroller() {
            @Override
            public boolean onLoadMore(int page) {
                //If displaying favourites, there are no more items to load
                boolean allowLoad = (mCurrentSortOrder.equals(getString(R.string.pref_array_sort_favourite))) ? false : true;
                if(allowLoad)
                    retrieveMovies(page);
                return true;
            }
        };
        movieView.setOnScrollListener(mScroller);

        movieView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie thisMovie = mImageAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(thisMovie);
            }
        });

        if(savedInstanceState == null || mImageAdapter.isEmpty()){
            //Initially populate the movies. This happens after the activity has been destroyed and
            //recreated also
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
                retrieveMovies(1);
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

        if(sortOrder.equals(getString(R.string.pref_array_sort_favourite))){
            Log.d(LOG_TAG, "Retrieving favourite movies: " + page);
            new DisplayFavouritesTask(mImageAdapter, getContext()).execute();
        } else {
            Log.d(LOG_TAG, "Retrieving movies page: " + page);
            new FetchMoviesTask(mImageAdapter, getContext()).execute(sortOrder, "" + page);
        }
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
