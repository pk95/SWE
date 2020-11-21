package com.example.tankverhalten;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class fahrzeuguebersicht_fragment extends Fragment {

    View view;
    public fahrzeuguebersicht_fragment() {}
    public TextView licensePlate;
    public TextView avgConsumption;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fahrzeuguebersicht_layout, container, false);

        Vehicle r = new Vehicle("Test1", "123", 0, 0, 0, 0, 0, 0, VehicleType.CAR);

        licensePlate = (TextView) view.findViewById(R.id.show_licensePlate);
        licensePlate.setText(r.licensePlate);

        avgConsumption = (TextView) view.findViewById(R.id.show_averageConsumption);
        avgConsumption.setText(String.valueOf(r.averageConsumption));
        return view;
    }
    /*
        Hier kommt die Fahrzeugübersicht Activity hin.
    */

}
