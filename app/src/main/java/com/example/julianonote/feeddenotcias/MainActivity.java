package com.example.julianonote.feeddenotcias;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Loader;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Feed>> {


    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String USGS_REQUEST_URL = "https://content.guardianapis.com/search";


    private static final int FEED_LOADER_ID = 1;
    private TextView mEmptyStateTextView;

    private FeedAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView feedListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        feedListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new FeedAdapter(this, new ArrayList<Feed>());
        feedListView.setAdapter(mAdapter);

        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Feed currentFeed = mAdapter.getItem(position);

                Uri feedUri = Uri.parse(currentFeed.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, feedUri);

                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(FEED_LOADER_ID, null, this);

        } else {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public Loader<List<Feed>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefers = PreferenceManager.getDefaultSharedPreferences(this);
        String basePesquisa = sharedPrefers.getString(getString(R.string.busca_key), getString(R.string.busca_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", basePesquisa);
        uriBuilder.appendQueryParameter("api-key", "test");

        return new FeedLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<Feed>> loader, List<Feed> feeds) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


        mEmptyStateTextView.setText(R.string.no_feeds);

        mAdapter.clear();


        if (feeds != null && !feeds.isEmpty()) {
            mAdapter.addAll(feeds);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Feed>> loader) {

        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_busca) {
            Intent settingsIntent = new Intent(this, BuscaActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}