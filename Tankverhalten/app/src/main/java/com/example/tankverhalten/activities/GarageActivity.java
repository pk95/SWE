package com.example.tankverhalten.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    static Vector<Vehicle> vehicles = new Vector<Vehicle>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent intent = new Intent(view.getContext(),EditVehicle.class);
                startActivity(intent);
            }
        });

        vehicles = Vehicle.load(this);

        /*
            Creaate new Vehicle
         */
        Vehicle r = new Vehicle("Test1", "123", 0, 0, 0, 0, 0, 0, VehicleType.CAR);
        vehicles.add(r);
        vehicles.add(r);


        /*
            Show Buttons for vehicles
         */
        recyclerView = findViewById(R.id.vehicleButtons);
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

    @Override
    public void onVehicleClick(int position) {
        Intent intent = new Intent(this, MainActivity_Menu.class);
        intent.putExtra("VehiclePosition", position);
        startActivity(intent);
    }
}