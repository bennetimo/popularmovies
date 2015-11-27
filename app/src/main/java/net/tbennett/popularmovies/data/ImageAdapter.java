package net.tbennett.popularmovies.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.tbennett.popularmovies.R;
import net.tbennett.popularmovies.util.Utility;
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
        FrameLayout view = (convertView == null) ? (FrameLayout)inflater.inflate(R.layout.movie_tile, null) :
                (FrameLayout) convertView;

        ImageView image = (ImageView) view.findViewById(R.id.movie_image);
        TextView rating = (TextView) view.findViewById(R.id.movie_vote_average);

        //Retrieve and load the poster image for this movie
        Movie thisMovie = getItem(position);
        Picasso.with(context).load(Utility.buildImageUri(context, thisMovie.posterPath, Utility.ImageType.IMAGE)).into(image);
        rating.setText(context.getString(R.string.format_rating_overlay, thisMovie.voteAverage));
        return view;
    }
}
