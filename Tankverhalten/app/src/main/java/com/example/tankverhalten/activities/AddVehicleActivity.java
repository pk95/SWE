package com.example.tankverhalten.activities;

import android.app.Activity;
import android.content.Context;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.example.tankverhalten.R;
import com.example.tankverhalten.datastructure.Vehicle;
import com.example.tankverhalten.datastructure.VehicleType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddVehicleActivity extends AppCompatActivity {

    String name = "";
    String license = "";
    float combinedConsumption = -1, urbanConsumption = -1, outsideConsumption = -1;
    int mile = -1, volume = -1, vehicleType;
    int co2 = -1;
    float fuel = -1;
    LocalDate permissionDate = null;
    LocalDate inspectionDate = null;
    boolean error = false;
    Activity c = this;
    Bundle bundle;
    int vehicleIndex = -1;
    Toolbar toolbar;
    Context context = this;

    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_vehicle);

        toolbar = (Toolbar) findViewById(R.id.toolbar_vehicle);
        setSupportActionBar(toolbar);

        //All hints above input-fields
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
        TextView permissionTxt = findViewById(R.id.permission_txt);
        TextView inspectionTxt = findViewById(R.id.inspection_txt);

        //Input-fields
        EditText displayName = findViewById(R.id.display_name_vehicle_edit);
        EditText licensePlate = findViewById(R.id.license_plate_edit_vehicle);
        EditText consumptionUrban = findViewById(R.id.consumption_urban_vehicle_edit);
        EditText consumptionOutside = findViewById(R.id.consumption_outside_vehicle_edit);
        EditText consumptionCombined = findViewById(R.id.consumption_combined_vehicle_edit);
        EditText mileage = findViewById(R.id.mileage_vehicle_edit);
        EditText fuelLevel = findViewById(R.id.fuel_level_vehicle_edit);
        EditText tankVolume = findViewById(R.id.tank_volume_vehicle_edit);
        EditText emissions = findViewById(R.id.emissions_vehicle_edit);
        EditText permission = findViewById(R.id.permission_edit);
        EditText inspection = findViewById(R.id.inspection_edit);

        //vehicleType selection-options
        RadioButton car = (RadioButton) findViewById(R.id.car_radio_btn);
        RadioButton motorcycle = (RadioButton) findViewById(R.id.motorcycle_radio_btn);
        RadioButton transporter = (RadioButton) findViewById(R.id.transporter_radio_btn);

        //Buttons
        Button confirmButton = findViewById(R.id.confirm_editing_vehicle);
        Button cancelButton = findViewById(R.id.cancel_editing_vehicle);


