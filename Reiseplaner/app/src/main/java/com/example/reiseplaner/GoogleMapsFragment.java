package com.example.reiseplaner;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.reiseplaner.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleMapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static EditText destination;
    public static TextView temperature;
    String CITY;
    public String urlSource;
    String coordsLon;
    String coordsLat;
    double latitude;
    double longitude;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng latLngCoords = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLngCoords).title("Marker in: " + CITY));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngCoords));
    }

    /*public GoogleMapsFragment() {
        // Required empty public constructor
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Location currentLocation = .FusedLocationApi.getLastLocation(googleApiClient);
        destination = (EditText) container.findViewById(R.id.editTextDestination);
        temperature = (TextView) container.findViewById(R.id.temperature);

        CITY = destination.getText().toString();
        urlSource = "https://openweathermap.org/data/2.5/weather?q=" +
                CITY +
                "&appid=439d4b804bc8187953eb36d2a8c26a02";

        getLongLat();
        new GoogleMapsAsyncTask().execute();

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_google_maps, container, false);

        return null;
    }

    class GoogleMapsAsyncTask extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            String responseJson = "";
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(urlSource).openConnection();
                connection.setRequestMethod(strings[0]);

                if (strings[0].equals("PUT")) {
                    connection = (HttpURLConnection) new URL(strings[1]).openConnection();
                    connection.setDoOutput(true);
                    byte[] data = strings[2].getBytes();
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setFixedLengthStreamingMode(data.length);
                    connection.getOutputStream().write(data);
                    connection.getOutputStream().flush();
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    responseJson = readResponseStream(reader);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseJson;
        }

        private String readResponseStream(BufferedReader reader) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }

    public void getLongLat(){
        String content;
        GoogleMapsAsyncTask gmasynctask = new GoogleMapsAsyncTask();
        try {
            content = gmasynctask.execute(urlSource).get();
            //First we will check data is retrieve successfully or not
            Log.i("contentData", content);

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String coordData = jsonObject.getString("coord");

            JSONObject mainPart = new JSONObject(coordData);
            coordsLon = mainPart.getString("lon");
            coordsLat = mainPart.getString("lat");

            latitude = Double.valueOf(coordsLat);
            longitude = Double.valueOf(coordsLon);
        }catch (JSONException e){
            e.printStackTrace();
        }catch (InterruptedException ie){
            ie.printStackTrace();
        }catch (ExecutionException ee){
            ee.printStackTrace();
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /*
    //Getting current location
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemServics(Context.LOCATION_SERVICE);
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(null, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(null, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //moving the map to location
           // moveMap();
        }
    }

    */
}
