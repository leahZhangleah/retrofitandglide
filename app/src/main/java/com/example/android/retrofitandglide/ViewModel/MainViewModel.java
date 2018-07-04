package com.example.android.retrofitandglide.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.retrofitandglide.database.PopMovie;

import java.util.List;

public class MainViewModel extends ViewModel {
    private static final String LOG_TAG = MainViewModel.class.getName();
    Repository mRepository;
    String category;
    int page;

    public MainViewModel(Repository mRepository, String category, int page) {
        this.mRepository = mRepository;
        this.category = category;
        this.page = page;
    }

    public  LiveData<List<PopMovie>> getPopularMovies(){
        return mRepository.getPopularMovies(category,page);
    }
}
