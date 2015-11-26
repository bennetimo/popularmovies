package net.tbennett.popularmovies.util;

import android.content.Context;
import android.net.Uri;

import net.tbennett.popularmovies.BuildConfig;
import net.tbennett.popularmovies.R;

public class Utility {

    public static String getLogTag(Class clazz) { return clazz.getSimpleName(); }

    public static Uri buildMovieDBUri(Context c, String sortBy) {
        Uri builtUri = Uri.parse(c.getString(R.string.tmdb_api_base)).buildUpon()
                .appendPath(c.getString(R.string.tmdb_api_version))
                .appendEncodedPath(c.getString(R.string.tmdb_api_ep_discover))
                .appendQueryParameter(c.getString(R.string.tmdb_api_qp_sort_order), sortBy)
                .appendQueryParameter(c.getString(R.string.tmdb_api_qp_api_key), BuildConfig.API_KEY)
                .build();
        return builtUri;
    }

    public static Uri buildImageUri(Context c, String imagePath) {
        Uri builtUri = Uri.parse(c.getString(R.string.tmdb_image_base)).buildUpon()
                .appendPath(c.getString(R.string.tmdb_image_size))
                .appendEncodedPath(imagePath)
                .build();
        return builtUri;
    }

}
