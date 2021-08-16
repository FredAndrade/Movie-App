package com.example.movieapptmdb.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapptmdb.R;


public class PopularViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    OnMovieListener onMovieListener;
    ImageView imageView22;
    RatingBar ratingBar22;
    TextView textView22;
    public PopularViewHolder(@NonNull View itemView, OnMovieListener onMovieListener){
        super(itemView);

        this.onMovieListener = onMovieListener;
        imageView22 = itemView.findViewById(R.id.movie_img_popular);
        ratingBar22 = itemView.findViewById(R.id.rating_bar_popular);
        textView22 = itemView.findViewById(R.id.movie_title_popular);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){

    }
}
