package com.example.architecturedesign.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.architecturedesign.MyApplication;
import com.example.architecturedesign.R;
import com.example.architecturedesign.util.AppInfoUtil;
import com.example.architecturedesign.view.MainActivity;

import java.util.List;

public class ForegroundAppService extends Service {
    private static final String TAG = "ForegroundAppService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            switch (action) {
                case MainActivity.NOTIFICATION:
                    showNotification();
                    break;
            }
        }
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification() {
        String packageName = "";
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                packageName = appProcess.processName;
            }
        }
        AppInfoUtil appInfoUtil = new AppInfoUtil(this);
        appInfoUtil.setPackageName(packageName);
        String appName = appInfoUtil.getAppName();

        Resources resources = getResources();
        Notification notification = new Notification.Builder(this, MyApplication.NOTIFICATION_CHANNEL_ID)
                .setTicker(resources.getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(resources.getString(R.string.foreground_app))
                .setContentText(appName + " is in the foreground")
                .setAutoCancel(true)
                .build();
        startForeground(1, notification);
    }
}
