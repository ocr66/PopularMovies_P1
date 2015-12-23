package com.example.ocr66.popularmovies;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by o.lopez.cienfuegos on 09/12/2015.
 */
public class CustomAdapter extends BaseAdapter {

    private static final String LOG_TAG = CustomAdapter.class.getSimpleName();
    private Context context;
    //private List<ImageView> posterList;
    private String[] posterList;
    private LayoutInflater inflater = null;

    public CustomAdapter(Context context, String[] list){
        this.context = context;
        posterList = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return posterList.length;
    }

    @Override
    public Object getItem(int position) {
        return posterList[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500";
        View v = convertView;
        CustomAdapterViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.movie_info, null);
            viewHolder = new CustomAdapterViewHolder(v);
            v.setTag(viewHolder);
        }else{
            viewHolder = (CustomAdapterViewHolder)v.getTag();
        }

        Picasso.with(context).load(BASE_IMAGE_URL + getItem(position)).into(viewHolder.imageView);
        //Log.v(LOG_TAG, "Build result: " + BASE_IMAGE_URL + getItem(position));
        return v;
    }

    class CustomAdapterViewHolder{
        public ImageView imageView;
        public CustomAdapterViewHolder(View view){
            imageView = (ImageView)view.findViewById(R.id.poster);
        }
    }
}
