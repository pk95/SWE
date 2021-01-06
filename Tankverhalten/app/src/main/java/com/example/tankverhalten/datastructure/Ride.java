package com.example.tankverhalten.datastructure;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;


/**
 * Contains data about a ride of a {@link Vehicle}.
 *
 * @author Stephan de Gavarelli
 * @version 1.0
 * @see Vehicle
 * @see LocalDate
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class Ride implements Serializable {
    private final LocalDate creationDate = LocalDate.now();
    private final Date dateCreation = Date.from(Instant.now());
    private final LocalTime creationTime = LocalTime.now();
    @RoadType
    public int roadType = RoadType.COMBINED;
    public int mileAge = 0;
    public float fuelLevel = 0;


    /**
     * Default-Constructor
     * <p>
     * Defaultvalues are:
     * roadType = combined
     * mileAge = 0
     * fueLLevel = 0
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


    /**
     * Returns all rides of a vehicle, which are between a start and an end LocalTimeDate
     *
     * @param rides
     * @param startDate
     * @param endDate
     * @return refuels
     */
    public static Ride[] getRidesBetweenDates(Vector<Ride> rides, LocalDateTime startDate, LocalDateTime endDate) {
        //Collects all rides between two dates
        Vector<Ride> ridesBetween = new Vector<>();
        for (Ride r : rides) {
            //is Date between this two dates
            if (!r.getCreationDateTime().isBefore(startDate) && !r.getCreationDateTime().isAfter(endDate))
                ridesBetween.add(r);
        }
        return ridesBetween.toArray(new Ride[0]);
    }


    /**
     * Camparison of Ride with this Ride.
     * Returns true, if Rides data matches.
     *
     * @param o to compare
     * @return bool ,whether objects are equal
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
     * @return Ride to clone
     */
    public Ride clone() {
        return new Ride(this.mileAge, this.fuelLevel, this.roadType);
    }


    /**
     * Returns the creationDate.
     *
     * @return LocalDate of the moment this was constructed
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }


    /**
     * returns the creationTime
     *
     * @return LocalTime of creation
     */
    public LocalTime getCreationTime() {
        return creationTime;
    }


    /**
     * Converts creationDate and time to one LocalDateTime
     *
     * @return LocalDaateTime of the moment this was constructed
     */
    public LocalDateTime getCreationDateTime() {
        return LocalDateTime.of(creationDate, creationTime);
    }

    /**
     *
     * @return Date of the moment this was constructed
     */
    public Date getDateCreation() {
        return dateCreation;
    }
}