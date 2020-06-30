package com.example.reiseplaner.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.reiseplaner.Adapter.PictureAdapter;
import com.example.reiseplaner.R;

import java.util.List;

public class ShowPictureFragment extends Fragment {

    PictureAdapter pictureAdapter;
    List<Uri> uris;
    ListView listView;

    public ShowPictureFragment(List<Uri> uris) {
        this.uris = uris;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_show_picture, container, false);
        pictureAdapter = new PictureAdapter(getActivity(), R.layout.imagelayoutinlist, uris);
        listView = v.findViewById(R.id.imagelistview);
        listView.setAdapter(pictureAdapter);
        pictureAdapter.notifyDataSetChanged();
        return v;
    }

}
