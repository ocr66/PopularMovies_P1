package com.example.ocr66.popularmovies;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by o.lopez.cienfuegos on 01/12/2015.
 */
public class FragmentMain extends Fragment{

    //private static final String LOG_TAG = FetchMovieInfo.class.getSimpleName();
    public View rootView;
    private String[] names;
    private String[] posters;
    private String[] synopsis;
    private String[] rating;
    private String[] releaseDate;
    private ArrayAdapter<ImageView> adapter;
    private GridView gridView;
    private String movieToGet = "popular";
    private TextView list;
    private Button previous;
    private Button next;
    private int page = 1;

    public FragmentMain(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(movieToGet == "popular") {
            menu.getItem(1).setEnabled(false);
            menu.getItem(2).setEnabled(true);
        }else{
            menu.getItem(1).setEnabled(true);
            menu.getItem(2).setEnabled(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            updateMovies();
            //return true;
        }else if(id == R.id.action_popular){
            if(movieToGet == "topRated") {
                movieToGet = "popular";
                page = 1;
                previous.setText(R.string.previous_page);
                previous.setEnabled(false);
                next.setText((page+1) + "");
                updateMovies();
            }
        }else if(id == R.id.action_top_rated) {
            if (movieToGet == "popular"){
                movieToGet = "topRated";
                page = 1;
                previous.setText(R.string.previous_page);
                previous.setEnabled(false);
                next.setText((page+1) + "");
                updateMovies();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new ArrayAdapter<ImageView>(getActivity(),
                R.layout.movie_info,
                R.id.poster,
                new ArrayList<ImageView>());

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        list = (TextView)rootView.findViewById(R.id.movie_list);

        previous = (Button)rootView.findViewById(R.id.previous_movies);
        next = (Button)rootView.findViewById(R.id.next_movies);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page--;
                if (page >= 2) {
                    previous.setText((page - 1) + "");
                    next.setText((page + 1) + "");
                } else if (page == 1) {
                    previous.setText(R.string.previous_page);
                    previous.setEnabled(false);
                    next.setText((page + 1) + "");
                }
                updateMovies();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                updateMovies();
                if (page == 2) {
                    previous.setText((page - 1) + "");
                    previous.setEnabled(true);
                    next.setText((page + 1) + "");
                } else if (page > 2) {
                    previous.setText((page - 1) + "");
                    next.setText((page + 1) + "");
                }
            }
        });

        gridView = (GridView)rootView.findViewById(R.id.grid_view_movies);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), names[position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MovieInfo.class);
                intent.putExtra("Name", names[position]);
                intent.putExtra("Poster", posters[position]);
                intent.putExtra("Synopsis", synopsis[position]);
                intent.putExtra("Rating", rating[position]);
                intent.putExtra("Released", releaseDate[position]);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.v(LOG_TAG, "Fetching info");
        updateMovies();
        //Log.v(LOG_TAG, "Names " + names);
    }

    private void updateMovies(){
        if(movieToGet == "popular"){
            list.setText("Popular");
        }else{
            list.setText("Top Rated");
        }
        FetchMovieInfo fetchMovieInfo = new FetchMovieInfo();
        //Log.v(LOG_TAG, "Fetching movies info...");
        fetchMovieInfo.execute();
        //Log.v(LOG_TAG, "Done");
    }

    private void fetchPosters(){
        //Log.v(LOG_TAG, "Fetching posters...");
        if(posters != null) {
            gridView.setAdapter(new CustomAdapter(getActivity(), posters));
            //Log.v(LOG_TAG, "Done");
        }
    }

    public class FetchMovieInfo extends AsyncTask<Void, Void, String[]> {

        private String LOG_TAG = FetchMovieInfo.class.getSimpleName();
        private String BASE_URL = "https://api.themoviedb.org/3/movie";
        private String POPULAR_MOVIES = "popular";
        private String TOP_RATED = "top_rated";
        private String LIST = "popular";
        private String LANGUAGE = "language";
        private String KEY = "api_key"; //Do not change this value, key must be defined on strings.xml
        private String RESULTS = "results";
        private String MOVIE_NAME = "title";
        private String MOVIE_IMAGE = "poster_path";
        private String MOVIE_SYNOPSIS = "overview";
        private String MOVIE_RATING = "vote_average";
        private String MOVIE_RELEASE_DATE = "release_date";
        private String PAGE = "page";
        private String pageNumber = "";

        private String[] getMovieInfoFromJson(String movieInfoJsonStr) throws JSONException {

            JSONObject moviesJson = new JSONObject(movieInfoJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(RESULTS);

            String[] resultsStr = new String[moviesArray.length()];
            for(int i = 0; i < moviesArray.length(); i++){
                String name;
                String posterPath;
                String synopsis;
                double rating;
                String released;

                JSONObject movie = moviesArray.getJSONObject(i);
                name = movie.getString(MOVIE_NAME);
                posterPath = movie.getString(MOVIE_IMAGE);
                synopsis = movie.getString(MOVIE_SYNOPSIS);
                rating = movie.getDouble(MOVIE_RATING);
                released = movie.getString(MOVIE_RELEASE_DATE);

                resultsStr[i] =
                        name + "--" + posterPath + "--" + synopsis + "--" + rating + "--" + released;
                //Si solo quiero la imagen sería
                //resultsStr[i] = posterPath;
            }

            /*for (String s : resultsStr){
                Log.v(LOG_TAG, "Movie entry " + s);
            }*/
            return resultsStr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.v(LOG_TAG, "On Pre-excecute");
            if(movieToGet == "popular"){
                LIST = POPULAR_MOVIES;
            }else{
                LIST = TOP_RATED;
            }

            pageNumber = page + "";
        }

        @Override
        protected String[] doInBackground(Void... params) {
            //Log.v(LOG_TAG, "Do in background");

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            try {
                Uri buildUri = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendPath(LIST)
                        .appendQueryParameter(KEY, getResources().getString(R.string.api_key))
                        .appendQueryParameter(PAGE, pageNumber)
                .build();

                URL url = new URL(buildUri.toString());
                //Log.v(LOG_TAG, "Build result: " + buildUri.toString());

                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if(inputStream == null){
                    moviesJsonStr = null;
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }

                if(stringBuffer.length() == 0){
                    moviesJsonStr = null;
                    return null;
                }
                moviesJsonStr = stringBuffer.toString();
                //Log.v(LOG_TAG, "Movie string: " + moviesJsonStr);

            }catch (Exception e){
                //Log.e(LOG_TAG, "Error " + e);
                moviesJsonStr = null;
                return null;
            } finally {
                if(urlConnection != null){
                    try {
                        reader.close();
                    }catch (final IOException e){
                        return null;
                    }catch (final Exception e){
                        return null;
                    }
                }
            }

            try {
                 return getMovieInfoFromJson(moviesJsonStr);
            }catch (JSONException e){
                //Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            //Log.v(LOG_TAG, "On post excecute");
            if(strings != null){
                String temp[];
                names = new String[strings.length];
                posters = new String[strings.length];
                synopsis = new String[strings.length];
                rating = new String[strings.length];
                releaseDate = new String[strings.length];

                for(int i = 0; i < strings.length; i++){
                    temp = strings[i].split("--");
                    names[i] = temp[0];
                    posters[i] = temp[1];
                    synopsis[i] = temp[2];
                    rating[i] = temp[3];
                    releaseDate[i] = temp[4];
                    temp[0] = temp[1] = temp[2] = temp[3] = temp[4] = null;
                }
            }

            //Log.v(LOG_TAG, "Fetching posters...");
            fetchPosters();
            //Log.v(LOG_TAG, "Fetching posters done");
        }
    }
}
