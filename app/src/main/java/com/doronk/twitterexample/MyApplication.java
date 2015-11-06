package com.doronk.twitterexample;

import android.app.Application;
import android.content.Context;

import com.doronk.twitterexample.data.DataManager;

/**
 * Created by Doron on 14/04/2015.
 */
public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();
    private static Context context;

    /**
     * *******************************************************************************
     * GETTERS SETTERS
     * ********************************************************************************
     */
    public static Context getAppContext() {
        return MyApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DataManager.getInstance();
    }

}
