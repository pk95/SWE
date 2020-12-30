package com.example.tankverhalten;

import com.example.tankverhalten.datastructure.Refuel;
import com.example.tankverhalten.datastructure.Ride;
import com.example.tankverhalten.datastructure.RoadType;
import com.example.tankverhalten.datastructure.Vehicle;
import com.example.tankverhalten.datastructure.VehicleType;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class VehicleTest extends TestCase {

    Vehicle v = new Vehicle();

    @Test
    public void testVehicleTypeChanges() {
        Vehicle v = new Vehicle("hallo", "dm", 1, 1, 1, 1, 100, 1, 1, 1, VehicleType.CAR);
        // Tests if vehicleType is set up right and value is readable as int and VehicleType
        assertEquals(0, v.vehicleType);
        assertEquals(VehicleType.CAR, v.vehicleType);

        // Tests if vehicleType can be set by int
        v.vehicleType = 1;
        assertEquals(1, v.vehicleType);
        assertEquals(VehicleType.MOTORCYCLE, v.vehicleType);

        // Tests if vehicleType can be set by VehicleType-definition
        v.vehicleType = VehicleType.TRANSPORTER;
        assertEquals(2, v.vehicleType);
        assertEquals(VehicleType.TRANSPORTER, v.vehicleType);
    }

    /**
     * Tests, whether a refuel results in a correct fuelLevel.
     */
    @Test
    public void testAddRefuel() {
        Vehicle v = new Vehicle();
        v.volume = 10;
        v.fuelLevel = 0;

        Refuel r = new Refuel(5, 5, "");
        v.add(r);
        assertEquals((float) 50, v.fuelLevel);
    }

    @Test
    public void testChangeRide() {
        Vehicle v = new Vehicle();
        v.add(new Ride(5, 1000, RoadType.CITY));
        assertEquals(5, v.getLastRide().mileAge);
        assertEquals((float) 1000, v.getLastRide().fuelLevel);
        assertEquals(RoadType.CITY, v.getLastRide().roadType);
    }

    @Test
    public void testChangeRefuel() {
        v.add(new Refuel(5, 10, "Test"));
        assertEquals((float) 5, v.getLastRefuel().refueled);
        assertEquals((float) 10, v.getLastRefuel().cost);
        assertEquals("Test", v.getLastRefuel().costImageSrc);
    }


    /**
     * Tests, whether vehicles are equal, when cloned
     */
    @Test
    public void testEqualVehicles() {
        Vehicle v1 = new Vehicle("hallo", "dm", 1, 1, 1, 1, 100, 1, 1, 1, VehicleType.CAR);
        Vehicle v2 = v1;
        assertTrue(v2.equals(v2));

        Vehicle v3 = v1.clone();
        assertTrue(v3.equals(v1));
    }

    @Test
    public void testUnequalVehicles() {
        Vehicle v1 = new Vehicle("hallo", "dm", 1, 1, 1, 1, 100, 1, 1, 1, VehicleType.CAR);
        Vehicle v2 = v1.clone();
        v1.mileAge = 1000000;
        assertFalse(v1.equals(v2));
    }

    //Does getLastRefuel can return something(null), if empty vector
    @Test
    public void testGetLastRefuel_emptyCase() {
        Vehicle v1 = new Vehicle();
        assertEquals(null, v1.getLastRefuel());
    }

    //Does getLastRide can return something(null), if empty vector
    @Test
    public void testGetLastRide_emptyCase() {
        Vehicle v1 = new Vehicle();
        assertEquals(null, v1.getLastRide());
    }

    @Test
    public void testCalcRemainingRange_simple() {
        // e.g. 40l volume, 5l/100km ^= max 800km, 8km per fueLevel percent
        Vehicle v1 = new Vehicle("test","dk",40,2,100,1000,100,5,4,6,VehicleType.CAR);
        v1.add(new Ride(1000,50, RoadType.COUNTRY));
        v1.add(new Ride(1160,30, RoadType.COUNTRY));    // 20 percent / 160km ^= 8 /160km = 5/100km
        v1.add(new Ride(1360,5, RoadType.CITY));        // 25 percent / 200km ^= 10/200km = 5/100km

        assertEquals(Math.round(100* (5f*40/100f) / 5), v1.calcRemainingRange());
    }


    @Test
    public void testCalcRemainingRange_simple2() {
        // e.g. 40l volume, 5l/100km ^= max 800km, 8km per fueLevel percent
        Vehicle v1 = new Vehicle("test","dk",60,2,100,1000,100,5,4,6,VehicleType.CAR);
        v1.add(new Ride(400,40, RoadType.COUNTRY));
        v1.add(new Ride(600,20, RoadType.COUNTRY));
        v1.add(new Ride(650,15, RoadType.CITY));

        assertEquals(Math.round(100* (15*60/100f) / 7),4, v1.calcRemainingRange());
    }

    @Test
    public void testCalcRemainingRange_complex() {
        // e.g. 40l volume, 5l/100km ^= max 800km, 8km per fueLevel percent
        Vehicle v1 = new Vehicle("test","dk",40,2,100,1000,100,5,4,6,VehicleType.CAR);
        v1.add(new Ride(1000,50, RoadType.COUNTRY));
        v1.add(new Ride(1160,30, RoadType.COUNTRY));    // 20 percent / 160km ^= 8 /160km = 5/100km
        v1.add(new Refuel(20,20,""));           // fuelLevel = 50
        v1.add(new Ride(1360,5, RoadType.CITY));        // 45 percent / 200km ^= 18/200km = 9/100km
        //mixed = 7/100km

        assertEquals(Math.round(100* (5*40/100f) / 7),4, v1.calcRemainingRange());
    }

    @Test
    public void testCalcRemainingRange_complex2() {
        // e.g. 40l volume, 5l/100km ^= max 800km, 8km per fueLevel percent
        Vehicle v1 = new Vehicle("test","dk",35,2,100,1000,100,5,4,6,VehicleType.CAR);
        v1.add(new Ride(100,50, RoadType.COUNTRY));
        v1.add(new Ride(200,30, RoadType.COUNTRY)); // 20pc / 100 = 7/100
        v1.add(new Refuel(20,20,""));
        v1.add(new Refuel(10,10,""));   //60%
        v1.add(new Ride(400, 10, RoadType.CITY));    //  50pc /200km = 17,5/200km = 8,75/100km
        //mixed = 7,875/100km

        assertEquals(Math.round(100* (10*35/100f) / 7.875),4, v1.calcRemainingRange());
    }
}