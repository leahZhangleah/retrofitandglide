package com.example.android.retrofitandglide.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private static final String LOG_TAG = MainViewModelFactory.class.getName();
    Repository mRepository;
    String category;
    int page;

    public MainViewModelFactory(Repository mRepository, String category, int page) {
        this.mRepository = mRepository;
        this.category = category;
        this.page = page;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d(LOG_TAG,"initialize main view model factory");
        return (T)new MainViewModel(mRepository,category,page);
    }
}
