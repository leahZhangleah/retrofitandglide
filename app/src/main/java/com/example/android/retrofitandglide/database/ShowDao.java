package com.example.android.retrofitandglide.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ShowDao {
    @Query("SELECT * FROM popular_movies")
    LiveData<List<PopMovie>> loadPopularMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<PopMovie> popMovies);

    @Delete
    void deleteMovies(PopMovie... movies);

    @Query("DELETE FROM popular_movies")
    void deleteAllMovies();

}
