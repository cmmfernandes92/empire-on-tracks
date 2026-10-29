package pt.ipp.isep.dei.domain;
import java.io.Serializable;

/**
 * Represents a simulation period using integer time units.
 * Note: Date class is reserved for the simulator clock.
 */
public class Period implements Serializable {
    private final int startDate;
    private final int endDate;

    /**
     * Constructs a new Period.
     *
     * @param startDate the start tick or time unit
     * @param endDate   the end tick or time unit
     * @throws IllegalArgumentException if startDate > endDate
     */
    public Period(int startDate, int endDate) {
        if (startDate > endDate) {
            throw new IllegalArgumentException("startDate must be <= endDate");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the start of the period.
     *
     * @return the start tick or time unit
     */
    public int getStartDate() {
        return startDate;
    }

    /**
     * Returns the end of the period.
     *
     * @return the end tick or time unit
     */
    public int getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return String.format("Period[%d -> %d]", startDate, endDate);
    }
}


