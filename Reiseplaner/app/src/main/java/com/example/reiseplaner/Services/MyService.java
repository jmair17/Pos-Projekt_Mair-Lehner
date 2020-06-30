package com.example.reiseplaner.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.reiseplaner.AsyncTask.MyAsyncTask;
import com.example.reiseplaner.R;

import java.util.ArrayList;


public class MyService extends Service {

    Thread t;
    ArrayList<String> dest;
    int i = 0;
    NotificationChannel channel;
    NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setNotificationManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.dest = intent.getStringArrayListExtra("Destinations");
        if (dest.size()!=0) {
            t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(5000);
                            checkTemperature();
                            i++;
                            if (i == dest.size()) {
                                i = 0;
                            }
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
            t.start();
        }
        else
        {
            Log.d("Tag", "Es ist noch keine Reise vorhanden.");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void sendNotification(String message, int id) {
        boolean send = true;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this, "id")
                .setSmallIcon(android.R.drawable.star_big_on)
                .setColor(Color.YELLOW)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
        for (StatusBarNotification activeNotification : activeNotifications) {
            if (activeNotification.getId() == id) {
                send = false;
            }
        }

        if (send) {
            notificationManager.notify(id, builder.build());
        }
    }

    public void checkTemperature()
    {
        MyAsyncTask myAsyncTask = new MyAsyncTask(response -> {
            double number = MyAsyncTask.controllTemperature(response);
            if (number < 14)
            {
                String destination = MyAsyncTask.getDestination(response);
                Log.d("Tag","It´s cold in "+destination);
                sendNotification("You may should get a coat! It´s getting cold in "+destination+".", dest.indexOf(destination));
            }
        });
            myAsyncTask.execute("GET", "https://openweathermap.org/data/2.5/weather?q=" + dest.get(i) + "&appid=439d4b804bc8187953eb36d2a8c26a02");

    }

    public void setNotificationManager()
    {
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
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Notifications will not be shown now", Toast.LENGTH_SHORT).show();
        t.interrupt();
        boolean check = t.isAlive();
        super.onDestroy();
    }

    /* //Gehört in onCreate in MainActivitiy

    public void startService(){
        startService(new Intent(getBaseContext(), MyService.class));
       }

       public void stopService(){
        stopService(new Intent(getBaseContext(), MyService.class));
       }
     */
}
