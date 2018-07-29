package com.example.android.retrofitandglide.Retrofit;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.example.android.retrofitandglide.R;
import com.example.android.retrofitandglide.database.PopMovie;
import com.example.android.retrofitandglide.model.MovieDetail;
import com.example.android.retrofitandglide.model.PopularMovie;
import com.example.android.retrofitandglide.model.Result;
import com.example.android.retrofitandglide.model.SearchMovie;
import com.example.android.retrofitandglide.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkDataSource {
    private static final String LOG_TAG = NetworkDataSource.class.getName();
    private  String api_key,append;
    private  NetworkInterface mNetworkInterface;
    private  MutableLiveData<List<PopMovie>> mutableLiveData;
    private  Context mContext;
    public NetworkDataSource(Context context,NetworkInterface mNetworkInterface) {
        this.mNetworkInterface = mNetworkInterface;
        mContext = context;
        mutableLiveData = new MutableLiveData<>();
        api_key = mContext.getResources().getString(R.string.api_key);
        append = mContext.getResources().getString(R.string.append_to_responde);
    }

    public interface ResponseCallback{
        void onSuccess(MutableLiveData<List<PopMovie>> mutableLiveData);
        void onFailure();
    }

    public void getPopularMoviesFromInternet(final ResponseCallback callback,String category, int page){
        Call<PopularMovie> popularMovieCall = mNetworkInterface.getPopularMovie(category,api_key,page,"zh");
        popularMovieCall.enqueue(new Callback<PopularMovie>() {
            @Override
            public void onResponse(Call<PopularMovie> call, Response<PopularMovie> response) {
                PopularMovie popularMovie = response.body();
                List<Result> results = popularMovie.getResults();
                List<PopMovie> popMovies = new ArrayList<>();
                for(Result result : results){
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


    public Observable<List<SearchResult>> searchFromInternet(String query, final int page){
        Log.d(LOG_TAG,"searchFromInternet is called");
        Observable<SearchMovie> searchMovieObservable = mNetworkInterface.search(api_key,query,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged();
        return searchMovieObservable
                .flatMap(new Function<SearchMovie, ObservableSource<List<SearchResult>>>() {
            @Override
            public ObservableSource<List<SearchResult>> apply(final SearchMovie searchMovie) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<SearchResult>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<SearchResult>> emitter) throws Exception {
                        if(!emitter.isDisposed()){
                            emitter.onNext(searchMovie.getResults());
                        }
                    }
                });
            }
        });
    }

    public Observable<MovieDetail> getMovieDetail(int movieId){
        Log.d(LOG_TAG,"get movie details from internet");
        return mNetworkInterface.getMovieDetail(movieId,api_key,append)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
