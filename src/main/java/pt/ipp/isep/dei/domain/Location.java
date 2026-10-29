package pt.ipp.isep.dei.domain;

import java.util.Objects;
import java.io.Serializable;

/**
 * Represents a two-dimensional coordinate (X, Y) on the map.
 * <p>
 * Used in the context of US03 to define the central position of a city
 * and the position of each house block within that city.
 *
 * @see City
 * @see HouseBlock
 */
public class Location implements Serializable {
    private int x;
    private int y;

    /**
     * Constructs a new {@code Location} with the given coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the X coordinate.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the Y coordinate.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Compares this location to another object.
     * Two {@code Location} objects are considered equal if they have the same X and Y coordinates.
     *
     * @param obj the object to compare to
     * @return {@code true} if both locations are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location location = (Location) obj;
        System.out.println("Comparing locations: " + this + " vs " + location);
        return x == location.x && y == location.y;
    }

    /**
     * Computes the hash code based on X and Y coordinates.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Returns a string representation of this location.
     *
     * @return a string in the format "Location{x=..., y=...}"
     */
    @Override
    public String toString() {
        return "Location{" + "x=" + x + ", y=" + y + '}';
    }
}
