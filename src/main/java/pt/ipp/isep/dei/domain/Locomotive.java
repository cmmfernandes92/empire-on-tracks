package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.io.Serializable;

/**
 * Represents a locomotive with technical specifications, operational costs,
 * economic data, and behavior for movement along a defined station route.
 */
public class Locomotive implements Serializable {

    // --- Identification and Economics ---

    /** Unique identifier for the locomotive. */
    private final String id;

    /** Type of locomotive: "Steam", "Diesel", or "Electric". */
    private final String type;

    /** Purchase price of the locomotive. */
    private final double price;

    /** Whether the locomotive requires electrified tracks. */
    private final boolean requiresElectrification;

    // --- Technical Specifications ---

    /** Power of the locomotive (e.g., in kW or HP). */
    private final int power;

    /** Acceleration rate (e.g., in m/s²). */
    private final double acceleration;

    /** Top speed (e.g., in km/h). */
    private final double topSpeed;

    /** First year the locomotive becomes available. */
    private final int startYear;

    // --- Operating Costs ---

    /** Fuel cost per kilometer. */
    private final double fuelCostPerKm;

    /** Yearly maintenance cost. */
    private final double maintenancePerYear;

    // --- Cargo Capacity ---

    /** Total cargo capacity (currently unused). */
    private final double capacity = 0;

    // --- Routing and Position ---

    /** List of stations that define the locomotive's current route. */
    private List<Station> route = List.of();

    /** Current index in the route. */
    private int currentIndex = 0;

    // --- Scenario Availability ---

    /** Period during which the locomotive is available in the scenario. */
    private Period availability;

    /**
     * Constructs a new Locomotive with full specifications.
     *
     * @param id                      unique identifier
     * @param type                    type of locomotive ("Steam", "Diesel", "Electric")
     * @param price                   purchase price
     * @param requiresElectrification whether electrification is required
     * @param power                   engine power
     * @param acceleration            acceleration rate
     * @param topSpeed                maximum speed
     * @param startYear               year locomotive becomes available
     * @param fuelCostPerKm           fuel cost per km
     * @param maintenancePerYear      maintenance cost per year
     */
    public Locomotive(String id,
                      String type,
                      double price,
                      boolean requiresElectrification,
                      int power,
                      double acceleration,
                      double topSpeed,
                      int startYear,
                      double fuelCostPerKm,
                      double maintenancePerYear) {
        this.id                      = Objects.requireNonNull(id);
        this.type                    = Objects.requireNonNull(type);
        this.price                   = price;
        this.requiresElectrification = requiresElectrification;
        this.power                   = power;
        this.acceleration            = acceleration;
        this.topSpeed                = topSpeed;
        this.startYear               = startYear;
        this.fuelCostPerKm           = fuelCostPerKm;
        this.maintenancePerYear      = maintenancePerYear;
        setRoute(route);
    }

    // --- Getters ---

    /** @return the unique locomotive ID */
    public String getId() {
        return id;
    }

    /** @return the locomotive type ("Steam", "Diesel", or "Electric") */
    public String getType() {
        return type;
    }

    /** @return the locomotive's purchase price */
    public double getPrice() {
        return price;
    }

    /** @return true if this locomotive requires electrification */
    public boolean requiresElectrification() {
        return requiresElectrification;
    }

    /** @return locomotive engine power */
    public int getPower() {
        return power;
    }

    /** @return acceleration rate */
    public double getAcceleration() {
        return acceleration;
    }

    /** @return top speed of the locomotive */
    public double getTopSpeed() {
        return topSpeed;
    }

    /** @return the start year for this locomotive */
    public int getStartYear() {
        return startYear;
    }

    /** @return fuel cost per kilometer */
    public double getFuelCostPerKm() {
        return fuelCostPerKm;
    }

    /** @return yearly maintenance cost */
    public double getMaintenancePerYear() {
        return maintenancePerYear;
    }

    /** @return cargo capacity (currently unused) */
    public double getCapacity() {
        return capacity;
    }

    /** @return a copy of the station route */
    public List<Station> getRoute() {
        return List.copyOf(route);
    }

    /**
     * Sets a new route for the locomotive and resets its position to the first station.
     *
     * @param route the list of stations that define the route
     */
    public void setRoute(List<Station> route) {
        this.route = new ArrayList<>(route);
        this.currentIndex = 0;
    }

    /**
     * @return the current station the locomotive is at; null if the route is empty
     */
    public Station getCurrentStation() {
        return route.isEmpty() ? null : route.get(currentIndex);
    }

    /**
     * Advances the locomotive to the next station in its route.
     * Wraps around to the beginning if at the end.
     */
    public void move() {
        if (!route.isEmpty())
            currentIndex = (currentIndex + 1) % route.size();
    }

    /** @return the availability period in the scenario */
    public Period getAvailability() {
        return availability;
    }

    /**
     * Sets the availability period of the locomotive in the scenario.
     *
     * @param a the period to set
     */
    public void setAvailability(Period a) {
        this.availability = a;
    }

    /**
     * Checks if this locomotive is electric.
     *
     * @return true if the type is "Electric" (case-insensitive)
     */
    public boolean isElectric() {
        return type.equalsIgnoreCase("Electric");
    }

    /**
     * @return a string representation including ID and type
     */
    @Override
    public String toString() {
        return String.format(
                "Locomotive[id=%s,type=%s]",
                id,
                type
        );
    }
}
