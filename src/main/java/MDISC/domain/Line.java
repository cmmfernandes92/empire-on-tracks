package MDISC.domain;

/**
 * Represents a connection (railway line) between two stations.
 * A line has two endpoints, may be electrified, and has an associated distance.
 */
public class Line {
    /** The first endpoint station of the line. */
    private final Station a;

    /** The second endpoint station of the line. */
    private final Station b;

    /** True if the line supports electric trains, false if diesel only. */
    private final boolean electrified;

    /** Distance of the line segment in kilometers. */
    private final double distance;

    /**
     * Constructs a new Line connecting two stations.
     *
     * @param a           The first station endpoint.
     * @param b           The second station endpoint.
     * @param electrified True if the line is electrified; false otherwise.
     * @param distance    The length of the line segment in kilometers.
     */
    public Line(Station a, Station b, boolean electrified, double distance) {
        this.a = a;
        this.b = b;
        this.electrified = electrified;
        this.distance = distance;
    }

    /**
     * Returns the first endpoint station of this line.
     *
     * @return The first station.
     */
    public Station getA() {
        return a;
    }

    /**
     * Returns the second endpoint station of this line.
     *
     * @return The second station.
     */
    public Station getB() {
        return b;
    }

    /**
     * Indicates whether this line is electrified (supports electric trains).
     *
     * @return True if electrified; false otherwise.
     */
    public boolean isElectrified() {
        return electrified;
    }

    /**
     * Returns the distance of this line segment in kilometers.
     *
     * @return The line distance.
     */
    public double getDistance() {
        return distance;
    }
}
