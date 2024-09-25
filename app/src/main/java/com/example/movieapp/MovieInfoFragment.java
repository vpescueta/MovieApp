package com.example.movieapp;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movieapp.databinding.FragmentMovieInfoBinding;
import com.squareup.picasso.Picasso;

public class MovieInfoFragment extends Fragment {
    private FragmentMovieInfoBinding movieInfoBinding;
    private WebView webView;

    public MovieInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_right));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movieInfoBinding = FragmentMovieInfoBinding.inflate(inflater, container, false);
        return movieInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = movieInfoBinding.webView;
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Movie movie = getArguments() != null ? getArguments().getParcelable("movie") : null;
        if (movie != null) {
            movieInfoBinding.tvMovieInfoTitle.setText(movie.getM_title());
            movieInfoBinding.tvMovieInfoYear.setText(movie.getM_year());
            movieInfoBinding.tvMovieInfoImdb.setText(movie.getM_imdbID());
            movieInfoBinding.tvMovieInfoType.setText(movie.getM_type());

            Picasso.get().load(movie.getM_posterURL()).into(movieInfoBinding.ivMovieInfoImage);
            movieInfoBinding.ivMovieInfoImage.setTransitionName(movie.getM_title());

            String videoId = extractVideoId(movie.getM_trailer());
            String videoUrl = "https://www.youtube.com/embed/" + videoId;
            webView.loadUrl(videoUrl);

            webView.setWebViewClient(new WebViewClient());
        }

        movieInfoBinding.btnMovieInfoBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomeFragment(v);
            }
        });
    }

    private String extractVideoId(String youtubeUrl) {

        String videoId = "";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("https://www.youtube.com/watch?v=")) {
            videoId = youtubeUrl.substring(youtubeUrl.indexOf("v=") + 2);
            int ampersandPosition = videoId.indexOf('&');
            if (ampersandPosition != -1) {
                videoId = videoId.substring(0, ampersandPosition);
            }
        }
        return videoId;
    }

    public void goToHomeFragment(View view) {
        HomeFragment homeFragment = new HomeFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fl_main, homeFragment)
                .commit();
        homeFragment.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        homeFragment.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.destroy();
        movieInfoBinding = null;
    }




}
