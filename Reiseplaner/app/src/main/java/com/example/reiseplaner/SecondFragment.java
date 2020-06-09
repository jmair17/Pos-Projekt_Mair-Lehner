package com.example.reiseplaner;


import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


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
    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        fab = v.findViewById(R.id.floatingactionbutton);
        editTextCategory = v.findViewById(R.id.editTextCategory);
        editTextDestination = v.findViewById(R.id.editTextDestination);
        editTextJourneyDate = v.findViewById(R.id.editTextJourneyDate);
        editTextThingsNotToForget = v.findViewById(R.id.editTextThingsNotToForget);
        editTextNotes = v.findViewById(R.id.editTextNotes);

        fab.setOnClickListener(k -> {

            View vDialog = v;
            vDialog.findViewById(R.id.editTextCategory);
            new AlertDialog.Builder(null)
                    .setTitle("New Journey")
                    .setView(vDialog)
                    .setPositiveButton("Add", (dialog, which) ->{
                        adapter.add(new Journey(editTextCategory.getText().toString(), editTextDestination.getText().toString(), editTextJourneyDate.getText().toString(), editTextThingsNotToForget.getText().toString(), editTextNotes.getText().toString()));
                        adapter.notifyDataSetChanged();
                    } ).setNegativeButton("Cancel", null)
                    .show();
        });
        return v;
    }

    ///////ADDING A NEW JOURNEY///////////
    public void addNewJourney(){

    }


}
