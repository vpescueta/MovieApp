package com.example.movieapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final MovieInterface movieInterface;
    private Context context;
    private ArrayList<Movie> movies;
    private int recyclerViewType;


    public static final int VIEW_1 = 1;
    public static final int VIEW_2 = 2;
    public static final int VIEW_3 = 3;


    public MovieAdapter(ArrayList<Movie> movies, Context context, MovieInterface movieInterface, int recyclerViewType) {
        this.movies = movies;
        this.context = context;
        this.movieInterface = movieInterface;
        this.recyclerViewType = recyclerViewType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_home_card_view, parent, false);
        if (recyclerViewType == VIEW_1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_home_card_view, parent, false);
        } else if(recyclerViewType == VIEW_2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_home_movie_list, parent, false);
        } else if (recyclerViewType == VIEW_3){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_home_search_movie, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Movie currentMovie = movies.get(position);
        holder.title.setText(currentMovie.getM_title());
        Picasso.get().load(currentMovie.getM_posterURL()).into(holder.poster);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieInterface != null && position != RecyclerView.NO_POSITION) {
                    movieInterface.onMovieItemClick(holder.poster, currentMovie);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateMovies(ArrayList<Movie> newMovies) {
        movies = newMovies;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView poster;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_recycler_cv_title);
            poster = view.findViewById(R.id.iv_recycler_cv_image);
        }
    }
}
