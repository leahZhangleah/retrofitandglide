package com.example.android.retrofitandglide.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class SearchViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    Repository mRepository;
    String query;
    int page;

    public SearchViewModelFactory(Repository mRepository, String query, int page) {
        this.mRepository = mRepository;
        this.query = query;
        this.page = page;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new SearchViewModel(mRepository,query,page);
    }
}
