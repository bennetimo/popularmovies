package io.coderunner.popularmovies.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.coderunner.popularmovies.fragment.MoviesFragment;
import io.coderunner.popularmovies.R;

public class MainActivity extends AppCompatActivity {

    private final String FRAGMENT_TAG = "FTAG_MOVIES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            //Add the movies fragment to the view
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_main, new MoviesFragment(), FRAGMENT_TAG)
                .commit();
        }
    }
}