package com.example.ocr66.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by o.lopez.cienfuegos on 01/12/2015.
 */
public class MovieInfo extends Activity{


    private Context context;
    private String movieNames;
    private String movieImage;
    private String movieSynopsis;
    private String movieReleaseDate;
    private float movieRating;
    private Intent intent;
    ImageView poster;
    TextView title;
    TextView synopsis;
    RatingBar rating;
    TextView released;
    String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500";

    public MovieInfo(){

    }

    public MovieInfo(Context context, String movieNames, String movieImage, String movieSynopsis, String movieRating, String movieReleaseDate){
        this.context = context;
        this.movieNames = movieNames;
        this.movieImage = movieImage;
        this.movieSynopsis = movieSynopsis;
        this.movieRating = Float.parseFloat(movieRating);
        this.movieReleaseDate = movieReleaseDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        intent = getIntent();

        poster = (ImageView)findViewById(R.id.movie_poster);
        title = (TextView)findViewById(R.id.movie_title);
        synopsis = (TextView)findViewById(R.id.movie_synopsis);
        rating = (RatingBar)findViewById(R.id.movie_rating);
        released = (TextView)findViewById(R.id.movie_release_date);

        if (intent != null) {
            movieNames = intent.getStringExtra("Name");
            movieImage = intent.getStringExtra("Poster");
            movieSynopsis = intent.getStringExtra("Synopsis");
            movieRating = Float.parseFloat(intent.getStringExtra("Rating"));
            movieReleaseDate = intent.getStringExtra("Released");

            title.setText(movieNames);
            synopsis.setText(movieSynopsis);
            rating.setStepSize((float) 0.1);
            rating.setRating(movieRating / 2);
            released.setText(movieReleaseDate);

            Picasso.with(getBaseContext()).load(BASE_IMAGE_URL + movieImage).into(poster);
        }
    }
}
