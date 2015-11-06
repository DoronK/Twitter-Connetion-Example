package com.doronk.twitterexample;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.doronk.twitterexample.data.Constants;
import com.doronk.twitterexample.data.DataManager;
import com.doronk.twitterexample.volleyobjects.TokenRequest;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static ImageLoader mImageLoader = DataManager.getInstance().getImageLoader();
    EndlessScrollListener listener = new EndlessScrollListener();
    SearchView mSearchView = null;
    private TweetAdapter tweetAdapter = null;
    private RequestQueue mRequestQueue = DataManager.getInstance().getRequestQueue();
    private TweetLoader tweetLoader;
    private ListView mTweetsLV;
    private Toolbar mToolbar;
    private String mSearchString;
    private ProgressBar pb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTweetsLV = (ListView) findViewById(R.id.tweets_lv);
        mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        pb = (ProgressBar) findViewById(R.id.main_pb);
        setSupportActionBar(mToolbar);
        doRequestAccessToken();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        if (mSearchView != null) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            mSearchView.setIconifiedByDefault(false);
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    mSearchString = newText;
                    tweetLoader.setQuery(mSearchString);
                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    return true;
                }
            };

            mSearchView.setOnQueryTextListener(queryTextListener);
        }
        return true;
    }


    /**
     * *******************************************************************************
     * WEB-SERVICES
     * ********************************************************************************
     */

    public void doRequestAccessToken() {
        StringRequest request = new TokenRequest(Method.POST, Constants.TWITTER_TOKEN_URL, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    DataManager.getInstance().setTwitterAccessToken(object.optString("access_token"));
                    tweetAdapter = new TweetAdapter(MainActivity.this);
                    tweetLoader = new TweetLoader(tweetAdapter, MainActivity.this);
                    listener.setOnEndReachedListener(tweetLoader);
                    mTweetsLV.setAdapter(tweetAdapter);
                    mTweetsLV.setOnScrollListener(listener);
                    pb.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error during request");
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }
}
