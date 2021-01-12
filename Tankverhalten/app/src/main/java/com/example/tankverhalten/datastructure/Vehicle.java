package com.example.tankverhalten.datastructure;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.tankverhalten.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
public class Vehicle implements Serializable {
    private final LocalDate creationDate = java.time.LocalDate.now();
    public LocalDate inspection = null;
    public LocalDate permission = null;
    public String name = "";
    public String licensePlate = "";
    public int vehicleType = VehicleType.CAR;
    public float urbanConsumption = 0;
    public float outsideConsumption = 0;
    public float combinedConsumption = 0;

    public int mileAge = 0;
    public int remainingRange = 0;
    public int volume = 0;
    public float fuelLevel = 100;
    public int co2emissions = 0;

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
     * * Constructor with all parameters
     * *
     * * @param volume              of tank
     * * @param co2emissions        while driving
     * * @param remainingRange      possible to drive
     * * @param mileAge             right now
     * * @param fuelLevel           right now
     * * @param combinedConsumption calculated with data
     *
     * @param name                of vehicle in software
     * @param licensePlate        of real vehicle
     * @param volume              maximal of tank
     * @param co2emissions        while driving
     * @param mileAge             since last ride
     * @param fuelLevel           actual level of tank
     * @param urbanConsumption    of manufacturer
     * @param outsideConsumption  of manufacturer
     * @param combinedConsumption of manufacturer
     * @param vehicleType
     */
    public Vehicle(String name, String licensePlate, int volume, int co2emissions, int mileAge, float fuelLevel, float urbanConsumption, float outsideConsumption, float combinedConsumption, @VehicleType int vehicleType) {
        this.name = name;
        this.licensePlate = licensePlate;
        this.volume = volume;
        this.co2emissions = co2emissions;
        this.remainingRange = remainingRange;
        this.mileAge = mileAge;
        this.fuelLevel = fuelLevel;
        this.urbanConsumption = urbanConsumption;
        this.outsideConsumption = outsideConsumption;
        this.combinedConsumption = combinedConsumption;
        this.vehicleType = vehicleType;
        rides = new Vector<Ride>();
        refuels = new Vector<Refuel>();
        calcRemainingRange();
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
        Vector<Vehicle> vehicles = null;
        try {
            Log.d("File", "File found in: " + context.getFilesDir().getAbsoluteFile().toString());
            fis = context.openFileInput("Vehicles.txt");
            os = new ObjectInputStream(fis);
            if (os != null) {
                vehicles = (Vector<Vehicle>) os.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            vehicles = new Vector<Vehicle>();
        }
        if (vehicles == null) {
            vehicles = new Vector<Vehicle>();
            Vehicle.save(vehicles, context);
        }
        return vehicles;
    }


    /**
     * Try to save a Vector of Vehicles in a file.
     *
     * @param vehicles Vector of Article to save
     * @param context  Activity that calls the function
     */
    public static void save(Vector<Vehicle> vehicles, Context context) {
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = context.openFileOutput("Vehicles.txt", MODE_PRIVATE);
            Log.d("File", context.getFilesDir().getAbsoluteFile().toString());
            os = new ObjectOutputStream(fos);
            os.writeObject(vehicles);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("File", "File not saved");
        }
    }


    /**
     * Return the average of float values in a FLoat-Vector
     *
     * @param vector
     * @return avergae value
     */
    private static float getAverage(Vector<Float> vector) {
        float r = 0;
        for (float f : vector)
            r += f;
        if (vector.size() > 0)
            return r / vector.size();
        else
            return 0;
    }

