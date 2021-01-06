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

import com.example.tankverhalten.datastructure.Ride;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class RecycleViewRidesAdapter extends RecyclerView.Adapter<RecycleViewRidesAdapter.MyViewHolder> {

    Context mContext;
    Vector<Ride> mData;
    OnRideListener OnRideListener;

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

    public RecycleViewRidesAdapter(Context mContext, Vector<Ride> mData, OnRideListener itemClickListener) {
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.rideslist, parent, false);
        //MyViewHolder vHolder = new MyViewHolder(v, itemClickListener);
        return new MyViewHolder(v, OnRideListener);
    }

    /** Connects Textview to the data
     *
     * @param holder        holder of this class
     * @param position      nth Row of holder
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int mileage = mData.get(position).mileAge;
        try {
            String mileagestr = Integer.toString(mileage);
            mileagestr += " km";
            holder.tv_length.setText(mileagestr);
        }
        catch (NumberFormatException e) {
            String mileagestr = "";
            holder.tv_length.setText(mileagestr);
        }

        try {
            LocalDate locald = mData.get(position).getCreationDate();
            DateTimeFormatter germandate = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            LocalTime localt = mData.get(position).getCreationTime();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

            String time = localt.format(dtf);
            String formatted = locald.format(germandate);

            formatted += " | " + time;
            holder.tv_date.setText(formatted);
        }
        catch (NumberFormatException e) {
            String date = "";
            holder.tv_date.setText(date);
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
        OnRideListener onRideListener;
        TextView tv_clock;

        /**
         * Constructor sets the Textview and Clicklistener
         * @param itemView
         * @param OnRideListener
         */
        public MyViewHolder(View itemView, OnRideListener OnRideListener) {
            super(itemView);
            tv_length = (TextView) itemView.findViewById(R.id.mileage);
            tv_date = (TextView) itemView.findViewById(R.id.date);
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
