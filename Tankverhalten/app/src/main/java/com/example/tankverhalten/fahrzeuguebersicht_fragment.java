package com.example.tankverhalten;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tankverhalten.R;

public class fahrzeuguebersicht_fragment extends Fragment {

    View view;
    public fahrzeuguebersicht_fragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fahrzeuguebersicht_layout, container, false);
        return view;
    }
    /*
        Hier kommt die Fahrzeugübersicht Activity hin.
    */
}
