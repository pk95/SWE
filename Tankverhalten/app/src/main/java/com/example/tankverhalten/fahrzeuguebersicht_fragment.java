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
import androidx.fragment.app.FragmentManager;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.tankverhalten.R;
import com.example.tankverhalten.EditVehicle;
import com.example.tankverhalten.MainActivity_Menu;
import com.example.tankverhalten.RecyclerviewVehicles;
import com.example.tankverhalten.Vehicle;
import com.example.tankverhalten.VehicleType;

import java.util.Vector;

public class fahrzeuguebersicht_fragment extends Fragment {

    View view;
    //static Vector<Vehicle> vehicles = new Vector<Vehicle>();
    public fahrzeuguebersicht_fragment() {}
    public TextView mTextView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fahrzeuguebersicht_layout, container, false);
        //vehicles = Vehicle.load(this);


        Vehicle r = new Vehicle("Test1", "123", 0, 0, 0, 0, 0, 0, VehicleType.CAR);
        //vehicles.add(r);
        //TextView doLicensePlate = (TextView) findViewById(R.id.show_licensePlate);

        mTextView = (TextView) view.findViewById(R.id.show_licensePlate);
        mTextView.setText(r.licensePlate);

        return view;
    }
    /*
        Hier kommt die Fahrzeugübersicht Activity hin.
    */

}
