package com.example.android.retrofitandglide.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.retrofitandglide.MyApp;
import com.example.android.retrofitandglide.R;
import com.example.android.retrofitandglide.ViewModel.ApiResponse;
import com.example.android.retrofitandglide.ViewModel.MovieDetailModelFactory;
import com.example.android.retrofitandglide.ViewModel.MovieDetailViewModel;
import com.example.android.retrofitandglide.ViewModel.Repository;
import com.example.android.retrofitandglide.model.Cast;
import com.example.android.retrofitandglide.model.MovieDetail;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DetailActivity extends AppCompatActivity {
    private int id;
    @Inject
    public Repository mRepository;
    private  Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView overview,firstReview;
    private RecyclerView castRv, trailerRv;
    private CastAdapter castAdapter;
    private TrailerAdapter trailerAdapter;
    private List<Cast> castList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((MyApp)getApplication()).getMovieComponent().inject(this);
        initViews();
        Intent intent = getIntent();
        if(intent!=null){
            id = intent.getIntExtra("movieId",-1);
            if(id != -1){
                MovieDetailModelFactory movieDetailModelFactory =
                        new MovieDetailModelFactory(getApplication(),mRepository,id);
                final MovieDetailViewModel movieDetailViewModel = ViewModelProviders
                        .of(this,movieDetailModelFactory)
                        .get(MovieDetailViewModel.class);
                movieDetailViewModel.getMovieDetailResponse().observe(this, new Observer<ApiResponse>() {
                    @Override
                    public void onChanged(@Nullable ApiResponse apiResponse) {
                        consumeApiResponse(apiResponse);
                    }
                });
                movieDetailViewModel.getMovieDetail();
            }
        }
    }
    private void initViews(){
        castList = new ArrayList<>();
        appBarLayout = findViewById(R.id.app_bar);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        overview = findViewById(R.id.overview);
        firstReview = findViewById(R.id.first_review);
        castRv = findViewById(R.id.cast_rv);
        trailerRv = findViewById(R.id.trailer_rv);
        castAdapter = new CastAdapter(castList,this);
        //todo
    }

    private void consumeApiResponse(ApiResponse apiResponse){
        switch (apiResponse.status){
            case LOADING:
                break;
            case SUCCESS:
                renderSuccessResponse((MovieDetail) apiResponse.data);
                break;
            case ERROR:
                Toast.makeText(this, "error in getting data", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void renderSuccessResponse(MovieDetail movieDetail){
        if(movieDetail!=null){
            collapsingToolbarLayout.setTitle(movieDetail.getTitle()
                    +" ("
                    +movieDetail.getReleaseDate()
                    +")");
            overview.setText(movieDetail.getOverview());
            //GlideApp.with(this)
              //      .load("https://image.tmdb.org/t/p/w200"+movieDetail.getBackdropPath())
                //    .into()
        }

    }
}
