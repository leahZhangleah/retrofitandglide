package com.example.android.retrofitandglide;

import android.app.SearchManager;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.android.retrofitandglide.Retrofit.SearchResult;
import com.example.android.retrofitandglide.ViewModel.Repository;
import com.example.android.retrofitandglide.ViewModel.SearchViewModel;
import com.example.android.retrofitandglide.ViewModel.SearchViewModelFactory;
import com.example.android.retrofitandglide.dagger.DaggerMovieComponent;
import com.example.android.retrofitandglide.dagger.MovieComponent;
import com.example.android.retrofitandglide.dagger.MovieModule;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SearchActivity extends AppCompatActivity {
    private static final String LOG_TAG = SearchActivity.class.getName();
    @Inject
    public Repository mRepository;
    private String query;
    private int page = 1;
    private SearchAdapter searchAdapter;
    private MovieComponent movieComponent;
    private ArrayList<SearchResult> searchResultArrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        movieComponent = DaggerMovieComponent.builder()
                .movieModule(new MovieModule(getApplication()))
                .build();
        movieComponent.inject(this);
        if(savedInstanceState!=null && savedInstanceState.containsKey(SearchManager.QUERY)){
            query = savedInstanceState.getString(SearchManager.QUERY);
            Log.d(LOG_TAG,"the query is string is from savedinstanceState");
        }
        Intent searchIntent = getIntent();
        if(searchIntent!=null && searchIntent.getAction().equals(Intent.ACTION_SEARCH)){
            query = searchIntent.getStringExtra(SearchManager.QUERY);
            Log.d(LOG_TAG,"the query is string is from MainActivity");
            //Toast.makeText(this,"the query string is: "+query,Toast.LENGTH_SHORT).show();
        }

        ListView searchList = findViewById(R.id.search_list);
        searchResultArrayList = new ArrayList<>();
        searchAdapter = new SearchAdapter(this,searchResultArrayList);
        searchList.setAdapter(searchAdapter);
        loadSearchResult(query);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SearchManager.QUERY,query);
    }

    private void loadSearchResult(String query){
        Log.d(LOG_TAG,"loadSearchResult method is called");
        SearchViewModelFactory searchViewModelFactory = new SearchViewModelFactory(mRepository,query,page);
        Log.d(LOG_TAG,"searchViewModelFactory is created");
        SearchViewModel searchViewModel = ViewModelProviders.of(this,searchViewModelFactory).get(SearchViewModel.class);
        Log.d(LOG_TAG,"searchViewModel is created");
        MutableLiveData<List<SearchResult>> mutableLiveSearchResults = searchViewModel.search();
        mutableLiveSearchResults.observe(this, new Observer<List<SearchResult>>() {
            @Override
            public void onChanged(@Nullable List<SearchResult> searchResults) {
                searchAdapter.addAll(searchResults);
                searchAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchAdapter.clear();
    }
}
