package com.example.movieapp;


import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.movieapp.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements MovieInterface{
    private HomeFragment homeFragment;
    private ActivityMainBinding mainActBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainActBinding.getRoot());

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(mainActBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        if (savedInstanceState == null) {
            homeFragment = new HomeFragment();
            replaceFragment(homeFragment);
        } else {
            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fl_main);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onMovieItemClick(View sharedImageView, Movie movie) {
        Fragment movieInfoFragment = new MovieInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("movie", movie);
        movieInfoFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(homeFragment);
        fragmentTransaction.addSharedElement(sharedImageView, sharedImageView.getTransitionName());

        fragmentTransaction.replace(R.id.fl_main, movieInfoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onMovieItemClick(Movie movie) {
        Fragment movieInfoFragment = new MovieInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("movie", movie);
        movieInfoFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(homeFragment);

        fragmentTransaction.replace(R.id.fl_main, movieInfoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}