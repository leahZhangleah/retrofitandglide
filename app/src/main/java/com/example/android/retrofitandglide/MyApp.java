package com.example.android.retrofitandglide;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class MyApp extends Application {
    RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        refWatcher = LeakCanary.install(this);
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }
}
