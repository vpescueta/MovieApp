package com.example.movieapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;


public class HomeFragment extends Fragment {
    FragmentHomeBinding homeBinding;
    ArrayList<Movie> movieArrayList = new ArrayList<>();
    ArrayList<Movie> featuredMovieList = new ArrayList<>();
    MovieInterface movieInterface;
    MovieAdapter movieAdapter;
    RecyclerView movieListRecyclerView;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> actvAdapter;
    ArrayList<String> titles;
    TextView movieList;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        setUpTransitions();
        setUpMovies();
        setUpRecyclerViews();
        setUpAutocompleteTextView();
        setUpFabButton();
        setUpMovieListFilter();
        return homeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeBinding = null;
    }

    private void setUpTransitions() {
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));
        setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));
        setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }


    protected void setUpMovies() {
        titles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.movie_titles)));
        String[] years = getResources().getStringArray(R.array.movie_years);
        String[] imdbIDs = getResources().getStringArray(R.array.movie_imdb_ids);
        String[] posterURLs = getResources().getStringArray(R.array.movie_poster);
        String[] types = getResources().getStringArray(R.array.movie_genres);
        String[] trailers = getResources().getStringArray(R.array.movie_trailers);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.movie_booleans);
        boolean[] movieBooleans = new boolean[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            movieBooleans[i] = typedArray.getBoolean(i, false);
        }
        typedArray.recycle();

        for (int i = 0; i < titles.size(); i++) {
            Movie movie = new Movie(titles.get(i), years[i], imdbIDs[i], types[i], posterURLs[i], movieBooleans[i], trailers[i]);
            movieArrayList.add(movie);
            Log.i("video trailer", trailers[i].toString());
            if (movieBooleans[i]) {
                featuredMovieList.add(movie);
            }
        }
    }

    private void setUpRecyclerViews() {
        RecyclerView featuredRecyclerView = homeBinding.rvFeatured;
        MovieAdapter movieAdapterFeatured = new MovieAdapter(featuredMovieList, getContext(), movieInterface, MovieAdapter.VIEW_1);
        featuredRecyclerView.setAdapter(movieAdapterFeatured);
        featuredRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        movieListRecyclerView = homeBinding.rvMovieList;
        movieAdapter = new MovieAdapter(movieArrayList, getContext(), movieInterface, MovieAdapter.VIEW_2);
        movieListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movieListRecyclerView.setAdapter(movieAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        movieListRecyclerView.addItemDecoration(new GridSpacingItemStyle(spacingInPixels));

        setUpItemTouchHelper(movieListRecyclerView, movieAdapter);

    }

    private void setUpItemTouchHelper(RecyclerView movieListRecyclerView, MovieAdapter movieAdapter) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Movie deletedMovie = movieArrayList.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();
                movieArrayList.remove(position);
                movieAdapter.notifyItemRemoved(position);

                Snackbar.make(movieListRecyclerView, deletedMovie.getM_title(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movieArrayList.add(position, deletedMovie);
                        movieAdapter.notifyItemInserted(position);
                    }
                }).show();
            }
        }).attachToRecyclerView(movieListRecyclerView);
    }


    private void setUpFabButton() {
        FloatingActionButton fab = homeBinding.fabtnHomeAdd;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovieDialog();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpAutocompleteTextView() {
        autoCompleteTextView = homeBinding.actvSearchMovie;
        actvAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, titles);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter((actvAdapter));

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movieSelected = (String) parent.getItemAtPosition(position);
                navigateToMovieInfo(movieSelected);
            }

            private void navigateToMovieInfo(String movieSelected) {
                for (Movie movie : movieArrayList) {
                    if (movie.getM_title().equals(movieSelected)) {
                        if (getActivity() instanceof MovieInterface) {
                            ((MovieInterface) getActivity()).onMovieItemClick(movie);
                        }
                        break;
                    }
                }
            }
        });

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String searchMovie = autoCompleteTextView.getText().toString();
                    boolean movieFound = false;
                    for (Movie movie : movieArrayList) {
                        if (movie.getM_title().equals(searchMovie)) {
                            movieFound = true;
                            break;
                        }
                    }
                    if (!movieFound) {
                        Toast.makeText(getContext(), "Search key not found", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });

    }


    private void addMovieDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add a new movie");

        final EditText inputTitle = new EditText(getContext());
        inputTitle.setHint("Title");
        final EditText inputYear = new EditText(getContext());
        inputYear.setHint("Year");
        final EditText inputType = new EditText(getContext());
        inputType.setHint("Genre");
        final EditText inputIMDB = new EditText(getContext());
        inputIMDB.setHint("imdb");
        final EditText inputURL = new EditText(getContext());
        inputURL.setHint("Poster URL");
        final EditText inputURI = new EditText(getContext());
        inputURI.setHint("Trailer URL");
        final CheckBox inputFeatured = new CheckBox(getContext());
        inputFeatured.setText("Featured Movie");


        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 10, 20, 10);
        layout.addView(inputTitle);
        layout.addView(inputYear);
        layout.addView(inputType);
        layout.addView(inputIMDB);
        layout.addView(inputURL);
        layout.addView(inputURI);
        layout.addView(inputFeatured);

        builder.setView(layout);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = inputTitle.getText().toString().trim();
                String type = inputType.getText().toString().trim();
                String year = inputYear.getText().toString().trim();
                String imdb = inputIMDB.getText().toString().trim();
                String posterURL = inputURL.getText().toString().trim();
                boolean featured = inputFeatured.isChecked();
                String trailer = inputURI.getText().toString().trim();
                if (!title.isEmpty() &&
                        !type.isEmpty() &&
                        !year.isEmpty() &&
                        !imdb.isEmpty() &&
                        !posterURL.isEmpty() &&
                        !trailer.isEmpty()) {
                    Movie newMovie = new Movie(title, year, imdb, type, posterURL, featured, trailer);
                    movieArrayList.add(newMovie);
                    if (featured) {
                        featuredMovieList.add(newMovie);
                    }
                    movieAdapter.notifyItemInserted(movieArrayList.size() - 1);
                    movieListRecyclerView.scrollToPosition(movieArrayList.size() - 1);


                    titles.add(title);
                    actvAdapter.notifyDataSetChanged();
                    autoCompleteTextView.setText("");
                    autoCompleteTextView.dismissDropDown();
                }else{
                    Toast.makeText(getContext(), "Movie added is invalid", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void setUpMovieListFilter(){
        movieList = homeBinding.tvHomeMovieListing;
        movieList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    showGenreDialog();
                    return true;
                }

                return false;
            }
        });
    }

    private void showGenreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_genre_list, null);
        ListView genreListView = dialogView.findViewById(R.id.genre_list_view);

        String[] genres = getResources().getStringArray(R.array.movie_genres_indiv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, genres);
        genreListView.setAdapter(adapter);

        builder.setView(dialogView);
        builder.setTitle("Select Genre");
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        genreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String genreSelected = (String) parent.getItemAtPosition(position);
                if (genreSelected.equals("All")){
                    filterMoviesByGenre(null);
                }else{
                    filterMoviesByGenre(genreSelected);
                }
                dialog.dismiss();
            }
        });

    }

    private void filterMoviesByGenre(String genreSelected) {
        if (genreSelected == null) {
            movieAdapter.updateMovies(movieArrayList);
            movieList.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_filter_list_24, 0);
            return;
        }

        movieList.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_filter_list_alt_24, 0);
        ArrayList<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movieArrayList) {

            String[] genres = movie.getM_type().split(",\\s*");
            for (String genre : genres) {
                if (genre.equalsIgnoreCase(genreSelected)) {
                    filteredMovies.add(movie);
                    break;
                }
            }
        }
        movieAdapter.updateMovies(filteredMovies);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MovieInterface) {
            movieInterface = (MovieInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement MovieInterface");
        }
    }


}
