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

        int backdropImageWidth = mContext.getResources().getDimensionPixelSize(R.dimen.movie_backdrop_width);
        int backdropImageHeight = mContext.getResources().getDimensionPixelSize(R.dimen.movie_backdrop_height);
        int imageWidth = mContext.getResources().getDimensionPixelSize(R.dimen.movie_thumb_width);
        int imageHeight = mContext.getResources().getDimensionPixelSize(R.dimen.movie_thumb_height);

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            mMovie = (Movie) intent.getParcelableExtra(Intent.EXTRA_TEXT);

            ImageView backdropImage = (ImageView) rootView.findViewById(R.id.movie_backdrop);
            Picasso.with(mContext)
                    .load(Utility.buildImageUri(mContext, mMovie.backdropPath, Utility.ImageType.BACKDROP))
                    .placeholder(R.drawable.movie_backdrop_placeholder)
                    .resize(backdropImageWidth, backdropImageHeight)
                    .centerCrop()
                    .into(backdropImage);

            ImageView image = (ImageView) rootView.findViewById(R.id.movie_image);
            Picasso.with(mContext)
                    .load(Utility.buildImageUri(mContext, mMovie.posterPath, Utility.ImageType.IMAGE))
                    .placeholder(R.drawable.movie_thumb_placeholder)
                    .resize(imageWidth, imageHeight)
                    .centerCrop()
                    .into(image);

            TextView title = (TextView) rootView.findViewById(R.id.movie_detail_title);
            title.setText(Utility.valueOrDefault(mContext, mMovie.title));

            //Only set the original title if it is different to the title
            if(!mMovie.title.equals(mMovie.originalTitle)) {
                TextView originalTitle = (TextView) rootView.findViewById(R.id.movie_detail_original_title);
                originalTitle.setVisibility(View.VISIBLE);
                originalTitle.setText(Utility.valueOrDefault(mContext, mContext.getString(R.string.format_original_title, mMovie.originalTitle)));
            }

            TextView plot = (TextView) rootView.findViewById(R.id.movie_detail_plot);
            plot.setText(Utility.valueOrDefault(mContext, mContext.getString(R.string.format_plot, mMovie.plotSynopsis)));

            TextView rating = (TextView) rootView.findViewById(R.id.movie_detail_rating);
            rating.setText(Utility.valueOrDefault(mContext, mContext.getString(R.string.format_rating, mMovie.voteAverage)));

            TextView releaseDate = (TextView) rootView.findViewById(R.id.movie_detail_releasedate);
            releaseDate.setText(Utility.valueOrDefault(mContext, mContext.getString(R.string.format_releasedate, mMovie.releaseDate)));

            TextView originalLanguage = (TextView) rootView.findViewById(R.id.movie_detail_original_language);
            originalLanguage.setText(Utility.valueOrDefault(mContext, mContext.getString(R.string.format_original_language, mMovie.originalLanguage)));
        }

        return rootView;
    }
}
