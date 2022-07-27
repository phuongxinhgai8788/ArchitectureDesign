package com.example.architecturedesign.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.view.ViewGroup;

import com.example.architecturedesign.R;
import com.example.architecturedesign.databinding.ActivityMainBinding;
import com.example.architecturedesign.databinding.UsageItemBinding;
import com.example.architecturedesign.model.AppInfo;
import com.example.architecturedesign.viewmodel.AppInfoViewModel;
import com.example.architecturedesign.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG="MainActivity";
    private ActivityMainBinding activityMainBinding;
    private MainActivityViewModel mainActivityViewModel;
    private List<AppInfo> appInfoList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();
        if(!checkForPermission(this)){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }else {
            openMostUsageList();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void openMostUsageList() {
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        activityMainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AppInfoAdapter adapter = new AppInfoAdapter();
        adapter.appInfoList =mainActivityViewModel.getMostUsageAppInfo();
        activityMainBinding.recyclerView.setAdapter(adapter);
        activityMainBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private boolean checkForPermission(Context context){
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private class AppInfoHolder extends RecyclerView.ViewHolder {

        private AppInfo appInfo;
        private UsageItemBinding binding;
        private AppInfoViewModel appInfoViewModel;

        public AppInfoHolder(@NonNull UsageItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(AppInfo appInfo){
            this.appInfo = appInfo;
            appInfoViewModel = new AppInfoViewModel(appInfo);
            binding.setViewModel(appInfoViewModel);
            binding.executePendingBindings();
        }

    }

    private class AppInfoAdapter extends RecyclerView.Adapter<AppInfoHolder>{

        private List<AppInfo> appInfoList;

        public AppInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
            UsageItemBinding binding = DataBindingUtil.inflate(
                    getLayoutInflater(),
                    R.layout.usage_item,
                    parent,
                    false
            );
            return new AppInfoHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull AppInfoHolder holder, int position) {
            AppInfo appInfo = appInfoList.get(position);
            holder.bind(appInfo);
        }

        @Override
        public int getItemCount() {
            return appInfoList.size();
        }
    }}