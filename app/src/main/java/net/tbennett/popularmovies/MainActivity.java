package net.tbennett.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private final String OVERVIEW_FRAGMENT_TAG = "FTAG_OVERVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            //Add the overview fragment to the view
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new OverviewFragment(), OVERVIEW_FRAGMENT_TAG)
                .commit();
        }
    }
}
