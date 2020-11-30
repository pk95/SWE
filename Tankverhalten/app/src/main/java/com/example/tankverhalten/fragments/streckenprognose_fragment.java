package com.example.tankverhalten.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tankverhalten.R;

public class streckenprognose_fragment extends Fragment {

    View view;

    public streckenprognose_fragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.streckenprognose_layout, container, false);
        return view;
    }
        /*
        Hier kommt die Streckenprognose Activity hin.
     */
}
