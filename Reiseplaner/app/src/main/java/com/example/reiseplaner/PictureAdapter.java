package com.example.reiseplaner;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.reiseplaner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PictureAdapter extends BaseAdapter {
    private Context context;
    private List<Uri> journeys;
    private int layoutId;
    private LayoutInflater inflater;

    public PictureAdapter(Context context, int layoutId, List<Uri> journeys) {
        this.context = context;
        this.layoutId = layoutId;
        this.journeys = journeys;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return journeys.size();
    }

    @Override
    public Object getItem(int position) {
        return journeys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = (convertView == null) ? inflater.inflate(this.layoutId , null) : convertView;
        for (int i = 0; i < journeys.size(); i++) {
            Picasso.get().load(journeys.get(i)).into((ImageView)listItem.findViewById(R.id.imageinlist));
        }

        return listItem;
    }


}
