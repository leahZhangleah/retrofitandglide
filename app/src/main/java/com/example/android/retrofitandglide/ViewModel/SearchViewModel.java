package com.example.android.retrofitandglide.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.retrofitandglide.Retrofit.SearchResult;

import java.util.List;

public class SearchViewModel extends ViewModel{
    private static final String LOG_TAG = SearchViewModel.class.getName();
    private Repository mRepository;
    private String query;
    private int page;

    public SearchViewModel(Repository mRepository, String query, int page) {
        this.mRepository = mRepository;
        this.query = query;
        this.page = page;
    }

    public MutableLiveData<List<SearchResult>> search(){
        Log.d(LOG_TAG,"search method from repository is called");
        return mRepository.search(query,page);
    }
}
