package com.example.tankverhalten.datastructure;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.Objects;


/**
 * Contains data about a ride of a {@link Vehicle}.
 *
 * @author Stephan de Gavarelli
 * @version 1.0
 * @see Vehicle
 * @see LocalDate
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class Ride {
    private final LocalDate creationDate = LocalDate.now();
    @RoadType
    public int roadType = RoadType.COMBINED;
    public int mileAge = 0;
    public float fuelLevel = 0;

    /**
     * Default-Constructor
     */
    public Ride() {
    }

    /**
     * Constructor with all data initialization
     *
     * @param mileAge   new mileAge to take over
     * @param fuelLevel new fuelLevel to take over
     * @param roadType  new roadType to take over
     */
    public Ride(int mileAge, float fuelLevel, @RoadType int roadType) {
        this.fuelLevel = fuelLevel;
        this.mileAge = mileAge;
        this.roadType = roadType;
    }

//    public void change(Ride ride){
//        this.fuelLevel = ride.fuelLevel;
//        this.mileAge = ride.mileAge;
//        this.road = ride.road;
//    }

    /**
     * Camparison of Ride with this Ride.
     * Returns true, if Rides data matches.
     *
     * @param o to compare
     * @return bool if true
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ride ride = (Ride) o;
        return roadType == ride.roadType &&
                mileAge == ride.mileAge &&
                Float.compare(ride.fuelLevel, fuelLevel) == 0 &&
                Objects.equals(creationDate, ride.creationDate);
    }

    /**
     * Clones a Ride to a new Ride with equal data
     *
     * @return Ride
     */
    public Ride clone() {
        return new Ride(this.mileAge, this.fuelLevel, this.roadType);
    }

    /**
     * Returns the creationDate.
     *
     * @return LocalDate of creation
     */
    public LocalDate getCreationDateDate() {
        return creationDate;
    }

}