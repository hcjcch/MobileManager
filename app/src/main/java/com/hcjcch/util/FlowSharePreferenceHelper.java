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

    public static void saveBoolean(String key, Boolean value) {
        SharedPreferences sharedPreferences = MApplication.getContext().getSharedPreferences(Constants.SP_NAME_FLOW, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
