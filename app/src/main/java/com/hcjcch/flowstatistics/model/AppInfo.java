package com.hcjcch.flowstatistics.model;

import android.graphics.drawable.Drawable;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/2 23:25
 */

public class AppInfo {
    private int uid;
    private Drawable appIcon;
    private String appName;
    private String appPackageName;
    private double appFlow;
    private boolean hasInternetPermission;
    private boolean wifiConnect;
    private boolean flowConnect;

    public AppInfo(int uid, String appName, Drawable appIcon, String appPackageName, double appFlow, boolean hasInternetPermission) {
        this.uid = uid;
        this.appName = appName;
        this.appIcon = appIcon;
        this.appPackageName = appPackageName;
        this.appFlow = appFlow;
        this.hasInternetPermission = hasInternetPermission;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public double getAppFlow() {
        return appFlow;
    }

    public void setAppFlow(double appFlow) {
        this.appFlow = appFlow;
    }

    public boolean isHasInternetPermission() {
        return hasInternetPermission;
    }

    public void setHasInternetPermission(boolean hasInternetPermission) {
        this.hasInternetPermission = hasInternetPermission;
    }

    public boolean isWifiConnect() {
        return wifiConnect;
    }

    public void setWifiConnect(boolean wifiConnect) {
        this.wifiConnect = wifiConnect;
    }

    public boolean isFlowConnect() {
        return flowConnect;
    }

    public void setFlowConnect(boolean flowConnect) {
        this.flowConnect = flowConnect;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}