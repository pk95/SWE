package com.example.tankverhalten.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.tankverhalten.R;
import com.example.tankverhalten.activities.GarageActivity;
import com.example.tankverhalten.datastructure.Vehicle;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;

public class fahrzeuguebersicht_fragment extends Fragment {

    public TextView licensePlate;
    public TextView consumption;
    public TextView co2;
    public TextView range;
    public TextView fuel;
    public TextView mileAge;
    public TextView inspection;
    public TextView permission;
    View view;
    Vehicle v;
    //    Bundle extras = getActivity().getIntent().getExtras();

    public fahrzeuguebersicht_fragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fahrzeuguebersicht_layout, container, false);

        v = new Vehicle();
        // get the selected vehicle
        int pos = -1;
        if (!GarageActivity.vehicleData.isEmpty()) {
            try {
                pos = GarageActivity.vehicleData.getInt("pos");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pos < 0)
            getActivity().finish();
        else
            v = GarageActivity.vehicles.get(pos);

        // Format of displayed Number
        DecimalFormat df = new DecimalFormat("#,###.##");
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);

        // int to float for NumberFormat
        float fRange = (float) v.remainingRange;
        float miles = (float) v.mileAge;

        // Date to String
        String strInspection;
        String strPermission;
        if (v.inspection == null)
            strInspection = "-";
        else
            strInspection = v.inspection.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        if (v.permission == null)
            strPermission = "-";
        else
            strPermission = v.permission.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // lock TextView object with TextView(id) from xml, then setText
        licensePlate = view.findViewById(R.id.show_licensePlate);
        licensePlate.setText(v.licensePlate);

        consumption = view.findViewById(R.id.show_averageConsumption);
        consumption.setText(df.format(v.averageConsumption));

        co2 = view.findViewById(R.id.show_co2emissions);
        co2.setText(df.format(v.co2emissions));

        range = view.findViewById(R.id.show_remainingRange);
        range.setText(df.format(fRange));

        fuel = view.findViewById(R.id.show_fuelLevel);
        fuel.setText(df.format(v.fuelLevel));

        mileAge = view.findViewById(R.id.show_mileAge);
        mileAge.setText(df.format(miles));

        inspection = view.findViewById(R.id.show_nextInspection);
        inspection.setText(strInspection);

        permission = view.findViewById(R.id.show_nextPermission);
        permission.setText(strPermission);

        return view;
    }
}