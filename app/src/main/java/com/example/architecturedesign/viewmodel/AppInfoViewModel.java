package com.example.architecturedesign.viewmodel;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.architecturedesign.BR;
import com.example.architecturedesign.model.AppInfo;

public class AppInfoViewModel extends BaseObservable {

    private AppInfo appInfo;

    public AppInfoViewModel(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    @Bindable
    public String getAppName() {
        return appInfo.getAppName();
    }

    @Bindable
    public Drawable getAppIcon() {
        return appInfo.getAppIcon();
    }

    @Bindable
    public String getAppUsage(){
        long millisecond = appInfo.getAppUsage();
        long hours = (millisecond/1000)/3600;
        long minutes = (millisecond/1000)/60;
        long seconds = (millisecond/1000)%60;
        return "Usage time: "+hours+"h:"+minutes+"m:"+seconds+"s";
    }
    private void setAppName(String appName) {
        appInfo.setAppName(appName);
        notifyPropertyChanged(BR.appName);
    }

    private void setAppIcon(Drawable iconDrawable) {
        appInfo.setAppIcon(iconDrawable);
        notifyPropertyChanged(BR.appIcon);
    }

    private void setAppUsage(Long appUsage){
        appInfo.setAppUsage(appUsage);
        notifyPropertyChanged(BR.appUsage);
    }

}
