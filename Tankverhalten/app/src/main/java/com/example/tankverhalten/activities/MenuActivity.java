package com.example.tankverhalten.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.tankverhalten.R;
import com.example.tankverhalten.fragments.fahrzeuguebersicht_fragment;
import com.example.tankverhalten.fragments.gefahrene_strecke_fragment;
import com.example.tankverhalten.fragments.statistik_fragment;
import com.example.tankverhalten.fragments.streckenprognose_fragment;
import com.example.tankverhalten.fragments.tankvorgang_fragment;
import com.google.android.material.tabs.TabLayout;

public class MenuActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    String active_tab_name = "Fahrzeuguebersicht";
    public static AppCompatActivity fa;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        fa = this;

        toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textview_title = findViewById(R.id.Title);

        viewPager = findViewById(R.id.viewPager_id);

        com.example.tankverhalten.ViewPagerAdapter adapter = new com.example.tankverhalten.ViewPagerAdapter(getSupportFragmentManager());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                active_tab_name = "";
                switch (position) {
                    case 0:
                        textview_title.setText("Fahrzeugübersicht");
                        active_tab_name = "Fahrzeuguebersicht";
                        break;
                    case 1:
                        textview_title.setText("gefahrene Strecken");
                        active_tab_name = "gefahrene Strecken";
                        break;
                    case 2:
                        textview_title.setText("Tankvorgänge");
                        active_tab_name = "Tankvorgaenge";
                        break;
                    case 3:
                        textview_title.setText("Statistik");
                        active_tab_name = "Statistik";
                        break;
                    case 4:
                        textview_title.setText("Streckenprognose");
                        active_tab_name = "Streckenprognose";
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        adapter.addFragment(new fahrzeuguebersicht_fragment(), "");
        adapter.addFragment(new gefahrene_strecke_fragment(), "");
        adapter.addFragment(new tankvorgang_fragment(), "");
        adapter.addFragment(new statistik_fragment(), "");
        adapter.addFragment(new streckenprognose_fragment(), "");

        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabLayout_id);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.car);
        tabLayout.getTabAt(1).setIcon(R.drawable.highway);
        tabLayout.getTabAt(2).setIcon(R.drawable.gasstation);
        tabLayout.getTabAt(3).setIcon(R.drawable.table);
        tabLayout.getTabAt(4).setIcon(R.drawable.calculator);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getColor(R.color.color_icons), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getColor(R.color.color_icons), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(getColor(R.color.color_icons), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(4).getIcon().setColorFilter(getColor(R.color.color_icons), PorterDuff.Mode.SRC_IN);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getColor(R.color.color_icons), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int pos = GarageActivity.vehicleData.getInt("pos");
        switch (item.getItemId()) {
            case R.id.editEntry:
                if (active_tab_name.equals("Fahrzeuguebersicht")) {
                    Intent editIntent = new Intent(this, AddVehicleActivity.class);
                    editIntent.putExtra("pos", pos);
                    startActivity(editIntent);
                    return true;
                }
                else if (active_tab_name.equals("gefahrene Strecken")) {
                    //TODO: .class zur Edit Acitivity ändern
                    Intent editIntent = new Intent(this, gefahrene_strecke_fragment.class);
                    editIntent.putExtra("pos", pos);
                    startActivity(editIntent);
                    return true;
                }
                else if (active_tab_name.equals("Tankvorgaenge")) {
                    Intent edit_fuel_receipt = new Intent(this, RefuelingProcessesActivity.class);
                    //TODO: PASS EXTRAS LEADS TO ERROR
                    edit_fuel_receipt.putExtra("com.example.tankverhalten.mode", "edit");
                    startActivity(edit_fuel_receipt);
                    return true;
                }


            case R.id.deleteEntry:
                if (active_tab_name.equals("Fahrzeuguebersicht")) {
                    Intent deleteIntent = new Intent(this, PopupDeleteActivity.class);
                    deleteIntent.putExtra("pos", pos);
                    startActivity(deleteIntent);
                    return true;
                }
                else if (active_tab_name.equals("gefahrene Strecken")) {
                    Intent deleteIntent = new Intent(this, PopupDeleteRideActivity.class);
                    deleteIntent.putExtra("pos", pos);
                    startActivity(deleteIntent);
                    return true;
                }
                else if (active_tab_name.equals("Tankvorgaenge")) {
                    Intent deleteIntent = new Intent(this, PopupDeleteRefuelActivity.class);
                    deleteIntent.putExtra("pos", pos);
                    startActivity(deleteIntent);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}