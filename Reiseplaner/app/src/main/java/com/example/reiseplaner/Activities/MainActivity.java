package com.example.reiseplaner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.reiseplaner.Fragments.StartFragment;
import com.example.reiseplaner.R;


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
