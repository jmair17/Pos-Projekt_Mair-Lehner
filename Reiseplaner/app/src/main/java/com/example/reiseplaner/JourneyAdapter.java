package com.example.reiseplaner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JourneyAdapter extends BaseAdapter {
    private List<Journey> journeys = new ArrayList<>();
    private int layoutId ;
    private LayoutInflater inflater ;
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm");
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
        ((TextView) listItem.findViewById(R.id.date)).setText(journey.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        ((TextView) listItem.findViewById(R.id.destination)).setText(journey.getDestination());
        ((TextView) listItem.findViewById(R.id.category)).setText(journey.getCategory());
        ((TextView) listItem.findViewById(R.id.note)).setText(journey.getNotes());
        ((TextView) listItem.findViewById(R.id.importentThings)).setText(journey.getThingsNotToForget());

        return listItem ;
    }
}
