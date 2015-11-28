package net.tbennett.popularmovies.data;

import android.widget.AbsListView;

/**
 * Endless scrolling class inspired by https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews
 */
public abstract class InfiniteListScroller implements AbsListView.OnScrollListener {

    //The maximum page that can be loaded
    private int maxPage = 100;
    //How many items need to be left to scroll before we start loading more
    private int remainingToScrollThreshold = 5;
    //The total items we had in our view before the last load
    private int previousTotalItemCount = 0;
    //Whether a load is currently taking place
    private boolean loading = true;
    //Number of items returned per page of TMDB API
    private int itemsPerPage = 20;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //How many items are not yet in view and still remain scrollable
        int remainingToScroll = totalItemCount - (firstVisibleItem + visibleItemCount);
        int nextPage = (totalItemCount/itemsPerPage) + 1; //Calculate the next page based on the number of items loaded

        //Check if a current load in progress has finished
        if(totalItemCount > previousTotalItemCount){
            //We must have loaded something
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        //Check if we need to load more
        if(!loading && remainingToScroll <= remainingToScrollThreshold && nextPage <= maxPage){
            //We're nearing the end of the list, load more data (but only if we're not already doing that)
            loading = onLoadMore(nextPage);
        }
    }

    public void resetPreviousTotalItemCount() {
        previousTotalItemCount = 0;
    }

    public abstract boolean onLoadMore(int page);
}
