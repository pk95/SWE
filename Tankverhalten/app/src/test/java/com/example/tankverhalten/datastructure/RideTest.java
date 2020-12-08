package com.example.tankverhalten.datastructure;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;


@RunWith(JUnit4.class)
public class RideTest {


    @Test
    public void equalsTest(){
        Ride r1 = new Ride(10,5,RoadType.CITY);
        Ride r2 = new Ride(10,5,RoadType.CITY);
        assertTrue(r1.equals(r2));
        assertTrue(r2.equals(r1));
    }

    @Test
    public void unequalsTest(){
        Ride r1 = new Ride(10,5,RoadType.CITY);
        Ride r2 = new Ride(11,5,RoadType.CITY);
        assertFalse(r1.equals(r2));
        assertFalse(r2.equals(r1));
    }

    @Test
    public void unequalsTest2(){
        Ride r1 = new Ride(10,5,RoadType.CITY);
        Ride r2 = new Ride(10,6,RoadType.CITY);
        assertFalse(r1.equals(r2));
        assertFalse(r2.equals(r1));
    }

    @Test
    public void unequalsTest3(){
        Ride r1 = new Ride(10,5,RoadType.CITY);
        Ride r2 = new Ride(10,5,RoadType.COUNTRY);
        assertFalse(r1.equals(r2));
        assertFalse(r2.equals(r1));
    }


    @Test
    public void cloneTest(){
        Ride r1 = new Ride(10,5,RoadType.CITY);
        Ride r2 = r1.clone();
        assertTrue(r1!=r2);
        assertEquals(r2.mileAge , r1.mileAge);
        assertEquals(r2.roadType, r1.roadType);
    }


}