package net.tbennett.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.tbennett.popularmovies.data.gson.Movie;

public class MovieDetailFragment extends Fragment {

    private Movie thisMovie;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            thisMovie = (Movie) intent.getParcelableExtra(Intent.EXTRA_TEXT);
            TextView title = (TextView) rootView.findViewById(R.id.movie_detail_title);
            title.setText(thisMovie.title);
        }

        return rootView;
    }
}
