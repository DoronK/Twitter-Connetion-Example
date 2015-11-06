package com.doronk.twitterexample.data;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import com.doronk.twitterexample.MyApplication;
import com.doronk.twitterexample.core.LruBitmapCache;

/**
 * Created by Doron on 14/04/2015.
 */
public class DataManager {
    public static final String TAG = DataManager.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private String mTwitterAccessToken = null;

    private DataManager() {
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(LruBitmapCache.getCacheSize(MyApplication.getAppContext()
        )));
    }

    public static DataManager getInstance() {
        return DataManagerHolder.INSTANCE;
    }

    /* ****************************************************************************************** */
    /* METHOD FOR VOLLEY IMAGE & THREADS                                              */
    /* ****************************************************************************************** */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * *******************************************************************************
     * GETTERS SETTERS
     * ********************************************************************************
     */

    public String getTwitterAccessToken() {
        return mTwitterAccessToken;
    }

    public void setTwitterAccessToken(String mTwitterAccessKey) {
        this.mTwitterAccessToken = mTwitterAccessKey;
    }

    private static class DataManagerHolder {
        public static final DataManager INSTANCE = new DataManager();
    }
}