//        if (bundle != null) {
//            bundle = getIntent().getExtras();
//            vehicleIndex = bundle.getInt("pos");
//        }
        vehicleIndex = GarageActivity.vehicleData.getInt("pos", -1);
        //Was a vehicle selected? Then fill fields with data of the vehicle
        if (vehicleIndex >= 0) {

            getSupportActionBar().setTitle("Fahrzeug bearbeiten");

            //set all input-fields with value of vehicle
            displayName.setText(GarageActivity.vehicles.elementAt(vehicleIndex).name);
            licensePlate.setText(GarageActivity.vehicles.elementAt(vehicleIndex).licensePlate);
            consumptionUrban.setText(String.valueOf(GarageActivity.vehicles.elementAt(vehicleIndex).urbanConsumption));
            consumptionOutside.setText(String.valueOf(GarageActivity.vehicles.elementAt(vehicleIndex).outsideConsumption));
            consumptionCombined.setText(String.valueOf(GarageActivity.vehicles.elementAt(vehicleIndex).combinedConsumption));
            mileage.setText(String.valueOf(GarageActivity.vehicles.elementAt(vehicleIndex).mileAge));
            fuelLevel.setText(String.valueOf(Math.round(GarageActivity.vehicles.elementAt(vehicleIndex).fuelLevel)));
            tankVolume.setText(String.valueOf(GarageActivity.vehicles.elementAt(vehicleIndex).volume));
            emissions.setText(String.valueOf(Math.round(GarageActivity.vehicles.elementAt(vehicleIndex).co2emissions)));

            if (GarageActivity.vehicles.elementAt(vehicleIndex).permission != null) {
                String permissionDate = GarageActivity.vehicles.elementAt(vehicleIndex).permission.format(formatter1);
                permission.setText(permissionDate);
            }

            if (GarageActivity.vehicles.elementAt(vehicleIndex).inspection != null) {
                String inspectionDate = GarageActivity.vehicles.elementAt(vehicleIndex).inspection.format(formatter1);
                inspection.setText(inspectionDate);
            }

            //set vehicleType from vehicle-data
            vehicleType = GarageActivity.vehicles.elementAt(vehicleIndex).vehicleType;
            switch (vehicleType) {
                case VehicleType.CAR:
                    car.setChecked(true);
                    break;
                case VehicleType.MOTORCYCLE:
                    motorcycle.setChecked(true);
                    break;
                case VehicleType.TRANSPORTER:
                    transporter.setChecked(true);
                    break;
            }
        } else {
            getSupportActionBar().setTitle("Fahrzeug anlegen");
            car.setChecked(true);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // default is no error until one is found
                error = false;

                //check input-fields
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
                        co2 = Integer.parseInt(emissions.getText().toString());
                        markOk(emissions, emissionsTxt);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        markError(emissions, emissionsTxt);
                    }
                } else {
                    markError(emissions, emissionsTxt);
                }

                if (car.isChecked() || transporter.isChecked() || motorcycle.isChecked()) {
                    selectVehicleTypeTxt.setTextColor(ResourcesCompat.getColor(getResources(), R.color.FHGreen, null));
                    selectVehicleTypeTxt.setError(null);
                } else {
                    error = true;
                    selectVehicleTypeTxt.setTextColor(Color.RED);
                    selectVehicleTypeTxt.setError("erforderlich");
                }

                if (permission.getText().length() == 10) {
                    try {
                        String s = permission.getText().toString();
                        permissionDate = LocalDate.parse(s, formatter1);
                        markOk(permission, permissionTxt);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        markError(permission, permissionTxt);
                    }
                } else if (permission.getText().length() != 0) {
                    markError(permission, permissionTxt);
                }

                if (inspection.getText().length() == 10) {
                    try {
                        String s = inspection.getText().toString();
                        inspectionDate = LocalDate.parse(s, formatter1);
                        markOk(inspection, inspectionTxt);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        markError(inspection, inspectionTxt);
                    }
                } else if (inspection.getText().length() != 0) {
                    markError(inspection, inspectionTxt);
                }


                // When no error was found, make a new vehicle with data of the input-fields and add it to vehicles-vector. Then go to the vehicle-overview-activity
                if (!error) {
                    Vehicle newVehicle = null;

                    //When vehicle is new / no vehicle was selected before ^= -1
                    //-> Add vehicle to vehicles-vector
                    if (vehicleIndex < 0) {
                        //Create new Vehicle
                        newVehicle = new Vehicle(name, license, volume, co2, mile, fuel, urbanConsumption, outsideConsumption, combinedConsumption, vehicleType);
                        newVehicle.calcRemainingRange();
                        if (permissionDate != null)
                            newVehicle.permission = permissionDate;
                        if (inspectionDate != null)
                            newVehicle.inspection = inspectionDate;

                        //Update Garage vehicles
                        GarageActivity.vehicles.add(newVehicle);
                        Vehicle.save(GarageActivity.vehicles, c);

                        int pos = GarageActivity.vehicles.indexOf(newVehicle);
                        if (pos >= 0) {
                            // Go to menu
//                            Intent vIntent = new Intent(AddVehicleActivity.this, MenuActivity.class);
//                            vIntent.putExtra("pos", pos);
                            GarageActivity.vehicleData.putInt("pos", pos);
                            finish();
//                            startActivity(vIntent);
                        }
                    } else {
                        //replace vehicle in vehicles-vector with a new vehicle with data of inputs
                        newVehicle = GarageActivity.vehicles.elementAt(vehicleIndex);
                        newVehicle.name = name;
                        newVehicle.licensePlate = license;
                        newVehicle.urbanConsumption = urbanConsumption;
                        newVehicle.outsideConsumption = outsideConsumption;
                        newVehicle.combinedConsumption = combinedConsumption;
                        newVehicle.mileAge = mile;
                        newVehicle.fuelLevel = fuel;
                        newVehicle.volume = volume;
                        newVehicle.co2emissions = co2;
                        newVehicle.vehicleType = vehicleType;
                        if (permissionDate != null)
                            newVehicle.permission = permissionDate;
                        if (inspectionDate != null)
                            newVehicle.inspection = inspectionDate;

                        newVehicle.calcRemainingRange();
                        Vehicle.save(GarageActivity.vehicles, context);
                        finish();
                    }
                }
            }
        });


        //cancel changes with cancel-button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //marks a Input-fields as declined
    private void markError(EditText input, TextView description) {
        error = true;
        description.setError("erforderlich");
        description.setTextColor(Color.RED);
    }

    //marks a Input-fields as accepted
    private void markOk(EditText input, TextView description) {
        description.setTextColor(ResourcesCompat.getColor(getResources(), R.color.FHGreen, null));
        description.setError(null);
    }
}
