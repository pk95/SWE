package com.example.tankverhalten.activities;

import android.Manifest;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.tankverhalten.R;
import com.example.tankverhalten.datastructure.Refuel;
import com.example.tankverhalten.datastructure.Ride;
import com.example.tankverhalten.datastructure.RoadType;
import com.example.tankverhalten.datastructure.Vehicle;
import com.example.tankverhalten.fragments.tankvorgang_fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

public class RefuelingProcessesActivity extends AppCompatActivity {

    ImageView imageview;
    String mode = "";
    EditText fuel_edittext;
    EditText price_edittext;
    String date;
    Toolbar toolbar;
    Vehicle active;
    int volume = 0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refuelingprocessactivity_layout);

        fuel_edittext = (EditText) findViewById(R.id.fuel_edittext);
        price_edittext = (EditText) findViewById(R.id.price_edittext);
        imageview = (ImageView) findViewById(R.id.image_view);

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

        // Get active Vehicle and set tank volume
        active = GarageActivity.vehicles.elementAt(GarageActivity.vehicleData.getInt("pos", -1));
        volume = active.volume;

        // Check if new or editing
        if(mode.equals("edit")) {
            getSupportActionBar().setTitle("Tankvorgang bearbeiten");
            //TODO: Crashed immer wenn man den letzten Refuel abfragen will.
            /*Refuel active_refuel = active.getLastRefuel();

            float refuel = active_refuel.refueled;
            String refuel_string = Float.toString(refuel);

            float cost = active_refuel.cost;
            String cost_string = Float.toString(cost);

            String costImageSrc = active_refuel.costImageSrc;

            fuel_edittext.setText(refuel_string);
            price_edittext.setText(cost_string);

            loadImageFromStorage(costImageSrc); // Name des Bildes der Refuelklasse
*/
        } else if (mode.equals("new")) {
            getSupportActionBar().setTitle("Tankvorgang eintragen");
        }

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

    // Displays the image
    Bitmap captureImage;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            captureImage = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(captureImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Creates Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refuelingprocessactivity_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Activity a9.3 saving/abort changes and returning to d05
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //TODO: .class wird Tankvorgänge anzeigen
        Intent save_fuel_receipt = new Intent(RefuelingProcessesActivity.this, GarageActivity.class);

        boolean fuel_error = true;
        boolean price_error = true;

        // Variables that store the values of the edittexts
        float fuel = 0;
        float price = 0;

        // Prevents that fuel_error occurs, when using backbutton
        if(item.getItemId() == R.id.save_button) {

            // Get String out of edittext field
            String Fuel_string = fuel_edittext.getText().toString().trim();
            String Price_string = price_edittext.getText().toString().trim();

            // Store edittext value in variable fuel
            try {
                fuel = Integer.parseInt(Fuel_string);
            } catch (NumberFormatException e) {
                fuel = 0;
            }
            // Store edittext value in variable price
            try {
                price = Integer.parseInt(Price_string);
            } catch (NumberFormatException e) {
                price = 0;
            }

            if (fuel > 0 && fuel <= volume) {
                fuel_error = false;
            } else {
                fuel_edittext.setError("Darf nicht leer oder größer als das Tankvolumen sein");
            }

            if (price > 0) {
                price_error = false;
            } else {
                price_edittext.setError("Darf nicht leer sein");
            }


            if (!fuel_error && !price_error) {

                saveToInternalStorage(captureImage);

                // Formats String to 2 decimals
                String price_string = Float.toString(price);
                String fuel_string = Float.toString(fuel);
                try {
                    price_string = String.format("%.2f", price);
                } catch (java.util.IllegalFormatException e) {
                    fuel_edittext.setError("Unerlaubte Eingabe");
                }
                try {
                    fuel_string = String.format("%.2f", fuel);
                } catch (java.util.IllegalFormatException e) {
                    price_edittext.setError("Unerlaubte Eingabe");
                }

                if(mode.equals("edit")) {
                    Refuel active_refuel = active.getLastRefuel();
                    active_refuel.cost = price;
                    active_refuel.refueled = fuel;
                    active_refuel.costImageSrc = date + "_fuelreceipt.jpg";
                }
                else if (mode.equals("new")) {
                    String costImgSrc = date + "_fuelreceipt.jpg";
                    Refuel temp = new Refuel(fuel, price, costImgSrc);
                    active.add(temp);
                }
                startActivity(save_fuel_receipt);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    // saving image to internal storage
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        // path to /data/data/appname/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        // Create unique name
        String fname = date + "_fuelreceipt.jpg";

        //save file in path
        File mypath = new File(directory,fname);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                fos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    // Loads picture if receipt is edited
    private void loadImageFromStorage(String child) {
        try {
            File f = new File("/data/data/com.example.tankverhalten/app_imageDir", child);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = (ImageView) findViewById(R.id.image_view);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

