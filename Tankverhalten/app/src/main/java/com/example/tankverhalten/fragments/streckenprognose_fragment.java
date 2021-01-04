package com.example.tankverhalten.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.tankverhalten.R;
import com.example.tankverhalten.activities.GarageActivity;
import com.example.tankverhalten.datastructure.Vehicle;

public class streckenprognose_fragment extends Fragment {

    View view;
    Vehicle vehicle;

    //inputs
    int distance = 0;
    int urbanRatio = 0;
    int combinedRatio = 0;
    int outsideRatio = 0;

    //outputs
    float consumption;
    float price;
    int refuelBreaks;
    float emission;

    public streckenprognose_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.streckenprognose_layout, container, false);

        //Get actual vehicle
        int pos = -1;
        if (!GarageActivity.vehicleData.isEmpty()) {
            try {
                pos = GarageActivity.vehicleData.getInt("pos");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pos >= 0) {
            vehicle = GarageActivity.vehicles.elementAt(pos);
        }

        return view;
    }

    /*
        Hier kommt die Streckenprognose Activity hin.
    */

    //Click on button -> do prediction()



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void prediction() {
        float[] prediction = vehicle.getPrediction(distance,urbanRatio,combinedRatio,outsideRatio);
        consumption = prediction[0];
        price = prediction[1];
        refuelBreaks = (int) prediction[2];
        emission = prediction[3];
    }
}
