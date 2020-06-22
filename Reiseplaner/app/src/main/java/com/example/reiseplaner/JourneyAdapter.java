package com.example.reiseplaner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JourneyAdapter extends BaseAdapter {
    private List<Journey> journeys = new ArrayList<>();
    private int layoutId ;
    private LayoutInflater inflater ;
    public static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public JourneyAdapter(Context ctx, int layoutId, List<Journey> journeys) {
        this.journeys = journeys;
        this.layoutId = layoutId;
        this.inflater = ( LayoutInflater ) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() { return journeys.size();}
    @Override
    public Object getItem( int i ) { return journeys.get(i);}
    @Override
    public long getItemId( int i ) { return 0; }


    @Override
    public View getView(int i , View view, ViewGroup viewGroup) {
        Journey journey = journeys.get(i);
        //String datum = DATE_FORMAT.format(journey.getDate());
        View listItem = (view == null) ? inflater.inflate(this.layoutId,null ) : view;
        ((TextView) listItem.findViewById(R.id.date)).setText(journey.getDate().format(DATE_FORMAT));
        ((TextView) listItem.findViewById(R.id.destination)).setText(journey.getDestination());
        ((TextView) listItem.findViewById(R.id.category)).setText(journey.getCategory());
        MyAsyncTask myAsyncTask = new MyAsyncTask(response -> {
            String temp = MyAsyncTask.search(response);
            ((TextView) listItem.findViewById(R.id.temperature)).setText(temp+"°C");
            notifyDataSetChanged();
        });
        myAsyncTask.execute("GET", "https://openweathermap.org/data/2.5/weather?q=" + journey.getDestination() + "&appid=439d4b804bc8187953eb36d2a8c26a02");

        return listItem ;
    }
}
