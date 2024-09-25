package com.example.movieapp;

import android.view.View;

public interface MovieInterface {
    void onMovieItemClick(View sharedImageView, Movie movie);
    void onMovieItemClick(Movie movie);
}
