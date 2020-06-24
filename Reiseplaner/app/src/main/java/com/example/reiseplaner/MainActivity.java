package com.example.reiseplaner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartFragment startFragment = new StartFragment();
        FragmentManager manager = getSupportFragmentManager();



        manager.beginTransaction().add(R.id.mainLayout,startFragment).commit();
    }
}
