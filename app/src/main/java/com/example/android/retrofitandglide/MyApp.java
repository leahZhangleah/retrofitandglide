package com.example.android.retrofitandglide;

import android.app.Application;

import com.example.android.retrofitandglide.dagger.DaggerMovieComponent;
import com.example.android.retrofitandglide.dagger.MovieComponent;
import com.example.android.retrofitandglide.dagger.MovieModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class MyApp extends Application {
    private RefWatcher refWatcher;
    private MovieComponent movieComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        refWatcher = LeakCanary.install(this);
        movieComponent = DaggerMovieComponent.builder()
                .movieModule(new MovieModule(this))
                .build();
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    public MovieComponent getMovieComponent(){
        return movieComponent;
    }
}
