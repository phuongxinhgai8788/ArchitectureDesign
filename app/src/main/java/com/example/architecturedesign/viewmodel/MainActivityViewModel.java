package com.example.architecturedesign.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;

import com.example.architecturedesign.Repository;
import com.example.architecturedesign.model.AppInfo;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private Repository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<AppInfo> getMostUsageAppInfo(){
        return repository.getMostUsageAppInfo();
    }

}
