package com.example.tankverhalten.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.tankverhalten.R;
import com.example.tankverhalten.activities.GarageActivity;
import com.example.tankverhalten.datastructure.Vehicle;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
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

    @SuppressLint("SetTextI18n")
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
        DecimalFormat df = new DecimalFormat("#,###");
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
        //Kennzeichen
        licensePlate = view.findViewById(R.id.show_licensePlate);
        licensePlate.setText(v.licensePlate);

        //Verbrauch
        consumption = view.findViewById(R.id.show_averageConsumption);
        consumption.setText(df.format(v.combinedConsumption) + " l(kWh)");

        //CO2-Ausstoß
        co2 = view.findViewById(R.id.show_co2emissions);
        co2.setText(df.format(v.co2emissions) + " g");

        //Reichweite
        range = view.findViewById(R.id.show_remainingRange);
        range.setText(df.format(fRange) + " km");

        //Tankstand
        fuel = view.findViewById(R.id.show_fuelLevel);
        fuel.setText(df.format(v.fuelLevel) + " %");

        //Kilometerstand
        mileAge = view.findViewById(R.id.show_mileAge);
        mileAge.setText(df.format(miles) + " km");

        //Next TÜV
        inspection = view.findViewById(R.id.show_nextInspection);
        inspection.setText(strInspection);
        setBackgroundColorByDate(inspection, v.inspection);

        //Next Inspection
        permission = view.findViewById(R.id.show_nextPermission);
        permission.setText(strPermission);
        setBackgroundColorByDate(permission, v.permission);

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setBackgroundColorByDate(TextView textview, LocalDate date) {
        if (textview == null || date == null)
            return;
        if (date.isBefore(LocalDate.now()))
            //Date is already over
            textview.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dateExceeded, null));
        else if (date.isAfter(LocalDate.now()) && (Period.between(LocalDate.now(), date).getMonths() > 0 || Period.between(LocalDate.now(), date).getYears() > 0)) {
            //Date is in the future above a month
            textview.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.FHGreen, null));
        } else
            //Date is of today or within a month
            textview.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dateIsApproaching, null));
    }
}


