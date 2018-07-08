package com.example.android.retrofitandglide.Retrofit;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.android.retrofitandglide.FetchIntentService;
import com.example.android.retrofitandglide.database.PopMovie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkDataSource {
    private static final String LOG_TAG = NetworkDataSource.class.getName();
    private  String api_key = "846cd2cdc1ddb950b6f8e90df6261b3a";
    private  NetworkInterface mNetworkInterface;
    private  MutableLiveData<List<PopMovie>> mutableLiveData;
    private Context mContext;

    public NetworkDataSource(Context context,NetworkInterface mNetworkInterface) {
        this.mNetworkInterface = mNetworkInterface;
        mContext = context;
        mutableLiveData = new MutableLiveData<>();
    }

    public interface ResponseCallback{
        void onSuccess(MutableLiveData<List<PopMovie>> mutableLiveData);
        void onFailure();
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

    public void startFetchIntentService(){
        Intent intentToFetch = new Intent(mContext, FetchIntentService.class);
        mContext.startService(intentToFetch);
    }
}
