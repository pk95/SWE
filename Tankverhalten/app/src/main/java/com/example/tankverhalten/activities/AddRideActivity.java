package com.example.tankverhalten.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tankverhalten.R;
import com.example.tankverhalten.datastructure.Ride;
import com.example.tankverhalten.datastructure.RoadType;
import com.example.tankverhalten.datastructure.Vehicle;

public class AddRideActivity extends AppCompatActivity {

    String mode = "";

    EditText fuellevel_edittext;
    EditText mileage_edittext;
    Toolbar toolbar;

    Vehicle active = null;
    Ride ride = null;
    RadioButton cityE;
    RadioButton combinedE;
    RadioButton countryE;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrideactivity_layout);

        //Get fields of GUI
        fuellevel_edittext = findViewById(R.id.fuelLevelE);
        mileage_edittext = findViewById(R.id.mileAgeE);

        cityE = findViewById(R.id.cityE);
        combinedE = findViewById(R.id.combinedE);
        countryE = findViewById(R.id.countryE);


        // Displaying Data if ride is being edited
        if (getIntent().hasExtra("com.example.tankverhalten.mode")) {
            mode = getIntent().getExtras().getString("com.example.tankverhalten.mode");
        }

        // Creates Toolbar with Backbutton
        toolbar = findViewById(R.id.toolbar_ride_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Was a vehicle selected? -> get vehicle or break
        int pos = GarageActivity.vehicleData.getInt("pos", -1);
        if (pos < 0)
            //no vehicle to get/add ride -> return to previous activity
            this.finish();

        // Get active vehicle
        active = GarageActivity.vehicles.elementAt(pos);

        // Check if new or editing
        // Was edit selected and a ride already exists?
        if (mode.equals("edit") && active.getLastRide() != null) {
            //Edit existing refuel
            getSupportActionBar().setTitle("Strecke bearbeiten");
            //set fields with last ride-data
            ride = active.getLastRide();
            fuellevel_edittext.setText(String.valueOf(ride.fuelLevel));
            mileage_edittext.setText(String.valueOf(ride.mileAge));

            // Sets current roadtype
            int current_roadtype = ride.roadType;
            if (current_roadtype == 0) {
                combinedE.setChecked(true);
            } else if (current_roadtype == 1) {
                cityE.setChecked(true);
            } else if (current_roadtype == 2) {
                countryE.setChecked(true);
            }

        } else if (mode.equals("new")) {
            //Add new ride
            getSupportActionBar().setTitle("Strecke anlegen");
        } else
            this.finish();

    }

    // Creates Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refuelingprocessactivity_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Checks Input and saves Data
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        boolean fuellevel_error = true;
        boolean mileage_error = true;
        boolean radio_error = true;

        // Variables that store the values of the edittexts
        float fuellevel = 0;
        int mileage = 0;
        int road_type = 0;
        // Prevents that fuel_error occurs, when using backbutton
        if (item.getItemId() == R.id.save_button) {

            // Get String out of edittext field
            String fuellevel_string = fuellevel_edittext.getText().toString().trim();
            String mileage_string = mileage_edittext.getText().toString().trim();

            // Store edittext value in variable fuel
            try {
                fuellevel = Float.parseFloat(fuellevel_string);
            } catch (NumberFormatException e) {
                fuellevel = 0;
                fuellevel_error = false;
            }
            // Store edittext value in variable price
            try {
                mileage = Integer.parseInt(mileage_string);
            } catch (NumberFormatException e) {
                mileage = 0;
                mileage_error = false;
            }

            if ((fuellevel > 0 && fuellevel <= 100)) {
                fuellevel_error = false;
            } else {
                fuellevel_edittext.setError("Nicht erlaubte Eingabe");
            }

            if ((mileage != 0)) {
                mileage_error = false;
            } else {
                mileage_edittext.setError("Nicht erlaubte Eingabe");
            }


            if (cityE.isChecked()) {
                road_type = RoadType.CITY;
                radio_error = false;

            } else if (combinedE.isChecked()) {
                road_type = RoadType.COMBINED;
                radio_error = false;

            } else if (countryE.isChecked()) {
                road_type = RoadType.COUNTRY;
                radio_error = false;

            } else {
                radio_error = true;
                cityE.setError("");
                combinedE.setError("");
                countryE.setError("");
            }


            if (!fuellevel_error && !mileage_error && !radio_error) {

                if (mode.equals("edit")) {
                    // Edit ride

                    // set vehicle values back to before the values were added
                    active.mileAge -= ride.mileAge;

                    //Update this ride's values
                    ride.fuelLevel = fuellevel;
                    ride.mileAge = mileage;
                    ride.roadType = road_type;

                    //Update vehicle
                    active.mileAge += mileage;
                    active.fuelLevel = fuellevel;

                } else if (mode.equals("new")) {
                    // Add a new Ride to vehicle
                    Ride temp = new Ride(mileage, fuellevel, road_type);
                    active.add(temp);
                }
                active.calcRemainingRange();
                Vehicle.save(GarageActivity.vehicles, this);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

