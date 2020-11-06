package com.example.tankverhalten;

import android.os.Build;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.LocalDate;
import java.util.Vector;


/**
 * Type of vehicle to represent different properties of vehicles.
 * <p>
 * Alternative for an Enum for VehicleTypes.
 * Reason: In Android enums blow up the app, so this is a common way of avoiding DEX space while running.
 * Link to video: https://www.youtube.com/watch?v=Hzs6OBcvNQE
 * <p>
 *
 * @author Stephan de Gavarelli
 * @version 1.0
 * @see java.lang.annotation.Annotation
 */
@IntDef({VehicleType.CAR, VehicleType.MOTORCYCLE, VehicleType.TRANSPORTER})
@Retention(RetentionPolicy.SOURCE)
@interface VehicleType {
    int CAR = 0;
    int MOTORCYCLE = 1;
    int TRANSPORTER = 2;
}


/**
 * Defines a Vehicle. Contains several vehicle specific data
 * Also handles a list of Rides ans Refuels.
 *
 * @author Stephan de Gavarelli
 * @version 1.0
 * @see Ride
 * @see Refuel
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class Vehicle {
    private final LocalDate creationDate = java.time.LocalDate.now();
    int averageConsumption = 0;
    int mileAge = 0;
    float fuelLevel = 100;
    int remainingRange = 0;
    int volume = 20;
    int co2emissions = 0;
    @VehicleType
    int vehicleType = VehicleType.CAR;
    private Vector<Ride> rides;
    private Vector<Refuel> refuels;


    /**
     * Default-Constructor
     */
    public Vehicle() {
        refuels = new Vector<Refuel>();
        rides = new Vector<Ride>();
    }

    /**
     * Constructor with all parameters
     *
     * @param volume             of tank
     * @param co2emissions       while driving
     * @param remainingRange     possible to drive
     * @param mileAge            right now
     * @param fuelLevel          right now
     * @param averageConsumption calculated with data
     */
    public Vehicle(int volume, int co2emissions, int remainingRange, int mileAge, float fuelLevel, int averageConsumption, @VehicleType int vehicleType) {
        this.volume = volume;
        this.co2emissions = co2emissions;
        this.remainingRange = remainingRange;
        this.mileAge = mileAge;
        this.fuelLevel = fuelLevel;
        this.averageConsumption = averageConsumption;
        this.vehicleType = vehicleType;
        rides = new Vector<Ride>();
        refuels = new Vector<Refuel>();
    }

    /**
     * Clones a vehicle
     *
     * @return a new Vehicle with copied values
     */
    @Override
    public Vehicle clone() {
        Vehicle v = new Vehicle(this.volume, this.co2emissions, this.remainingRange, this.mileAge, this.fuelLevel, this.averageConsumption, this.vehicleType);
        v.refuels = new Vector<Refuel>(this.refuels);
        v.rides = new Vector<Ride>(this.rides);
        return v;
    }

    /**
     * Get last ride.
     *
     * @return last Ride
     */
    public Ride getLastRide() {
        return rides.lastElement();
    }

    /**
     * Get last refuel.
     *
     * @return last Refuel
     */
    public Refuel getLastRefuel() {
        return refuels.lastElement();
    }

    /**
     * Adds a ride to the vector rides
     *
     * @param ride to add
     */
    public void add(Ride ride) {
        rides.add(ride);
    }

    /**
     * Adds the refueled percents to this vehicles fuelLevel
     *
     * @param refuel to add
     */
    public void add(Refuel refuel) {
        refuels.add(refuel);
        this.fuelLevel += ((float) refuel.refueled / (float) volume) * 100;
    }

//    /**
//     * Allows changes on last added private decelerated Ride-Vector   !!Right now possible with getLastRide and access to the Ride's attributes
//     *
//     * @param mileAge to take over
//     * @param fuelLevel to take over
//     * @param road to take over
//     */
//    public void setLastRide(int mileAge, float fuelLevel, RoadType road) {
//        Ride r = rides.lastElement();
//        r.road = road;
//        r.mileAge = mileAge;
//        r.fuelLevel = fuelLevel;
//    }
//
//    /**
//     * Allows changes on last added private decelerated Ride-Vector   !!Right now possible with getLastRefuel and access to the Refuel's attributes
//     *
//     * @param refueled     to take over
//     * @param cost         to take over
//     * @param costImageSrc to take over
//     */
//    public void setLastRefuel(int refueled, float cost, String costImageSrc) {
//        Refuel r = refuels.lastElement();
//        r.refueled = refueled;
//        r.cost = cost;
//        r.costImageSrc = costImageSrc;
//    }


    /**
     * Compares two vehicles.
     * If all attributes are identical and vectors contain same content the result is true.
     * Does not check if vectors have same id.
     *
     * @param vehicle to compare with
     * @return boolean true if equal values
     */
    public boolean equals(Vehicle vehicle) {
        boolean sameAttr = this.remainingRange == vehicle.remainingRange && this.volume == vehicle.volume && this.mileAge == vehicle.mileAge && this.fuelLevel == vehicle.fuelLevel && this.co2emissions == vehicle.co2emissions && this.averageConsumption == vehicle.averageConsumption;// && this.creationDate == vehicle.creationDate;
        return (sameAttr && this.rides.equals(vehicle.rides) && this.refuels.equals(vehicle.refuels));
    }

    public LocalDate getCreationDateDate() {
        return creationDate;
    }
}
