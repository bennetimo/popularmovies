package io.coderunner.popularmovies.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import io.coderunner.popularmovies.R;
import io.coderunner.popularmovies.data.MovieContract;
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

            Button addFavourite = (Button) rootView.findViewById(R.id.movie_detail_favourite_button);
            addFavourite.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Favourite recorded",
                            Toast.LENGTH_LONG).show();
                    markFavourite();
                }
            });

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
            getFavourites();
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

    private void markFavourite(){
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.id);
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, mMovie.backdropPath);
        values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, mMovie.originalLanguage);
        values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, mMovie.originalTitle);
        values.put(MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSYS, mMovie.plotSynopsis);
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.releaseDate);
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, mMovie.posterPath);
        values.put(MovieContract.MovieEntry.COLUMN_POPULARITY, mMovie.popularity);
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.voteAverage);
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.title);
        getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
    }

    private void getFavourites(){
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        String[] projection = { MovieContract.MovieEntry.COLUMN_TITLE, MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSYS};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null,
                null);


        String favourite = "Favourites: ";
        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String plot = cursor.getString(cursor
                        .getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSYS));
                Log.d("plot: ",  plot);

                favourite = favourite + ", " + cursor.getString(cursor
                        .getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_TITLE));

                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d("movie", mMovie.title);
        Log.d("test", favourite);
    }
}
