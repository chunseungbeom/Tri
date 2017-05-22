package com.example.yunan.tripscanner;

import android.app.Application;
import android.content.Context;

/**
 * Created by yunan on 2017-05-21.
 */

public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext(){
        return context;
    }

}
