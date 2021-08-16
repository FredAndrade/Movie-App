package com.example.movieapptmdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.movieapptmdb.adapters.MovieRecyclerView;
import com.example.movieapptmdb.adapters.OnMovieListener;
import com.example.movieapptmdb.models.MovieModel;
import com.example.movieapptmdb.response.MovieResponse;
import com.example.movieapptmdb.response.MovieSearchResponse;
import com.example.movieapptmdb.retrofit.RetrofitClient;
import com.example.movieapptmdb.util.Credential;
import com.example.movieapptmdb.util.MovieApi;
import com.example.movieapptmdb.viewmodel.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerViewAdapter;

    private MovieListViewModel movieListViewModel;

    boolean isPopular = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SetupSearchView();

        recyclerView = findViewById(R.id.recyclerView);

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        movieListViewModel.searchMovieApiPopular(1);

        ConfigureRecyclerView();
        ObserveChange();
        ObservePopularMovies();


    }
    private void ObservePopularMovies(){

        movieListViewModel.getPopular().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null){
                    for (MovieModel movieModel: movieModels){

                        movieRecyclerViewAdapter.setmMovies(movieModels);
                    }
                }

            }
        });

    }


    private void ObserveChange(){

        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null){
                    for (MovieModel movieModel: movieModels){

                        movieRecyclerViewAdapter.setmMovies(movieModels);
                    }
                }

            }
        });
    }


    private void ConfigureRecyclerView(){
        movieRecyclerViewAdapter = new MovieRecyclerView(this);

        recyclerView.setAdapter(movieRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)){
                    movieListViewModel.searchNextPage();

                }
            }
        });



    }

    @Override
    public void onMovieClick(int position) {

        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie", movieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);

    }

    @Override
    public void onCategoryClick(String category) {

    }

    private void SetupSearchView() {

        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(
                        query,
                        1
                );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopular = false;
            }
        });


    }
}