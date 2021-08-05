package com.example.movieapptmdb.retrofit;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapptmdb.AppExecutors;
import com.example.movieapptmdb.models.MovieModel;
import com.example.movieapptmdb.response.MovieSearchResponse;
import com.example.movieapptmdb.util.Credential;
import com.example.movieapptmdb.util.MovieApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;

    private RetrieveMoviesRunnable retrieveMoviesRunnable;





    public static MovieApiClient getInstance(){
        if (instance == null){
            instance = new MovieApiClient();
        }return instance;
    }

    private MovieApiClient(){
        mMovies = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return mMovies;
    }

    public void searchMoviesApi(String query, int pageNumber) {

        if (retrieveMoviesRunnable != null){
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);
    }

        private class RetrieveMoviesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest = false;

            public RetrieveMoviesRunnable(String query, int pageNumber) {
                this.query = query;
                this.pageNumber = pageNumber;
            }

            @Override
            public void run() {

                try {
                    Response response = getMovies(query, pageNumber).execute();
                    if (cancelRequest) {
                        return;
                    }
                    if (response.code() == 200) {
                        List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                        if (pageNumber == 1){
                            mMovies.postValue(list);
                        }else {
                            List<MovieModel> currentMovies = mMovies.getValue();
                            currentMovies.addAll(list);
                            mMovies.postValue(currentMovies);


                        }
                    } else {
                        String error = response.errorBody().string();
                        Log.v("Tag", "Error" +error);
                        mMovies.postValue(null);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    mMovies.postValue(null);
                }


                if (cancelRequest) {
                    return;
                }
            }
                private Call<MovieSearchResponse> getMovies(String query, int pageNumber){
                    return RetrofitClient.getMovieApi().searchMovie(
                            Credential.API_KEY,
                            query,
                            pageNumber);

                }
                private void cancelRequest(){
                    Log.v("Tag", "Cancelling search request");
                    cancelRequest = true;
                }


        }

}
