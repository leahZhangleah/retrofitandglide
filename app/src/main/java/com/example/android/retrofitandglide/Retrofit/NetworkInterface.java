package com.example.android.retrofitandglide.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkInterface {
    @GET("movie/{category}")
    Call<PopularMovie> getPopularMovie(@Path("category") String category,
                                       @Query("api_key") String api_key,
                                       @Query("page") int page);
}
