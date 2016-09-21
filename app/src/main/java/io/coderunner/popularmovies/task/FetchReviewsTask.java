package io.coderunner.popularmovies.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import io.coderunner.popularmovies.R;
import io.coderunner.popularmovies.data.ReviewAdapter;
import io.coderunner.popularmovies.data.gson.Review;
import io.coderunner.popularmovies.data.gson.Reviews;
import io.coderunner.popularmovies.util.Utility;

public class FetchReviewsTask extends AsyncTask<String, Void, Reviews> {

    private final String LOG_TAG = Utility.getLogTag(this.getClass());
    private Context mContext;
    private ReviewAdapter mReviewAdapter;

    public FetchReviewsTask(ReviewAdapter adapter, Context context) {
        mReviewAdapter = adapter;
        mContext = context;
    }

    @Override
    protected void onPostExecute(Reviews reviews) {
        if(reviews != null) {
            for (Review review : reviews.reviews) {
                Log.d(LOG_TAG, "adding author: " + review.author);
                mReviewAdapter.add(review);
            }
        }
    }

    protected Reviews doInBackground(String... params) {
        final String movieID = params[0];
        try {
            //Connect to The Movie DB
            URL url = new URL(Utility.buildMovieEndpointUri(mContext, movieID, mContext.getString(R.string.tmdb_api_ep_reviews)).toString());
            String retrievedJson = Utility.retrieveData(LOG_TAG, url);
            Log.d(LOG_TAG, retrievedJson);
            Gson gson = new Gson();
            Reviews reviews = gson.fromJson(retrievedJson, Reviews.class);
            return reviews;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: ", e);
            return null;
        }
    }
}