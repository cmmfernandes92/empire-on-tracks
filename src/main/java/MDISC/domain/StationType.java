package MDISC.domain;

/**
 * Enumerates station types:
 *   D* → DEPOT
 *   T* → TERMINAL
 *   S* (or any other prefix) → STATION
 */
public enum StationType {
    /** Represents a depot station. */
    DEPOT,

    /** Represents a regular station. */
    STATION,

    /** Represents a terminal station. */
    TERMINAL;

    /**
     * Determines the {@link StationType} from a station name.
     * The first character of the name is used as a prefix:
     * <ul>
     *   <li>'D' → {@link #DEPOT}</li>
     *   <li>'T' → {@link #TERMINAL}</li>
     *   <li>Any other or null/empty name → {@link #STATION}</li>
     * </ul>
     *
     * @param name the station name, from which the type is inferred
     * @return the corresponding {@code StationType}
     */
    public static StationType fromName(String name) {
        if (name == null || name.isEmpty()) {
            return STATION;
        }
        char prefix = name.charAt(0);
        if (prefix == 'D') {
            return DEPOT;
        }
        if (prefix == 'T') {
            return TERMINAL;
        }
        return STATION;
    }
}
