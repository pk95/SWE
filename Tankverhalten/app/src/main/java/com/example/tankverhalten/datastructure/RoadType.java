package com.example.tankverhalten.datastructure;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines the type of used road.
 * Can take the states: city, combined or country.
 *
 * @author Stephan de Gavarelli
 * @version 1.0
 * @see Ride
 */
@IntDef({RoadType.COMBINED, RoadType.CITY, RoadType.COUNTRY})
@Retention(RetentionPolicy.SOURCE)
public @interface RoadType {
    int COMBINED = 0;
    int CITY = 1;
    int COUNTRY = 2;
}