package com.example.tankverhalten.datastructure;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RefuelTest extends TestCase {

    @Test
    public void equalsTest(){
        Refuel r1 = new Refuel(1,2,"");
        Refuel r2 = new Refuel(1,2,"");
        assertEquals(r1,r2);
    }



}