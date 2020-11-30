package com.example.tankverhalten.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tankverhalten.R;

public class tankvorgang_fragment extends Fragment {

    View view;

    public tankvorgang_fragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.tankvorgang_layout, container, false);
    return view;
    }
        /*
        Hier kommt die Tankvorgang Activity hin.
     */
}
