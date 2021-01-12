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
                int urbanKilometresSinceLastRefuel = 0;
                int outsideKilometresSinceLastRefuel = 0;
                int combinedKilometresSinceLastRefuel = 0;

                int urbanConsumedFuelLevel = 0;
                int combinedConsumedFuelLevel = 0;
                int outsideonsumedFuelLevel = 0;


                boolean containsUrban = false;
                boolean containsCombined = false;
                boolean containsOutside = false;

                Ride lastRide = null;
                //categorize miles of rides by RoadTypes
                if (ridesSinceLastRefuel.length > 0) {
                    for (Ride ride : ridesSinceLastRefuel) {
                        //At start need old mileAge before Refuel
                        float fuelDifference = 0;
                        int kilometresDifference = 0;
                        if (lastRide == null) {
                            //Get lastRide's fuelLevel before refueled
                            lastRide = Ride.getLastRideOfRideUntil(rides, lastRefuel.getCreationDate());
                            fuelDifference = (lastRide.fuelLevel + lastRefuel.refueled) - ride.fuelLevel;
                        } else
                            fuelDifference = lastRide.fuelLevel - ride.fuelLevel;

                        if (fuelDifference > 0) {
                            if (ride.roadType == RoadType.CITY) {
                                urbanKilometresSinceLastRefuel += ride.mileAge;
                                urbanConsumedFuelLevel += fuelDifference;
                                containsUrban = true;
                            } else if (ride.roadType == RoadType.COMBINED) {
                                combinedKilometresSinceLastRefuel += ride.mileAge;
                                combinedConsumedFuelLevel += fuelDifference;
                                containsCombined = true;
                            } else if (ride.roadType == RoadType.COUNTRY) {
                                outsideKilometresSinceLastRefuel += ride.mileAge;
                                outsideonsumedFuelLevel += fuelDifference;
                                containsOutside = true;
                            }
                        }

                        lastRide = ride;
                    }
                    //sum all kilometres of each roadType
                    //urban, combined, outside
                    roadTypesKilometres[0] += urbanKilometresSinceLastRefuel;
                    roadTypesKilometres[1] += combinedKilometresSinceLastRefuel;
                    roadTypesKilometres[2] += outsideKilometresSinceLastRefuel;

                    int kilometresSinceLastRefuel = roadTypesKilometres[0] + roadTypesKilometres[1] + roadTypesKilometres[2];

                    //Add Prices
                    // Formula: Price/100km * (roadTypeKilometres/KilometresSinceLastRefuel)
                    if (containsUrban) {
                        // fuellDifference = (kilometresPart / allKilometres) * (refueled / max fuel)
                        urbanLPer100km.add(lPer100km(roadTypesKilometres[0], urbanConsumedFuelLevel));
//                    urbanPricePer100km.add(refuel.cost / 100 * urbanKilometresSinceLastRefuel / milesSinceLastRefuel);
                    }
                    if (containsCombined) {
                        //(roadTypesKilometres[1] / kilometresSinceLastRefuel) * (refuel.refueled / this.volume) * 100)
                        combinedLPer100km.add(lPer100km(roadTypesKilometres[1], combinedConsumedFuelLevel));
//                    outsidePricePer100Km.add(refuel.cost / 100 * combinedKilometresSinceLastRefuel / milesSinceLastRefuel);
                    }
                    if (containsOutside) {
                        outsideLPer100km.add(lPer100km(roadTypesKilometres[2], outsideonsumedFuelLevel));
//                    combinedPricePer100Km.add(refuel.cost / 100 * outsideKilometresSinceLastRefuel / milesSinceLastRefuel);
                    }
                }
            }

            //Calc whole consumption
            // Formular ^= averagae = 4l/100km * 1000km * (30/100)
            // code     ^=  avergae =  4 * (100km/100km) * (30/100)
            if (urbanLPer100km.size() > 0)
                averageConsumption += getAverage(urbanLPer100km) * (distance / 100f) * (urbanRatio / 100f);
            else
                averageConsumption += this.urbanConsumption * urbanRatio / 100;
            if (combinedLPer100km.size() > 0)
                averageConsumption += getAverage(combinedLPer100km) * (distance / 100f) * (combinedRatio / 100f);
            else
                averageConsumption += this.combinedConsumption * combinedRatio / 100;
            if (outsideLPer100km.size() > 0)
                averageConsumption += getAverage(outsideLPer100km) * (distance / 100f) * (outsideRatio / 100f);
            else
                averageConsumption += this.outsideConsumption * outsideRatio / 100;
        }
        //ErrorCase
        if (averageConsumption <= 0) {
            //averageConsumption of manufacturer information proportionately to roadType-ratio
            float newAvergaeConsumption = 0;
            newAvergaeConsumption += this.urbanConsumption * urbanRatio / 100;
            newAvergaeConsumption += this.combinedConsumption * combinedRatio / 100;
            newAvergaeConsumption += this.outsideConsumption * outsideRatio / 100;
            averageConsumption = newAvergaeConsumption;
        }
        float emission = distance * this.co2emissions;

        //Calc refuelBreaks
        float kilometresToDrive = (float) distance;
        int refuelBreaks = 0;
        //check if remaining fuel is enough
        kilometresToDrive -= (fuelLevel / 100 * volume) / averageConsumption * 100;
        //then check how much complete fuel volumes are also needed
        int maxRefuelBReaks = 0;
        while (kilometresToDrive > 0 && maxRefuelBReaks++ < 100) {
            ++refuelBreaks;
            kilometresToDrive -= volume / averageConsumption * 100;
        }

        //round value to 2 digits after dot
        return new float[]{averageConsumption, Math.round(averageConsumption * pricePerLiter * 100f) / 100f, refuelBreaks, emission};
    }


    /**
     * Calculates a 'liter per 100km' value of this vehicle
     *
     * @param kilometre
     * @param fuelDifference in % like 80% for 40l/50l
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
                    refuelDifference += (r.refueled / volume) * 100;
                }

                //calculate km per fuelLevel
                // if no division with zero
                if (ride.mileAge > 0)
                    //e.g. (2100 -2000) / ( 60 + 0 - 40 )
                    rideProportion = (lastRide.fuelLevel + refuelDifference - ride.fuelLevel) / 100 * volume / (ride.mileAge / 100f);
//                    rideProportion = (ride.mileAge - lastRide.mileAge) / (lastRide.fuelLevel + refuelDifference - ride.fuelLevel);
                litersPer100km += rideProportion;

                //this ride will be the next lastRide to compare with
                lastRide = ride;
            }
            litersPer100km /= entries - 1;
        } else {
            litersPer100km = (combinedConsumption + urbanConsumption + outsideConsumption) / 3f;
        }
        //
        this.remainingRange = Math.round((this.fuelLevel / 100f) * this.volume / litersPer100km * 100);
    }
}
