package com.example.tankverhalten.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tankverhalten.EditVehicle;
import com.example.tankverhalten.MainActivity_Menu;
import com.example.tankverhalten.R;
import com.example.tankverhalten.RecyclerviewVehicles;
import com.example.tankverhalten.Vehicle;
import com.example.tankverhalten.VehicleType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Vector;

public class GarageActivity extends AppCompatActivity implements RecyclerviewVehicles.OnVehicleListener {

    RecyclerView recyclerView;
    RecyclerviewVehicles rows;
    static Vector<Vehicle> vehicles;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vehicles = Vehicle.load(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),AddVehicleActivity.class);
                //intent.putExtra("pos", -1);
                startActivity(intent);
            }
        });

        vehicles = Vehicle.load(this);

        /*
            Creaate new Vehicle
         */
        recyclerView = findViewById(R.id.garage_add_vehicle);
//        Vehicle r = new Vehicle("Test1", "123", 0, 0, 0, 0, 0, 0, VehicleType.CAR);
//        vehicles.add(r);
//        Vehicle.save(vehicles, this);

        /*
            Show Buttons for vehicles
         */
        rows = new RecyclerviewVehicles(this, vehicles, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rows);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        rows = new RecyclerviewVehicles(this, vehicles, this);
        recyclerView.setAdapter(rows);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Vehicle.save(vehicles, this);
        super.onResume();
    }

    public static Intent vIntent;
    @Override
    public void onVehicleClick(int position) {
        vIntent = new Intent(this, MainActivity_Menu.class);
        vIntent.putExtra("pos", position);
        startActivity(vIntent);
    }

    public static Vector<Vehicle> getVehicle() {
        return vehicles;
    }
}