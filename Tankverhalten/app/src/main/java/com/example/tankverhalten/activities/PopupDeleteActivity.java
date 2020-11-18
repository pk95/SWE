package com.example.tankverhalten.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.example.tankverhalten.MainActivity;
import com.example.tankverhalten.R;
import com.example.tankverhalten.Vehicle;

import java.util.Vector;

public class PopupDeleteActivity extends Activity {

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        setContentView(R.layout.popupwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 1), (int) (height * 1));

        Button cancelButton = (Button) findViewById(R.id.canel_button);
        Button confirmButton = (Button) findViewById(R.id.confim_button);


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getIntent().getExtras();
                if (bundle.getInt("pos") >= 0) {
                    GarageActivity.vehicles.remove(bundle.getInt("pos"));
                    System.gc();
                }

                startActivity(new Intent(PopupDeleteActivity.this, GarageActivity.class));
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