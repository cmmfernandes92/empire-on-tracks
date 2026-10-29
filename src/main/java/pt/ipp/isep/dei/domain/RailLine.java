package pt.ipp.isep.dei.domain;

import java.io.Serializable;

/**
 * Represents a railway line connecting two stations on the map.
 * Each rail line may be electrified, single or double track, and has
 * associated costs and distance.
 */
public class RailLine implements Serializable {
    private Station start;
    private Station end;
    private boolean isElectrified;
    private boolean isDouble;
    private double cost;
    private double distance;

    /**
     * Constructs a RailLine between two stations, with optional electrification and double tracking.
     *
     * @param start         The starting station of the rail line.
     * @param end           The ending station of the rail line.
     * @param isElectrified Whether the rail line is electrified.
     * @param isDouble      Whether the rail line is a double track.
     * @param distance      The length of the rail line in kilometers.
     */
    public RailLine(Station start, Station end, boolean isElectrified, boolean isDouble, double distance) {
        this.start = start;
        this.end = end;
        this.isElectrified = isElectrified;
        this.isDouble = isDouble;
        this.cost = calculateCost();
        this.distance = distance;
    }

    /**
     * Calculates the construction cost of the rail line.
     *
     * @return The total construction cost.
     */
    private double calculateCost() {
        double base = 6000;
        if (isElectrified) base += 5000;
        if (isDouble) base += 3000;
        return base;
    }

    /**
     * Returns the starting station of the rail line.
     *
     * @return The start station.
     */
    public Station getStart() {
        return start;
    }

    /**
     * Returns the ending station of the rail line.
     *
     * @return The end station.
     */
    public Station getEnd() {
        return end;
    }

    /**
     * Checks if the rail line is electrified.
     *
     * @return {@code true} if the rail line is electrified, otherwise {@code false}.
     */
    public boolean isElectrified() {
        return isElectrified;
    }

    /**
     * Checks if the rail line is a double track.
     *
     * @return {@code true} if the rail line is double, otherwise {@code false}.
     */
    public boolean isDouble() {
        return isDouble;
    }

    /**
     * Returns the construction cost of the rail line.
     *
     * @return The construction cost.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Returns the yearly maintenance cost of the rail line.
     *
     * @return The maintenance cost.
     */
    public double getMaintenanceCost() {
        double maintenanceCost = 60;
        if (isElectrified) maintenanceCost += 12;
        if (isDouble) maintenanceCost += 24;
        return maintenanceCost;
    }

    /**
     * Returns the distance of the rail line in kilometers.
     *
     * @return The rail line distance.
     */
    public double getDistance() {
        return distance;
    }
}
