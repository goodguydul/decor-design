package com.KevAndz.decordesign.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.KevAndz.decordesign.LoginActivity;
import com.KevAndz.decordesign.User;

public class PrefManager {
    private static final String SHARED_PREF_NAME = "decordesignpref";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_NAME = "user_fullName";
    private static final String KEY_ID = "user_id";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_PROFILE_IMAGE_URL= "prof_img_url";
    private static final String KEY_PHONE_NUMBER = "user_phonenumber";
    private static final String KEY_USER_LEVEL = "user_level";

    private static PrefManager mInstance;
    private static Context mCtx;

    private PrefManager (Context context) {
        mCtx = context;
    }

    public static synchronized PrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PrefManager(context);
        }
        return mInstance;
    }
    public void setUserLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_PHONE_NUMBER, user.getPhonenumber());
        editor.putString(KEY_PROFILE_IMAGE_URL, user.getProf_img_url());
        editor.putInt(KEY_USER_LEVEL, user.getUserLevel());
        editor.putBoolean(KEY_IS_LOGGED_IN,true);
        editor.apply();
    }
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_PHONE_NUMBER, null),
                sharedPreferences.getString(KEY_PROFILE_IMAGE_URL, null),
                sharedPreferences.getInt(KEY_USER_LEVEL, -1)
        );
    }
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));

    }
}