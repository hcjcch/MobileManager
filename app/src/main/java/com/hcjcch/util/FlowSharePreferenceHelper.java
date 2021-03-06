package com.hcjcch.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.hcjcch.mobilemanager.MApplication;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/3 12:01
 */

public class FlowSharePreferenceHelper {
    public static Boolean getBoolean(String key) {
        SharedPreferences sharedPreferences = MApplication.getContext().getSharedPreferences(Constants.SP_NAME_FLOW, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static Boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = MApplication.getContext().getSharedPreferences(Constants.SP_NAME_FLOW, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void saveBoolean(String key, Boolean value) {
        SharedPreferences sharedPreferences = MApplication.getContext().getSharedPreferences(Constants.SP_NAME_FLOW, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void saveString(String key, String value) {
        SharedPreferences sharedPreferences = MApplication.getContext().getSharedPreferences(Constants.SP_NAME_FLOW, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, String value) {
        SharedPreferences sharedPreferences = MApplication.getContext().getSharedPreferences(Constants.SP_NAME_FLOW, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, value);
    }

    public static void saveInt(String key, int value) {
        SharedPreferences sharedPreferences = MApplication.getContext().getSharedPreferences(Constants.SP_NAME_FLOW, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(String key) {
        SharedPreferences sharedPreferences = MApplication.getContext().getSharedPreferences(Constants.SP_NAME_FLOW, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }
}