    /**
     * Calculates averageCosts of vehicle's rides and refuels.
     * The rides until a next refuels are categorized by RoatTypes. So each roadType has an average consumption at the end.
     * Calculates averageConsumption, averageCosts, refuelbreaks and emissions
     * <p>
     * The calculation needs two rides to compare usages.
     * If the vehicles has less wides,the calculator uses the manufactorer information for calculation
     * <p>
     *
     * @param distance      in kilometres
     * @param urbanRatio    in percent
     * @param combinedRatio in percent
     * @param outsideRatio  in percent
     * @return a float[4] with [averageConsumption, price, refuelBreaks, emissions]
     */
    public float[] getPrediction(int distance, int urbanRatio, int combinedRatio, int outsideRatio, float literprice) {
        //Prices per 100km
        Vector<Float> urbanPricePer100km = new Vector<Float>();
        Vector<Float> combinedPricePer100Km = new Vector<Float>();
        Vector<Float> outsidePricePer100Km = new Vector<Float>();

        //Liters per 100km
        Vector<Float> urbanLPer100km = new Vector<Float>();
        Vector<Float> combinedLPer100km = new Vector<Float>();
        Vector<Float> outsideLPer100km = new Vector<Float>();


        //Kilometres of RoadTypes, order = [urban,combined,outside]
        int[] roadTypesKilometres = new int[3];

        int refuelEntities = this.refuels.size();
        float pricePerLiter = literprice;
        Refuel lastRefuel;
        Refuel refuel;
        Ride[] ridesSinceLastRefuel;

//        float averageCost = 0;
        float averageConsumption = 0;

        if (refuelEntities > 1) {
            //initialize
            lastRefuel = this.refuels.elementAt(0);

            for (int i = 1; i < refuelEntities; i++) {
                refuel = this.refuels.elementAt(i);

                //get refuels since lastRefuel
                ridesSinceLastRefuel = Ride.getRidesBetweenDates(this.rides, lastRefuel.getCreationDate(), refuel.getCreationDate());
                int milesSinceLastRefuel = 0;
                int urbanKilometresSinceLastRefuel = 0;
                int outsideKilometresSinceLastRefuel = 0;
                int combinedKilometresSinceLastRefuel = 0;

                boolean containsUrban = false;
                boolean containsCombined = false;
                boolean containsOutside = false;


                //Get all miles since last refuel
                for (Ride ride : ridesSinceLastRefuel) {
                    milesSinceLastRefuel += ride.mileAge;
                }

                //categorize miles of rides by RoadTypes
                for (Ride ride : ridesSinceLastRefuel) {
                    if (ride.roadType == RoadType.CITY) {
                        urbanKilometresSinceLastRefuel += ride.mileAge;
                        containsUrban = true;
                    } else if (ride.roadType == RoadType.COMBINED) {
                        combinedKilometresSinceLastRefuel += ride.mileAge;
                        containsCombined = true;
                    } else if (ride.roadType == RoadType.COUNTRY) {
                        outsideKilometresSinceLastRefuel += ride.mileAge;
                        containsOutside = true;
                    }
                }
                //sum all kilometres of each roadType
                //urban, combined, outside
                roadTypesKilometres[0] += urbanKilometresSinceLastRefuel;
                roadTypesKilometres[1] += combinedKilometresSinceLastRefuel;
                roadTypesKilometres[2] += outsideKilometresSinceLastRefuel;


                //Are data missing ? -> use manufacturer information
                if (urbanPricePer100km.size() == 0)
                    urbanPricePer100km.add(this.urbanConsumption * pricePerLiter);
                if (combinedPricePer100Km.size() == 0)
                    combinedPricePer100Km.add(this.combinedConsumption * pricePerLiter);
                if (outsidePricePer100Km.size() == 0)
                    outsidePricePer100Km.add(this.outsideConsumption * pricePerLiter);

                //Add Prices
                // Formula: Price/100km * (roadTypeKilometres/KilometresSinceLastRefuel)
                if (containsUrban)
                    urbanPricePer100km.add(refuel.cost / 100 * urbanKilometresSinceLastRefuel / milesSinceLastRefuel);
                if (containsCombined)
                    outsidePricePer100Km.add(refuel.cost / 100 * combinedKilometresSinceLastRefuel / milesSinceLastRefuel);
                if (containsOutside)
                    combinedPricePer100Km.add(refuel.cost / 100 * outsideKilometresSinceLastRefuel / milesSinceLastRefuel);
            }
            //Calc whole cost of all roadTypes by ratios
//            averageCost += getAverage(urbanPricePer100km) * distance / 100f * (urbanRatio / 100f) * pricePerLiter;
//            averageCost += getAverage(urbanPricePer100km) * distance / 100f * (urbanRatio / 100f) * pricePerLiter;
//            averageCost += getAverage(combinedPricePer100Km) * distance / 100f * (combinedRatio / 100f) * pricePerLiter;

            //Calc whole consumption
            // Formular ^= averagae = 4l/100km * 1000km * (30/100)
            // code     ^=  avergae =  4 * (100km/100km) * (30/100)
            averageConsumption += getAverage(urbanLPer100km) * (distance / 100f) * (urbanRatio / 100f);
            averageConsumption += getAverage(combinedLPer100km) * (distance / 100f) * (combinedRatio / 100f);
            averageConsumption += getAverage(outsideLPer100km) * (distance / 100f) * (outsideRatio / 100f);

        } else {
            // Not even two refuels to compare with-> use manufacturer information
//            averageCost += this.urbanConsumption * distance / 100 * urbanRatio / 100 * pricePerLiter;
//            averageCost += this.outsideConsumption * distance / 100 * outsideRatio / 100 * pricePerLiter;
//            averageCost += this.combinedConsumption * distance / 100 * combinedRatio / 100 * pricePerLiter;
        }
        if (averageConsumption == 0) {
            //averageConsumption of manufacturer information proportionately to roadType-ratio
            averageConsumption += this.urbanConsumption * urbanRatio / 100;
            averageConsumption += this.combinedConsumption * combinedRatio / 100;
            averageConsumption += this.outsideConsumption * outsideRatio / 100;
        }
        float emission = distance * this.co2emissions;

        //Calc refuelBreaks
        float kilometresToDrive = (float) distance;
        int refuelBreaks = 0;
        //check if remaining fuel is enough
        kilometresToDrive -= (fuelLevel / 100 * volume) / averageConsumption * 100;
        //then check how much complete fuel volumes are also needed
        while (kilometresToDrive > 0) {
            ++refuelBreaks;
            kilometresToDrive -= volume / averageConsumption * 100;
        }

        //round value to 2 digits after dot
        return new float[]{averageConsumption, Math.round(averageConsumption*pricePerLiter * 100f) / 100f, refuelBreaks, emission};
    }


