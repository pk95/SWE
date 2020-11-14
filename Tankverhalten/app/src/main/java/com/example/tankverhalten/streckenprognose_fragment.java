package com.example.tankverhalten;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
