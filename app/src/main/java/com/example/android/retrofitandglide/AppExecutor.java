package com.example.android.retrofitandglide;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {
    private static final String LOG_TAG = AppExecutor.class.getName();
    private static final Object LOCK = new Object();
    private Executor diskIO, networkIO, mainThreadIO;
    private static AppExecutor mInstance;

    public AppExecutor(Executor diskIO, Executor networkIO, Executor mainThreadIO) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThreadIO = mainThreadIO;
    }

    public static AppExecutor getInstance() {
        if (mInstance==null){
            synchronized (LOCK){
                mInstance = new AppExecutor(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        Log.d(LOG_TAG,"create an instance of app executor");
        return mInstance;
    }

    public static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThreadIO() {
        return mainThreadIO;
    }
}
