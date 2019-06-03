package com.example.julianonote.feeddenotcias;

import android.content.Context;
import android.content.AsyncTaskLoader;
import java.util.List;

public class FeedLoader extends AsyncTaskLoader<List<Feed>> {

    private static final String LOG_TAG = FeedLoader.class.getName();

    private String mUrl;


    public FeedLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Feed> loadInBackground() {
        if (mUrl == null) {
            return null;
        }


        List<Feed> feeds = QueryUtils.fetchFeedData(mUrl);
        return feeds;
    }


}
