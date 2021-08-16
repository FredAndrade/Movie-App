package com.example.movieapptmdb.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapptmdb.R;
import com.example.movieapptmdb.models.MovieModel;
import com.example.movieapptmdb.util.Credential;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;

    private static final int DISPLAY_POPULARS = 1;
    private static final int DISPLAY_SEARCH = 2;

    public MovieRecyclerView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        if (viewType == DISPLAY_SEARCH){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,
                    parent,false);
            return new MovieViewHolder(view, onMovieListener);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movies_layout,
                    parent,false);
            return new MovieViewHolder(view, onMovieListener);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

       int itemViewType = getItemViewType(i);
       if (itemViewType == DISPLAY_SEARCH){
           ((MovieViewHolder)holder).title.setText(mMovies.get(i).getTitle());

           ((MovieViewHolder)holder).ratingBar.setRating(mMovies.get(i).getVote_average()/2);

           Glide.with(holder.itemView.getContext())
                   .load("https://image.tmdb.org/t/p/w500"
                           +mMovies.get(i).getPoster_path())
                   .into(((MovieViewHolder)holder).imageView);
       } else {
           ((PopularViewHolder)holder).textView22.setText(mMovies.get(i).getTitle());
           ((PopularViewHolder)holder).ratingBar22.setRating(mMovies.get(i).getVote_average()/2);

           Glide.with(holder.itemView.getContext())
                   .load("https://image.tmdb.org/t/p/w500"
                           +mMovies.get(i).getPoster_path())
                   .into(((PopularViewHolder)holder).imageView22);

       }

    }

    @Override
    public int getItemCount() {
        if (mMovies != null){
            return mMovies.size();
        }
        return 0;
    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }

    public MovieModel getSelectedMovie(int position){
        if (mMovies != null){
            if (mMovies.size() > 0){
                return mMovies.get(position);
            }
        }

        return null;

    }

    @Override
    public int getItemViewType(int position){
        if (Credential.POPULAR){
            return DISPLAY_POPULARS;
        } else
            return DISPLAY_SEARCH;
    }
}
