package com.example.tankverhalten;

import android.os.Build;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.LocalDate;

/**
 * Defines the type of used road.
 * Can take the states: city, combined or country.
 *
 * @author Stephan de Gavarelli
 * @version 1.0
 * @see Ride
 */
//enum RoadType {
//    CITY,
//    COMBINED,
//    COUNTRY
//}

@IntDef({RoadType.COMBINED, RoadType.CITY, RoadType.COUNTRY})
@Retention(RetentionPolicy.SOURCE)
@interface RoadType {
    int COMBINED = 0;
    int CITY = 1;
    int COUNTRY = 2;
}


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
    int road = RoadType.COMBINED;
    int mileAge = 0;
    float fuelLevel = 0;

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
     * @param road      new roadType to take over
     */
    public Ride(int mileAge, float fuelLevel, @RoadType int road) {
        this.fuelLevel = fuelLevel;
        this.mileAge = mileAge;
        this.road = road;
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
     * @param ride Ride to compare with
     * @return bool if true
     */
    public boolean equals(Ride ride) {
        return (ride.road == this.road && ride.mileAge == this.mileAge && ride.fuelLevel == this.fuelLevel && ride.creationDate == this.creationDate);
    }

    /**
     * Clones a Ride to a new Ride with equal data
     *
     * @param ride clone Ride
     * @return bool
     */
    public Ride clone(Ride ride) {
        return new Ride(ride.mileAge, ride.fuelLevel, ride.road);
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