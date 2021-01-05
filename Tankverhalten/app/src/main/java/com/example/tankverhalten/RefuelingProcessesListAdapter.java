package com.example.tankverhalten;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tankverhalten.datastructure.Refuel;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class RefuelingProcessesListAdapter extends RecyclerView.Adapter<RefuelingProcessesListAdapter.MyViewHolder> {
    Context mContext;
    Vector<Refuel> mData;
    RefuelingProcessesListAdapter.OnRideListener OnRideListener;

    private LayoutInflater inflater;

    public interface OnRideListener{
        void OnRideListener(int position);
    }

    /** Constructor
     *
     * @param mContext      context
     * @param mData         Vector of Rides
     * @param itemClickListener Listener of Clicking
     */

    public RefuelingProcessesListAdapter(Context mContext, Vector<Refuel> mData, RefuelingProcessesListAdapter.OnRideListener itemClickListener) {
        this.mContext = mContext;
        this.mData = mData;
        this.OnRideListener = itemClickListener;
    }

    /** Created at start
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RefuelingProcessesListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.refuellist, parent, false);
        //MyViewHolder vHolder = new MyViewHolder(v, itemClickListener);
        return new RefuelingProcessesListAdapter.MyViewHolder(v, OnRideListener);
    }

    /** Connects Textview to the data
     *
     * @param holder        holder of this class
     * @param position      nth Row of holder
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RefuelingProcessesListAdapter.MyViewHolder holder, int position) {
        float fuel = mData.get(position).refueled;
        float price = mData.get(position).cost;
        try {
            String fuel_string = Float.toString(fuel);
            String cost_string = Float.toString(price);
            fuel_string += " l(kWh)";
            fuel_string += " | " + cost_string + "€";
            holder.refuelvolume_cost.setText(fuel_string);
        }
        catch (NumberFormatException e) {
            String fuel_string = "";
            holder.refuelvolume_cost.setText(fuel_string);
        }

        try {
            LocalDateTime locald = mData.get(position).getCreationDate();
            DateTimeFormatter germandate = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            LocalTime localt = mData.get(position).getCreationTime();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            String time = localt.format(dtf);

            String formatted = locald.format(germandate);
            formatted += " | " + time;
            holder.refuel_date.setText(formatted);
        }
        catch (NumberFormatException e) {
            String date = "";
            holder.refuel_date.setText(date);
        }
    }

    /**
     * gets mData size
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Row of a Ride.
     * contains a Oncklicklistener
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView refuelvolume_cost;
        RefuelingProcessesListAdapter.OnRideListener onRideListener;
        TextView refuel_date;

        /**
         * Constructor sets the Textview and Clicklistener
         * @param itemView
         * @param OnRideListener
         */
        public MyViewHolder(View itemView, RefuelingProcessesListAdapter.OnRideListener OnRideListener) {
            super(itemView);
            refuelvolume_cost = (TextView) itemView.findViewById(R.id.refuelvolume_cost);
            refuel_date = (TextView) itemView.findViewById(R.id.refueldate);
            this.onRideListener = OnRideListener;
            itemView.setOnClickListener(this);
        }

        /**
         * A OnClicklistener that sends to the Interface wehen clicked
         * @param v
         */
        @Override
        public void onClick(View v) {
            onRideListener.OnRideListener(getAdapterPosition());
        }
    }
}