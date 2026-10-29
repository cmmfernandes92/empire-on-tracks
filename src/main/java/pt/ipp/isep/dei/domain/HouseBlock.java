package pt.ipp.isep.dei.domain;

import java.io.Serializable;

/**
 * Represents a single house block belonging to a city,
 * identified by its specific coordinates in the map.
 * <p>
 * Used in the context of US03 to represent manually or automatically
 * assigned house blocks around a city's central position.
 *
 * @see City
 * @see Location
 */
public class HouseBlock implements Serializable {

    private Location location;

    /**
     * Constructs a new {@code HouseBlock} at the specified coordinates.
     *
     * @param positionX the X coordinate of the house block
     * @param positionY the Y coordinate of the house block
     */
    public HouseBlock(int positionX, int positionY) {
        this.location = new Location(positionX, positionY);
    }

    /**
     * Returns the {@link Location} of this house block.
     *
     * @return the location of the house block
     */
    public Location getLocation() {
        return location;
    }
}