    /**
     * Calculates a 'liter per 100km' value of this vehicle
     *
     * @param kilometre
     * @param fuelDifference
     * @return value of liters per 100 km
     */
    public float lPer100km(int kilometre, float fuelDifference) {
        return ((fuelDifference) / 100 * volume / (kilometre / 100f));
    }

    /**
     * Clones a vehicle
     *
     * @return a new Vehicle with copied values
     */
    @Override
    public Vehicle clone() {
        Vehicle v = new Vehicle(this.name, this.licensePlate, this.volume, this.co2emissions, this.mileAge, this.fuelLevel, this.urbanConsumption, this.outsideConsumption, this.combinedConsumption, this.vehicleType);
        v.refuels = new Vector<Refuel>(this.refuels);
        v.rides = new Vector<Ride>(this.rides);
        return v;
    }

    /**
     * Get last ride.
     * If rides is empty, nulll is returned.
     *
     * @return last Ride
     */
    public Ride getLastRide() {
        if (rides.size() <= 0)
            return null;
        return rides.lastElement();
    }

    public Ride[] getRides() {
//        Ride[] rides = new Ride[this.rides.size()];
//        for (int i = 0; i < this.rides.size(); i++) {
//            rides[i] = this.rides.elementAt(i).clone();
//        }
        return this.rides.toArray(new Ride[0]);
    }


    /**
     * Get last refuel.
     * If refuels is empty, nulll is returned.
     *
     * @return last Refuel.
     */
    public Refuel getLastRefuel() {
        if (refuels.size() <= 0)
            return null;
        return refuels.lastElement();
    }

    /**
     * A copied array of refuels.
     * Changes of refuels are not done at the vehicle.
     *
     * @return refuels
     */
    public Refuel[] getRefuels() {
//        Refuel[] refuels = new Refuel[this.refuels.size()];
//        for (int i = 0; i < this.refuels.size(); i++) {
//            refuels[i] = this.refuels.elementAt(i).clone();
//        }
//        return refuels;
        return this.refuels.toArray(new Refuel[0]);
    }

