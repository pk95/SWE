package com.example.tankverhalten.datastructure;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class RefuelTest extends TestCase {

    @Test
    public void equalsTest(){
        Refuel r1 = new Refuel(1,2,"");
        Refuel r2 = new Refuel(1,2,"");
        assertEquals(r1,r2);
    }

    @Test
    public void testGetRefuelsBetweenDates() throws InterruptedException {
        Vector<Refuel> refuels = new Vector<>();
        refuels.add(new Refuel(10,10,"") );
        TimeUnit.SECONDS.sleep(1);
        //start here
        refuels.add(new Refuel(10,10,"") );
        TimeUnit.SECONDS.sleep(1);
        refuels.add(new Refuel(10,10,"") );
        TimeUnit.SECONDS.sleep(1);
        refuels.add(new Refuel(10,10,"") );
        //until here
        TimeUnit.SECONDS.sleep(1);
        refuels.add(new Refuel(10,10,"") );
        TimeUnit.SECONDS.sleep(1);
        refuels.add(new Refuel(10,10,"") );

       assertEquals(3,Refuel.getRefuelsBetweenDates(refuels, refuels.elementAt(1).getCreationDate(), refuels.elementAt(3).getCreationDate()).length );
    }



}