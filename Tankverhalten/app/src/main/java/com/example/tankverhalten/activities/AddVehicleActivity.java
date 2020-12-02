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

    Intent vIntent;
    String name = "";
    String license = "";
    double urban = -1, outside = -1;
    float combined = -1;
    int mile = -1, volume = -1, vehicleType;
    float co2 = -1, fuel = -1;
    boolean error = false;
    Activity c = this;

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


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {


//                //LIst all parses from editText to Vehicle
//                ArrayList<VehicleParseItem<EditText, String, String, TextView>> vehicleValues = new ArrayList<>();
//                vehicleValues.add(new VehicleParseItem(consumptionCombined, "float", "averageConsumption", consumptionCombinedTxt));
//                vehicleValues.add(new VehicleParseItem(consumptionUrban, "float", "averageConsumption", consumptionUrbanTxt));
//                vehicleValues.add(new VehicleParseItem(consumptionOutside, "float", "averageConsumption", consumptionOutsideTxt));

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
                        urban = Double.parseDouble(consumptionUrban.getText().toString());
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
                        outside = Double.parseDouble(consumptionOutside.getText().toString());
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
                        combined = Float.parseFloat(consumptionCombined.getText().toString());
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
                        markOk(fuelLevel, fuelLevelTxt);
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
                    Vehicle newVehicle = new Vehicle(name, license, volume, co2, 0, mile, fuel, combined, vehicleType);
                    GarageActivity.vehicles.add(newVehicle);
                    Vehicle.save(GarageActivity.vehicles, c);

//                finish();

                    int pos = GarageActivity.vehicles.indexOf(newVehicle);
                    if (pos >= 0) {
                        vIntent = new Intent(AddVehicleActivity.this, MenuActivity.class);
                        vIntent.putExtra("pos", pos);
                        GarageActivity.vehicleData.putInt("pos", pos);
//                    vIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(vIntent);
                    }
                }

                /*
                Intent intent = new Intent(this, MainActivity_Menu.class);
                intent.putExtra("pos", pos);
                startActivity(intent);
                 */
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
