package com.example.architecturedesign.model;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String appName;
    private String packageName;
    private Drawable appIcon;
    private Long appUsage;

    public AppInfo() {

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

    public String getPackageName() {
        return packageName;
    }

    public void setAppName(String mAppName) {
        this.appName = mAppName;
    }

    public void setPackageName(String mPackageName) {
        this.packageName = mPackageName;
    }

    public Long getAppUsage() {
        return appUsage;
    }

    public void setAppUsage(Long appUsage) {
        this.appUsage = appUsage;
    }
}
