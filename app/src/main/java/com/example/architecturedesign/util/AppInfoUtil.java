package com.example.architecturedesign.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.architecturedesign.R;

public class AppInfoUtil {

    private Context context;
    private String packageName;

    public AppInfoUtil(Context context) {
        this.context = context;
    }

    public Drawable getAppIcon() {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return context.getDrawable(R.drawable.ic_launcher_foreground);
    }

    public String getAppName() {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {

        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "Unknown");

    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
