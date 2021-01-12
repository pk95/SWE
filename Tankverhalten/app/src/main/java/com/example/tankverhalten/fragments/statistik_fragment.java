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

import java.sql.Ref;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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

            tabLayoutTime = view.findViewById(R.id.timeBar);
            tabLayoutTime.getTabAt(0).setText(getResources().getText(R.string.week));
            tabLayoutTime.getTabAt(1).setText(getResources().getText(R.string.month));
            tabLayoutTime.getTabAt(2).setText(getResources().getText(R.string.year));
            tabLayoutTime.setTabTextColors(iconColor, Color.WHITE);
        }
        /**
         * End Bottom Tabs
         */

        chart = view.findViewById(R.id.chart);

        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        XAxis botAxis = chart.getXAxis();

        // Tab selected logic
        entries = new ArrayList<>();
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

                    if (selTime == 0) {
                        startTime = currentTime.minusWeeks(1);
                        endTime = currentTime;

                        Ride[] unsortedWeekDistance = Ride.getRidesBetweenDates(distance, startTime, endTime);
                        float[] sortedWeekDistance = new float[]{0, 0, 0, 0, 0, 0, 0};

                        /**
                         * i and j are run variables
                         *      i for sortedArray
                         *      j for unsortedArray
                         */
                        int i = 0;
                        int j = 0;

                        /**
                         * Checking, if entry exists
                         * if startDate != first entry of unsortedArray, set sortedArray at position 0 = 0
                         * continue while unsortedArray date = date in selected range or end the end is reached
                         */
                        if (unsortedWeekDistance[0].getCreationDateTime() != startTime) {
                            j++;
                            i++;
                            while (unsortedWeekDistance[i].getCreationDateTime() == startTime.plusDays(i) || i < 7) {
                                if (unsortedWeekDistance[i].getCreationDateTime() != startTime.plusDays(i)) {
                                    j++;
                                    i++;
                                }
                            }
                        }

                        /**
                         * first while = Array Size of 7
                         * second while = Array Size of all rides in time period
                         *
                         * first if = checking, if first run
                         * second if = checking if actual entry is same date as the entry before
                         *      if it is the same date, mileage adding to sorted array
                         *      if it is not the same date, i++ and break second while to continue with increased run variable for i
                         */
                        while (i < 7) {
                            while (j < unsortedWeekDistance.length) {
                                if (unsortedWeekDistance[j].getCreationDateTime() == unsortedWeekDistance[j - 1].getCreationDateTime()) {
                                    sortedWeekDistance[i] = sortedWeekDistance[i] + (float) unsortedWeekDistance[j].mileAge;
                                    j++;

                                }

                                if (unsortedWeekDistance[j].getCreationDateTime() != unsortedWeekDistance[j - 1].getCreationDateTime()) {
                                    i++;
                                    break;
                                }
                            }
                        }

                        entries.add(new BarEntry(0f, sortedWeekDistance[0]));
                        entries.add(new BarEntry(1f, sortedWeekDistance[1]));
                        entries.add(new BarEntry(2f, sortedWeekDistance[2]));
                        entries.add(new BarEntry(3f, sortedWeekDistance[3]));
                        entries.add(new BarEntry(4f, sortedWeekDistance[4]));
                        entries.add(new BarEntry(5f, sortedWeekDistance[5]));
                        entries.add(new BarEntry(6f, sortedWeekDistance[6]));
                        /********************************* End Week ********************************************/

                    } else if (selTime == 1) {
                        startTime = currentTime.minusMonths(6).withDayOfMonth(1);
                        endTime = currentTime;
                        Ride[] unsortedMonthDistance = Ride.getRidesBetweenDates(distance, startTime, endTime);
                        float[] sortedMonthDistance = new float[]{0, 0, 0, 0, 0, 0, 0};

                        /**
                         * i and j are run variables
                         *      i for sortedArray
                         *      j for unsortedArray
                         */
                        int i = 0;
                        int j = 0;

                        /**
                         * Checking, if entry exists
                         * if startDate != first entry of unsortedArray, set sortedArray at position 0 = 0
                         * continue while unsortedArray date = date in selected range or end the end is reached
                         */
                        if (unsortedMonthDistance[0].getCreationDateTime() != startTime) {
                            j++;
                            i++;
                            while (unsortedMonthDistance[i].getCreationDateTime() == startTime.plusMonths(i) || i < 7) {
                                if (unsortedMonthDistance[i].getCreationDateTime() != startTime.plusMonths(i)) {
                                    j++;
                                    i++;
                                }
                            }
                        }

                        /**
                         * first while = Array Size of 7
                         * second while = Array Size of all rides in time period
                         *
                         * first if = checking, if first run
                         * second if = checking if actual entry is same date as the entry before
                         *      if it is the same date, mileage adding to sorted array
                         *      if it is not the same date, i++ and break second while to continue with increased run variable for i
                         */
                        while (i < 7) {
                            while (j < unsortedMonthDistance.length) {
                                if (unsortedMonthDistance[j].getCreationDateTime().getMonth() == unsortedMonthDistance[j - 1].getCreationDateTime().getMonth()) {
                                    sortedMonthDistance[i] = sortedMonthDistance[i] + (float) unsortedMonthDistance[j].mileAge;
                                    j++;

                                }

                                if (unsortedMonthDistance[j].getCreationDateTime() != unsortedMonthDistance[j - 1].getCreationDateTime()) {
                                    i++;
                                    break;
                                }
                            }
                        }

                        entries.add(new BarEntry(0f, sortedMonthDistance[0]));
                        entries.add(new BarEntry(1f, sortedMonthDistance[1]));
                        entries.add(new BarEntry(2f, sortedMonthDistance[2]));
                        entries.add(new BarEntry(3f, sortedMonthDistance[3]));
                        entries.add(new BarEntry(4f, sortedMonthDistance[4]));
                        entries.add(new BarEntry(5f, sortedMonthDistance[5]));
                        entries.add(new BarEntry(6f, sortedMonthDistance[6]));
                        /********************************* End Month ********************************************/

                    } else if (selTime == 2) {
                        startTime = currentTime.minusYears(7);
                        endTime = currentTime.minusYears(1).with(TemporalAdjusters.lastDayOfYear());
                        Ride[] unsortedYearDistance = Ride.getRidesBetweenDates(distance, startTime, endTime);
                        float[] sortedYearDistance = new float[]{0, 0, 0, 0, 0, 0, 0};

                        /**
                         * i and j are run variables
                         *      i for sortedArray
                         *      j for unsortedArray
                         */
                        int i = 0;
                        int j = 0;

                        /**
                         * Checking, if entry exists
                         * if startDate != first entry of unsortedArray, set sortedArray at position 0 = 0
                         * continue while unsortedArray date = date in selected range or end the end is reached
                         */
                        if (unsortedYearDistance[0].getCreationDateTime() != startTime) {
                            j++;
                            i++;
                            while (unsortedYearDistance[i].getCreationDateTime() == startTime.plusYears(i) || i < 7) {
                                if (unsortedYearDistance[i].getCreationDateTime() != startTime.plusYears(i)) {
                                    j++;
                                    i++;
                                }
                            }
                        }

                        /**
                         * first while = Array Size of 7
                         * second while = Array Size of all rides in time period
                         *
                         * first if = checking, if first run
                         * second if = checking if actual entry is same date as the entry before
                         *      if it is the same date, mileage adding to sorted array
                         *      if it is not the same date, i++ and break second while to continue with increased run variable for i
                         */
                        while (i < 7) {
                            while (j < unsortedYearDistance.length) {
                                if (unsortedYearDistance[j].getCreationDateTime().getYear() == unsortedYearDistance[j - 1].getCreationDateTime().getYear()) {
                                    sortedYearDistance[i] = sortedYearDistance[i] + (float) unsortedYearDistance[j].mileAge;
                                    j++;

                                }

                                if (unsortedYearDistance[j].getCreationDateTime() != unsortedYearDistance[j - 1].getCreationDateTime()) {
                                    i++;
                                    break;
                                }
                            }
                        }

                        entries.add(new BarEntry(0f, sortedYearDistance[0]));
                        entries.add(new BarEntry(1f, sortedYearDistance[1]));
                        entries.add(new BarEntry(2f, sortedYearDistance[2]));
                        entries.add(new BarEntry(3f, sortedYearDistance[3]));
                        entries.add(new BarEntry(4f, sortedYearDistance[4]));
                        entries.add(new BarEntry(5f, sortedYearDistance[5]));
                        entries.add(new BarEntry(6f, sortedYearDistance[6]));
                        /********************************* End Year ********************************************/
                    }

                    /********************************* Start Fuel ********************************************/
                } else if (selValue == 1) {

                    if (selTime == 0) {
                        startTime = currentTime.minusYears(7);
                        endTime = currentTime.minusYears(1).with(TemporalAdjusters.lastDayOfYear());
                        Ride[] unsortedWeekFuel = Ride.getRidesBetweenDates(distance, startTime, endTime);
                        float[] sortedWeekFuel = new float[]{0, 0, 0, 0, 0, 0, 0};

                        /**
                         * i and j are run variables
                         *      i for sortedArray
                         *      j for unsortedArray
                         */
                        int i = 0;
                        int j = 0;


                        entries.add(new BarEntry(0f, sortedWeekFuel[0]));
                        entries.add(new BarEntry(1f, sortedWeekFuel[1]));
                        entries.add(new BarEntry(2f, sortedWeekFuel[2]));
                        entries.add(new BarEntry(3f, sortedWeekFuel[3]));
                        entries.add(new BarEntry(4f, sortedWeekFuel[4]));
                        entries.add(new BarEntry(5f, sortedWeekFuel[5]));
                        entries.add(new BarEntry(6f, sortedWeekFuel[6]));



                    } else if (selTime == 1) {
                        startTime = currentTime.minusYears(7);
                        endTime = currentTime.minusYears(1).with(TemporalAdjusters.lastDayOfYear());
                        Ride[] unsortedMonthFuel = Ride.getRidesBetweenDates(distance, startTime, endTime);
                        float[] sortedMonthFuel = new float[]{0, 0, 0, 0, 0, 0, 0};

                        /**
                         * i and j are run variables
                         *      i for sortedArray
                         *      j for unsortedArray
                         */
                        int i = 0;
                        int j = 0;


                        entries.add(new BarEntry(0f, sortedMonthFuel[0]));
                        entries.add(new BarEntry(1f, sortedMonthFuel[1]));
                        entries.add(new BarEntry(2f, sortedMonthFuel[2]));
                        entries.add(new BarEntry(3f, sortedMonthFuel[3]));
                        entries.add(new BarEntry(4f, sortedMonthFuel[4]));
                        entries.add(new BarEntry(5f, sortedMonthFuel[5]));
                        entries.add(new BarEntry(6f, sortedMonthFuel[6]));

                    } else if (selTime == 2) {
                        startTime = currentTime.minusYears(7);
                        endTime = currentTime.minusYears(1).with(TemporalAdjusters.lastDayOfYear());
                        Ride[] unsortedYearFuel = Ride.getRidesBetweenDates(distance, startTime, endTime);
                        float[] sortedYearFuel = new float[]{0, 0, 0, 0, 0, 0, 0};

                        /**
                         * i and j are run variables
                         *      i for sortedArray
                         *      j for unsortedArray
                         */
                        int i = 0;
                        int j = 0;


                        entries.add(new BarEntry(0f, sortedYearFuel[0]));
                        entries.add(new BarEntry(1f, sortedYearFuel[1]));
                        entries.add(new BarEntry(2f, sortedYearFuel[2]));
                        entries.add(new BarEntry(3f, sortedYearFuel[3]));
                        entries.add(new BarEntry(4f, sortedYearFuel[4]));
                        entries.add(new BarEntry(5f, sortedYearFuel[5]));
                        entries.add(new BarEntry(6f, sortedYearFuel[6]));
                    }

                    /********************************* Start Cost ********************************************/
                } else if (selValue == 2) {
                    costs = new Vector(Arrays.asList(vehicle.getRefuels()));
                    if (selTime == 0) {
                        startTime = currentTime.minusDays(6);
                        endTime = currentTime;
                        Refuel[] unsortedWeekCost = Refuel.getRefuelsBetweenDates(costs, startTime, endTime);
                        float[] sortedWeekCost = new float[]{0, 0, 0, 0, 0, 0, 0};

                        /**
                         * i and j are run variables
                         *      i for sortedArray
                         *      j for unsortedArray
                         */
                        int i = 0;
                        int j = 0;

                        /**
                         * Checking, if entry exists
                         * if startDate != first entry of unsortedArray, set sortedArray at position 0 = 0
                         * continue while unsortedArray date = date in selected range or end the end is reached
                         */
                        if (unsortedWeekCost[0].getCreationDate() != startTime) {
                            j++;
                            i++;
                            while (unsortedWeekCost[i].getCreationDate() == startTime.plusDays(i) && i < 7) {
                                if (unsortedWeekCost[i].getCreationDate() != startTime.plusDays(i)) {
                                    j++;
                                    i++;
                                }
                            }
                        }

                        /**
                         * first while = Array Size of 7
                         * second while = Array Size of all rides in time period
                         *
                         * first if = checking, if first run
                         * second if = checking if actual entry is same date as the entry before
                         *      if it is the same date, mileage adding to sorted array
                         *      if it is not the same date, i++ and break second while to continue with increased run variable for i
                         */
                        while (i < 7) {
                            while (j < unsortedWeekCost.length) {
                                if (j == 0) {
                                    sortedWeekCost[0] = unsortedWeekCost[0].cost;
                                    j++;
                                    continue;
                                }

                                if (unsortedWeekCost[j].getCreationDate() == unsortedWeekCost[j - 1].getCreationDate()) {
                                    sortedWeekCost[i] += unsortedWeekCost[j].cost;
                                    j++;

                                }

                                if (unsortedWeekCost[j].getCreationDate() != unsortedWeekCost[j - 1].getCreationDate()) {
                                    i++;
                                    break;
                                }
                            }
                        }

                        entries.add(new BarEntry(0f, sortedWeekCost[0]));
                        entries.add(new BarEntry(1f, sortedWeekCost[1]));
                        entries.add(new BarEntry(2f, sortedWeekCost[2]));
                        entries.add(new BarEntry(3f, sortedWeekCost[3]));
                        entries.add(new BarEntry(4f, sortedWeekCost[4]));
                        entries.add(new BarEntry(5f, sortedWeekCost[5]));
                        entries.add(new BarEntry(6f, sortedWeekCost[6]));

                    } else if (selTime == 1) {
                        startTime = currentTime.minusYears(6);
                        endTime = currentTime;
                        Refuel[] unsortedMonthCost = Refuel.getRefuelsBetweenDates(costs, startTime, endTime);
                        float[] sortedMonthCost = new float[]{0, 0, 0, 0, 0, 0, 0};

                        /**
                         * i and j are run variables
                         *      i for sortedArray
                         *      j for unsortedArray
                         */
                        int i = 0;
                        int j = 0;

                        /**
                         * Checking, if entry exists
                         * if startDate != first entry of unsortedArray, set sortedArray at position 0 = 0
                         * continue while unsortedArray date = date in selected range or end the end is reached
                         */
                        if (unsortedMonthCost[0].getCreationDate() != startTime) {
                            j++;
                            i++;
                            while (unsortedMonthCost[i].getCreationDate() == startTime.plusDays(i) && i < 7) {
                                if (unsortedMonthCost[i].getCreationDate() != startTime.plusDays(i)) {
                                    j++;
                                    i++;
                                }
                            }
                        }

                        /**
                         * first while = Array Size of 7
                         * second while = Array Size of all rides in time period
                         *
                         * first if = checking, if first run
                         * second if = checking if actual entry is same date as the entry before
                         *      if it is the same date, mileage adding to sorted array
                         *      if it is not the same date, i++ and break second while to continue with increased run variable for i
                         */
                        while (i < 7) {
                            while (j < unsortedMonthCost.length) {
                                if (j == 0) {
                                    sortedMonthCost[0] = unsortedMonthCost[0].cost;
                                    j++;
                                    continue;
                                }

                                if (unsortedMonthCost[j].getCreationDate() == unsortedMonthCost[j - 1].getCreationDate()) {
                                    sortedMonthCost[i] += unsortedMonthCost[j].cost;
                                    j++;

                                }

                                if (unsortedMonthCost[j].getCreationDate() != unsortedMonthCost[j - 1].getCreationDate()) {
                                    i++;
                                    break;
                                }
                            }
                        }

                        entries.add(new BarEntry(0f, sortedMonthCost[0]));
                        entries.add(new BarEntry(1f, sortedMonthCost[1]));
                        entries.add(new BarEntry(2f, sortedMonthCost[2]));
                        entries.add(new BarEntry(3f, sortedMonthCost[3]));
                        entries.add(new BarEntry(4f, sortedMonthCost[4]));
                        entries.add(new BarEntry(5f, sortedMonthCost[5]));
                        entries.add(new BarEntry(6f, sortedMonthCost[6]));


                    } else if (selTime == 2) {
                        startTime = currentTime.minusYears(7);
                        endTime = currentTime.minusYears(1).with(TemporalAdjusters.lastDayOfYear());
                        Refuel[] unsortedYearCost = Refuel.getRefuelsBetweenDates(costs, startTime, endTime);
                        float[] sortedYearCost = new float[]{0, 0, 0, 0, 0, 0, 0};

                        /**
                         * i and j are run variables
                         *      i for sortedArray
                         *      j for unsortedArray
                         */
                        int i = 0;
                        int j = 0;

                        /**
                         * Checking, if entry exists
                         * if startDate != first entry of unsortedArray, set sortedArray at position 0 = 0
                         * continue while unsortedArray date = date in selected range or end the end is reached
                         */
                        if (unsortedYearCost[0].getCreationDate() != startTime) {
                            j++;
                            i++;
                            while (unsortedYearCost[i].getCreationDate() == startTime.plusDays(i) && i < 7) {
                                if (unsortedYearCost[i].getCreationDate() != startTime.plusDays(i)) {
                                    j++;
                                    i++;
                                }
                            }
                        }

                        /**
                         * first while = Array Size of 7
                         * second while = Array Size of all rides in time period
                         *
                         * first if = checking, if first run
                         * second if = checking if actual entry is same date as the entry before
                         *      if it is the same date, mileage adding to sorted array
                         *      if it is not the same date, i++ and break second while to continue with increased run variable for i
                         */
                        while (i < 7) {
                            while (j < unsortedYearCost.length) {
                                if (j == 0) {
                                    sortedYearCost[0] = unsortedYearCost[0].cost;
                                    j++;
                                    continue;
                                }

                                if (unsortedYearCost[j].getCreationDate() == unsortedYearCost[j - 1].getCreationDate()) {
                                    sortedYearCost[i] += unsortedYearCost[j].cost;
                                    j++;

                                }

                                if (unsortedYearCost[j].getCreationDate() != unsortedYearCost[j - 1].getCreationDate()) {
                                    i++;
                                    break;
                                }
                            }
                        }

                        entries.add(new BarEntry(0f, sortedYearCost[0]));
                        entries.add(new BarEntry(1f, sortedYearCost[1]));
                        entries.add(new BarEntry(2f, sortedYearCost[2]));
                        entries.add(new BarEntry(3f, sortedYearCost[3]));
                        entries.add(new BarEntry(4f, sortedYearCost[4]));
                        entries.add(new BarEntry(5f, sortedYearCost[5]));
                        entries.add(new BarEntry(6f, sortedYearCost[6]));
                    }

                    /********************************* Start Emissions ********************************************/
                } else if (selValue == 3) {
                    int emissions = vehicle.co2emissions;
                    if (selTime == 0) {
                        startTime = currentTime.minusDays(6);
                        endTime = currentTime;
                        Ride[] unsortedWeekEmissions = Ride.getRidesBetweenDates(distance, startTime, endTime);
                        float[] sortedWeekEmissions = new float[]{0, 0, 0, 0, 0, 0, 0};


                        /**
                         * i and j are run variables
                         *      i for sortedArray
                         *      j for unsortedArray
                         */
                        int i = 0;
                        int j = 0;

                        /**
                         * Checking, if entry exists
                         * if startDate != first entry of unsortedArray, set sortedArray at position 0 = 0
                         * continue while unsortedArray date = date in selected range or end the end is reached
                         */
                        if (unsortedWeekEmissions[0].getCreationDateTime().isAfter(startTime)) {
                            j++;
                            i++;
                            while (unsortedWeekEmissions[i].getCreationDateTime().isBefore(startTime.plusDays(i)) && i < 7) {
                                if (unsortedWeekEmissions[i].getCreationDateTime() != startTime.plusDays(i)) {
                                    j++;
                                    i++;
                                }
                            }
                        }

                        /**
                         * first while = Array Size of 7
                         * second while = Array Size of all rides in time period
                         *
                         * first if = checking, if first run
                         * second if = checking if actual entry is same date as the entry before
                         *      if it is the same date, mileage adding to sorted array
                         *      if it is not the same date, i++ and break second while to continue with increased run variable for i
                         */
                        while (i < 7) {
                            while (j < unsortedWeekEmissions.length) {
                                if (j == 0) {
                                    sortedWeekEmissions[0] = (float) unsortedWeekEmissions[0].mileAge;
                                    j++;
                                    continue;
                                }

                                if (unsortedWeekEmissions[j].getCreationDateTime() == unsortedWeekEmissions[j - 1].getCreationDateTime()) {
                                    sortedWeekEmissions[i] = sortedWeekEmissions[i] + (float) unsortedWeekEmissions[j].mileAge;
                                    sortedWeekEmissions[i] *= emissions;
                                    j++;

                                }

                                if (unsortedWeekEmissions[j].getCreationDateTime() != unsortedWeekEmissions[j - 1].getCreationDateTime()) {
                                    i++;
                                    break;
                                }
                            }
                        }

                        entries.add(new BarEntry(0f, sortedWeekEmissions[0]));
                        entries.add(new BarEntry(1f, sortedWeekEmissions[1]));
                        entries.add(new BarEntry(2f, sortedWeekEmissions[2]));
                        entries.add(new BarEntry(3f, sortedWeekEmissions[3]));
                        entries.add(new BarEntry(4f, sortedWeekEmissions[4]));
                        entries.add(new BarEntry(5f, sortedWeekEmissions[5]));
                        entries.add(new BarEntry(6f, sortedWeekEmissions[6]));

                    } else if (selTime == 1) {
                        startTime = currentTime.minusMonths(7);
                        endTime = currentTime;
                        Ride[] unsortedMonthEmissions = Ride.getRidesBetweenDates(distance, startTime, endTime);
                        float[] sortedMonthEmissions = new float[]{0, 0, 0, 0, 0, 0, 0};

                        /**
                         * i and j are run variables
                         *      i for sortedArray
                         *      j for unsortedArray
                         */
                        int i = 0;
                        int j = 0;

                        /**
                         * Checking, if entry exists
                         * if startDate != first entry of unsortedArray, set sortedArray at position 0 = 0
                         * continue while unsortedArray date = date in selected range or end the end is reached
                         */
                        if (unsortedMonthEmissions[0].getCreationDateTime() != startTime) {
                            j++;
                            i++;
                            while (unsortedMonthEmissions[i].getCreationDateTime() == startTime.plusDays(i) && i < 7) {
                                if (unsortedMonthEmissions[i].getCreationDateTime() != startTime.plusDays(i)) {
                                    j++;
                                    i++;
                                }
                            }
                        }

                        /**
                         * first while = Array Size of 7
                         * second while = Array Size of all rides in time period
                         *
                         * first if = checking, if first run
                         * second if = checking if actual entry is same date as the entry before
                         *      if it is the same date, mileage adding to sorted array
                         *      if it is not the same date, i++ and break second while to continue with increased run variable for i
                         */
                        while (i < 7) {
                            while (j < unsortedMonthEmissions.length) {
                                if (j == 0) {
                                    sortedMonthEmissions[0] = (float) unsortedMonthEmissions[0].mileAge;
                                    j++;
                                    continue;
                                }

                                if (unsortedMonthEmissions[j].getCreationDateTime() == unsortedMonthEmissions[j - 1].getCreationDateTime()) {
                                    sortedMonthEmissions[i] = sortedMonthEmissions[i] + (float) unsortedMonthEmissions[j].mileAge;
                                    sortedMonthEmissions[i] *= emissions;
                                    j++;

                                }

                                if (unsortedMonthEmissions[j].getCreationDateTime() != unsortedMonthEmissions[j - 1].getCreationDateTime()) {
                                    i++;
                                    break;
                                }
                            }
                        }

                        entries.add(new BarEntry(0f, sortedMonthEmissions[0]));
                        entries.add(new BarEntry(1f, sortedMonthEmissions[1]));
                        entries.add(new BarEntry(2f, sortedMonthEmissions[2]));
                        entries.add(new BarEntry(3f, sortedMonthEmissions[3]));
                        entries.add(new BarEntry(4f, sortedMonthEmissions[4]));
                        entries.add(new BarEntry(5f, sortedMonthEmissions[5]));
                        entries.add(new BarEntry(6f, sortedMonthEmissions[6]));

                    } else if (selTime == 2) {
                        startTime = currentTime.minusYears(6);
                        endTime = currentTime;
                        Ride[] unsortedYearEmissions = Ride.getRidesBetweenDates(distance, startTime, endTime);
                        float[] sortedYearEmissions = new float[]{0, 0, 0, 0, 0, 0, 0};

                        /**
                         * i and j are run variables
                         *      i for sortedArray
                         *      j for unsortedArray
                         */
                        int i = 0;
                        int j = 0;

                        /**
                         * Checking, if entry exists
                         * if startDate != first entry of unsortedArray, set sortedArray at position 0 = 0
                         * continue while unsortedArray date = date in selected range or end the end is reached
                         */
                        if (unsortedYearEmissions[0].getCreationDateTime() != startTime) {
                            j++;
                            i++;
                            while (unsortedYearEmissions[i].getCreationDateTime() == startTime.plusDays(i) && i < 7) {
                                if (unsortedYearEmissions[i].getCreationDateTime() != startTime.plusDays(i)) {
                                    j++;
                                    i++;
                                }
                            }
                        }

                        /**
                         * first while = Array Size of 7
                         * second while = Array Size of all rides in time period
                         *
                         * first if = checking, if first run
                         * second if = checking if actual entry is same date as the entry before
                         *      if it is the same date, mileage adding to sorted array
                         *      if it is not the same date, i++ and break second while to continue with increased run variable for i
                         */
                        while (i < 7) {
                            while (j < unsortedYearEmissions.length) {
                                if (j == 0) {
                                    sortedYearEmissions[0] = (float) unsortedYearEmissions[0].mileAge;
                                    j++;
                                    continue;
                                }

                                if (unsortedYearEmissions[j].getCreationDateTime() == unsortedYearEmissions[j - 1].getCreationDateTime()) {
                                    sortedYearEmissions[i] = sortedYearEmissions[i] + (float) unsortedYearEmissions[j].mileAge;
                                    sortedYearEmissions[i] *= emissions;
                                    j++;

                                }

                                if (unsortedYearEmissions[j].getCreationDateTime() != unsortedYearEmissions[j - 1].getCreationDateTime()) {
                                    i++;
                                    break;
                                }
                            }
                        }

                        entries.add(new BarEntry(0f, sortedYearEmissions[0]));
                        entries.add(new BarEntry(1f, sortedYearEmissions[1]));
                        entries.add(new BarEntry(2f, sortedYearEmissions[2]));
                        entries.add(new BarEntry(3f, sortedYearEmissions[3]));
                        entries.add(new BarEntry(4f, sortedYearEmissions[4]));
                        entries.add(new BarEntry(5f, sortedYearEmissions[5]));
                        entries.add(new BarEntry(6f, sortedYearEmissions[6]));
                    }
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
                } else if (selTime == 1) {
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
                } else if (selTime == 2) {
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