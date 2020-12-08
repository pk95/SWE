package com.example.tankverhalten.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.tankverhalten.R;
import com.google.android.material.tabs.TabLayout;

public class statistik_fragment extends Fragment{

    View view;
    ViewPager viewPager;
    TabLayout tabLayoutTime;
    TabLayout tabLayoutValue;
    int selTime = 0;
    int selValue = 0;
    int iconColor;

    public statistik_fragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statistik_layout, container, false);

        iconColor = getResources().getColor(R.color.color_icons);

        // Bottom Tabs Design
        {
            tabLayoutValue = view.findViewById(R.id.valueBar);
            tabLayoutValue.getTabAt(0).setIcon(R.drawable.highway);
            tabLayoutValue.getTabAt(1).setIcon(R.drawable.gasstation);
            tabLayoutValue.getTabAt(2).setIcon(R.drawable.ic_euro_white);
            tabLayoutValue.getTabAt(3).setIcon(R.drawable.co2);
            tabLayoutValue.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            tabLayoutValue.getTabAt(1).getIcon().setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            tabLayoutValue.getTabAt(2).getIcon().setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            tabLayoutValue.getTabAt(3).getIcon().setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);

            tabLayoutTime = view.findViewById(R.id.timeBar);
            tabLayoutTime.getTabAt(0).setText(getResources().getText(R.string.week));
            tabLayoutTime.getTabAt(1).setText(getResources().getText(R.string.month));
            tabLayoutTime.getTabAt(2).setText(getResources().getText(R.string.year));
            tabLayoutTime.setTabTextColors(iconColor, Color.WHITE);
        }

        // Tab selected logic
        tabLayoutValue.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                selValue = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        tabLayoutTime.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selTime = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // diagram
        viewPager = view.findViewById(R.id.content);

        return view;
    }
}