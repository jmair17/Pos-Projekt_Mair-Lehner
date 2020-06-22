package com.example.reiseplaner;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.reiseplaner.R;

public class MySettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
