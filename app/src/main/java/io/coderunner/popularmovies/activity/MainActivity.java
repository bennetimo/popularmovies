package io.coderunner.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.coderunner.popularmovies.data.gson.Movie;
import io.coderunner.popularmovies.fragment.MovieDetailFragment;
import io.coderunner.popularmovies.fragment.MoviesFragment;
import io.coderunner.popularmovies.R;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {

    private final String DETAIL_FRAGMENT_TAG = "DFTAG_MOVIES";

    // Whether we are in tablet (two-pane) mode or not
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            // In two-pane mode, show the detail view
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(), DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in adjacent fragment
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailFragment.DETAIL_URI, movie);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            // In one-pane mode, start an activity for the details
            Intent intent = new Intent(this, MovieDetailActivity.class).putExtra(Intent.EXTRA_TEXT, movie);
            startActivity(intent);
        }
    }

    @Override
    public void loadFirstItem(Movie movie) {
        // If we're two-pane, auto select the first item to populate the details fragment
        if(mTwoPane){
            onItemSelected(movie);
        }
    }
}
