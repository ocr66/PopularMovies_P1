package com.example.ocr66.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by o.lopez.cienfuegos on 01/12/2015.
 */
public class MovieInfo extends BaseAdapter {


    private Context context;
    private String[] movieNames;
    private String[] movieImage;

    public MovieInfo(Context context, String[] movieImage){
        this.context = context;
        this.movieImage = movieImage;
    }

    public MovieInfo(Context context, String[] movieNames, String[] movieImage){
        this.context = context;
        this.movieNames = movieNames;
        this.movieImage = movieImage;
    }


    @Override
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
    }
}
