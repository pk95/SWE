package com.example.tankverhalten;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tankverhalten.datastructure.Vehicle;

import java.util.Vector;

/**
 * Contains Rows of a Vehicle.
 * Generates a RowHolder for each Vehicle
 *
 * @see RecyclerView.Adapter
 */
public class RecyclerviewVehiclesAdapter extends RecyclerView.Adapter<RecyclerviewVehiclesAdapter.RowHolder> {

    private final Context context;
    private final OnVehicleListener mOnVehicleListener;
    Vector<Vehicle> vehicles;
    int rows = 0;


    /**
     * Constructs a Recyclerview that creates Rows for the vehicles
     *
     * @param ct                context
     * @param vehicles          Vector of vehicles to create Rows for
     * @param onVehicleListener Listener for clickHandling
     */
    public RecyclerviewVehiclesAdapter(Context ct, Vector<Vehicle> vehicles, OnVehicleListener onVehicleListener) {
        this.context = ct;
        this.vehicles = vehicles;
        this.mOnVehicleListener = onVehicleListener;
    }


    /**
     * When this class is constructing this method is called.
     * It creates a RowHolder this class handles.
     *
     * @param parent
     * @param viewType
     * @return RowHolder a Row of a Vehicle
     * @see RecyclerviewVehiclesAdapter|
     * @see RecyclerView.Adapter
     */
    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.vehicle_mainmenue_button, parent, false);
        return new RowHolder(view, mOnVehicleListener);
    }

    /**
     * When a Row is created, the Activity is connected to this RecycleView.
     * Connects RowHolder variables with Activity-ids.
     *
     * @param holder   is the RowHolder of this class
     * @param position the nth Row of the RowHolder
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
        holder.vehicleName_txt.setText(vehicles.elementAt(position).name);
        if (vehicles.elementAt(position).getDrawIdOfVehicleType(context) != 0)
//            holder.vehicleButton_img.setImageResource( context.getResources().getIdentifier(vehicles.elementAt(position).getIconName(),"drawable",context.getPackageName() ));
            holder.vehicleButton_img.setImageResource(vehicles.elementAt(position).getDrawIdOfVehicleType(context));
//            holder.vehicleButton_img.setImageResource(R.drawable.ic_car_black);
        else
            holder.vehicleButton_img.setImageResource(R.drawable.ic_launcher_background);
        rows++;
    }

    /**
     * @return the amount of Rows this ReycleView's RowHolder contains
     */
    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    /**
     * Interface that starts an onCLick on the Row-element
     */
    public interface OnVehicleListener {
        void onVehicleClick(int position);
    }

    /**
     * Row of a Vehicle.
     * Contains an Activity, that represents a Vehicle.
     * Contains a OnVehicleListener, that calls the OnClick-Event of the Activity.
     */
    public static class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vehicleName_txt;
        ImageView vehicleButton_img;
        OnVehicleListener onVehicleListener;

        /**
         * Constructor.
         * Contains variables to address the Activity-ids and allow changes in Activity
         * After construction uses the onBindViewHolder.
         *
         * @param itemView          Viewelement/Activity
         * @param onVehicleListener Listener of the Row
         */
        public RowHolder(@NonNull View itemView, OnVehicleListener onVehicleListener) {
            super(itemView);
            vehicleName_txt = (TextView) itemView.findViewById(R.id.vehicleName);
            vehicleButton_img = (ImageView) itemView.findViewById(R.id.vehicleIcon);


            // When this Row is clicked, trigger a onClick-Event on the Activity
            this.onVehicleListener = onVehicleListener;
            itemView.setOnClickListener(this);
        }

        /**
         * A onClick of this RecylceView is send to the Row-element's interface onVehicleClick
         *
         * @param view Row-element clicked on
         */
        @Override
        public void onClick(View view) {
            onVehicleListener.onVehicleClick(getAdapterPosition());
        }
    }
}