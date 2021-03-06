package com.hcjcch.flowstatistics.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/2 23:25
 */

public class AppInfo implements Serializable {
    private int uid;
    private Drawable appIcon;
    private List<String> appName = new ArrayList<>();
    private String appPackageName;
    private double appFlow;
    private boolean hasInternetPermission;
    private boolean wifiCheck;
    private boolean flowCheck;

    public AppInfo() {

    }

    public AppInfo(int uid, String appName, Drawable appIcon, String appPackageName, double appFlow, boolean hasInternetPermission) {
        this.uid = uid;
        this.appName.add(appName);
        this.appIcon = appIcon;
        this.appPackageName = appPackageName;
        this.appFlow = appFlow;
        this.hasInternetPermission = hasInternetPermission;
    }

    public AppInfo(int uid, String appName, boolean wifiCheck, boolean flowCheck) {
        this.uid = uid;
        this.appName.add(appName);
        this.wifiCheck = wifiCheck;
        this.flowCheck = flowCheck;
        this.hasInternetPermission = true;
    }

    public AppInfo(int uid, Drawable appIcon, String appName, String appPackageName) {
        this.uid = uid;
        this.appIcon = appIcon;
        this.appName.add(appName);
        this.appPackageName = appPackageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getName() {
        StringBuilder s = new StringBuilder();
        //if (uid > 0) s.append(uid + ": ");
        for (int i = 0; i < appName.size(); i++) {
            if (i != 0) s.append(", ");
            s.append(appName.get(i));
        }
        s.append("\n");
        return s.toString();
    }

    public List<String> getAppName() {
        return appName;
    }

    public void setAppName(List<String> appName) {
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

    public boolean isWifiCheck() {
        return wifiCheck;
    }

    public void setWifiCheck(boolean wifiCheck) {
        this.wifiCheck = wifiCheck;
    }

    public boolean isFlowCheck() {
        return flowCheck;
    }

    public void setFlowCheck(boolean flowCheck) {
        this.flowCheck = flowCheck;
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
