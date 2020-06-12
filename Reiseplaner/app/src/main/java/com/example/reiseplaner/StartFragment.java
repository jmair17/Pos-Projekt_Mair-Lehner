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
public class StartFragment extends Fragment {

    Button b1;
    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        b1 = v.findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondFragment secondFragment = new SecondFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainLayout, secondFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return v;
    }

}
