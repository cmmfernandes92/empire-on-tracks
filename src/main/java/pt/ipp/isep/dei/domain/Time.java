// Time.java
package pt.ipp.isep.dei.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Simulation clock that holds the current simulation date
 * and enforces a fixed end date.
 * Time progression is driven externally by the simulator.
 */
public class Time implements Serializable {
    private LocalDateTime currentDate;
    private final LocalDateTime endDate;

    /**
     * Constructs the simulation clock.
     *
     * @param startDate Initial date of the simulation.
     * @param endDate   Final date of the simulation; manual ticks stop here.
     */
    public Time(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be on or after start date");
        }
        this.currentDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the current date of the simulator.
     *
     * @return the current simulation date
     */
    public LocalDateTime getDate() {
        return currentDate;
    }

    /**
     * Advances the clock by one month, respecting the end date.
     */
    public void advanceByMonth() {
        if (currentDate.isBefore(endDate)) {
            currentDate = currentDate.plusMonths(1);
        }
    }

    /**
     * Checks if the simulation has reached its end.
     *
     * @return true if current date is on or after the configured end date
     */
    public boolean isFinished() {
        return !currentDate.isBefore(endDate);
    }
}
