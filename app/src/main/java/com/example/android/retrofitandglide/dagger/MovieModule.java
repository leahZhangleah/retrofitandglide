package com.example.android.retrofitandglide.dagger;

import android.app.Application;
import android.util.Log;

import com.example.android.retrofitandglide.AppExecutor;
import com.example.android.retrofitandglide.Retrofit.NetworkDataSource;
import com.example.android.retrofitandglide.Retrofit.NetworkInterface;
import com.example.android.retrofitandglide.ViewModel.Repository;
import com.example.android.retrofitandglide.database.ShowDao;
import com.example.android.retrofitandglide.database.ShowDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MovieModule {
    private static final String LOG_TAG = MovieModule.class.getName();
    private static NetworkInterface networkInterface;
    private static final Object LOCK = new Object();
    private static String baseUrl = "https://api.themoviedb.org/3/";
    private Application mApplication;

    public MovieModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    public Repository providesRepository(NetworkDataSource networkDataSource, ShowDao showDao, AppExecutor appExecutor){
        Log.d(LOG_TAG,"provides repository");
        return Repository.getInstance(networkDataSource,showDao,appExecutor);
    }

    @Provides
    @Singleton
    public NetworkDataSource providesNetworkDataSource(NetworkInterface networkInterface){
        Log.d(LOG_TAG,"provides network data source");
        return new NetworkDataSource(mApplication,networkInterface);
    }

    @Provides
    @Singleton
    public NetworkInterface providesNetworkInterface(){
        if (networkInterface == null){
            synchronized (LOCK){
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                networkInterface = retrofit.create(NetworkInterface.class);
            }
        }
        Log.d(LOG_TAG,"provides network interface");
        return networkInterface;
    }

    @Provides
    @Singleton
    public ShowDao providesShowDao(ShowDatabase showDatabase){
        Log.d(LOG_TAG,"provides show dao");
        return showDatabase.getShowDao();
    }

    @Provides
    @Singleton
    public ShowDatabase providesShowDatabase(){
        Log.d(LOG_TAG,"provides show database");
        return ShowDatabase.getInstance(mApplication);
    }

    @Provides
    @Singleton
    public AppExecutor providesAppExecutor(){
        Log.d(LOG_TAG,"provides app executor");
        return AppExecutor.getInstance();
    }
}
