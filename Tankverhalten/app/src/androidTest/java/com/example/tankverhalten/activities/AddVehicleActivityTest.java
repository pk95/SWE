package com.example.tankverhalten.activities;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.tankverhalten.R;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(JUnit4.class)
public class AddVehicleActivityTest extends TestCase {

    //see https://developer.android.com/training/testing/junit-rules
    @Rule
//    public ActivityScenarioRule<AddVehicleActivity> activityRule = new ActivityScenarioRule(AddVehicleActivity.class) ;
    public ActivityScenarioRule<GarageActivity> activityRule = new ActivityScenarioRule(GarageActivity.class);

    @Test
    public void testAddVehicle_addVehicle() {
        //open AddVehicle
        Espresso.onView(withId(R.id.fab)).perform(click());

        //data to type-in
        String name = "TestVehicle";
        String licence = "TEST-V-01";
        String urbanInput= "8.2";
        float urban = (float) 8.2;
        float outside = (float) 3.1;
        float combined =(float) 5.1;
        int mileage = 2000;
        int fuelLevel = 86;
        int volume = 35;
        int emission = 160;

        //type in all parameters
        Espresso.onView(withId(R.id.display_name_vehicle_edit)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.display_name_vehicle_edit)).perform(typeText(name));

        Espresso.onView(withId(R.id.license_plate_edit_vehicle)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.license_plate_edit_vehicle)).perform(typeText(licence));

        Espresso.onView(withId(R.id.consumption_urban_vehicle_edit)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.consumption_urban_vehicle_edit)).perform(typeText(urbanInput));

        Espresso.onView(withId(R.id.consumption_outside_vehicle_edit)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.consumption_outside_vehicle_edit)).perform(typeText(String.valueOf(outside)));

        Espresso.onView(withId(R.id.consumption_combined_vehicle_edit)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.consumption_combined_vehicle_edit)).perform(typeText(String.valueOf(combined)));

        Espresso.onView(withId(R.id.mileage_vehicle_edit)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.mileage_vehicle_edit)).perform(typeText(String.valueOf(mileage)));

        Espresso.onView(withId(R.id.fuel_level_vehicle_edit)).perform(ViewActions.scrollTo());
        Espresso.onView(withId(R.id.fuel_level_vehicle_edit)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.fuel_level_vehicle_edit)).perform(typeText(String.valueOf(fuelLevel)));

        Espresso.onView(withId(R.id.tank_volume_vehicle_edit)).perform(ViewActions.scrollTo());
        Espresso.onView(withId(R.id.tank_volume_vehicle_edit)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.tank_volume_vehicle_edit)).perform(typeText(String.valueOf(volume)));

        Espresso.onView(withId(R.id.emissions_vehicle_edit)).perform(ViewActions.scrollTo());
        Espresso.onView(withId(R.id.emissions_vehicle_edit)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.emissions_vehicle_edit)).perform(typeText(String.valueOf(emission)));

        //confirm inputs with click on button
        Espresso.onView(withId(R.id.confirm_editing_vehicle)).perform(ViewActions.scrollTo());
        Espresso.onView(withId(R.id.confirm_editing_vehicle)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.confirm_editing_vehicle)).perform(click());

        //Compare vehicle data
        assertEquals(name, GarageActivity.vehicles.lastElement().name);
        assertEquals(licence, GarageActivity.vehicles.lastElement().licensePlate);
        assertEquals(urban, GarageActivity.vehicles.lastElement().urbanConsumption);
        assertEquals(outside, GarageActivity.vehicles.lastElement().outsideConsumption);
        assertEquals(combined, GarageActivity.vehicles.lastElement().combinedConsumption);
        assertEquals(mileage, GarageActivity.vehicles.lastElement().mileAge);
        assertEquals((float)fuelLevel, GarageActivity.vehicles.lastElement().fuelLevel);
        assertEquals(volume, GarageActivity.vehicles.lastElement().volume);
        assertEquals(emission, GarageActivity.vehicles.lastElement().co2emissions);
    }
}