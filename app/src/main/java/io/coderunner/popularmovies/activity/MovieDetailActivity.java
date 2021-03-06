package io.coderunner.popularmovies.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.coderunner.popularmovies.fragment.MovieDetailFragment;
import io.coderunner.popularmovies.R;

public class MovieDetailActivity extends AppCompatActivity {

    private final String FRAGMENT_TAG = "FTAG_MOVIE_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.DETAIL_URI, getIntent().getData());

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);

            //Add the movie detail fragment to the view
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.movie_detail_container, new MovieDetailFragment(), FRAGMENT_TAG)
                .commit();
        }
    }
}
