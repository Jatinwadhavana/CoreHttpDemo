package com.example.corehttpdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static final String APP_ISLOGIN = "isLogin";
    private static final String APP_PREF = "appPref";
    private static final String APP_USER_DISPLAY_NAME = "userDisName";
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
    }

    public static boolean getAppIsLogin(Context context) {
        return getSharedPreferences(context).getBoolean(APP_ISLOGIN , false);
    }

    public static void setAppIsLogin(Context context, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(APP_ISLOGIN , newValue);
        editor.apply();
    }

    public static String getAppUserDisplayName(Context context) {
        return getSharedPreferences(context).getString(APP_USER_DISPLAY_NAME , "");
    }

    public static void setAppUserDisplayName(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(APP_USER_DISPLAY_NAME , newValue);
        editor.apply();
    }
}