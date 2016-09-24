package io.coderunner.popularmovies.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import io.coderunner.popularmovies.BuildConfig;
import io.coderunner.popularmovies.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Helper methods
 */
public class Utility {

    //Backdrop images are displayed larger on the details page
    public enum ImageType {
        IMAGE, BACKDROP
    }

    public static String getLogTag(Class clazz) { return clazz.getSimpleName(); }

    /**
     * Constructs a URI for accessing TMDB /discover API
     * @param c Context
     * @param sortBy the sort order query param to use
     * @param page the page number query param to use
     * @return the constructed URI
     */
    public static Uri buildDiscoverUri(Context c, String sortBy, String page) {
        Uri builtUri = Uri.parse(c.getString(R.string.tmdb_api_base)).buildUpon()
                .appendPath(c.getString(R.string.tmdb_api_version))
                .appendEncodedPath(c.getString(R.string.tmdb_api_ep_discover))
                .appendQueryParameter(c.getString(R.string.tmdb_api_qp_sort_order), sortBy)
                .appendQueryParameter(c.getString(R.string.tmdb_api_qp_page), page)
                .appendQueryParameter(c.getString(R.string.tmdb_api_qp_api_key), BuildConfig.API_KEY)
                .build();
        return builtUri;
    }

    /**
     * Makes a connection to the given URL, returning the data as a string
     * @param LOG_TAG to use for debugging purposes
     * @param url to load
     * @return
     */
    public static String retrieveData(String LOG_TAG, URL url) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            //Connect to The Movie DB
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Retrieve the results
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            //Empty input
            if(inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0) {
                Log.d(LOG_TAG, "The response from TMDB was empty");
                return null;
            }

            String retrievedJson = buffer.toString();
            return retrievedJson;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: ", e);
            return null;
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    /**
     * Constructs a URI for accessing data from a particular movie
     * @param c Context
     * @param movieID the ID of the movie
     * @param endpoint the endpoint to return data for (e.g. 'videos', 'reviews')
     * @return the constructed URI
     */
    public static Uri buildMovieEndpointUri(Context c, String movieID, String endpoint) {
        Uri builtUri = Uri.parse(c.getString(R.string.tmdb_api_base)).buildUpon()
                .appendPath(c.getString(R.string.tmdb_api_version))
                .appendPath(c.getString(R.string.tmdb_api_movie))
                .appendEncodedPath(movieID)
                .appendEncodedPath(endpoint)
                .appendQueryParameter(c.getString(R.string.tmdb_api_qp_api_key), BuildConfig.API_KEY)
                .build();
        return builtUri;
    }

    /**
     * Constructs a URI for accessing an image on TMDB
     * @param c Context
     * @param imagePath Relative path to the image
     * @param imageType Whether the image is a backdrop or standard
     * @return the constructed URI
     */
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

    /**
     * Given a yyyy-MM-dd formatted date string, returns just the year as yyyy
     * @param c Context
     * @param dateString to convert
     * @return the yyyy formatted date string, or "" if there was any problem parsing
     */
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
