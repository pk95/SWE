package com.example.tankverhalten.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tankverhalten.R;
import com.example.tankverhalten.RecycleViewRidesAdapter;
//import com.example.tankverhalten.activities.AddRideActivity;
import com.example.tankverhalten.activities.AddVehicleActivity;
import com.example.tankverhalten.datastructure.Ride;
import com.example.tankverhalten.datastructure.RoadType;
import com.example.tankverhalten.datastructure.Vehicle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.Vector;
import com.example.tankverhalten.activities.GarageActivity;

import static com.example.tankverhalten.activities.GarageActivity.vehicleData;


public class gefahrene_strecke_fragment extends Fragment implements RecycleViewRidesAdapter.OnRideListener{

    FloatingActionButton fab;
    View view;
    private RecyclerView myrecyclerview;
    private RecycleViewRidesAdapter myviewadapter;
    RecyclerView.LayoutManager mLayoutmanager;
    private Vector<Ride> rides;
    public static Bundle rideData = new Bundle();
    Vehicle v;
    public gefahrene_strecke_fragment() {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.gefahrene_strecken_layout, container, false);


        myrecyclerview = (RecyclerView) view.findViewById(R.id.rides_recycleview);
        mLayoutmanager = new LinearLayoutManager(getActivity());
        myrecyclerview.setLayoutManager(mLayoutmanager);
        myviewadapter = new RecycleViewRidesAdapter(getActivity(), rides, this);
        myrecyclerview.setAdapter(myviewadapter);

        fab = (FloatingActionButton) view.findViewById(R.id.fab_addride);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        v = new Vehicle();

        rides = new Vector<>();
        rides.add(new Ride(50, 70, RoadType.COUNTRY ));
        rides.add(new Ride(80, 65, RoadType.COUNTRY ));

    }

    @Override
    public void OnRideListener(int position) {
        rideData.putInt("pos", position);

        //Intent intent = new Intent(this, );
        //startActivity(intent);
    }
}
