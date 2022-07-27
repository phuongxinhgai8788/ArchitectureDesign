package com.example.architecturedesign.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.architecturedesign.MyApplication;
import com.example.architecturedesign.R;
import com.example.architecturedesign.util.AppInfoUtil;

import java.util.List;

public class ForegroundWorker extends Worker {
    public ForegroundWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        String packageName = "";
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                packageName = appProcess.processName;
            }
        }
        AppInfoUtil appInfoUtil = new AppInfoUtil(getApplicationContext());
        appInfoUtil.setPackageName(packageName);
        String appName = appInfoUtil.getAppName();

        Resources resources = getApplicationContext().getResources();
        Notification notification = new Notification.Builder(getApplicationContext(), MyApplication.NOTIFICATION_CHANNEL_ID)
                .setTicker(resources.getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(resources.getString(R.string.foreground_app))
                .setContentText(appName + " is in the foreground")
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(1, notification);
        return Result.success();
    }
}
