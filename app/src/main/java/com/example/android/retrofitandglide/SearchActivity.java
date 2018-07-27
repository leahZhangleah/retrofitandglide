package com.example.android.retrofitandglide;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.retrofitandglide.Retrofit.SearchResult;
import com.example.android.retrofitandglide.ViewModel.ApiResponse;
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
    //private String DEFAULT_QUERY_STRING = "1234";
    private String query;
    private int page = 1;
    private SearchAdapter searchAdapter;
    private MovieComponent movieComponent;
    private ArrayList<SearchResult> searchResultArrayList;
    private SearchViewModel searchViewModel;
    //private CompositeDisposable compositeDisposable;
    private ProgressBar loading;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        movieComponent = DaggerMovieComponent.builder()
                .movieModule(new MovieModule(getApplication()))
                .build();
        movieComponent.inject(this);
        /*if(savedInstanceState!=null && savedInstanceState.containsKey("savedQueryKey")){
            query = savedInstanceState.getString("savedQueryKey");
            Log.d(LOG_TAG,"the query string"+query+ "is from savedinstanceState");
        }*/
        Intent searchIntent = getIntent();
        if(searchIntent!=null && searchIntent.getAction().equals(Intent.ACTION_SEARCH)){
            query = searchIntent.getStringExtra(SearchManager.QUERY);

            Log.d(LOG_TAG,"the query string"+query+ " is from MainActivity");
            //Toast.makeText(this,"the query string is: "+query,Toast.LENGTH_SHORT).show();
        }

        ListView searchList = findViewById(R.id.search_list);
        searchResultArrayList = new ArrayList<>();
        searchAdapter = new SearchAdapter(this,searchResultArrayList);
        searchList.setAdapter(searchAdapter);
        loading = (ProgressBar) findViewById(R.id.loading);

        Log.d(LOG_TAG,"loadSearchResult method is called");
        SearchViewModelFactory searchViewModelFactory = new SearchViewModelFactory(mRepository,query,page);
        Log.d(LOG_TAG,"searchViewModelFactory is created");
        searchViewModel = ViewModelProviders.of(this,searchViewModelFactory).get(SearchViewModel.class);
        Log.d(LOG_TAG,"searchViewModel is created");
        searchViewModel.searchResponse().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {
                consumeApiResponse(apiResponse);
            }
        });
        loadSearchResult(query);
    }

    private void consumeApiResponse(ApiResponse apiResponse){
        switch ((apiResponse.status)){
            case LOADING:
                loading.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                loading.setVisibility(View.INVISIBLE);
                renderSuccessResponse(apiResponse.data);
                break;
            case ERROR:
                loading.setVisibility(View.INVISIBLE);
                Toast.makeText(this,"error in getting data",Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;
        }
    }

    private void renderSuccessResponse(List<SearchResult> searchResults){
        if(!searchResults.isEmpty()){
           searchResultArrayList.clear();
           searchResultArrayList.addAll(searchResults);
           searchAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this,"There is no movie with this keyword",Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSearchResult(String query){
        searchViewModel.search();
        /*
        final Observable<List<SearchResult>> mutableLiveSearchResults = searchViewModel.search();
        DisposableObserver observer = mutableLiveSearchResults.subscribeWith(new DisposableObserver<List<SearchResult>>() {
            @Override
            public void onNext(List<SearchResult> searchResults) {
                searchResultArrayList.clear();
                searchResultArrayList.addAll(searchResults);
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        compositeDisposable.add(observer);
                /*
        mutableLiveSearchResults.observe(this, new Observer<List<SearchResult>>() {
            @Override
            public void onChanged(@Nullable List<SearchResult> searchResults) {
                searchAdapter.addAll(searchResults);
                searchAdapter.notifyDataSetChanged();
                mutableLiveSearchResults.removeObserver(this);
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"ON RESUME IS CALLED");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG,"ON stop IS CALLED");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG,"ON start IS CALLED");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //searchViewModel.cancelRetrofitCall();
        Log.d(LOG_TAG,"ON pause IS CALLED");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG,"ON restart IS CALLED");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchResultArrayList.clear();
        searchAdapter.notifyDataSetChanged();
        Log.d(LOG_TAG,"adapter's data is cleared");
        query = null;
        Log.d(LOG_TAG,"ON destroy IS CALLED");
    }
}
