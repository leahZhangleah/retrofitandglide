package com.example.android.retrofitandglide;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent searchIntent = getIntent();
        if(searchIntent.getAction().equals(Intent.ACTION_SEARCH)){
            String query = searchIntent.getStringExtra(SearchManager.QUERY);
            //Toast.makeText(this,"the query string is: "+query,Toast.LENGTH_SHORT).show();
        }
    }
}
