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
import com.example.android.retrofitandglide.model.Cast;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private List<Cast> castList;
    private Context mContext;

    public CastAdapter(List<Cast> castList, Context mContext) {
        this.castList = castList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_row,parent,false);
        return new CastViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        Cast cast = castList.get(position);
        holder.castName.setText(cast.getName());
        GlideApp.with(mContext)
                .load("https://image.tmdb.org/t/p/w200"+cast.getProfilePath())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.castImg);
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder{
        ImageView castImg;
        TextView castName;
        public CastViewHolder(View itemView) {
            super(itemView);
            castImg = itemView.findViewById(R.id.cast_img);
            castName = itemView.findViewById(R.id.cast_name);
        }
    }
}
