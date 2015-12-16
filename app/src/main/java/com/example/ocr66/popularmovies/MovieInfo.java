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
    private float movieRating;
    private Intent intent;
    ImageView poster;
    TextView title;
    TextView synopsis;
    RatingBar rating;
    String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500";

    public MovieInfo(){

    }
    public MovieInfo(Context context, String movieImage){
        this.context = context;
        this.movieImage = movieImage;
    }

    public MovieInfo(Context context, String movieNames, String movieImage, String movieSynopsis, String movieRating){
        this.context = context;
        this.movieNames = movieNames;
        this.movieImage = movieImage;
        this.movieSynopsis = movieSynopsis;
        this.movieRating = Float.parseFloat(movieRating);
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

        if (intent != null) {
            movieNames = intent.getStringExtra("Name");
            movieImage = intent.getStringExtra("Poster");
            movieSynopsis = intent.getStringExtra("Synopsis");
            movieRating = Float.parseFloat(intent.getStringExtra("Rating"));

            title.setText(movieNames);
            synopsis.setText(movieSynopsis);
            rating.setStepSize((float)0.1);
            rating.setRating(movieRating/2);

            Picasso.with(getBaseContext()).load(BASE_IMAGE_URL + movieImage).into(poster);
        }
    }


    /*@Override
    public int getCount() {return 0;}

    @Override
    public Object getItem(int position) {return null;}

    @Override
    public long getItemId(int position) {return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(convertView == null){
            gridView = new View(context);

            gridView = inflater.inflate(R.layout.movie_info, null);
            //TextView textView = (TextView)gridView.findViewById(R.id.movie_name);
            ImageView imageView = (ImageView)gridView.findViewById(R.id.poster);

            //private String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500";
            //Picasso.with(getActivity()).load(BASE_IMAGE_URL + params[i]).into(posterTest);
        }
        return null;
    }*/
}