    /**
     * Adds a ride to the vector rides
     *
     * @param ride to add
     */
    public void add(Ride ride) {
        rides.add(ride);
        this.mileAge += ride.mileAge;
        this.fuelLevel = ride.fuelLevel;
        calcRemainingRange();
    }

    /**
     * Adds the refueled percents to this vehicles fuelLevel
     *
     * @param refuel to add
     */
    public void add(Refuel refuel) {
        refuels.add(refuel);
        this.fuelLevel += (refuel.refueled / (float) volume) * 100;
        if (this.fuelLevel > 100)
            this.fuelLevel = 100;
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
        boolean sameAttr = this.remainingRange == vehicle.remainingRange && this.volume == vehicle.volume && this.mileAge == vehicle.mileAge && this.fuelLevel == vehicle.fuelLevel && this.co2emissions == vehicle.co2emissions && this.combinedConsumption == vehicle.combinedConsumption;// && this.creationDate == vehicle.creationDate;
        return (sameAttr && this.rides.equals(vehicle.rides) && this.refuels.equals(vehicle.refuels));
    }

    /**
     * @return LocalDate of creation this vehicle
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }


    /**
     * Get id of icon for R.id.###
     *
     * @param context
     * @return int for Resscource
     */
    public int getDrawIdOfVehicleType(Context context) {
        int icon = 0;
        switch (this.vehicleType) {
            case VehicleType.CAR:
                icon = R.mipmap.ic_car2_foreground;
                break;
            case VehicleType.MOTORCYCLE:
                icon = R.mipmap.ic_motorcycle2_foreground;
                break;
            case VehicleType.TRANSPORTER:
                icon = R.mipmap.ic_transporter2_foreground;
        }
        return icon;
//        final int resourceId = context.getResources().getIdentifier(resourceName, "drawable", null);
//        return ResourcesCompat.getDrawable(context.getResources(),resourceId,null) ;
    }


    /**
     * Updates remainingRange.
     * If rides have at least two ride to compare, the method calculates remainingRange from vehicle's rides and refuels.
     * If less the method calculates remainingRange from manufacturer information.
     */
    public void calcRemainingRange() {
        float litersPer100km = 0; // liters per 100km
        int entries = this.rides.size();

        //values while look-through rides
        Ride ride = null;
        Ride lastRide = null;
        Refuel[] refuelsBetween = null;

        float refuelDifference = 0;
        float rideProportion = 0;


        if (entries > 1) {
            //compares pairs of rides (actual and last ride)
            lastRide = this.rides.elementAt(0);
            for (int i = 1; i < entries; i++) {
                //Get next Ride
                ride = this.rides.elementAt(i);

                // Get refuels between rides to get all fuelLevel changes.
                // e.g fuelLevel between two rides could be 50-> 45 but a big refuel between was not considered (50 -> 90 -> 45)
                refuelsBetween = Refuel.getRefuelsBetweenDates(this.refuels, lastRide.getCreationDateTime(), ride.getCreationDateTime());
                refuelDifference = 0;
                for (Refuel r : refuelsBetween) {
                    refuelDifference += r.refueled / volume;
                }

                //calculate km per fuelLevel
                // if no division with zero
                if (ride.mileAge - lastRide.mileAge != 0)
                    //e.g. (2100 -2000) / ( 60 + 0 - 40 )
                    rideProportion = (lastRide.fuelLevel + refuelDifference - ride.fuelLevel) / 100 * volume / ((ride.mileAge - lastRide.mileAge) / 100f);
//                    rideProportion = (ride.mileAge - lastRide.mileAge) / (lastRide.fuelLevel + refuelDifference - ride.fuelLevel);
                litersPer100km += rideProportion;

                //this ride will be the next lastRide to compare with
                lastRide = ride;
            }
            litersPer100km /= entries - 1;
        } else {
            litersPer100km = (combinedConsumption + urbanConsumption + outsideConsumption) / 3;
        }
        //
        this.remainingRange = Math.round((this.fuelLevel / 100) * this.volume / litersPer100km * 100);
    }
}
