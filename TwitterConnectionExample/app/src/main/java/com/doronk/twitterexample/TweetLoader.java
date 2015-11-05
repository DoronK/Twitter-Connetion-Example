package com.doronk.twitterexample;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.doronk.twitterexample.data.DataManager;
import com.doronk.twitterexample.volleyobjects.TweetsRequest;

public class TweetLoader implements EndlessScrollListener.OnEndReachedListener {

    public static final String TAG = TweetLoader.class.getSimpleName();

    public static final String LIST_URL = "https://api.twitter.com/1.1/search/tweets.json";
    private ArrayAdapter<JSONObject> tweetAdapter;
    private TweetJsonListener mDownListener = new TweetJsonListener();
    private String query = "";
    private Activity activity;

    public TweetLoader(ArrayAdapter<JSONObject> tweetAdapter, Activity activity) {
        this.tweetAdapter = tweetAdapter;
        this.activity = activity;
    }

    public void setQuery(String query) {
        tweetAdapter.clear();
        this.query = query;
        onEndReached();
    }

    @Override
    public void onEndReached() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("q", query));
        if (tweetAdapter.getCount() > 0) {
            try {
                long maxId = (tweetAdapter.getItem(tweetAdapter.getCount() - 1).getLong("id") - 1);
                params.add(new BasicNameValuePair("max_id", "" + maxId));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        doRequestGetTweets(params);
    }

    /**
     * *******************************************************************************
     * WEB-SERVICES
     * ********************************************************************************
     */
    public void doRequestGetTweets(List<NameValuePair> params) {
        DataManager.getInstance().getRequestQueue().add(new TweetsRequest(LIST_URL, mDownListener, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w(TAG, "Error with request");
                error.printStackTrace();
            }
        }, params));
    }

    private class TweetJsonListener implements Listener<JSONObject> {

        @Override
        public void onResponse(JSONObject response) {
            JSONObject jsonObject = null;
            JSONArray tweets = response.optJSONArray("statuses");
            int count = tweets != null ? tweets.length() : 0;
            for (int i = 0; i < count; i++) {
                jsonObject = tweets.optJSONObject(i);
                if (jsonObject != null) {
                    tweetAdapter.add(jsonObject);
                }
            }
        }
    }
}
