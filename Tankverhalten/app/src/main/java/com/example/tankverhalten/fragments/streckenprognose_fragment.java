package com.example.tankverhalten.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.tankverhalten.R;
import com.example.tankverhalten.activities.GarageActivity;
import com.example.tankverhalten.datastructure.Vehicle;
import com.gregacucnik.EditableSeekBar;

import java.text.DecimalFormat;

public class streckenprognose_fragment extends Fragment {

    View view;
    Vehicle vehicle;
    boolean inputError = false;

    //variables in
    int distance = 0;
    int urbanRatio = 0;
    int combinedRatio = 0;
    int outsideRatio = 0;

    //variables out
    float consumption;
    float price;
    int refuelBreaks;
    float emission;

    //layout views
    //fieldsDescriptions
    TextView distanceFieldDescription;
    TextView urbanFieldDescription;
    TextView combinedFieldDescription;
    TextView outsideFieldDescription;

    //Inputs
    EditText distanceField;
    EditableSeekBar urbanField;
    EditableSeekBar combinedField;
    EditableSeekBar outsideField;

    //Results
    TextView consumptionReport;
    TextView costReport;
    TextView refuelsReport;
    TextView emissionReport;

    Button calcPredictionBtn;

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
        } else {
            getActivity().finish();
        }

        //Assign viewElementDescription
        distanceFieldDescription = (TextView) view.findViewById(R.id.ride_length_stat);
        urbanFieldDescription = (TextView) view.findViewById(R.id.ride_type_innercity);
        combinedFieldDescription = (TextView) view.findViewById(R.id.ride_type_combined);
        outsideFieldDescription = (TextView) view.findViewById(R.id.ride_type_outercity);

        //Assign viewElements InputFields
        distanceField = (EditText) view.findViewById(R.id.ride_length_stat_edit);
        urbanField = (EditableSeekBar) view.findViewById(R.id.ride_type_innercity_edit);
        combinedField = (EditableSeekBar) view.findViewById(R.id.ride_type_combined_edit);
        outsideField = (EditableSeekBar) view.findViewById(R.id.ride_type_outercity_edit);

        //Assign viewElements Results
        consumptionReport = (TextView) view.findViewById(R.id.used_gas_calc);
        costReport = (TextView) view.findViewById(R.id.used_gas_cost_calc);
        refuelsReport = (TextView) view.findViewById(R.id.number_refueling_calc);
        emissionReport = (TextView) view.findViewById(R.id.co2emissions_calc);

        //Interaction with Buttons
        calcPredictionBtn = view.findViewById(R.id.confirm_calculating);
        calcPredictionBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View view) {
                inputError = false;

                //check inputFields
                if (distanceField.length() > 0)
                    markOk(distanceField, distanceFieldDescription);
                else
                    markError(distanceField, distanceFieldDescription);

                //No input errors? -> process inputs
                if (!inputError) {
                    try {
                        //Get textInput into variableInputs
                        distance = Integer.parseInt(distanceField.getText().toString());
                        //urbanRatio = Integer.parseInt(urbanField.toString());
                        //combinedRatio = Integer.parseInt(combinedField.toString());
                        //outsideRatio = Integer.parseInt(outsideField.toString());
                        urbanRatio = urbanField.getValue();
                        combinedRatio = combinedField.getValue();
                        outsideRatio = outsideField.getValue();

                        //Values out of range? -> break
                        if (distance <= 0 ||
                                (urbanRatio < 0 || urbanRatio > 100) ||
                                (combinedRatio < 0 || combinedRatio > 100) ||
                                (outsideRatio < 0 || outsideRatio > 100)
                        )
                            return;


                        if (urbanRatio + combinedRatio + outsideRatio == 100) {
                            //Calc results
                            prediction();

                            //write text to show results
                            DecimalFormat consumptionDf = new DecimalFormat();
                            consumptionDf.setMaximumFractionDigits(1);
                            consumptionReport.setText(consumptionDf.format(consumption).replace('.', ','));

                            DecimalFormat priceDf = new DecimalFormat();
                            priceDf.setMaximumFractionDigits(2);
                            costReport.setText(priceDf.format(price).replace('.', ','));

                            refuelsReport.setText(String.valueOf(refuelBreaks));

                            DecimalFormat emissionDf = new DecimalFormat();
                            emissionDf.setMaximumFractionDigits(0);
                            emissionReport.setText(emissionDf.format(Math.round(emission)).replace('.', ','));
                        } else {
                            //Mark rideRatios as error/markError(urbanField, urbanFieldDescription);
                            //markError(combinedField, combinedFieldDescription);
                            //markError(outsideField, outsideFieldDescription);
                            //Show advice-message
                            Toast.makeText(getActivity(), "% müssen zusammen 100% ergeben", Toast.LENGTH_LONG).show();
                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        return view;
    }

    //Click on button -> do prediction()


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void prediction() {
        float[] prediction = vehicle.getPrediction(distance, urbanRatio, combinedRatio, outsideRatio);
        consumption = prediction[0];
        price = prediction[1];
        refuelBreaks = (int) prediction[2];
        emission = prediction[3];
        Log.d("Prediction", "Prediction done");
    }

    //marks a Input-fields as declined
    private void markError(EditText input, TextView description) {
        inputError = true;
        description.setError("erforderlich");
        description.setTextColor(Color.RED);
    }

    //marks a Input-fields as accepted
    private void markOk(EditText input, TextView description) {
        description.setTextColor(Color.BLACK);
        description.setError(null);
    }
}
