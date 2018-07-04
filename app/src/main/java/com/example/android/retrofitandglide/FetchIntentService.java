package com.example.android.retrofitandglide;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.retrofitandglide.Retrofit.NetworkDataSource;

public class FetchIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NetworkDataSource mNetworkDataSource;
    public FetchIntentService() {
        super("FetchIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }
}
