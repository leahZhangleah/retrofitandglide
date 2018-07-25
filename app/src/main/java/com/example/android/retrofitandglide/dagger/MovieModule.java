package com.example.android.retrofitandglide.dagger;

import android.app.Application;
import android.util.Log;

import com.example.android.retrofitandglide.AppExecutor;
import com.example.android.retrofitandglide.R;
import com.example.android.retrofitandglide.Retrofit.NetworkDataSource;
import com.example.android.retrofitandglide.Retrofit.NetworkInterface;
import com.example.android.retrofitandglide.ViewModel.Repository;
import com.example.android.retrofitandglide.database.ShowDao;
import com.example.android.retrofitandglide.database.ShowDatabase;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MovieModule {
    private static final String LOG_TAG = MovieModule.class.getName();
    private static NetworkInterface networkInterface;
    private static final Object LOCK = new Object();
    private static String baseUrl;
    private Application mApplication;

    public MovieModule(Application application) {
        this.mApplication = application;
        baseUrl = mApplication.getString(R.string.base_url);
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
    public NetworkInterface providesNetworkInterface(){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(interceptor);
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder()
                        .addHeader("Accept","application/json")
                        .addHeader("Request-Type","Android")
                        .addHeader("Content-Type","application/json");
                Request request = builder.build();
                return chain.proceed(request);
            }
        });
        OkHttpClient okHttpClient = httpClientBuilder.build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        networkInterface = retrofit.create(NetworkInterface.class);
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
