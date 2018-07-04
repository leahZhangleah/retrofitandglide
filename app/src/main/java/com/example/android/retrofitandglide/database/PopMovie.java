package com.example.android.retrofitandglide.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "popular_movies")
public class PopMovie {
    @PrimaryKey
    private int id;
    private double vote_average;
    private String title;
    private String image_path;

    public PopMovie(int id, double vote_average, String title, String image_path) {
        this.id = id;
        this.vote_average = vote_average;
        this.title = title;
        this.image_path = image_path;
    }

    public int getId() {
        return id;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_path() {
        return image_path;
    }
}
