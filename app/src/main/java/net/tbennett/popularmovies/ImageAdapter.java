package net.tbennett.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.tbennett.popularmovies.data.gson.Movie;

public class ImageAdapter extends ArrayAdapter<Movie> {
    public ImageAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = getContext();
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        //Recycle the view if possible
        ImageView view = (convertView == null) ? (ImageView)inflater.inflate(R.layout.movie_tile, null).findViewById(R.id.movie_tile) :
                (ImageView) convertView;

        //Retrieve and load the poster image for this movie
        Movie thisMovie = getItem(position);
        Picasso.with(context).load(Utility.buildImageUri(context, thisMovie.posterPath)).into(view);
        return view;
    }
}
