package com.example.reiseplaner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity{


    TextView editTextSearchForDestination;
    Button buttonSearchForDestination;
    TextView showWeather;

    TextView editText;

    ///////////////ADDING NEW JOURNEY////////////////
    private EditText editTextCategory;
    private EditText editTextDestination;
    private EditText editTextJourneyDate;
    private EditText editTextThingsNotToForget;
    private EditText editTextNotes;

    ///////////////OTHERS////////////////////
    private MyAdapter<Journey> adapter;
    private FloatingActionButton fab;
    Button buttonStart;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StartFragment startFragment = new StartFragment();
        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction().add(R.id.mainLayout,startFragment).commit();


    }

    public class Weather extends AsyncTask<String, Void, String> {

        public void searchForDestination(View view){


            editTextSearchForDestination = view.findViewById(R.id.destination);
            showWeather = view.findViewById(R.id.temperature);

            String destinationName = editTextSearchForDestination.getText().toString().toLowerCase();

            String content;
            Weather weather = new Weather();

            try {
                content = weather.execute("https://openweathermap.org/data/2.5/weather?q=" + destinationName+"&appid=439d4b804bc8187953eb36d2a8c26a02").get();

                Log.i("content", content);

                //Json
                JSONObject jsonObject = new JSONObject(content);
                String weatherData = jsonObject.getString("weather");
                String mainTemperatur = jsonObject.getString("main");

                //Log.i("weatherData",weatherData);

                JSONArray array = new JSONArray(weatherData);

                String main ="";
                String description="";
                String temperature="";

                for (int i = 0; i < array.length(); i++){
                    JSONObject weatherPart = array.getJSONObject(i);
                    main = weatherPart.getString("main");
                    description = weatherPart.getString("description");
                }

                JSONObject mainPart = new JSONObject(mainTemperatur);
                temperature = mainPart.getString("temp");

                Log.i("Temperature", temperature);
                /*
                Log.i("main",main);
                Log.i("description",description); */

                ///explicit text zeigt informationen wie zb. wolken
                String explicitText = "Main :"+main+"\nDescription :"+description;

                ///temperatur text zeigt nur die Temperatur
                String tempText = "Temperatur" + temperature + "°";

                showWeather.setText(tempText);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... address) {
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data=isr.read();
                String content = "";
                char ch;
                while (data != 0){
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }
                return content;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void handleWeather(View vDialog){
            searchForDestination(vDialog);
        }
    }

}
