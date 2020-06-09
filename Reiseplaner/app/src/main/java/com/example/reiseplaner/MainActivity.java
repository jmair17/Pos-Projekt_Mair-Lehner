package com.example.reiseplaner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StartFragment startFragment = new StartFragment();
        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction().add(R.id.mainLayout,startFragment).commit();

    }
    public View addLayout(){
        View vDialog = getLayoutInflater().inflate(R.layout.layout_newjourney, null);

        editTextCategory = findViewById(R.id.editTextCategory);
        editTextDestination = findViewById(R.id.editTextDestination);
        editTextJourneyDate = findViewById(R.id.editTextJourneyDate);
        editTextThingsNotToForget = findViewById(R.id.editTextThingsNotToForget);
        editTextNotes = findViewById(R.id.editTextNotes);

        return vDialog;
    }

    ///////ADDING A NEW JOURNEY///////////
    public void addNewJourney(){
        setContentView(R.layout.fragment_fragment_overview);
        fab = findViewById(R.id.floatingactionbutton);

        fab.setOnClickListener(v -> {

            View vDialog = addLayout();
            vDialog.findViewById(R.id.editTextCategory);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("New Journey")
                    .setView(vDialog)
                    .setPositiveButton("Add", (dialog, which) ->{
                        adapter.add(new Journey(editTextCategory.getText().toString(), editTextDestination.getText().toString(), editTextJourneyDate.getText().toString(), editTextThingsNotToForget.getText().toString(), editTextNotes.getText().toString()));
                        adapter.notifyDataSetChanged();
                    } ).setNegativeButton("Cancel", null)
                    .show();
        });
    }
}
