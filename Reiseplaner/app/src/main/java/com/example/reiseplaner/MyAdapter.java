package com.example.reiseplaner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.reiseplaner.Journey;
import com.example.reiseplaner.MainActivity;

import java.util.Calendar;
import java.util.List;

public class MyAdapter<T> extends ArrayAdapter {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences prefs;

    public MyAdapter(@NonNull Context context, int resource, List objects) {
        super(context, resource, objects);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        try {
            Calendar noteCal = Calendar.getInstance();
            //noteCal.setTime(((Journey) getItem(position)).getDate());
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        return view;
    }
}
