package MDISC.domain;

/**
 * Represents a railway station identified by a name (e.g., "D8" or "S2").
 * <p>
 * The station name is stored as provided (without underscores), and the
 * station type is inferred from the first character of the name.
 * </p>
 */
public class Station {

    /**
     * The station's unique identifier name (e.g., "D8" or "S2").
     */
    private final String name;

    /**
     * The inferred type of the station based on its name prefix (D, S, or T).
     */
    private final StationType type;

    /**
     * Constructs a new Station with the specified name.
     * <p>
     * Determines the station type by inspecting the first character of the name.
     * </p>
     *
     * @param name the station's name (e.g., "D8" or "S2"), without underscores
     */
    public Station(String name) {
        this.name = name;
        this.type = StationType.fromName(name);
    }

    /**
     * Returns the name of this station.
     *
     * @return the station name as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of this station.
     *
     * @return the StationType enum value (D, S, or T)
     */
    public StationType getType() {
        return type;
    }

    /**
     * Returns a string representation of this station,
     * which is simply its name.
     *
     * @return the station name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Compares this station with another object for equality.
     * <p>
     * Two Station instances are equal if they have the same name and type.
     * </p>
     *
     * @param o the object to compare with
     * @return {@code true} if the given object is a Station
     *         with an identical name and type; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;
        Station other = (Station) o;
        return name.equals(other.name) && type == other.type;
    }

    /**
     * Returns a hash code value for the station,
     * based on its name.
     *
     * @return the hash code of the station name
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
