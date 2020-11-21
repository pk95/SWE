package com.example.tankverhalten;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class fahrzeuguebersicht_fragment extends Fragment {

    View view;
    public fahrzeuguebersicht_fragment() {}
    public TextView licensePlate;
    public TextView consumption;
    public TextView co2;
    public TextView range;
    public TextView fuel;
    public TextView mileAge;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fahrzeuguebersicht_layout, container, false);

        Vehicle r = new Vehicle("Test1", "123", 0, 500, 250, 35000, 75, (float) 7.5, VehicleType.CAR);

        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(dfs);

        licensePlate = (TextView) view.findViewById(R.id.show_licensePlate);
        licensePlate.setText(r.licensePlate);

        consumption = (TextView) view.findViewById(R.id.show_averageConsumption);
        consumption.setText(df.format(r.averageConsumption));

        co2 = (TextView) view.findViewById(R.id.show_co2emissions);
        co2.setText(df.format(r.co2emissions));

        range = (TextView) view.findViewById(R.id.show_remainingRange);
        range.setText(df.format(r.remainingRange));

        fuel = (TextView) view.findViewById(R.id.show_fuelLevel);
        fuel.setText(df.format(r.fuelLevel));

        mileAge = (TextView) view.findViewById(R.id.show_mileAge);
        mileAge.setText(df.format(r.mileAge));

        return view;
    }
}