package com.example.tankverhalten.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import java.sql.Ref;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

@RequiresApi(api = Build.VERSION_CODES.O)
public class statistik_fragment extends Fragment {

    View view;
    Button before;
    Button later;
    BarChart chart;
    TabLayout tabLayoutTime;
    TabLayout tabLayoutValue;
    Vehicle vehicle;
    TextView value;
    TextView time;
    int selTime = 0;
    int selValue = 0;
    int iconColor;
    int colorFH;
    int before_later = 0; // negative -> more in the past
    List<BarEntry> entries;
    int pos = -1;

    private LocalDateTime currentTime = java.time.LocalDateTime.now();
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Vector<Ride> distance;      //Vector for distance of an vehicle
    private Vector<Refuel> costs;       //Vector for costs of refueling operations of an vehicle

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statistik_layout, container, false);

        vehicle = new Vehicle();

        /**
         * Get currently selected vehicle
         */
        if (!GarageActivity.vehicleData.isEmpty()) {
            try {
                pos = GarageActivity.vehicleData.getInt("pos");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //close activity, if no vehicle is selected
        if (pos < 0) {
            getActivity().finish();
        } else {
            vehicle = GarageActivity.vehicles.get(pos);
        }

        iconColor = getResources().getColor(R.color.color_icons);

        /**
         * Buttons for earlier and later periods
         */
        /**
         * End Buttons
         */


        /**
         * Bottom Tabs Desgin
         */
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

            //Woche/Monat/Jahr
        }
        /**
         * End Bottom Tabs
         */

        value = view.findViewById(R.id.value);
        time = view.findViewById(R.id.time);

        value.setText("Km");
        time.setText("Tag");

        chart = view.findViewById(R.id.chart);

        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        XAxis botAxis = chart.getXAxis();

        // Tab selected logic
        entries = new ArrayList<>();
        String[] s = {"", "", "", "", "", "", ""};
        tabLayoutValue.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {     // auswählen der Arten (Strecke/Tankvorgänge/...
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                selValue = tab.getPosition();
                entries.clear();
                /**
                 * get selected bottom tab (selValue)
                 *      0 = distance
                 *      1 = used fuel
                 *      2 = money spent
                 *      3 = CO2 emissions
                 *
                 * get selected range (selTime)
                 *      0 = week
                 *      1 = month
                 *      2 = year
                 */
                distance = new Vector(Arrays.asList(vehicle.getRides()));
                if (selValue == 0) {

                    /********************************* Start Mileage ********************************************/

                    value.setText("Km");
                    entries.clear();

                    //RECHNUNG
                    for(int o = 0; o < 7; o++)
                        s[o] = "";
                    Ride[] r = vehicle.getRides();

                    int zz = r.length - 1;
                    LocalDate d = r[zz].getCreationDate();
                    d = d.minusDays(7);
                    while (r[zz].getCreationDate() != d && zz > 0){
                        zz--;
                    }
                    if(r[zz].getCreationDate() == d)
                        zz++;

                    for(int i = 0; i < 7 && zz < r.length; i++)
                    {
                        int miles = 0;
                        s[i] = "" + r[zz].getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM"));

                        d = r[zz].getCreationDate();
                        while (d.equals(r[zz].getCreationDate())){
                            miles += r[zz].mileAge;
                            zz++;
                            if(zz > r.length - 1)
                                break;
                        }

                        entries.add(new BarEntry((float) i, (float) miles));
                    }
                    BarDataSet set = new BarDataSet(entries, "BarDataSet");

                    colorFH = ColorTemplate.rgb("0c9a9a");

                    set.setColors(colorFH);
                    set.setValueTextColor(colorFH);

                    BarData data = new BarData(set);
                    data.setBarWidth(0.4f);
                    chart.setData(data);

                    /********************************* End Mileage ********************************************/

                } else if (selValue == 1) {

                    /********************************* Start Fuel ********************************************/

                    value.setText("Liter");
                    entries.clear();

                    //RECHNUNG
                    for(int o = 0; o < 7; o++)
                        s[o] = "";
                    Refuel[] r = vehicle.getRefuels();

                    int zz = r.length - 1;
                    LocalDate d = r[zz].getcDate();
                    d = d.minusDays(7);
                    while (r[zz].getcDate() != d && zz > 0){
                        zz--;
                    }
                    if(r[zz].getcDate() == d)
                        zz++;

                    for(int i = 0; i < 7 && zz < r.length; i++)
                    {
                        int fuel = 0;
                        s[i] = "" + r[zz].getcDate().format(DateTimeFormatter.ofPattern("dd.MM"));

                        d = r[zz].getcDate();
                        while (d.equals(r[zz].getcDate())){
                            fuel += r[zz].refueled;
                            zz++;
                            if(zz > r.length - 1)
                                break;
                        }

                        entries.add(new BarEntry((float) i, (float) fuel));
                    }
                    BarDataSet set = new BarDataSet(entries, "BarDataSet");

                    colorFH = ColorTemplate.rgb("0c9a9a");

                    set.setColors(colorFH);
                    set.setValueTextColor(colorFH);

                    BarData data = new BarData(set);
                    data.setBarWidth(0.4f);
                    chart.setData(data);

                    /********************************* End Fuel ********************************************/

                } else if (selValue == 2) {

                    /********************************* Start Cost ********************************************/

                    value.setText("Euro");
                    entries.clear();

                    //RECHNUNG
                    for(int o = 0; o < 7; o++)
                        s[o] = "";
                    Refuel[] r = vehicle.getRefuels();

                    int zz = r.length - 1;
                    LocalDate d = r[zz].getcDate();
                    d = d.minusDays(7);
                    while (r[zz].getcDate() != d && zz > 0){
                        zz--;
                    }
                    if(r[zz].getcDate() == d)
                        zz++;

                    for(int i = 0; i < 7 && zz < r.length; i++)
                    {
                        int euro = 0;
                        s[i] = "" + r[zz].getcDate().format(DateTimeFormatter.ofPattern("dd.MM"));

                        d = r[zz].getcDate();
                        while (d.equals(r[zz].getcDate())){
                            euro += r[zz].cost;
                            zz++;
                            if(zz > r.length - 1)
                                break;
                        }

                        entries.add(new BarEntry((float) i, (float) euro));
                    }
                    BarDataSet set = new BarDataSet(entries, "BarDataSet");

                    colorFH = ColorTemplate.rgb("0c9a9a");

                    set.setColors(colorFH);
                    set.setValueTextColor(colorFH);

                    BarData data = new BarData(set);
                    data.setBarWidth(0.4f);
                    chart.setData(data);

                    /********************************* End Cost ********************************************/

                } else if (selValue == 3) {

                    /********************************* Start Emissions ********************************************/

                    value.setText("Gramm CO2");
                    entries.clear();

                    //RECHNUNG
                    for(int o = 0; o < 7; o++)
                        s[o] = "";
                    Ride[] r = vehicle.getRides();

                    int zz = r.length - 1;
                    LocalDate d = r[zz].getCreationDate();
                    d = d.minusDays(7);
                    while (r[zz].getCreationDate() != d && zz > 0){
                        zz--;
                    }
                    if(r[zz].getCreationDate() == d)
                        zz++;

                    for(int i = 0; i < 7 && zz < r.length; i++)
                    {
                        int co2 = 0;
                        s[i] = "" + r[zz].getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM"));

                        d = r[zz].getCreationDate();
                        while (d.equals(r[zz].getCreationDate())){
                            co2 += r[zz].mileAge * vehicle.co2emissions;
                            zz++;
                            if(zz > r.length - 1)
                                break;
                        }

                        entries.add(new BarEntry((float) i, (float) co2));
                    }
                    BarDataSet set = new BarDataSet(entries, "BarDataSet");

                    colorFH = ColorTemplate.rgb("0c9a9a");

                    set.setColors(colorFH);
                    set.setValueTextColor(colorFH);

                    BarData data = new BarData(set);
                    data.setBarWidth(0.4f);
                    chart.setData(data);

                    /********************************* End Emissions ********************************************/
                }
                BarDataSet set = new BarDataSet(entries, "BarDataSet");
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


        rightAxis.setEnabled(false);

        /*
        String[] dates = {};
        distance = new Vector(Arrays.asList(vehicle.getRides()));
        int here = distance.size() - 1;
        for(int i = 6; i >= 0; i--)
        {
            int miles = 0;
            LocalDate d = distance.elementAt(here).getCreationDate();
            while (d == distance.elementAt(here).getCreationDate())
            {
                miles += distance.elementAt(here).mileAge;
                here--;
            }
            entries.add(new BarEntry(0f, (float) miles));
            dates[6 - i] = "" + d.getDayOfMonth() + d.getMonthValue();
        }
        */

        //distance = new Vector(Arrays.asList(vehicle.getRides()));

        //RECHNUNG
        Ride[] r = vehicle.getRides();

        int zz = r.length - 1;
        LocalDate d = r[zz].getCreationDate();
        d = d.minusDays(7);
        while (r[zz].getCreationDate() != d && zz > 0){
            zz--;
        }
        if(r[zz].getCreationDate() == d)
            zz++;

        for(int i = 0; i < 7 && zz < r.length; i++)
        {
            int miles = 0;
            s[i] = "" + r[zz].getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM"));

            d = r[zz].getCreationDate();
            while (d.equals(r[zz].getCreationDate())){
                miles += r[zz].mileAge;
                zz++;
                if(zz > r.length - 1)
                    break;
            }

            entries.add(new BarEntry((float) i, (float) miles));
        }






        // standard on Create
        class Formatter extends ValueFormatter {
            private String[] days = s;

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