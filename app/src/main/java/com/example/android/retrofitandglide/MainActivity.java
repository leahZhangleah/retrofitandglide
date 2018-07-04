package com.example.android.retrofitandglide;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.retrofitandglide.ViewModel.MainViewModel;
import com.example.android.retrofitandglide.ViewModel.MainViewModelFactory;
import com.example.android.retrofitandglide.ViewModel.Repository;
import com.example.android.retrofitandglide.dagger.DaggerMovieComponent;
import com.example.android.retrofitandglide.dagger.MovieComponent;
import com.example.android.retrofitandglide.dagger.MovieModule;
import com.example.android.retrofitandglide.database.PopMovie;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity{
    private static final String LOG_TAG = MainActivity.class.getName();
    @Inject public Repository mRepository;
    private String category = "popular";
    private int page = 1;
    //CustomAdapter customAdapter;
    private CustomRecyclerviewAdapter adapter;
    private MovieComponent movieComponent;
    //ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieComponent = DaggerMovieComponent.builder().movieModule(new MovieModule(getApplication())).build();
        Log.d(LOG_TAG,"instantialize movie component");
        movieComponent.inject(this);
        //ListView popularMovie = findViewById(R.id.popular_movie_list);
        RecyclerView popularMovie = findViewById(R.id.popular_movie_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        popularMovie.setLayoutManager(layoutManager);
        adapter = new CustomRecyclerviewAdapter(this);
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
                adapter.setResults(popMovies);
            }
        });
        Log.d(LOG_TAG,"observe data returned from database and listen to change");

    }

}
