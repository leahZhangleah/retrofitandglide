package com.example.android.retrofitandglide.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.android.retrofitandglide.model.MovieDetail;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MovieDetailViewModel extends AndroidViewModel {
    private Repository mRepository;
    private int movieId;
    private MutableLiveData<ApiResponse> movieDetailResponse;
    private CompositeDisposable compositeDisposable;


    public MovieDetailViewModel(@NonNull Application application, Repository mRepository, int movieId) {
        super(application);
        this.mRepository = mRepository;
        this.movieId = movieId;
        movieDetailResponse = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
    }

    public MutableLiveData<ApiResponse> getMovieDetailResponse() {
        return movieDetailResponse;
    }

    public void getMovieDetail(){
        compositeDisposable.add(
                mRepository.getMovieDetail(movieId)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        movieDetailResponse.setValue(ApiResponse.loading());
                    }
                })
                .subscribe(new Consumer<MovieDetail>() {
                    @Override
                    public void accept(MovieDetail movieDetail) throws Exception {
                        movieDetailResponse.setValue(ApiResponse.success(movieDetail));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        movieDetailResponse.setValue(ApiResponse.error(throwable));
                    }
                })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
