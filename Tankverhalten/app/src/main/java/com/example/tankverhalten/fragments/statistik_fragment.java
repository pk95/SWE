package com.example.tankverhalten.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.tankverhalten.R;
import com.example.tankverhalten.activities.GarageActivity;
import com.example.tankverhalten.datastructure.Refuel;
import com.example.tankverhalten.datastructure.RoadType;
import com.example.tankverhalten.datastructure.Vehicle;
import com.example.tankverhalten.datastructure.Ride;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import com.github.mikephil.charting.charts.BarChart;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class statistik_fragment extends Fragment{

    View view;
    Button before;
    Button later;
    BarChart chart;
    TabLayout tabLayoutTime;
    TabLayout tabLayoutValue;
    Vehicle v;
    int selTime = 0;
    int selValue = 0;
    int iconColor;
    int colorFH;
    int before_later = 0; // negative -> more in the past
    List<BarEntry> entries;

    public statistik_fragment() {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statistik_layout, container, false);

        v = new Vehicle();
        // get the selected vehicle
        int pos = -1;
        if (!GarageActivity.vehicleData.isEmpty()) {
            try {
                pos = GarageActivity.vehicleData.getInt("pos");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pos < 0)
            getActivity().finish();
        else
            v = GarageActivity.vehicles.get(pos);

        iconColor = getResources().getColor(R.color.color_icons);

        before = view.findViewById(R.id.before);
        before.setOnClickListener(new View.OnClickListener() {      //Buttons entfernen?
            @Override
            public void onClick(View v) {
                before_later--;
            }
        });
        later = view.findViewById(R.id.later);
        later.setOnClickListener(new View.OnClickListener() {       //Buttons entfernen?
            @Override
            public void onClick(View v) {
                before_later++;
            }
        });

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

        chart = view.findViewById(R.id.chart);

        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        XAxis botAxis = chart.getXAxis();



        // Tab selected logic
        List<BarEntry> entries = new ArrayList<>();
        tabLayoutValue.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {     // auswählen der Arten (Strecke/Tankvorgänge/...
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                selValue = tab.getPosition();
                entries.clear();
                if (selValue == 0) {    // km: stand letzte fahrt - letzte fahrt die nicht mehr im zeitraum ist
                    Ride[] r = v.getRides();
                    if (r.length > 0) {
                        for (int i = r.length, o = 0; i > 0 && o < 7; i--, o++) {
                            LocalDate d = r[i - 1].getCreationDate();
                            int miles = r[i - 1].mileAge;
                            if (selTime == 0) {
                                while (d == r[i - 1].getCreationDate()) {
                                    i--;
                                    if (i == 1)
                                        break;
                                }
                                miles = miles - r[i - 1].mileAge;
                            } else if (selTime == 1) {
                                if (i - 1 != 0)
                                    i--;
                                while (r[i - 1].getCreationDate().getDayOfWeek() != DayOfWeek.SUNDAY) {
                                    i--;
                                    if (i == 1)
                                        break;
                                }
                                miles = miles - r[i - 1].mileAge;
                            }
                            else if (selTime == 2){
                                if (i - 1 != 0)
                                    i--;
                                while (r[i - 1].getCreationDate().getMonthValue() != d.minusMonths(1).getMonthValue()) {
                                    i--;
                                    if (i == 1)
                                        break;
                                }
                                miles = miles - r[i - 1].mileAge;
                            }
                            //entries.add(new BarEntry(o, miles));
                        }
                    }

                    entries.add(new BarEntry(0f, 50f));
                    entries.add(new BarEntry(1f, 10f));
                    entries.add(new BarEntry(2f, 10f));
                    entries.add(new BarEntry(3f, 10f));
                    entries.add(new BarEntry(4f, 10f));
                    entries.add(new BarEntry(5f, 10f));
                    entries.add(new BarEntry(6f, 10f));
                }
                else if (selValue == 1){
                    Refuel[] r = v.getRefuels();

                    entries.add(new BarEntry(0f, 10f));
                    entries.add(new BarEntry(1f, 50f));
                    entries.add(new BarEntry(2f, 10f));
                    entries.add(new BarEntry(3f, 10f));
                    entries.add(new BarEntry(4f, 10f));
                    entries.add(new BarEntry(5f, 10f));
                    entries.add(new BarEntry(6f, 10f));
                }
                else if (selValue == 2){
                    entries.add(new BarEntry(0f, 10f));
                    entries.add(new BarEntry(1f, 10f));
                    entries.add(new BarEntry(2f, 50f));
                    entries.add(new BarEntry(3f, 10f));
                    entries.add(new BarEntry(4f, 10f));
                    entries.add(new BarEntry(5f, 10f));
                    entries.add(new BarEntry(6f, 10f));
                }
                else if (selValue == 3){
                    entries.add(new BarEntry(0f, 10f));
                    entries.add(new BarEntry(1f, 10f));
                    entries.add(new BarEntry(2f, 10f));
                    entries.add(new BarEntry(3f, 50f));
                    entries.add(new BarEntry(4f, 10f));
                    entries.add(new BarEntry(5f, 10f));
                    entries.add(new BarEntry(6f, 10f));
                }
                chart.notifyDataSetChanged();
                chart.invalidate();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        tabLayoutTime.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {      // auswählen welche Zeit angezeigt wird (Woche/Monat/Jahr)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selTime = tab.getPosition();

                if (selTime == 0) {
                    class Formatter extends ValueFormatter {
                        private String[] days = {"20.12", "21.12", "22.12", "23.12", "24.12", "25.12", "26.12"};

                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            return days[(int) value];
                        }
                    }
                    botAxis.setValueFormatter(new Formatter());
                }
                else if(selTime == 1){
                    class Formatter extends ValueFormatter {
                        private String[] weeks = {"KW45", "KW46", "KW47", "KW48", "KW49", "KW50", "KW51"};

                        /*
                        LocalDate date = LocalDate.now();
                        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                        int weekNumber = date.get(woy);
                        */

                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            return weeks[(int) value];
                        }
                    }
                    botAxis.setValueFormatter(new Formatter());
                }
                else if(selTime == 2) {
                    class Formatter extends ValueFormatter {
                        private String[] months = {"5.20", "6.20", "7.20", "8.20", "9.20", "10.20", "11.20"};

                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            return months[(int) value];
                        }
                    }
                    botAxis.setValueFormatter(new Formatter());
                }

                chart.notifyDataSetChanged();
                chart.invalidate();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



        rightAxis.setEnabled(false);

        // waren zum testen gedacht, vielleicht nicht mehr brauchbar
        Ride ride1 = new Ride(12000, 0.6f, RoadType.COMBINED);
        Ride ride2 = new Ride(13000, 0.4f, RoadType.COMBINED);
        Ride ride3 = new Ride(14000, 0.2f, RoadType.COMBINED);

        Ride[] rides = {ride1, ride2, ride3}; //v.getRides();

        // diagram - anfang von km zusammenrechnen pro tag/woche/monat
        for(int i = rides.length - 1; i >= 0; i--){
            LocalDate d = rides[i].getCreationDate();
            int miles = 0;
            while (d == rides[i].getCreationDate()){
                if(i-1 < 0)
                    break;
                miles += (rides[i].mileAge - rides[i-1].mileAge);
                i--;
            }
            //entries.add(new BarEntry(1f, miles));
        }


        LocalDate zero = rides[0].getCreationDate();/*
        float x0 = rides[0].getCreationDateDate().compareTo(zero);
        float x1 = rides[1].getCreationDateDate().compareTo(zero);
        float x2 = rides[2].getCreationDateDate().compareTo(zero);

        TextView t = view.findViewById(R.id.value);
        t.setText(x0 + " " + x1 + " " + x2);
        */


        //Date d = (Date)ride1.getCreationDateDate();

        entries.add(new BarEntry(0f, 50f));
        entries.add(new BarEntry(1f, 10f));
        entries.add(new BarEntry(2f, 10f));
        entries.add(new BarEntry(3f, 10f));
        entries.add(new BarEntry(4f, 10f));
        entries.add(new BarEntry(5f, 10f));
        entries.add(new BarEntry(6f, 10f));


        // standard on Create
        class Formatter extends ValueFormatter {
            private String[] days = {"20.12", "21.12", "22.12", "23.12", "24.12", "25.12", "26.12"};

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return days[(int) value];
            }
        }
        botAxis.setValueFormatter(new Formatter());

        // Achsen Anpassen
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawLabels(true);
        leftAxis.setAxisMinimum(0);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisLineColor(Color.BLACK);
        leftAxis.setAxisLineWidth(0.5f);
        leftAxis.setTextSize(12);

        botAxis.setDrawGridLines(false);
        botAxis.setDrawAxisLine(true);
        botAxis.setDrawLabels(true);
        botAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        botAxis.setGranularity(1);
        botAxis.setAxisLineColor(Color.BLACK);
        botAxis.setAxisLineWidth(0.5f);
        botAxis.setLabelCount(7);
        botAxis.setTextSize(12);

        // Diagramm anpassen
        BarDataSet set = new BarDataSet(entries, "BarDataSet");

        colorFH = ColorTemplate.rgb("0c9a9a");

        set.setColors(colorFH);
        set.setValueTextColor(colorFH);

        BarData data = new BarData(set);
        data.setBarWidth(0.4f);
        chart.setData(data);
        chart.setFitBars(true);
        chart.invalidate();
        chart.setNoDataTextColor(colorFH);
        chart.setNoDataText("Keine Daten vorhanden");
        chart.setDrawValueAboveBar(false);
        Description desc = chart.getDescription();
        desc.setEnabled(false);
        Legend l = chart.getLegend();
        l.setEnabled(false);

        return view;
    }
}