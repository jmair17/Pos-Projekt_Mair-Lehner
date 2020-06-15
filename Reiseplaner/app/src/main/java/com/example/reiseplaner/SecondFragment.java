package com.example.reiseplaner;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private FloatingActionButton fab;
    ///////////////ADDING NEW JOURNEY////////////////
    private EditText editTextCategory;
    private EditText editTextDestination;
    private EditText editTextJourneyDate;
    private EditText editTextThingsNotToForget;
    private EditText editTextNotes;
    private MyAdapter adapter;
    private JourneyAdapter journeyAdapter;
    private List<Journey> toDo = new ArrayList<>();
    private ListView listView;
    DatePickerDialog mDatePicker;
    TimePickerDialog mTimePicker;
    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        fab = v.findViewById(R.id.floatingactionbutton);
        listView = v.findViewById(R.id.listView_trips);
        //adapter = new MyAdapter(getActivity(), R.layout.listview_item, toDo);
        //listView.setAdapter(adapter);
        journeyAdapter = new JourneyAdapter(getActivity(), R.layout.listview_item2, toDo);
        listView.setAdapter(journeyAdapter);
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");



        fab.setOnClickListener(k -> {
            View w = getLayoutInflater().inflate(R.layout.layout_newjourney,null);
            editTextCategory = w.findViewById(R.id.editTextCategory);
            editTextDestination = w.findViewById(R.id.editTextDestination);
            editTextJourneyDate = w.findViewById(R.id.editTextJourneyDate);
            editTextThingsNotToForget = w.findViewById(R.id.editTextThingsNotToForget);
            editTextNotes = w.findViewById(R.id.editTextNotes);

            editTextJourneyDate.setOnClickListener(on -> {
                Calendar currentDate = Calendar.getInstance();
                if (!editTextJourneyDate.getText().toString().isEmpty()) {
                    try {
                        currentDate.setTime(DATE_FORMAT.parse(editTextJourneyDate.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else
                    mDatePicker = new DatePickerDialog(getActivity(), (datePicker, year, month, dayOfMonth) -> {
                        Calendar calInput = Calendar.getInstance();
                        calInput.set(year, month, dayOfMonth);
                        mTimePicker = new TimePickerDialog(getActivity(), (timePicker, hourOfDay, minute) -> {
                            calInput.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calInput.set(Calendar.MINUTE, minute);
                            editTextJourneyDate.setText(DATE_FORMAT.format(calInput.getTime()));
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true);
                        mTimePicker.setTitle("Select time");
                        mTimePicker.show();
                    }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            });


            new AlertDialog.Builder(getActivity())
                    .setTitle("New Journey")
                    .setView(w)
                    .setPositiveButton("Add", (dialog, which) ->{
                        handleDialog(w);
                    } ).setNegativeButton("Cancel", null)
            .show();
        });
        return v;

    }

    public void handleDialog( final View vDialog)
    {
        TextView category = vDialog.findViewById(R.id.editTextCategory);
        String cat = category.getText().toString();
        TextView destination = vDialog.findViewById(R.id.editTextDestination);
        String dest = destination.getText().toString();
        TextView journeyDate = vDialog.findViewById(R.id.editTextJourneyDate);
        String date = journeyDate.getText().toString();
        TextView thingsNot = vDialog.findViewById(R.id.editTextThingsNotToForget);
        String things = thingsNot.getText().toString();
        TextView notes = vDialog.findViewById(R.id.editTextNotes);
        String n = notes.getText().toString();

        toDo.add(new Journey(cat, dest,things, n,date));
        journeyAdapter.notifyDataSetChanged();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("Ok", "onOptionsItemSelected:" + id);
        dialogView = getLayoutInflater().inflate(R.layout.dialog_layout,null);
        switch (id) {
            case R.id.newItem:
                alert.setView(dialogView);
                alert.setPositiveButton("OK", (dialog, which)-> handleDialog(dialogView));
                alert.setNegativeButton("CANCEL", null);
                alert.show();
                break;
            case R.id.save:
                save();
                break;
            case R.id.doneItems:
                doneItems();
                break;
            case R.id.openItems:
                openItems();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    /*public View addLayout() {
        View z = getLayoutInflater().inflate(R.layout.layout_newjourney,null);
        editTextCategory = z.findViewById(R.id.editTextCategory);
        editTextDestination = z.findViewById(R.id.editTextDestination);
        editTextJourneyDate = z.findViewById(R.id.editTextJourneyDate);
        editTextThingsNotToForget = z.findViewById(R.id.editTextThingsNotToForget);
        editTextNotes = z.findViewById(R.id.editTextNotes);


        return z;
    }*/


}
