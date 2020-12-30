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
}