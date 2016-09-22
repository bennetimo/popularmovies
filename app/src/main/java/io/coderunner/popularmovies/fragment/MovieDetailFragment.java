package io.coderunner.popularmovies.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.coderunner.popularmovies.R;
import io.coderunner.popularmovies.data.adapter.ReviewAdapter;
import io.coderunner.popularmovies.data.adapter.TrailerAdapter;
import io.coderunner.popularmovies.data.gson.Movie;
import io.coderunner.popularmovies.data.gson.Reviews;
import io.coderunner.popularmovies.data.gson.Trailer;
import io.coderunner.popularmovies.data.gson.Trailers;
import io.coderunner.popularmovies.task.FetchMovieDataTask;
import io.coderunner.popularmovies.util.Utility;

/**
 * Displays details of a specific movie chosen by the user
 */
public class MovieDetailFragment extends Fragment {

    private Movie mMovie;
    private Context mContext;

    private ReviewAdapter mReviewAdapter;
    private ArrayAdapter<Trailer> mTrailersAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReviewAdapter = new ReviewAdapter(getActivity(), R.layout.review);
        mTrailersAdapter = new TrailerAdapter(getActivity(), R.layout.review);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        mContext = getContext();

        //Retrieve the dimensions for the images
        int backdropImageWidth = mContext.getResources().getDimensionPixelSize(R.dimen.movie_backdrop_width);
        int backdropImageHeight = mContext.getResources().getDimensionPixelSize(R.dimen.movie_backdrop_height);
        int imageWidth = mContext.getResources().getDimensionPixelSize(R.dimen.movie_thumb_width);
        int imageHeight = mContext.getResources().getDimensionPixelSize(R.dimen.movie_thumb_height);

        //Retrieve the details for the selected movie
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

            ListView reviews = (ListView) rootView.findViewById(R.id.movie_detail_reviews);
            reviews.setAdapter(mReviewAdapter);
            retrieveReviews();

            ListView trailers = (ListView) rootView.findViewById(R.id.movie_detail_trailers);
            trailers.setAdapter(mTrailersAdapter);

            trailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Launch the movie detail activity for this movie
                    Trailer trailer = mTrailersAdapter.getItem(position);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.key));
                    startActivity(intent);
                }
            });

            retrieveTrailers();
        }

        return rootView;
    }

    private void retrieveReviews() {
        String endpoint = mContext.getString(R.string.tmdb_api_ep_reviews);
        new FetchMovieDataTask<>(mReviewAdapter, getContext(), Reviews.class).execute("" + mMovie.id, endpoint);
    }

    private void retrieveTrailers() {
        String endpoint = mContext.getString(R.string.tmdb_api_ep_trailers);
        new FetchMovieDataTask<>(mTrailersAdapter, getContext(), Trailers.class).execute("" + mMovie.id, endpoint);
    }
}
