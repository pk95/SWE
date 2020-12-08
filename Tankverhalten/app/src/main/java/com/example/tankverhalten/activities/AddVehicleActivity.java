package com.example.tankverhalten.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tankverhalten.R;
import com.example.tankverhalten.datastructure.Vehicle;
import com.example.tankverhalten.datastructure.VehicleType;

public class AddVehicleActivity extends AppCompatActivity {

    String name = "";
    String license = "";
    float combinedConsumption = -1, urbanConsumption = -1, outsideConsumption = -1;
    int mile = -1, volume = -1, vehicleType;
    float co2 = -1, fuel = -1;
    boolean error = false;
    Activity c = this;
    Bundle bundle;
    int vehicleIndex = -1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_vehicle);

        TextView displayNameTxt = findViewById(R.id.displayname_txt);
        TextView licensePlateTxt = findViewById(R.id.licenseplate_txt);
        TextView consumptionUrbanTxt = findViewById(R.id.consumption_urban_txt);
        TextView consumptionOutsideTxt = findViewById(R.id.consumption_outside_txt);
        TextView consumptionCombinedTxt = findViewById(R.id.consumption_combined_txt);
        TextView mileageTxt = findViewById(R.id.mileage_txt);
        TextView fuelLevelTxt = findViewById(R.id.fuel_level_txt);
        TextView tankVolumeTxt = findViewById(R.id.tank_volume_txt);
        TextView emissionsTxt = findViewById(R.id.emissions_txt);
        TextView selectVehicleTypeTxt = findViewById(R.id.select_vehicle_type_txt);

        EditText displayName = findViewById(R.id.display_name_vehicle_edit);
        EditText licensePlate = findViewById(R.id.license_plate_edit_vehicle);
        EditText consumptionUrban = findViewById(R.id.consumption_urban_vehicle_edit);
        EditText consumptionOutside = findViewById(R.id.consumption_outside_vehicle_edit);
        EditText consumptionCombined = findViewById(R.id.consumption_combined_vehicle_edit);
        EditText mileage = findViewById(R.id.mileage_vehicle_edit);
        EditText fuelLevel = findViewById(R.id.fuel_level_vehicle_edit);
        EditText tankVolume = findViewById(R.id.tank_volume_vehicle_edit);
        EditText emissions = findViewById(R.id.emissions_vehicle_edit);

        Button confirmButton = findViewById(R.id.confirm_editing_vehicle);
        Button cancelButton = findViewById(R.id.cancel_editing_vehicle);

        RadioButton car = findViewById(R.id.car_radio_btn);
        RadioButton motorcycle = findViewById(R.id.motorcycle_radio_btn);
        RadioButton transporter = findViewById(R.id.transporter_radio_btn);

