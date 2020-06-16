package com.example.reiseplaner;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

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
import java.util.Arrays;
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
    DatePickerDialog mDatePicker;
    TimePickerDialog mTimePicker;
    private List<Journey> ranOutItems;
    private String filename;
    private View w;
    TextView category;
    TextView destination;
    TextView importantThings;
    TextView notes;
    TextView time;
    MainActivity.Weather handleWeather;


    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        View y = inflater.inflate(R.layout.listview_item,container,false);
        fab = v.findViewById(R.id.floatingactionbutton);
        journeys = new ArrayList<>();
        ranOutItems = new ArrayList<>();
        alert = new AlertDialog.Builder(getActivity());
        journeyAdapter = new JourneyAdapter(getActivity(), R.layout.listview_item, journeys);
        listView = v.findViewById(R.id.listView_trips);
        listView.setAdapter(journeyAdapter);
        listView.setLongClickable(true);
        registerForContextMenu(listView);
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        setHasOptionsMenu(true);
        handleWeather = new MainActivity().new Weather();
        filename = "journeys.txt";
        w = getLayoutInflater().inflate(R.layout.layout_newjourney,null);





        load();



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
                        handleDialog(w);
                        handleWeather.handleWeather(y);
                        journeyAdapter.notifyDataSetChanged();
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

        journeys.add(new Journey(cat, dest,things, n,date));
        journeyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_layout, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("Ok", "onOptionsItemSelected:" + id);
        switch (id) {
            case R.id.save:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save()
    {
        try{
            Gson gson = new Gson();

            FileOutputStream fileOutputStream = getActivity().openFileOutput(filename, getActivity().MODE_PRIVATE);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(fileOutputStream));
            for (int i = 0; i < journeys.size(); i++) {
                String sJson = gson.toJson(journeys.get(i));
                out.println(sJson);
            }
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
        Gson gson = builder.create();
        String sJson = gson.toJson(journeys);

        try{
            FileInputStream fis = getActivity().openFileInput(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            String line;
            String category;
            String destiny;
            String thingsNotToForget;
            String notes;
            String time;
            while ((line = in.readLine())!= null)
            {
                String sJsonLoad = line;
                Journey j = gson.fromJson(sJsonLoad, Journey.class);
                category = j.getCategory();
                destiny = j.getDestination();
                thingsNotToForget = j.getThingsNotToForget();
                notes = j.getNotes();
                time = j.getDateAsString();
                journeys.add(new Journey(category,destiny,thingsNotToForget,notes,time));
            }
            journeyAdapter.notifyDataSetChanged();
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
/*        if (item.getItemId () == R.id.showJourney ) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            dialogView = getLayoutInflater().inflate(R.layout.details_layout,null);

            Button button = dialogView.findViewById(R.id.back);


            TextView date = dialogView.findViewById(R.id.date);
            TextView text = dialogView.findViewById(R.id.textView);
            TextView me = dialogView.findViewById(R.id.mess);
            String datestring = notizen.get(info.position).getDate().toString();
            date.setText(datestring);
            String textstring = notizen.get(info.position).getText();
            text.setText(textstring);
            String messtring = notizen.get(info.position).getMessage();
            me.setText(messtring);

            alert2.setView(dialogView);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogReference.dismiss();
                }
            });

            dialogReference = alert2.create();
            dialogReference.show();



            //alert.setNegativeButton("CANCEL", null);
            //AlertDialog dialg = alert.create();
            //dialg.show();
            //Button negativeButton = dialg.getButton(AlertDialog.BUTTON_NEGATIVE);
            //negativeButton.setTextColor(Color.parseColor("#000000"));
            //negativeButton.setBackgroundColor(Color.parseColor("#F7CD57"));
        }*/
        return super.onContextItemSelected(item);
    }

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
