package com.example.tankverhalten.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.customview.widget.ViewDragHelper;

import com.example.tankverhalten.MainActivity_Menu;
import com.example.tankverhalten.R;
import com.example.tankverhalten.Vehicle;
import com.example.tankverhalten.VehicleType;

public class AddVehicleActivity extends AppCompatActivity {

    String name = "";
    String license = "";
    double urban = -1, outside = -1, combined = -1;
    int mile = -1, volume = -1, vehicleType;
    float co2 = -1, fuel = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_vehicle);

        TextView displayNameTxt = (TextView) findViewById(R.id.displayname_txt);
        TextView licensePlateTxt = (TextView) findViewById(R.id.licenseplate_txt);
        TextView consumptionUrbanTxt = (TextView) findViewById(R.id.consumption_urban_txt);
        TextView consumptionOutsideTxt = (TextView) findViewById(R.id.consumption_outside_txt);
        TextView consumptionCombinedTxt = (TextView) findViewById(R.id.consumption_combined_txt);
        TextView mileageTxt = (TextView) findViewById(R.id.mileage_txt);
        TextView fuelLevelTxt = (TextView) findViewById(R.id.fuel_level_txt);
        TextView tankVolumeTxt = (TextView) findViewById(R.id.tank_volume_txt);
        TextView emissionsTxt = (TextView) findViewById(R.id.emissions_txt);
        TextView selectVehicleTypeTxt = (TextView) findViewById(R.id.select_vehicle_type_txt);

        EditText displayName = (EditText) findViewById(R.id.display_name_vehicle_edit);
        EditText licensePlate = (EditText) findViewById(R.id.license_plate_edit_vehicle);
        EditText consumptionUrban = (EditText) findViewById(R.id.consumption_urban_vehicle_edit);
        EditText consumptionOutside = (EditText) findViewById(R.id.consumption_outside_vehicle_edit);
        EditText consumptionCombined = (EditText) findViewById(R.id.consumption_combined_vehicle_edit);
        EditText mileage = (EditText) findViewById(R.id.mileage_vehicle_edit);
        EditText fuelLevel = (EditText) findViewById(R.id.fuel_level_vehicle_edit);
        EditText tankVolume = (EditText) findViewById(R.id.tank_volume_vehicle_edit);
        EditText emissions = (EditText) findViewById(R.id.emissions_vehicle_edit);

        Button confirmButton = (Button) findViewById(R.id.confirm_editing_vehicle);
        Button cancelButton = (Button) findViewById(R.id.cancel_editing_vehicle);

        RadioButton car = (RadioButton) findViewById(R.id.car_radio_btn);
        RadioButton motorcycle = (RadioButton) findViewById(R.id.motorcycle_radio_btn);
        RadioButton transporter = (RadioButton) findViewById(R.id.transporter_radio_btn);


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                boolean error = false;

                if (displayName.getText().length() > 0) {
                    name = displayName.getText().toString();
                } else {
                    error = true;
                    displayName.setError("erforderlich");
                    displayNameTxt.setTextColor(Color.RED);
                }

                if (licensePlate.getText().length() > 0) {
                    license = licensePlate.getText().toString();
                } else {
                    error = true;
                    licensePlate.setError("erforderlich");
                    licensePlateTxt.setTextColor(Color.RED);
                }

                if (consumptionUrban.getText().toString().length() > 0 && Double.parseDouble(consumptionUrban.getText().toString()) >= 0) {
                    urban = Double.parseDouble(consumptionUrban.getText().toString());
                } else{
                    error = true;
                    consumptionUrban.setError("erforderlich");
                    consumptionUrbanTxt.setTextColor(Color.RED);
                }

                if (consumptionOutside.getText().length() >= 0 && Double.parseDouble(consumptionOutside.getText().toString()) >= 0) {
                    outside = Double.parseDouble(consumptionOutside.getText().toString());
                } else {
                    error = true;
                    consumptionOutside.setError("erforderlich");
                    consumptionOutsideTxt.setTextColor(Color.RED);
                }

                if (consumptionCombined.getText().length() >= 0 && Double.parseDouble(consumptionCombined.getText().toString()) >= 0) {
                    combined = Double.parseDouble(consumptionCombined.getText().toString());
                } else {
                    error = true;
                    consumptionCombined.setError("erforderlich");
                    consumptionCombinedTxt.setTextColor(Color.RED);
                }

                if (mileage.getText().length() >= 0 && Integer.parseInt(mileage.getText().toString()) >= 0) {
                    mile = Integer.parseInt(mileage.getText().toString());
                } else {
                    error = true;
                    mileage.setError("erforderlich");
                    mileageTxt.setTextColor(Color.RED);
                }

                if (fuelLevel.getText().length() >= 0 && Float.parseFloat(fuelLevel.getText().toString()) >= 0 ) {
                    fuel = Float.parseFloat(fuelLevel.getText().toString());
                } else {
                    error = true;
                    fuelLevel.setError("erforderlich");
                    fuelLevelTxt.setTextColor(Color.RED);
                }

                if (tankVolume.getText().length() >= 0 && Integer.parseInt(tankVolume.getText().toString()) >= 0) {
                    volume = Integer.parseInt(tankVolume.getText().toString());
                } else {
                    error = true;
                    tankVolume.setError("erforderlich");
                    tankVolumeTxt.setTextColor(Color.RED);
                }

                if (emissions.getText().length() >= 0 && Float.parseFloat(emissions.getText().toString()) >= 0) {
                    co2 = Float.parseFloat(emissions.getText().toString());
                } else {
                    error = true;
                    emissions.setError("erforderlich");
                    emissionsTxt.setTextColor(Color.RED);
                }

                if (car.isChecked()) {
                    vehicleType = VehicleType.CAR;
                } else if (motorcycle.isChecked()) {
                    vehicleType = VehicleType.MOTORCYCLE;
                } else if (transporter.isChecked()) {
                    vehicleType = VehicleType.TRANSPORTER;
                } else {
                    error = true;
                    selectVehicleTypeTxt.setTextColor(Color.RED);
                    car.setTextColor(Color.RED);
                    motorcycle.setTextColor(Color.RED);
                    transporter.setTextColor(Color.RED);
                    transporter.setError("erforderlich");
                }

                if (error) {
                    return;
                }

                Vehicle newVehicle = new Vehicle();
                GarageActivity.vehicles.add(newVehicle);

                int pos = GarageActivity.vehicles.indexOf(newVehicle);

                Intent intent = new Intent(AddVehicleActivity.this, MainActivity_Menu.class);
                intent.putExtra("pos", pos);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}