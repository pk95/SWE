package com.example.tankverhalten.activities;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tankverhalten.R;
import com.example.tankverhalten.datastructure.Refuel;
import com.example.tankverhalten.datastructure.Vehicle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RefuelingProcessesActivity extends AppCompatActivity {

    ImageView imageview;
    String mode = "";
    String date;

    EditText fuel_edittext;
    EditText price_edittext;
    Toolbar toolbar;

    Vehicle active = null;
    Refuel refuel = null;
    int volume = 0;
    // Displays the image
    Bitmap captureImage;
    Boolean photo_taken = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refuelingprocessactivity_layout);

        //Get fields of GUI
        fuel_edittext = findViewById(R.id.fuel_edittext);
        price_edittext = findViewById(R.id.price_edittext);
        imageview = findViewById(R.id.image_view);

        // Set date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        date = simpleDateFormat.format(calendar.getTime());

        // Displaying Data if fuelreceipt is being edited
        if (getIntent().hasExtra("com.example.tankverhalten.mode")) {
            mode = getIntent().getExtras().getString("com.example.tankverhalten.mode");
        }

        // Creates Toolbar with Backbutton
        toolbar = findViewById(R.id.toolbar_refuel_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Was a vehicle selected? -> get vehicle or break
        int pos = GarageActivity.vehicleData.getInt("pos", -1);
        if (pos < 0)
            //no vehicle to get/add refuel -> return to previous activity
            this.finish();

        // Get active vehicle and set tank volume
        active = GarageActivity.vehicles.elementAt(pos);
        volume = active.volume;


        // Check if new or editing
        // Was edit selected and a refuel already exists?
        if (mode.equals("edit") && active.getLastRefuel() != null) {
            //Edit existing refuel
            getSupportActionBar().setTitle("Tankvorgang bearbeiten");

            //set fields with last refuel-data
            refuel = active.getLastRefuel();
            fuel_edittext.setText(String.valueOf(refuel.refueled));
            price_edittext.setText(String.valueOf(refuel.cost));
            loadImageFromStorage(refuel.costImageSrc);

        } else if (mode.equals("new")) {
            //Add new refuel
            getSupportActionBar().setTitle("Tankvorgang eintragen");
        } else
            this.finish();


        // Camera permissions
        Button camera_button = findViewById(R.id.camera_button);
        if (ContextCompat.checkSelfPermission(RefuelingProcessesActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(RefuelingProcessesActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);
        }
        // Starts camera activity
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(open_camera, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            captureImage = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(captureImage);
            photo_taken = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

        boolean fuel_error = true;
        boolean price_error = true;

        // Variables that store the values of the edittexts
        float fuel = 0;
        float price = 0;

        // Prevents that fuel_error occurs, when using backbutton
        if (item.getItemId() == R.id.save_button) {

            // Get String out of edittext field
            String fuel_string = fuel_edittext.getText().toString().trim();
            String price_string = price_edittext.getText().toString().trim();

            // Store edittext value in variable fuel
            try {
                fuel = Float.parseFloat(fuel_string);
            } catch (NumberFormatException e) {
                fuel = 0;
                fuel_error = false;
            }
            // Store edittext value in variable price
            try {
                price = Float.parseFloat(price_string);
            } catch (NumberFormatException e) {
                price = 0;
                fuel_error = false;
            }

            if ((fuel > 0 && fuel <= volume)) {
                fuel_error = false;
            } else {
                fuel_edittext.setError("Darf nicht leer oder größer als das Tankvolumen sein");
            }

            if ((price > 0)) {
                price_error = false;
            } else {
                price_edittext.setError("Darf nicht leer sein");
            }

            if (!fuel_error && !price_error) {

                DecimalFormat a = new DecimalFormat();
                a.setMaximumFractionDigits(3);
                String fuel_format = a.format(fuel);
                String price_format = a.format(price);

                try {
                    price = Float.parseFloat(price_format);
                } catch (NumberFormatException e) {
                    price = 0;
                }
                try {
                    fuel = Float.parseFloat(fuel_format);
                } catch (NumberFormatException e) {
                    fuel = 0;
                }

                if (mode.equals("edit")) {
                    // Only change the related photo, if new one is taken and saved
                    if (photo_taken) {
                        saveToInternalStorage(captureImage);
                        refuel.costImageSrc = date + "_fuelreceipt.jpg";
                    }
                    // set vehicle values back to before the values were added
                    active.fuelLevel -= ((active.getLastRefuel().refueled/ (float)volume)*100);

                    //Update Refuel
                    refuel.cost = price;
                    refuel.refueled = fuel;

                    //Update vehicle data
                    active.fuelLevel =active.fuelLevel + (fuel / (float)volume)*100 ;
                } else if (mode.equals("new")) {
                    //Add a new Refuel to vehicle
                    saveToInternalStorage(captureImage);
                    String costImgSrc = date + "_fuelreceipt.jpg";
                    Refuel temp = new Refuel(fuel, price, costImgSrc);
                    active.add(temp);
                }
                active.calcRemainingRange();
                Vehicle.save(GarageActivity.vehicles, this);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // saving image to internal storage
    private void saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        // path to /data/data/appname/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        // Create unique name
        String fname = date + "_fuelreceipt.jpg";

        //save file in path
        File mypath = new File(directory, fname);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Loads picture if receipt is edited
    private void loadImageFromStorage(String child) {
        try {
            File f = new File("/data/data/com.example.tankverhalten/app_imageDir", child);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = findViewById(R.id.image_view);
            img.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

