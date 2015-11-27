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

    public static Uri buildMovieDBUri(Context c, String sortBy, String page) {
        Uri builtUri = Uri.parse(c.getString(R.string.tmdb_api_base)).buildUpon()
                .appendPath(c.getString(R.string.tmdb_api_version))
                .appendEncodedPath(c.getString(R.string.tmdb_api_ep_discover))
                .appendQueryParameter(c.getString(R.string.tmdb_api_qp_sort_order), sortBy)
                .appendQueryParameter(c.getString(R.string.tmdb_api_qp_page), page)
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

        if(dateString == null) return ""; //Year is unknown

        try {
            Date fromDate = fromFormat.parse(dateString);
            return toFormat.format(fromDate);
        } catch (ParseException e) {
            Log.v(getLogTag(Utility.class), "Problem parsing date: " + dateString);
            return ""; //Use a blank string instead
        }
    }

    /**
     * If TMDB doesn't contain data for a particular item, display a human readable
     * text that there is no data
     * @param c Context
     * @param s String to check
     * @return the string unmodified if it has a value, or the R.string.default_text otherwise
     */
    public static String valueOrDefault(Context c, String s) {
        return (s == null || s.equals("null")) ? c.getString(R.string.default_text) : s;
    }

}
