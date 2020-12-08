package com.example.tankverhalten.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.example.tankverhalten.R;
import com.example.tankverhalten.datastructure.Vehicle;

public class PopupDeleteActivity extends Activity {

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        setContentView(R.layout.popupwindow);

        /**
         * Get Display Size and set the popup window to fullscreen
         */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 1), (int) (height * 1));

        Button cancelButton = (Button) findViewById(R.id.canel_button);
        Button confirmButton = (Button) findViewById(R.id.confim_button);

        /**
         * Confirm button to delete the current vehicle
         */
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                //Get current vector position
                Bundle bundle = getIntent().getExtras();
                if (bundle.getInt("pos") >= 0) {
                    GarageActivity.vehicles.remove(bundle.getInt("pos"));
                    System.gc();
                    Vehicle.save(GarageActivity.vehicles, getApplication());
                }
                //go back to garage
                MenuActivity.fa.finish();
                finish();
            }
        });

        /**
         * cancel button closes popup window and go back to last activity
         */
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}