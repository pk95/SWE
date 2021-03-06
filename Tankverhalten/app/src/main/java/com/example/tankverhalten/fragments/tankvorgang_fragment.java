package com.example.tankverhalten.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tankverhalten.R;
import com.example.tankverhalten.RecyclerviewVehiclesAdapter;
import com.example.tankverhalten.RefuelingProcessesListAdapter;
import com.example.tankverhalten.activities.AddVehicleActivity;
import com.example.tankverhalten.activities.GarageActivity;
import com.example.tankverhalten.activities.RefuelingProcessesActivity;
import com.example.tankverhalten.datastructure.Refuel;
import com.example.tankverhalten.datastructure.Vehicle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Vector;

import static com.example.tankverhalten.activities.GarageActivity.vehicleData;

public class tankvorgang_fragment extends Fragment implements RefuelingProcessesListAdapter.OnRideListener {

    FloatingActionButton fab_main, fab_add, fab_edit;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    boolean isOpen = false;
    TextView tv_add, tv_edit;


    View view;
    private RecyclerView myrecyclerview;
    private RefuelingProcessesListAdapter myviewadapter;
    RecyclerView.LayoutManager mLayoutmanager;
    private Vector<Refuel> refuels;
    Activity activity;
    Vehicle v;
    public tankvorgang_fragment() {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        view = inflater.inflate(R.layout.tankvorgang_layout, container, false);

        int pos = -1;

        v = new Vehicle();
        if(!GarageActivity.vehicleData.isEmpty()) {
            try {
                pos = GarageActivity.vehicleData.getInt("pos");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(pos <0) {
            getActivity().finish();
        }
        else {
            v = GarageActivity.vehicles.get(pos);
        }

        refuels = new Vector(Arrays.asList(v.getRefuels()));


        myrecyclerview = (RecyclerView) view.findViewById(R.id.refuels_recycleview);
        mLayoutmanager = new LinearLayoutManager(getActivity());
        myrecyclerview.setLayoutManager(mLayoutmanager);
        myviewadapter = new RefuelingProcessesListAdapter(getActivity(), refuels, this);
        myrecyclerview.setAdapter(myviewadapter);

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL);
        myrecyclerview.addItemDecoration(divider);

        fab_main = (FloatingActionButton) view.findViewById(R.id.fab_expand);
        fab_add = (FloatingActionButton) view.findViewById(R.id.fab_add);
        fab_edit = (FloatingActionButton) view.findViewById(R.id.fab_editlast);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_rotate_anticlock);

        tv_add = (TextView) view.findViewById(R.id.tv_add);
        tv_edit = (TextView) view.findViewById(R.id.tv_edit);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen) {

                    tv_add.setVisibility(View.INVISIBLE);
                    tv_edit.setVisibility(View.INVISIBLE);
                    fab_add.startAnimation(fab_close);
                    fab_edit.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab_add.setClickable(false);
                    fab_edit.setClickable(false);
                    isOpen = false;
                }
                else {
                    tv_add.setVisibility(View.VISIBLE);
                    tv_edit.setVisibility(View.VISIBLE);
                    fab_add.startAnimation(fab_open);
                    fab_edit.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab_add.setClickable(true);
                    fab_edit.setClickable(true);
                    isOpen = true;
                }
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), RefuelingProcessesActivity.class);
                intent.putExtra("com.example.tankverhalten.mode", "new");
                startActivity(intent);
            }
        });
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editintent = new Intent(getActivity().getApplicationContext(), RefuelingProcessesActivity.class);
                editintent.putExtra("com.example.tankverhalten.mode", "edit");
                startActivity(editintent);
            }
        });
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

            // Refresh tab data:

            if(getFragmentManager() != null) {

                getFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .attach(this)
                        .commit();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void OnRideListener(int position) {
        //rideData.putInt("pos", position);

        //Intent intent = new Intent(this, );
        //startActivity(intent);
    }
}
