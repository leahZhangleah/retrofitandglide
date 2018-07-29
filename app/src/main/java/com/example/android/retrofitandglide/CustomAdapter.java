package com.example.android.retrofitandglide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.retrofitandglide.model.Result;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Result> {
    public CustomAdapter(@NonNull Context context, ArrayList<Result> results) {
        super(context, R.layout.popular_movie_item,results);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.popular_movie_item,parent,false);
        }
        Result result = getItem(position);
        TextView titleV = (TextView) convertView.findViewById(R.id.popular_movie_title);
        ImageView imageV = (ImageView) convertView.findViewById(R.id.popular_movie_image);
        titleV.setText(result.getTitle());
        String imagePath = result.getPosterPath();
        Glide.with(getContext()).load("https://image.tmdb.org/t/p/w500"+imagePath).into(imageV);
        return convertView;
    }
}