//        if (bundle != null) {
//            bundle = getIntent().getExtras();
//            vehicleIndex = bundle.getInt("pos");
//        }
        vehicleIndex = GarageActivity.vehicleData.getInt("pos",-1);
        if (vehicleIndex >= 0) {
            displayName.setText(GarageActivity.vehicles.elementAt(vehicleIndex).name);
            licensePlate.setText(GarageActivity.vehicles.elementAt(vehicleIndex).licensePlate);
            consumptionUrban.setText(String.valueOf(GarageActivity.vehicles.elementAt(vehicleIndex).urbanConsumption));
            consumptionOutside.setText(String.valueOf(GarageActivity.vehicles.elementAt(vehicleIndex).outsideConsumption));
            consumptionCombined.setText(String.valueOf(GarageActivity.vehicles.elementAt(vehicleIndex).combinedConsumption));
            mileage.setText(String.valueOf(GarageActivity.vehicles.elementAt(vehicleIndex).mileAge));
            fuelLevel.setText(String.valueOf(Math.round(GarageActivity.vehicles.elementAt(vehicleIndex).fuelLevel)));
            tankVolume.setText(String.valueOf(GarageActivity.vehicles.elementAt(vehicleIndex).volume));
            emissions.setText(String.valueOf(Math.round(GarageActivity.vehicles.elementAt(vehicleIndex).co2emissions)));

            vehicleType = GarageActivity.vehicles.elementAt(vehicleIndex).vehicleType;
            switch (vehicleType) {
                case VehicleType.CAR:
                    car.isChecked();
                    break;
                case VehicleType.MOTORCYCLE:
                    motorcycle.isChecked();
                    break;
                case VehicleType.TRANSPORTER:
                    transporter.isChecked();
                    break;
            }


        }


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                error = false;
                if (displayName.getText().length() > 0) {
                    name = displayName.getText().toString();
                    markOk(displayName, displayNameTxt);
                } else {
                    markError(displayName, displayNameTxt);
                }

                if (licensePlate.getText().length() > 0) {
                    license = licensePlate.getText().toString();
                    markOk(licensePlate, licensePlateTxt);
                } else {
                    markError(licensePlate, licensePlateTxt);
                }

                if (consumptionUrban.getText().toString().length() > 0) {
                    try {
                        urbanConsumption = Float.parseFloat(consumptionUrban.getText().toString());
                        markOk(consumptionUrban, consumptionUrbanTxt);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        markError(consumptionUrban, consumptionUrbanTxt);
                    }
                } else {
                    markError(consumptionUrban, consumptionUrbanTxt);
                }

                if (consumptionOutside.getText().length() > 0) {
                    try {
                        outsideConsumption = Float.parseFloat(consumptionOutside.getText().toString());
                        markOk(consumptionOutside, consumptionOutsideTxt);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        markError(consumptionOutside, consumptionOutsideTxt);
                    }
                } else {
                    markError(consumptionOutside, consumptionOutsideTxt);
                }

                if (consumptionCombined.getText().toString().length() > 0) {
                    try {
                        combinedConsumption = Float.parseFloat(consumptionCombined.getText().toString());
                        markOk(consumptionCombined, consumptionCombinedTxt);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        markError(consumptionCombined, consumptionCombinedTxt);
                    }
                } else {
                    markError(consumptionCombined, consumptionCombinedTxt);
                }

                if (mileage.getText().length() >= 0) {
                    try {
                        mile = Integer.parseInt(mileage.getText().toString());
                        markOk(mileage, mileageTxt);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        markError(mileage, mileageTxt);
                    }
                } else {
                    markError(mileage, mileageTxt);
                }

                if (fuelLevel.getText().length() > 0) {
                    try {
                        fuel = Float.parseFloat(fuelLevel.getText().toString());
                        if (fuel >= 0 && fuel <= 100) {
                            markOk(fuelLevel, fuelLevelTxt);
                        } else {
                            markError(fuelLevel, fuelLevelTxt);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        markError(fuelLevel, fuelLevelTxt);
                    }
                } else {
                    markError(fuelLevel, fuelLevelTxt);
                }

                if (tankVolume.getText().length() > 0) {
                    try {
                        volume = Integer.parseInt(tankVolume.getText().toString());
                        markOk(tankVolume, tankVolumeTxt);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        markError(tankVolume, tankVolumeTxt);
                    }
                } else {
                    markError(tankVolume, tankVolumeTxt);
                }

                if (emissions.getText().length() > 0) {
                    try {
                        co2 = Float.parseFloat(emissions.getText().toString());
                        markOk(emissions, emissionsTxt);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        markError(emissions, emissionsTxt);
                    }
                } else {
                    markError(emissions, emissionsTxt);
                }

                if (car.isChecked()) {
                    vehicleType = VehicleType.CAR;
                    selectVehicleTypeTxt.setTextColor(Color.BLACK);
                    car.setTextColor(Color.BLACK);
                    motorcycle.setTextColor(Color.BLACK);
                    transporter.setTextColor(Color.BLACK);
                    selectVehicleTypeTxt.setError(null);

                } else if (motorcycle.isChecked()) {
                    vehicleType = VehicleType.MOTORCYCLE;
                    selectVehicleTypeTxt.setTextColor(Color.BLACK);
                    car.setTextColor(Color.BLACK);
                    motorcycle.setTextColor(Color.BLACK);
                    transporter.setTextColor(Color.BLACK);
                    selectVehicleTypeTxt.setError(null);

                } else if (transporter.isChecked()) {
                    vehicleType = VehicleType.TRANSPORTER;
                    selectVehicleTypeTxt.setTextColor(Color.BLACK);
                    car.setTextColor(Color.BLACK);
                    motorcycle.setTextColor(Color.BLACK);
                    transporter.setTextColor(Color.BLACK);
                    selectVehicleTypeTxt.setError(null);

                } else {
                    error = true;
                    selectVehicleTypeTxt.setTextColor(Color.RED);
                    selectVehicleTypeTxt.setError("erforderlich");
                    car.setTextColor(Color.RED);
                    motorcycle.setTextColor(Color.RED);
                    transporter.setTextColor(Color.RED);
                }

                if (!error) {
                    Vehicle newVehicle = new Vehicle(name, license, volume, co2, 0, mile, fuel, urbanConsumption, outsideConsumption, combinedConsumption, vehicleType);

                    if (vehicleIndex < 0) {
                        GarageActivity.vehicles.add(newVehicle);
                        Vehicle.save(GarageActivity.vehicles, c);

                        int pos = GarageActivity.vehicles.indexOf(newVehicle);
                        if (pos >= 0) {
                            Intent vIntent = new Intent(AddVehicleActivity.this, MenuActivity.class);
                            vIntent.putExtra("pos", pos);
                            GarageActivity.vehicleData.putInt("pos", pos);
                            finish();
                            startActivity(vIntent);
                        }
                    } else {
                        GarageActivity.vehicles.set(vehicleIndex, newVehicle);
                        finish();
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void markError(EditText input, TextView description) {
        error = true;
        description.setError("erforderlich");
        description.setTextColor(Color.RED);
    }

    private void markOk(EditText input, TextView description) {
        description.setTextColor(Color.BLACK);
        description.setError(null);
    }
}
