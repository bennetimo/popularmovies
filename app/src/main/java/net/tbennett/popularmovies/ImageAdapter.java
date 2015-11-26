package net.tbennett.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class ImageAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater inflater;

    private int[] dummyImages = new int[50];

    public ImageAdapter(Context c) {
        mContext = c;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Arrays.fill(dummyImages, 0, 50, R.mipmap.ic_launcher);
    }

    @Override
    public int getCount() {
        return dummyImages.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Recycle the view if possible
        ImageView view = (convertView == null) ? (ImageView)inflater.inflate(R.layout.movie_tile, null).findViewById(R.id.movie_tile) :
                (ImageView) convertView;
        Picasso.with(mContext).load(dummyImages[position]).into(view);
        return view;
    }
}
