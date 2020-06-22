package com.example.reiseplaner.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

//public class NotificationService extends Service {
    /*LocationManager lm;
    LocationListener ll;
    Location currentLocation;

    static List<Note> notes = new ArrayList<>();

    NotificationChannel channel;
    NotificationManager notificationManager;

    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener;
    boolean showNotifications;

    @Override
    public void onCreate() {
        super.onCreate();
        setUpPreferences();

        CharSequence name = "channel name";
        String description = "description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        channel = new NotificationChannel(
                "id", name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other showNotifications behaviors after this
        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                checkNotesTime();

                boolean changed = updateCurrentLocation(location);
                if (changed) {
                    checkNotesLocation(location);
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        try {
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    0,
                    ll);
        } catch (SecurityException e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setUpPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        showNotifications = prefs.getBoolean("showNotifications", false);

        preferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("showNotifications")) {
                    showNotifications = prefs.getBoolean(key, false);
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(preferencesChangeListener);
    }

    public boolean updateCurrentLocation(Location location) {
        int distance = -1;
        if (currentLocation == null) {
            currentLocation = location;
        } else {
            distance = calcDistanceInMeters(currentLocation, location);
        }
        if (distance > 20 || distance == -1) {
            currentLocation = location;
            return true;
        }
        return false;
    }

    public int calcDistanceInMeters(Location currentLocation, Location locationOfNote) {
        return (int) currentLocation.distanceTo(locationOfNote);
    }

    public void checkNotesTime() {
        LocalDateTime currentDate = LocalDateTime.now();
        for (Note note : notes) {
            long difference = ChronoUnit.SECONDS.between(currentDate, note.dueDate);
            Log.d("Difference", difference + "");
            if (difference > 0 && difference <= 10 && !note.state) {
                sendNotification("It's now time to: " + note.title, note.id);
            }
        }
    }

    public void checkNotesLocation(Location location) {
        for (Note note : notes) {
            //get Location of Note
            String[] splittedData = note.additionalData.split(";");
            Location locationOfNote = new Location("gps");
            locationOfNote.setLatitude(Double.parseDouble(splittedData[2]));
            locationOfNote.setLongitude(Double.parseDouble(splittedData[3]));

            int difference = calcDistanceInMeters(currentLocation, locationOfNote);
            Log.d("Difference", difference + "");
            if (difference < 20 && !note.state) {
                sendNotification("Don't forget to:  " + note.title, note.id);
            }
        }
    }

    public void sendNotification(String message, int id) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this, "id")
                .setSmallIcon(android.R.drawable.star_big_on)
                .setColor(Color.YELLOW)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        boolean send = showNotifications;
        StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
        for (StatusBarNotification activeNotification : activeNotifications) {
            if (activeNotification.getId() == id) {
                send = false;
            }
        }

        if (send) {
            notificationManager.notify(id, builder.build());
        }
    }*/
//}