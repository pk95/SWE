package com.example.tankverhalten.datastructure;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Vector;


/**
 * Handles a refuel of a {@link Vehicle}.
 *
 * @author Stephan de Gavarelli
 * @version 1.0
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class Refuel implements Serializable {

    private final LocalDateTime creationDate = java.time.LocalDateTime.now();
    private final LocalTime creationTime = LocalTime.now();
    public float refueled = 0;
    public float cost = 0;
    public String costImageSrc = "";

    /**
     * Default-Constructor
     */
    public Refuel() {
    }

    /**
     * Constructor with all variables as parameters
     *
     * @param refueled     by user
     * @param cost         of refuel
     * @param costImageSrc is a link to an Image of the bill
     */
    public Refuel(float refueled, float cost, String costImageSrc) {
        this.refueled = refueled;
        this.cost = cost;
        this.costImageSrc = costImageSrc;
    }

    /**
     * Returns all refuels of a vehicle, which are between a start and an end LocalTimeDate
     *
     * @param refuels
     * @param startDate
     * @param endDate
     * @return refuels
     */
    public static Refuel[] getRefuelsBetweenDates(Vector<Refuel> refuels, LocalDateTime startDate, LocalDateTime endDate) {
        Vector<Refuel> refuelsBetween = new Vector<>();
        for (Refuel r : refuels) {
            if (!r.getCreationDate().isBefore(startDate) && !r.getCreationDate().isAfter(endDate))
                refuelsBetween.add(r);
        }
        return refuelsBetween.toArray(new Refuel[0]);
    }

    /**
     * @return LocalDate, when this Refuel was created
     * @see LocalDate
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }


    @Override
    public Refuel clone() {
        return new Refuel(this.refueled, this.cost, this.costImageSrc);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Refuel) {
            Refuel r = (Refuel) o;
            return (this.refueled == r.refueled && this.cost == r.cost && this.costImageSrc.equals(r.costImageSrc));
        }
        return false;
    }

    public LocalTime getCreationTime() {
        return creationTime;
    }
}

