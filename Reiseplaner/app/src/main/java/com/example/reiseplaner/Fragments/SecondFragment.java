package com.example.reiseplaner.Fragments;


import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.reiseplaner.Journey.Journey;
import com.example.reiseplaner.AsyncTask.MyAsyncTask;
import com.example.reiseplaner.Services.MyService;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class SecondFragment extends Fragment{

    ///////////////BUTTON////////////////
    private FloatingActionButton fab;
    ///////////////VIEW////////////////
    private View w;
    private View x;
    ///////////////TEXTVIEW////////////////
    private TextView category;
    private TextView destination;
    private TextView importantThings;
    private TextView notes;
    private TextView time;
    private TextView temperature;
    ///////////////LIST////////////////
    private List<Journey> ranOutItems;
    private List<Journey> journeys;
    private List<Uri> uris;
    ///////////////STRING////////////////
    private String filename;
    private String ziel;
    private String finalTemperature;
    ///////////////FORMAT////////////////
    DateTimeFormatter DATE_FORMAT2;
    SimpleDateFormat DATE_FORMAT;
    ///////////////ALERT////////////////
    private AlertDialog dialogReference;
    private AlertDialog.Builder alert;
    private AlertDialog.Builder alert2;
    ///////////////SHARED PREFERENCES////////////////
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    ///////////////PICKER DIALOG////////////////
    private DatePickerDialog mDatePicker;
    private TimePickerDialog mTimePicker;
    ///////////////OTHERS////////////////
    private EditText editTextJourneyDate;
    private JourneyAdapter journeyAdapter;
    private ListView listView;
    private boolean showNotifications;
    MyService m;


    public SecondFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        m = new MyService();
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        View y = inflater.inflate(R.layout.listview_item, container, false);
        w = getLayoutInflater().inflate(R.layout.layout_newjourney, null);
        x = getLayoutInflater().inflate(R.layout.listview_item2, null);
        temperature = y.findViewById(R.id.temperature);
        filename = "journeys.txt";
        DATE_FORMAT2 = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
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


            editTextJourneyDate = w.findViewById(R.id.editTextJourneyDate);

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

            AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());

           dialogbuilder
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
                        ((ViewGroup)w.getParent()).removeAllViews();
                    } ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   ((ViewGroup)w.getParent()).removeView(w);
               }
           })
            .show();
        });
        w = getLayoutInflater().inflate(R.layout.layout_newjourney, null);

        return v;
    }

    public void loadPreferences()
    {
        this.showNotifications = prefs.getBoolean("showNotifications", true);
        if (showNotifications== true)
        {
            startService();
            Log.d(null, "startService");
        }
        else
        {
            stopService();
            Log.d(null, "stopService");
        }
    }

    public void startService() {
        Intent i = new Intent(getActivity(), MyService.class);
        ArrayList<String> d = new ArrayList<>();
        for (int j = 0; j < journeys.size(); j++) {
            d.add(journeys.get(j).getDestination());
        }
        i.putStringArrayListExtra("Destinations",d);
        getActivity().startService(i);
    }

    public void stopService() {
        if (isMyServiceRunning(MyService.class)) {
            getActivity().stopService(new Intent(getActivity().getBaseContext(), MyService.class));
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

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
        }catch (IOException io)
        {
            io.getStackTrace();
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
            //w = getLayoutInflater().inflate(R.layout.layout_newjourney, null);
            getInfosAndWrite(info,w);


            alert.setView(w);
            alert.setPositiveButton("OK", (dialog, which)-> sethandleDialog(w, info));
            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((ViewGroup)w.getParent()).removeView(w);
                    w = getLayoutInflater().inflate(R.layout.layout_newjourney, null);
                }
            });
            alert.show();
            //w = getLayoutInflater().inflate(R.layout.layout_newjourney, null);

        }
        if (item.getItemId () == R.id.showJourney ) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            TextView category = x.findViewById(R.id.categoryShow);
            category.setText(journeys.get(info.position).getCategory());
            TextView destination = x.findViewById(R.id.destinationShow);
            destination.setText(journeys.get(info.position).getDestination());
            TextView thingsNot = x.findViewById(R.id.importentThingsShow);
            thingsNot.setText(journeys.get(info.position).getThingsNotToForget());
            TextView notes = x.findViewById(R.id.noteShow);
            notes.setText(journeys.get(info.position).getNotes());
            TextView date = x.findViewById(R.id.dateShow);
            date.setText(journeys.get(info.position).getDate().format(DATE_FORMAT2));
            alert2.setView(x);
            dialogReference = alert2.create();
            dialogReference.show();
            x = getLayoutInflater().inflate(R.layout.listview_item2, null);
        }
        if (item.getItemId() == R.id.showPicture)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
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




    private void getInfosAndWrite(AdapterView.AdapterContextMenuInfo info, View dialogView)
    {
        category = dialogView.findViewById(R.id.editTextCategory);
        destination = dialogView.findViewById(R.id.editTextDestination);
        importantThings = dialogView.findViewById(R.id.editTextThingsNotToForget);
        notes = dialogView.findViewById(R.id.editTextNotes);
        time = dialogView.findViewById(R.id.editTextJourneyDate);


        Journey n = journeys.get(info.position);

        LocalDateTime t = n.getDate();

        category.setText(n.getCategory());
        destination.setText(n.getDestination());
        importantThings.setText(n.getThingsNotToForget());
        notes.setText(n.getNotes());
        time.setText(t.format(DATE_FORMAT2));
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

        ((ViewGroup)w.getParent()).removeView(w);
        w = getLayoutInflater().inflate(R.layout.layout_newjourney, null);

    }
}
