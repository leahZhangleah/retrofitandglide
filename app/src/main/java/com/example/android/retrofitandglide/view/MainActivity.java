package com.example.android.retrofitandglide.view;

import android.app.SearchManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.retrofitandglide.MyApp;
import com.example.android.retrofitandglide.R;
import com.example.android.retrofitandglide.ViewModel.MainViewModel;
import com.example.android.retrofitandglide.ViewModel.MainViewModelFactory;
import com.example.android.retrofitandglide.ViewModel.Repository;
import com.example.android.retrofitandglide.database.PopMovie;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements CustomRecyclerviewAdapter.ViewClickListener{
    private static final String LOG_TAG = MainActivity.class.getName();
    @Inject public Repository mRepository;
    private String category = "popular";
    private int page = 1;
    //CustomAdapter customAdapter;
    private CustomRecyclerviewAdapter adapter;
    private List<PopMovie> popMoviesList;
    //ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG,"instantialize movie component");
        ((MyApp)getApplication()).getMovieComponent().inject(this);
        //ListView popularMovie = findViewById(R.id.popular_movie_list);
        RecyclerView popularMovie = findViewById(R.id.popular_movie_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        popularMovie.setLayoutManager(layoutManager);
        popMoviesList = new ArrayList<>();
        adapter = new CustomRecyclerviewAdapter(this,popMoviesList,this);
        //customAdapter = new CustomAdapter(this,new ArrayList<Result>());
        //arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        popularMovie.setAdapter(adapter);
        loadJson();
    }

    private void loadJson(){
        MainViewModelFactory factory = new MainViewModelFactory(mRepository,category,page);
        Log.d(LOG_TAG,"build main view model factory");
        MainViewModel mainViewModel = ViewModelProviders.of(this,factory).get(MainViewModel.class);
        Log.d(LOG_TAG,"build main view model");
        LiveData<List<PopMovie>> popMovies = mainViewModel.getPopularMovies();
        popMovies.observe(this, new Observer<List<PopMovie>>() {
            @Override
            public void onChanged(@Nullable List<PopMovie> popMovies) {
                popMoviesList.clear();
                popMoviesList.addAll(popMovies);
                adapter.notifyDataSetChanged();
            }
        });
        Log.d(LOG_TAG,"observe data returned from database and listen to change");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.search);
        ComponentName componentName = new ComponentName(this,SearchActivity.class);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    searchItem.collapseActionView();
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        popMoviesList.clear();
    }

    @Override
    public void onClick(int movieId) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("movieId",movieId);
        startActivity(intent);
    }
}
