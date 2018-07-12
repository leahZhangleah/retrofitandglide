package com.example.android.retrofitandglide.Retrofit;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.example.android.retrofitandglide.R;
import com.example.android.retrofitandglide.database.PopMovie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkDataSource {
    private static final String LOG_TAG = NetworkDataSource.class.getName();
    private  String api_key;
    private  NetworkInterface mNetworkInterface;
    private  MutableLiveData<List<PopMovie>> mutableLiveData;
    private  Context mContext;
    private MutableLiveData<List<SearchResult>> searchResults;

    public NetworkDataSource(Context context,NetworkInterface mNetworkInterface) {
        this.mNetworkInterface = mNetworkInterface;
        mContext = context;
        mutableLiveData = new MutableLiveData<>();
        api_key = mContext.getResources().getString(R.string.api_key);
        searchResults = new MutableLiveData<>();
    }

    public interface ResponseCallback{
        void onSuccess(MutableLiveData<List<PopMovie>> mutableLiveData);
        void onFailure();
    }

    public interface SearchCallback{
        void onSearchSuccess(List<SearchResult> searchResults);
    }

    public void getPopularMoviesFromInternet(final ResponseCallback callback, String category, int page){
        Call<PopularMovie> popularMovieCall = mNetworkInterface.getPopularMovie(category,api_key,page,"zh");
        popularMovieCall.enqueue(new Callback<PopularMovie>() {
            @Override
            public void onResponse(Call<PopularMovie> call, Response<PopularMovie> response) {
                PopularMovie popularMovie = response.body();
                List<Result> results = popularMovie.getResults();
                List<PopMovie> popMovies = new ArrayList<>();
                for (Result result:results){
                    int id = result.getId();
                    String title = result.getOriginalTitle();
                    double vote = result.getVoteAverage();
                    String image_path = result.getPosterPath();
                    PopMovie popMovie = new PopMovie(id,vote,title,image_path);
                    popMovies.add(popMovie);
                }
                mutableLiveData.postValue(popMovies);
                Log.d(LOG_TAG,"getPopularMoviesFromInternet by retrofit and save it in a mutable livedata");
                callback.onSuccess(mutableLiveData);
            }

            @Override
            public void onFailure(Call<PopularMovie> call, Throwable t) {
                callback.onFailure();
            }
        });
    }


    public void searchFromInternet(final SearchCallback mSearchCallback, String query, int page){
        Log.d(LOG_TAG,"searchFromInternet is called");
        Call<SearchMovie> searchMovieCall = mNetworkInterface.search(api_key,query,page);
        String requestURL = mNetworkInterface.search(api_key,query,page).request().url().toString();
        Log.d(LOG_TAG,"the request url is: "+requestURL);
        Log.d(LOG_TAG,"search method from network interface is called");
        searchMovieCall.enqueue(new Callback<SearchMovie>() {
            @Override
            public void onResponse(Call<SearchMovie> call, Response<SearchMovie> response) {
                Log.d(LOG_TAG,"the data is returned from retrofit search call and passed to mutablelivedata");
                SearchMovie mSearchMovie = response.body();
                Log.d(LOG_TAG,"the returned result is: "+searchResults);
                mSearchCallback.onSearchSuccess(mSearchMovie.getResults());
            }

            @Override
            public void onFailure(Call<SearchMovie> call, Throwable t) {

            }
        });
    }

}
