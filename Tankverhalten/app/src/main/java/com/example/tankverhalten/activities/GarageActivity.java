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

import com.example.tankverhalten.R;
import com.example.tankverhalten.RecyclerviewVehiclesAdapter;
import com.example.tankverhalten.datastructure.Vehicle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Vector;

public class GarageActivity extends AppCompatActivity implements RecyclerviewVehiclesAdapter.OnVehicleListener {

    public static Vector<Vehicle> vehicles;
    public static Bundle vehicleData = new Bundle();

    RecyclerView recyclerView;
    RecyclerviewVehiclesAdapter rows;

//    public static Vector<Vehicle> getVehicle() {
//        return vehicles;
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        Toolbar toolbar = findViewById(R.id.toolbar_vehicle);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.vehicleButtonsContainer);

        vehicles = Vehicle.load(this);
        showVehicleButtons();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicleData.putInt("pos", -1);
                Intent intent = new Intent(getApplicationContext(), AddVehicleActivity.class);
                intent.putExtras(vehicleData);
                startActivity(intent);
            }
        });

        //Specification: When just one vehicle in garage open the menu of this vehicle
        if (vehicles.size() == 1) {
            vehicleData.putInt("pos", 0);
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtras(vehicleData);
            startActivity(intent);
        }
    }

    /**
     *When continue on this screen
     * clear pos / vehicleId
     * recreate buttons for vehicles
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        vehicleData.putInt("pos", -1);
        showVehicleButtons();
        super.onResume();
    }

    /**
     * Starts the Menu for the vehicle withthe index at the vector vehicles
     *
     * @param position index of vehicle in vector vehicles
     */
    @Override
    public void onVehicleClick(int position) {
        //Collect all data for putthrough to a new activity
        vehicleData.putInt("pos", position);
        //Make intent for new activity
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    /**
     * Shows a button for each vehicle.
     * A click opens the overview about the vehicle's data.
     */
    void showVehicleButtons() {
        rows = new RecyclerviewVehiclesAdapter(getApplicationContext(), vehicles, this);
        recyclerView.setAdapter(rows);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}