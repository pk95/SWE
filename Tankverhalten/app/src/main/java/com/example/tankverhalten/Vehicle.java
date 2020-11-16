package com.example.tankverhalten;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.Vector;

import static android.content.Context.MODE_PRIVATE;


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
    LocalDate inspection = null;
    LocalDate permission = null;
    String name = "";
    String licensePlate = "";
    int vehicleType = VehicleType.CAR;
    int averageConsumption = 0;
    int mileAge = 0;
    int remainingRange = 0;
    int volume = 0;
    float fuelLevel = 100;
    float co2emissions = 0;
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
    public Vehicle(String name, String licensePlate, int volume, float co2emissions, int remainingRange, int mileAge, float fuelLevel, int averageConsumption, @VehicleType int vehicleType) {
        this.name = name;
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
     * Loads a Vector of Article.
     * The file is used from the app's directory /file
     * <p>
     * Trys to load a existing file, else create a new Vector
     *
     * @param context Activity starts this procedure
     * @return Vector<Vehicle> that contains all Vehicles with data
     */
    @SuppressWarnings("unchecked")
    public static Vector<Vehicle> load(Context context) {
        FileInputStream fis = null;
        ObjectInputStream os = null;
        Vector<Vehicle> articles = null;
        try {
            Log.d("File", "File found in: " + context.getFilesDir().getAbsoluteFile().toString());
            fis = context.openFileInput("Articles.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Vector<Vehicle>();
        }
        try {
            os = new ObjectInputStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
            articles = new Vector<Vehicle>();
        }
        if (os != null) {
            try {
                articles = (Vector<Vehicle>) os.readObject();
                Log.d("File", "File not found in: " + context.getFilesDir().getAbsoluteFile().toString());
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                articles = new Vector<Vehicle>();
            }
        }
        return articles;
    }

    /**
     * Try to save a Vector of Vehicles in a file.
     *
     * @param articles Vector of Article to save
     * @param context  Activity that calls the function
     */
    public static void save(Vector<Vehicle> articles, Context context) {
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = context.openFileOutput("Articles.txt", MODE_PRIVATE);
            Log.d("File", context.getFilesDir().getAbsoluteFile().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            os = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (os != null) {
            try {
                os.writeObject(articles);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Clones a vehicle
     *
     * @return a new Vehicle with copied values
     */
    @Override
    public Vehicle clone() {
        Vehicle v = new Vehicle(this.name, this.licensePlate, this.volume, this.co2emissions, this.remainingRange, this.mileAge, this.fuelLevel, this.averageConsumption, this.vehicleType);
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
        this.fuelLevel += (refuel.refueled / (float) volume) * 100;
    }

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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @param context
     * @return int for Resscource
     */
    public int getDrawIdOfVehicleType(Context context) {
        int icon =0;
        switch (this.vehicleType) {
            case VehicleType.CAR:
                icon = R.drawable.ic_car_white;
                break;
            case VehicleType.MOTORCYCLE:
                icon = R.drawable.ic_motorcycle_white;
                break;
            case VehicleType.TRANSPORTER:
                icon = R.drawable.ic_car_white;
        }
        return icon;
//        final int resourceId = context.getResources().getIdentifier(resourceName, "drawable", null);
//        return ResourcesCompat.getDrawable(context.getResources(),resourceId,null) ;
    }
}
