package com.example.reiseplaner;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.Calendar;


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
    private MyAdapter<Journey> adapter;
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



        fab.setOnClickListener(k -> {
            View w = addLayout();
            editTextCategory = w.findViewById(R.id.editTextCategory);
            editTextDestination = w.findViewById(R.id.editTextDestination);
            editTextJourneyDate = w.findViewById(R.id.editTextJourneyDate);
            editTextThingsNotToForget = w.findViewById(R.id.editTextThingsNotToForget);
            editTextNotes = w.findViewById(R.id.editTextNotes);



            editTextJourneyDate.setOnClickListener(on -> {
                Calendar currentDate = Calendar.getInstance();
                if (!editTextJourneyDate.getText().toString().isEmpty()) {
                    try {
                        currentDate.setTime(Journey.DATE_FORMAT.parse(editTextJourneyDate.getText().toString()));
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
                            editTextJourneyDate.setText(Journey.DATE_FORMAT.format(calInput.getTime()));
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true);
                        mTimePicker.setTitle("Select time");
                        mTimePicker.show();
                    }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            });


            new AlertDialog.Builder(getActivity())
                    .setTitle("New Journey")
                    .setView(inflater.inflate(R.layout.layout_newjourney,null))
                    .setPositiveButton("Add", (dialog, which) ->{
                        adapter.add(new Journey(editTextCategory.getText().toString(), editTextDestination.getText().toString(), editTextJourneyDate.getText().toString(), editTextThingsNotToForget.getText().toString(), editTextNotes.getText().toString()));
                        adapter.notifyDataSetChanged();
                    } ).setNegativeButton("Cancel", null)
            .show();
        });
        return v;
    }


    public View addLayout() {
        View z = getLayoutInflater().inflate(R.layout.layout_newjourney,null);

        editTextCategory = z.findViewById(R.id.editTextCategory);
        editTextDestination = z.findViewById(R.id.editTextDestination);
        editTextJourneyDate = z.findViewById(R.id.editTextJourneyDate);
        editTextThingsNotToForget = z.findViewById(R.id.editTextThingsNotToForget);
        editTextNotes = z.findViewById(R.id.editTextNotes);

        //adapter.notifyDataSetChanged();

        return z;
    }


}
