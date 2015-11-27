package net.tbennett.popularmovies.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.tbennett.popularmovies.R;
import net.tbennett.popularmovies.data.gson.Movie;
import net.tbennett.popularmovies.util.Utility;

public class MovieDetailFragment extends Fragment {

    private Movie mMovie;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        mContext = getContext();

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            mMovie = (Movie) intent.getParcelableExtra(Intent.EXTRA_TEXT);

            ImageView backdropImage = (ImageView) rootView.findViewById(R.id.movie_backdrop);
            Picasso.with(mContext).load(Utility.buildImageUri(mContext, mMovie.backdropPath, Utility.ImageType.BACKDROP)).into(backdropImage);

            ImageView image = (ImageView) rootView.findViewById(R.id.movie_image);
            Picasso.with(mContext).load(Utility.buildImageUri(mContext, mMovie.posterPath, Utility.ImageType.IMAGE)).into(image);

            TextView title = (TextView) rootView.findViewById(R.id.movie_detail_title);
            title.setText(mMovie.title);

            TextView plot = (TextView) rootView.findViewById(R.id.movie_detail_plot);
            plot.setText(mContext.getString(R.string.format_plot, mMovie.plotSynopsis));

            TextView rating = (TextView) rootView.findViewById(R.id.movie_detail_rating);
            rating.setText(mContext.getString(R.string.format_rating, mMovie.voteAverage));

            TextView releaseDate = (TextView) rootView.findViewById(R.id.movie_detail_releasedate);
            releaseDate.setText(mContext.getString(R.string.format_releasedate, mMovie.releaseDate));
        }

        return rootView;
    }
}
