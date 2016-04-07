package com.hcjcch.mobilemanager;

import android.app.Application;
import android.content.Context;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/3 12:11
 */

public class MApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
