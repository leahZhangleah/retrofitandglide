package com.example.android.retrofitandglide.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.retrofitandglide.AppExecutor;
import com.example.android.retrofitandglide.Retrofit.NetworkDataSource;
import com.example.android.retrofitandglide.Retrofit.SearchResult;
import com.example.android.retrofitandglide.database.PopMovie;
import com.example.android.retrofitandglide.database.ShowDao;

import java.util.List;

import io.reactivex.Observable;

public class Repository implements NetworkDataSource.ResponseCallback,NetworkDataSource.SearchCallback{
    private static final String LOG_TAG = Repository.class.getName();
    private static final Object LOCK = new Object();
    private boolean mInitialized = false;
    private NetworkDataSource mNetworkDataSource;
    private ShowDao mShowDao;
    private AppExecutor mAppExecutor;
    private static Repository mInstance;
    private MutableLiveData<List<PopMovie>> mutableLiveData;
    private MutableLiveData<List<SearchResult>> mutableLiveSearchResults;

    public Repository(NetworkDataSource mNetworkDataSource, ShowDao mShowDao, AppExecutor mAppExecutor) {
        this.mNetworkDataSource = mNetworkDataSource;
        this.mShowDao = mShowDao;
        this.mAppExecutor = mAppExecutor;
        Log.d(LOG_TAG,"repository is instantiated");
        mutableLiveSearchResults = new MutableLiveData<>();

    }

    public static Repository getInstance(NetworkDataSource mNetworkDataSource, ShowDao mShowDao, AppExecutor mAppExecutor) {
        if (mInstance==null){
            synchronized (LOCK){
                mInstance = new Repository(mNetworkDataSource,mShowDao,mAppExecutor);
                Log.d(LOG_TAG,"get repository's instance by creating a new repository");
            }
        }
        return mInstance;
    }

    public LiveData<List<PopMovie>> getPopularMovies(String category, int page){
        initializeData(category,page);
        Log.d(LOG_TAG,"get movie from database by repository ");
        //startFetchService();
        return mShowDao.loadPopularMovies();
    }

    private void initializeData(String category, int page){
        if (mInitialized) return;
        mInitialized = true;
        mNetworkDataSource.getPopularMoviesFromInternet(this,category,page);
        Log.d(LOG_TAG,"initialize mutable live data by getting data from internet");

    }

    private void deletePopularMoviesInDB(){
        mShowDao.deleteAllMovies();
        Log.d(LOG_TAG,"delete data in database");
    }

    private void bulkInsertPopMoviesInDB(List<PopMovie> popMovies){
        mShowDao.insertMovies(popMovies);
        Log.d(LOG_TAG,"insert new data in database");
    }


    @Override
    public void onSuccess(MutableLiveData<List<PopMovie>> popMovies) {
        mutableLiveData = popMovies;
        mutableLiveData.observeForever(new Observer<List<PopMovie>>() {
            @Override
            public void onChanged(@Nullable final List<PopMovie> popMovies) {
                mAppExecutor.getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        deletePopularMoviesInDB();
                        bulkInsertPopMoviesInDB(popMovies);
                        Log.d(LOG_TAG,"when internet data has changed, change database also ");
                    }
                });

            }
        });

    }

    @Override
    public void onFailure() {

    }

    public Observable<List<SearchResult>> search(String query, int page){
        Log.d(LOG_TAG,"search method from NetworkDatasource is called");
        return mNetworkDataSource.searchFromInternet(query,page);
    }

    @Override
    public void onSearchSuccess(List<SearchResult> searchResults) {
        mutableLiveSearchResults.postValue(searchResults);
    }

    public void cancelRetrofitCall(){
        mNetworkDataSource.cancelRetrofitCall();
        Log.d(LOG_TAG,"retrofit call is cancelled");
    }

}
