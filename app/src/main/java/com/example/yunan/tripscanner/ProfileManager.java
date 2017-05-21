package com.example.yunan.tripscanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by yunan on 2017-05-21.
 */

class ProfileManager {
    private static ProfileManager instance;

    SharedPreferences mPref;
    SharedPreferences.Editor mEditor;

    public static ProfileManager getInstance() {
        if(instance == null){
            instance = new ProfileManager();
        }
        return instance;
    }

    private ProfileManager() {
        Context context = MyApplication.getAppContext();

        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPref.edit();

    }

    public void saveUserEmail(String email){
        mEditor.putString("email", email);
        mEditor.commit();
    }
    public String getUserEmail(){
        return mPref.getString("email", "");
    }
    public void saveUserToken(String token){
        mEditor.putString("authentication_token", token);
        mEditor.commit();
    }
    public String getUserToken(){
        return mPref.getString("authentication_token" , "");
    }
}
