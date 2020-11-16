package com.example.tankverhalten;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Type of vehicle to represent different properties of vehicles.
 * <p>
 * Alternative for an Enum for VehicleTypes.
 * Reason: In Android enums blow up the app, so this is a common way of avoiding DEX space while running.
 * Link to video: https://www.youtube.com/watch?v=Hzs6OBcvNQE
 * <p>
 *
 * @author Stephan de Gavarelli
 * @version 1.0
 * @see java.lang.annotation.Annotation
 */
@IntDef({VehicleType.CAR, VehicleType.MOTORCYCLE, VehicleType.TRANSPORTER})
@Retention(RetentionPolicy.SOURCE)
public @interface VehicleType {
    int CAR = 0;
    int MOTORCYCLE = 1;
    int TRANSPORTER = 2;
}