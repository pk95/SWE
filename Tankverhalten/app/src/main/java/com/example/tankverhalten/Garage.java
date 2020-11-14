package com.example.tankverhalten;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Garage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garage_layout);

        Button Main_Menu_Button = (Button) findViewById(R.id.main_menu_button);
        Main_Menu_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(Garage.this, com.example.tankverhalten.MainActivity_Menu.class);
                startActivity(startIntent);
            }
        });
    }
    /*
        Hier kommt die Garagen Activity hin.
     */
}