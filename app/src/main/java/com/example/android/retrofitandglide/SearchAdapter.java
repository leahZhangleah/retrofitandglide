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
import com.example.android.retrofitandglide.Retrofit.SearchResult;

import java.util.ArrayList;

public class SearchAdapter extends ArrayAdapter<SearchResult> {
    private String imagePath = "";
    private String searchTitle = "";
    private String searchSummary = "";
    private String baseUrl;
    private Context mContext;
    public SearchAdapter(@NonNull Context context, ArrayList<SearchResult> objects) {
        super(context, R.layout.search_list_item, objects);
        baseUrl = context.getString(R.string.base_url);
        mContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_list_item,parent,false);
        }

        SearchResult mSearchResult = getItem(position);
        ImageView searchResultIMG = convertView.findViewById(R.id.search_result_image_view);
        TextView searchResultTitle = convertView.findViewById(R.id.search_result_title);
        TextView searchResultSummary = convertView.findViewById(R.id.search_result_summary);

        if(mSearchResult!=null){
            imagePath = mSearchResult.getPosterPath();
            searchTitle = mSearchResult.getOriginalTitle();
            searchSummary = mSearchResult.getOverview();
            searchResultTitle.setText(searchTitle);
            searchResultSummary.setText(searchSummary);
            Glide.with(mContext).load("https://image.tmdb.org/t/p/w500"+imagePath).into(searchResultIMG);
        }

        //todo: handle error imageUrl
        return convertView;
    }
}
