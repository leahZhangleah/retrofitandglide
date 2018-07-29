package com.example.android.retrofitandglide.ViewModel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieDetailModelFactory extends ViewModelProvider.NewInstanceFactory{
    private Repository mRepository;
    private int movieId;
    private Application application;

    public MovieDetailModelFactory(@NonNull Application application,Repository mRepository, int movieId) {
        this.mRepository = mRepository;
        this.movieId = movieId;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new MovieDetailViewModel(application,mRepository,movieId);
    }
}
