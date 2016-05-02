package com.hcjcch.pm;

import android.graphics.drawable.Drawable;

public class ApkFile {
    private Drawable icon;
    private String name;
    private int versionCode;
    private String path;
    private boolean isInstalled;

    public ApkFile(String path) {
        this.path = path;
    }

    public ApkFile(Drawable icon, String name, int versionCode) {
        this.icon = icon;
        this.name = name;
        this.versionCode = versionCode;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ApkFile{" +
                "icon=" + icon +
                ", name='" + name + '\'' +
                ", versionCode=" + versionCode +
                ", path='" + path + '\'' +
                '}';
    }
}