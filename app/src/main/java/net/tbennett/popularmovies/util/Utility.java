package net.tbennett.popularmovies.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import net.tbennett.popularmovies.BuildConfig;
import net.tbennett.popularmovies.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    public enum ImageType {
        IMAGE, BACKDROP
    }

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

    public static Uri buildImageUri(Context c, String imagePath, ImageType imageType) {
        String imageSize;
        switch(imageType) {
            case IMAGE: imageSize = c.getString(R.string.tmdb_image_size);
                break;
            case BACKDROP: imageSize = c.getString(R.string.tmdb_backdrop_size);
                break;
            default: imageSize = c.getString(R.string.tmdb_image_size);
                break;
        }
        Uri builtUri = Uri.parse(c.getString(R.string.tmdb_image_base)).buildUpon()
                .appendPath(imageSize)
                .appendEncodedPath(imagePath)
                .build();
        return builtUri;
    }

    public static String getShortDateString(Context c, String dateString) {
        SimpleDateFormat fromFormat = new SimpleDateFormat(c.getString(R.string.date_format_tmdb_api), Locale.getDefault());
        SimpleDateFormat toFormat = new SimpleDateFormat(c.getString(R.string.date_format_year), Locale.getDefault());

        try {
            Date fromDate = fromFormat.parse(dateString);
            return toFormat.format(fromDate);
        } catch (ParseException e) {
            Log.e(getLogTag(Utility.class), "Problem parsing date: ", e);
            return ""; //Use a blank string instead
        }
    }

}
