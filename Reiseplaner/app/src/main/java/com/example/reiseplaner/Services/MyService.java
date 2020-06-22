package com.example.reiseplaner.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Notifications will be shown now", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Notifications will be shown now", Toast.LENGTH_SHORT).show();
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
