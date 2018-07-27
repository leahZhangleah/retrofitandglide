package com.example.android.retrofitandglide.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.retrofitandglide.Retrofit.SearchResult;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SearchViewModel extends ViewModel{
    private static final String LOG_TAG = SearchViewModel.class.getName();
    private Repository mRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseMutableLiveData = new MutableLiveData<>();
    private String query;
    private int page;
    //private boolean isCancelled = false;

    public SearchViewModel(Repository mRepository, String query, int page) {
        this.mRepository = mRepository;
        this.query = query;
        this.page = page;

    }

    public MutableLiveData<ApiResponse> searchResponse(){
        return responseMutableLiveData;
    }

    public void search(){
        Log.d(LOG_TAG,"search method from repository is called");
        compositeDisposable.add(
                mRepository.search(query,page)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseMutableLiveData.setValue(ApiResponse.loading());
                    }
                })
                .subscribe(new Consumer<List<SearchResult>>() {
                               @Override
                               public void accept(List<SearchResult> searchResults) throws Exception {
                                    responseMutableLiveData.setValue(ApiResponse.success(searchResults));
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   responseMutableLiveData.setValue(ApiResponse.error(throwable));
                               }
                           }
                )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

































