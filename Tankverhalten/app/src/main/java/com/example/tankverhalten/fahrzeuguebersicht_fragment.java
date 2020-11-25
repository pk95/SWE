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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class fahrzeuguebersicht_fragment extends Fragment {

    View view;
    public fahrzeuguebersicht_fragment() {}
    public TextView licensePlate;
    public TextView consumption;
    public TextView co2;
    public TextView range;
    public TextView fuel;
    public TextView mileAge;
    public TextView inspection;
    public TextView permission;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fahrzeuguebersicht_layout, container, false);

        Vehicle r = new Vehicle("Test1", "123", 0, (float) 1500.69, 250, 35000, (float) 75.3, (float) 7.5, VehicleType.CAR);
        r.inspection = LocalDate.of(2020, 12, 21);
        r.permission = LocalDate.of(2021, 2, 10);

        // Format of displayed Number
        DecimalFormat df = new DecimalFormat("#,###.##");
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);

        // int to float for NumberFormat
        float fRange = (float) r.remainingRange;
        float miles = (float) r.mileAge;

        // Date to String
        String strInspection = r.inspection.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String strPermission = r.permission.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // lock TextView object with TextView(id) from xml, then setText
        licensePlate = (TextView) view.findViewById(R.id.show_licensePlate);
        licensePlate.setText(r.licensePlate);

        consumption = (TextView) view.findViewById(R.id.show_averageConsumption);
        consumption.setText(df.format(r.averageConsumption));

        co2 = (TextView) view.findViewById(R.id.show_co2emissions);
        co2.setText(df.format(r.co2emissions));

        range = (TextView) view.findViewById(R.id.show_remainingRange);
        range.setText(df.format(fRange));

        fuel = (TextView) view.findViewById(R.id.show_fuelLevel);
        fuel.setText(df.format(r.fuelLevel));

        mileAge = (TextView) view.findViewById(R.id.show_mileAge);
        mileAge.setText(df.format(miles));

        inspection = (TextView) view.findViewById(R.id.show_nextInspection);
        inspection.setText(strInspection);

        permission = (TextView) view.findViewById(R.id.show_nextPermission);
        permission.setText(strPermission);

        return view;
    }
}