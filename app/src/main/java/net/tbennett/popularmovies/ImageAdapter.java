package net.tbennett.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class ImageAdapter extends BaseAdapter{

    private Context mContext;

    private int[] dummyImages = new int[50];

    public ImageAdapter(Context c) {
        mContext = c;
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
        ImageView view = (convertView == null) ? new ImageView(mContext) : (ImageView) convertView;
        Picasso.with(mContext).load(dummyImages[position]).into(view);
        return view;
    }
}
