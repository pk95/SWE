package com.example.tankverhalten;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;


/**
 * Handles a refuel of a {@link Vehicle}.
 *
 * @author Stephan de Gavarelli
 * @version 1.0
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class Refuel {

    private final LocalDate creationDate = java.time.LocalDate.now();
    float refueled = 0;
    float cost = 0;
    String costImageSrc = "";

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
     * @return LocalDate, when this Refuel was created
     * @see LocalDate
     */
    public LocalDate getCreationDateDate() {
        return creationDate;
    }
}

