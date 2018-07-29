package com.example.android.retrofitandglide.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.example.android.retrofitandglide.GlideApp;
import com.example.android.retrofitandglide.R;
import com.example.android.retrofitandglide.database.PopMovie;

import java.util.List;

public class CustomRecyclerviewAdapter extends RecyclerView.Adapter<CustomRecyclerviewAdapter.CustomViewHolder> {
    private static final String LOG_TAG = CustomRecyclerviewAdapter.class.getName();
    private List<PopMovie> popMovies;
    private Context mContext;
    private ViewClickListener viewClickListener;
    public CustomRecyclerviewAdapter(Context context,List<PopMovie> popMovies,ViewClickListener viewClickListener){
        mContext = context;
        this.popMovies = popMovies;
        this.viewClickListener = viewClickListener;
    }
    public interface ViewClickListener{
        void onClick(int movieId);
    }
    @NonNull
    @Override
    public CustomRecyclerviewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.popular_movie_item,parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (popMovies !=null){
            final PopMovie movie = popMovies.get(position);
            //holder.titleV.setText("test");
            //String imagePath = result.getPosterPath();
            //Glide.with(mContext).load("https://image.tmdb.org/t/p/w500"+imagePath).into(holder.imageV);
            holder.bindMovie(movie);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewClickListener.onClick(movie.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (popMovies !=null){
            return popMovies.size();
        }
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView titleV;
        ImageView imageV;
        public CustomViewHolder(View itemView) {
            super(itemView);
            titleV = (TextView) itemView.findViewById(R.id.popular_movie_title);
            imageV = (ImageView) itemView.findViewById(R.id.popular_movie_image);
        }

        public void bindMovie(PopMovie movie){
            titleV.setText(movie.getTitle());
            String imagePath = movie.getImage_path();
            GlideApp.with(mContext)
                    .load("https://image.tmdb.org/t/p/w200"+imagePath)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageV);
        }
    }

}
