package com.example.reiseplaner;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_start extends Fragment {

    Button b1;


    public fragment_start() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_start, container, false);
        b1 = v.findViewById(R.id.buttonStart);
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fragment_overview fragmentOverview = new fragment_overview();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.mainLayout, fragmentOverview);
                ft.commit();
            }
        });


        return v;
    }

}
