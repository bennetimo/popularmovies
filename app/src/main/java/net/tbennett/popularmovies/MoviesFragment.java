package net.tbennett.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MoviesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_movies, container, false);

        ImageAdapter imageAdapter = new ImageAdapter(getActivity());

        GridView movieView = (GridView) fragmentView.findViewById(R.id.grid_movies);
        movieView.setAdapter(imageAdapter);

        movieView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Loading position " + position, Toast.LENGTH_SHORT).show();
            }
        });

        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = Utility.getLogTag(this.getClass());

        protected Void doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String apiKey = "&api_key=" + BuildConfig.API_KEY;
            String path = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc" + apiKey;

            String retrievedJson = null;

            try {
                //Connect to The Movie DB
                URL url = new URL(path);
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

                retrievedJson = buffer.toString();
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
            return null;
        }
    }

}
