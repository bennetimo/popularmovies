package net.tbennett.popularmovies;

import android.content.Context;
import android.net.Uri;

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

}
