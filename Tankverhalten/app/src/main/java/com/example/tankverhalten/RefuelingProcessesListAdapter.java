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
        v = inflater.inflate(R.layout.refueling, parent, false);
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
        float refueled = mData.get(position).refueled;
        try {
            String mileagestr = Float.toString(refueled);
            mileagestr += " km";
            holder.tv_length.setText(mileagestr);
        }
        catch (NumberFormatException e) {
            String mileagestr = "";
            holder.tv_length.setText(mileagestr);
        }

        try {
            LocalDateTime locald = mData.get(position).getCreationDate();
            DateTimeFormatter germandate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formatted = locald.format(germandate);
            holder.tv_date.setText(formatted);
        }
        catch (NumberFormatException e) {
            String date = "";
            holder.tv_date.setText(date);
        }
        try{
            LocalTime localt = mData.get(position).getCreationTime();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            String time = localt.format(dtf);
            holder.tv_clock.setText(time);
        }
        catch (NumberFormatException e) {
            String time = "";
            holder.tv_clock.setText(time);
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

        TextView tv_length;
        TextView tv_date;
        RefuelingProcessesListAdapter.OnRideListener onRideListener;
        TextView tv_clock;

        /**
         * Constructor sets the Textview and Clicklistener
         * @param itemView
         * @param OnRideListener
         */
        public MyViewHolder(View itemView, RefuelingProcessesListAdapter.OnRideListener OnRideListener) {
            super(itemView);
            tv_length = (TextView) itemView.findViewById(R.id.mileage);
            tv_date = (TextView) itemView.findViewById(R.id.date);
            tv_clock = (TextView) itemView.findViewById(R.id.clock);
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