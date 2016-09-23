package io.coderunner.popularmovies.task;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.coderunner.popularmovies.data.MovieContract;
import io.coderunner.popularmovies.data.adapter.ImageAdapter;
import io.coderunner.popularmovies.data.gson.Movie;
import io.coderunner.popularmovies.data.gson.Movies;
import io.coderunner.popularmovies.util.Utility;

public class DisplayFavouritesTask extends AsyncTask<String, Void, Movies> {

    private final String LOG_TAG = Utility.getLogTag(this.getClass());
    private ImageAdapter mImageAdapter;
    private Context mContext;

    public DisplayFavouritesTask(ImageAdapter adapter, Context context) {
        mImageAdapter = adapter;
        mContext = context;
    }

    @Override
    protected void onPostExecute(Movies movies) {
        if(movies != null) {
            for (Movie movie : movies.movies) {
                mImageAdapter.add(movie);
            }
        }
    }

    protected Movies doInBackground(String... params) {
        Movies movies = new Movies(getFavourites(), 1);
        return movies;
    }

    private List<Movie> getFavourites(){
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);

        List<Movie> movies = new ArrayList<Movie>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String backdropPath = cursor.getString(cursor
                        .getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH));
                long id = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                String originalLanguage = cursor.getString(cursor
                        .getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE));
                String originalTitle = cursor.getString(cursor
                        .getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
                String plotSynopsis = cursor.getString(cursor
                        .getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSYS));
                double popularity = cursor.getDouble(cursor
                        .getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_POPULARITY));
                String posterPath = cursor.getString(cursor
                        .getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
                String releaseDate = cursor.getString(cursor
                        .getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_TITLE));
                double voteAverage = cursor.getDouble(cursor
                        .getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));

                Movie movie = new Movie(
                    backdropPath,
                    id,
                    originalLanguage,
                    originalTitle,
                    plotSynopsis,
                    popularity,
                    posterPath,
                    releaseDate,
                    title,
                    voteAverage
                );

                movies.add(movie);
            }
            cursor.close();
        }
        return movies;
    }
}