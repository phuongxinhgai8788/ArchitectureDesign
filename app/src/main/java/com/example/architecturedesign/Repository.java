package com.example.architecturedesign;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.architecturedesign.model.AppInfo;
import com.example.architecturedesign.util.AppInfoUtil;
import com.example.architecturedesign.util.MapUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Repository {
    private static Repository INSTANCE;
    private Context context;
    private final String TAG = "Repository";

    private Repository(Context context){
        this.context = context;
    }

    public static Repository getInstance(Context context){
        if(INSTANCE==null){
            synchronized(Repository.class){
                if (INSTANCE == null){
                    INSTANCE = new Repository(context);
                }
            }
        }
        return INSTANCE;
    }
    public Map<String,UsageStats> loadUsageStats(){
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
//            LiveData<List<UsageStats>> usageStatsList;
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            executor.execute(()->{
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        long start = calendar.getTimeInMillis();
        long end = System.currentTimeMillis();
        Map<String, UsageStats> usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(start, end);
//        Set<Map.Entry<String, UsageStats>> setEntries = usageStatsMap.entrySet();
//        for(Map.Entry<String, UsageStats> entry:setEntries){
//            Log.i(TAG, entry.getKey());
//        }
//            });
        return usageStatsMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Long> sortUsageStatsBySpentTime(){
        Map<String, UsageStats> usageStatsMap = loadUsageStats();
        Set<Map.Entry<String, UsageStats>> setEntries = usageStatsMap.entrySet();
        Map<String, Long> spentTimeMap = new LinkedHashMap<String, Long>();
        for(Map.Entry<String, UsageStats> entry:setEntries){
            UsageStats usageStats = entry.getValue();
            long spentTime = usageStats.getTotalTimeInForeground();
            spentTimeMap.put(entry.getKey(), spentTime);
        }
        Map<String, Long> sortedSpentTimeMap = MapUtil.sortByValueDescendingOrder(spentTimeMap);
        List<Long> spentTime = sortedSpentTimeMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        spentTime.forEach(i -> {
            Log.i(TAG, "Spent time: "+i);
        });
        return spentTimeMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Map<String, Long> getMostUsage(){
        Map<String, UsageStats> usageStatsMap = loadUsageStats();
        Set<Map.Entry<String, UsageStats>> setEntries = usageStatsMap.entrySet();
        Map<String, Long> spentTimeMap = new LinkedHashMap<String, Long>();
        for(Map.Entry<String, UsageStats> entry:setEntries){
            UsageStats usageStats = entry.getValue();
            long spentTime = usageStats.getTotalTimeInForeground();
            spentTimeMap.put(entry.getKey(), spentTime);
        }
        Map<String, Long> usageMapDiffZero = spentTimeMap.entrySet().stream()
                .filter(x -> x.getValue()>0)
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
        Map<String, Long> sortedSpentTimeMap = MapUtil.sortByValueDescendingOrder(usageMapDiffZero);
        List<Long> spentTime = sortedSpentTimeMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        spentTime.forEach(i -> {
            Log.i(TAG, "Spent time # 0: "+i);
        });
        return sortedSpentTimeMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<AppInfo> getMostUsageAppInfo(){
        List<AppInfo>  mostUsageAppInfoList = new ArrayList<>();
        AppInfoUtil appInfoUtil = new AppInfoUtil(context);
        Map<String, Long> mostUsageAppMap = getMostUsage();
        mostUsageAppMap.entrySet().forEach((x) ->{
            AppInfo appInfo = new AppInfo();
            appInfo.setPackageName(x.getKey());
            appInfoUtil.setPackageName(x.getKey());
            String appName = appInfoUtil.getAppName();
            Drawable appIconDrawable = appInfoUtil.getAppIcon();
            appInfo.setAppName(appName);
            appInfo.setAppIcon(appIconDrawable);
            appInfo.setAppUsage(x.getValue());
            mostUsageAppInfoList.add(appInfo);
        });
        return mostUsageAppInfoList;
           }
}
