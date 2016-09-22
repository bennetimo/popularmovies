package io.coderunner.popularmovies.data.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.coderunner.popularmovies.R;
import io.coderunner.popularmovies.data.gson.Trailer;

/**
 * An adaptor to render trailers for a particular movie
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {

    public TrailerAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = getContext();
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        //Recycle the view if possible
        LinearLayout view = (convertView == null) ? (LinearLayout) inflater.inflate(R.layout.trailer, null) :
                (LinearLayout) convertView;

        TextView trailer = (TextView) view.findViewById(R.id.trailer_name);

        Trailer thisTrailer = getItem(position);

        trailer.setText(context.getString(R.string.format_trailer, thisTrailer.name));
        return view;
    }
}
