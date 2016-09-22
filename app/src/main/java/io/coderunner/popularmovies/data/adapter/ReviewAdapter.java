package io.coderunner.popularmovies.data.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.coderunner.popularmovies.R;
import io.coderunner.popularmovies.data.gson.Review;

/**
 * An adaptor to render reviews for a particular movie
 */
public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = getContext();
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        //Recycle the view if possible
        LinearLayout view = (convertView == null) ? (LinearLayout) inflater.inflate(R.layout.review, null) :
                (LinearLayout) convertView;

        TextView review = (TextView) view.findViewById(R.id.review_content);

        Review thisReview = getItem(position);

        review.setText(Html.fromHtml(context.getString(R.string.format_review, thisReview.author, thisReview.content)));
        return view;
    }
}
