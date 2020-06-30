package com.example.reiseplaner.AsyncTask;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MyAsyncTask extends AsyncTask<String, Integer, String> {

    OnTaskFinishedListener listener;

    public MyAsyncTask(OnTaskFinishedListener listener){
        this.listener = listener;
    }

    public static String searchTemperature(String content){
        try {
            //First we will check data is retrieve successfully or not

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String mainTemperature = jsonObject.getString("main");

            String temperatureJson;

            JSONObject mainPart = new JSONObject(mainTemperature);
            temperatureJson = mainPart.getString("temp");

            return temperatureJson;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double controllTemperature(String degreece)
    {
        try {
        JSONObject jsonObject = new JSONObject(degreece);
        String mainTemperature = jsonObject.getString("main");

        String temperatureJson;

        JSONObject mainPart = new JSONObject(mainTemperature);
        temperatureJson = mainPart.getString("temp");
        double number = Double.parseDouble(temperatureJson);
        return number;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getDestination(String dest)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(dest);
            return jsonObject.getString("name");
        } catch (Exception e) {
        e.printStackTrace();
        }
        return null;
    }

    public static String searchLatLon(String content){
        String coords;
        try {
            //First we will check data is retrieve successfully or not
            Log.i("contentData", content);

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String coordData = jsonObject.getString("coord");

            JSONObject mainPart = new JSONObject(coordData);
            coords = mainPart.getString("lat");
            coords +="," + mainPart.getString("lon");

            return coords;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String doInBackground(String... strings) {
        String responseJson = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(strings[1]).openConnection();
            connection.setRequestMethod(strings[0]);

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

    @Override
    protected void onPostExecute(String response) {
        // called after doInBackground finishes
        listener.onTaskFinished(response);
    }

    public interface OnTaskFinishedListener{
        public void onTaskFinished(String response);

    }

}
