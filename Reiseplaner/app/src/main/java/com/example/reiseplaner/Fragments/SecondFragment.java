package com.example.reiseplaner.Fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.example.reiseplaner.Adapter.JourneyAdapter;
import com.example.reiseplaner.Adapter.LocalDateTimeAdapter;
import com.example.reiseplaner.Adapter.UriAdapter;
import com.example.reiseplaner.Journey;
import com.example.reiseplaner.MyAsyncTask;
import com.example.reiseplaner.R;
import com.example.reiseplaner.Activities.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment{

    private FloatingActionButton fab;
    ///////////////ADDING NEW JOURNEY////////////////
    private EditText editTextCategory;
    private EditText editTextDestination;
    private EditText editTextJourneyDate;
    private EditText editTextThingsNotToForget;
    private EditText editTextNotes;
    private JourneyAdapter journeyAdapter;
    private List<Journey> journeys;
    private ListView listView;
    AlertDialog.Builder alert;
    AlertDialog.Builder alert2;
    DatePickerDialog mDatePicker;
    TimePickerDialog mTimePicker;
    private List<Journey> ranOutItems;
    private String filename;
    private View w;
    private View x;
    private View p;
    TextView category;
    TextView destination;
    TextView importantThings;
    TextView notes;
    TextView time;
    List<Uri> uris;
    public int checkInfo;
    TextView temperature;
    private EditText editTextTemperature;
    String finalTemperature;

    private AlertDialog dialogReference;
    String ziel;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    boolean showNotifications;

    public SecondFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        View y = inflater.inflate(R.layout.listview_item, container, false);
        w = getLayoutInflater().inflate(R.layout.layout_newjourney, null);
        x = getLayoutInflater().inflate(R.layout.listview_item2, null);
        temperature = y.findViewById(R.id.temperature);
        filename = "journeys.txt";
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        listView = v.findViewById(R.id.listView_trips);
        listView.setLongClickable(true);
        registerForContextMenu(listView);
        journeys = new ArrayList<>();
        load();
        journeyAdapter = new JourneyAdapter(getActivity(), R.layout.listview_item, journeys);
        listView.setAdapter(journeyAdapter);
        journeyAdapter.notifyDataSetChanged();

        this.uris = new ArrayList<>();
        fab = v.findViewById(R.id.floatingactionbutton);
        ranOutItems = new ArrayList<>();
        alert = new AlertDialog.Builder(getActivity());
        alert2 = new AlertDialog.Builder(getActivity());


        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d(null, "entered: preferencesChanged");
                loadPreferences();
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        loadPreferences();


        setHasOptionsMenu(true);

        fab.setOnClickListener(k -> {

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
                        getDestination(w);
                        MyAsyncTask myAsyncTask = new MyAsyncTask(response -> {
                            String temp = MyAsyncTask.searchTemperature(response);
                            finalTemperature = temp+"°C";
                            temperature.setText(temp);
                            handleDialog(w);
                            journeyAdapter.notifyDataSetChanged();
                            save();
                        });
                        myAsyncTask.execute("GET", "https://openweathermap.org/data/2.5/weather?q=" + ziel + "&appid=439d4b804bc8187953eb36d2a8c26a02");

                    } ).setNegativeButton("Cancel", null)
            .show();
        });
        return v;
    }

    public void loadPreferences()
    {
        this.showNotifications = prefs.getBoolean("showNotifications", true);
        if (showNotifications)
        {
            //startService();
            Log.d(null, "startService");
        }
        else
        {
            //stopService();
            Log.d(null, "stopService");
        }
    }

    /*public void startService() {
        startService(new Intent(getActivity().getBaseContext(), MyService.class));
    }

    public void stopService() {
        stopService(new Intent(getActivity(), MyService.class));
    }*/

    public void getDestination(View vDialog){
        TextView temp = vDialog.findViewById(R.id.editTextDestination);
        ziel = temp.getText().toString().toLowerCase();
    }


    public void handleDialog(final View vDialog)
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

        journeys.add(new Journey(cat, dest,things, n,date, new ArrayList<Uri>(),finalTemperature));
        journeyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.preferences_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("Ok", "onOptionsItemSelected:" + id);
        switch (id) {
            case R.id.menu_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    public void save()
    {
        try{
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
            builder.registerTypeHierarchyAdapter(Uri.class, new UriAdapter());
            Gson gson = builder.create();

            FileOutputStream fileOutputStream = getActivity().openFileOutput(filename, getActivity().MODE_PRIVATE);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(fileOutputStream));
                String sJson = gson.toJson(journeys);
                out.println(sJson);
            out.flush();
            out.close();
        } catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }

    public void load()
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        builder.registerTypeHierarchyAdapter(Uri.class, new UriAdapter());
        Gson gson = builder.create();

        try{
            FileInputStream fis = getActivity().openFileInput(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = in.readLine())!= null)
            {
                json.append(line);
            }
            TypeToken<List<Journey>> token = new TypeToken<List<Journey>>() {
            };
            journeys = gson.fromJson(json.toString(), token.getType());

            //proveIfRunOut();
        }catch (IOException io)
        {
            io.getStackTrace();
        }
    }

    public void proveIfRunOut()
    {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < journeys.size(); i++) {
            if (journeys.get(i).getDate().isBefore(now))
            {
                ranOutItems.add(journeys.get(i));
                journeys.remove(journeys.get(i));
                i--;
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        w = getLayoutInflater().inflate(R.layout.layout_newjourney,null);
        if (item.getItemId() == R.id.delete) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            journeys.remove(info.position);
            journeyAdapter.notifyDataSetChanged();
            save();
            return true;
        }
        if (item.getItemId() == R.id.change ) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

            getInfosAndWrite(info,w);

            alert.setView(w);
            alert.setPositiveButton("OK", (dialog, which)-> sethandleDialog(w, info));
            alert.setNegativeButton("CANCEL", null);
            alert.show();
        }
        if (item.getItemId () == R.id.showJourney ) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            TextView category = x.findViewById(R.id.categoryShow);
            category.setText(journeys.get(info.position).getCategory());
            TextView destination = x.findViewById(R.id.destinationShow);
            destination.setText(journeys.get(info.position).getDestination());
            TextView journeyDate = x.findViewById(R.id.dateShow);
            journeyDate.setText(journeys.get(info.position).getDateAsString());
            TextView thingsNot = x.findViewById(R.id.importentThingsShow);
            thingsNot.setText(journeys.get(info.position).getThingsNotToForget());
            TextView notes = x.findViewById(R.id.noteShow);
            notes.setText(journeys.get(info.position).getNotes());
            alert2.setView(x);
            dialogReference = alert2.create();
            dialogReference.show();
        }
        if (item.getItemId() == R.id.showPicture)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            //journeys.get(checkInfo).setUris(this.uris);
            uris = journeys.get(info.position).getUris();
            ShowPictureFragment showPictureFragment = new ShowPictureFragment(uris);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.mainLayout , showPictureFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        if (item.getItemId() == R.id.showMap)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            ziel = journeys.get(info.position).getDestination();
            MyAsyncTask task = new MyAsyncTask(new MyAsyncTask.OnTaskFinishedListener() {
                @Override
                public void onTaskFinished(String response) {
                    String coords = MyAsyncTask.searchLatLon(response);
                    openMap(coords);
                }
            });
            task.execute("GET","https://openweathermap.org/data/2.5/weather?q=" + ziel + "&appid=439d4b804bc8187953eb36d2a8c26a02");

        }
        if (item.getItemId() == R.id.addPicture)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            PictureFragment pictureFragment = new PictureFragment(info.position, this);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.mainLayout , pictureFragment);
            ft.addToBackStack(null);
            ft.commit();
            this.checkInfo = info.position;
            save();
        }
        return super.onContextItemSelected(item);
    }

    public void addUri(Uri u, int pos)
    {
        journeys.get(pos).addUri(u);
        save();
    }

    private void openMap(String coords) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + coords);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private final static int RQ_PREFERENCES = 1; //look in
    the doc
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_preferences) {
            Intent intent = new Intent(this,
                    MySettingsActivity.class);
            startActivityForResult(intent, RQ_PREFERENCES);
        }
        return super.onOptionsItemSelected(item);
    }*/



    private void getInfosAndWrite(AdapterView.AdapterContextMenuInfo info, View dialogView)
    {
        category = dialogView.findViewById(R.id.editTextCategory);
        destination = dialogView.findViewById(R.id.editTextDestination);
        importantThings = dialogView.findViewById(R.id.editTextThingsNotToForget);
        notes = dialogView.findViewById(R.id.editTextNotes);
        time = dialogView.findViewById(R.id.editTextJourneyDate);


        Journey n = journeys.get(info.position);

        category.setText(n.getCategory());
        destination.setText(n.getDestination());
        importantThings.setText(n.getThingsNotToForget());
        notes.setText(n.getNotes());
        time.setText(n.getDateAsString());
    }

    private void sethandleDialog(final View vDialog, AdapterView.AdapterContextMenuInfo info) {


        TextView category = vDialog.findViewById(R.id.editTextCategory);
        String cat = category.getText().toString();
        TextView destination = vDialog.findViewById(R.id.editTextDestination);
        String dest = destination.getText().toString();
        TextView journeyDate = vDialog.findViewById(R.id.editTextJourneyDate);
        String date1 = journeyDate.getText().toString();
        TextView thingsNot = vDialog.findViewById(R.id.editTextThingsNotToForget);
        String things = thingsNot.getText().toString();
        TextView notes = vDialog.findViewById(R.id.editTextNotes);
        String n = notes.getText().toString();


        journeys.get(info.position).setCategory(cat);
        journeys.get(info.position).setDestination(dest);
        journeys.get(info.position).setThingsNotToForget(things);
        journeys.get(info.position).setNotes(n);
        journeys.get(info.position).setDate(date1);
        journeyAdapter.notifyDataSetChanged();

    }
}
