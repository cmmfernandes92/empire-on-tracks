package pt.ipp.isep.dei.domain;

import java.util.*;
import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a Point-of-Route: a trio of station, list of cargoes, and cargo mode.
 * Each Point-of-Route corresponds to picking up one or more cargo types at a station,
 * using a specific cargo loading mode.
 */
public class PointOfRoute implements Serializable {
    private final Station station;
    private final Map<CargoType, Integer> cargoCarriages;  // Map to store cargo types and their respective carriage count
    private final CargoMode cargoMode;

    /**
     * Constructs a Point-of-Route.
     * @param station the station where the train stops
     * @param cargoCarriages a map of cargo types and the number of carriages to be picked up for each cargo type
     * @param cargoMode the loading mode (FULL, HALF, or AVAILABLE)
     * @throws IllegalArgumentException if any argument is null or if cargoCarriages is empty
     */
    public PointOfRoute(Station station, Map<CargoType, Integer> cargoCarriages, CargoMode cargoMode) {
        this.station = Objects.requireNonNull(station, "Station cannot be null");
        Objects.requireNonNull(cargoCarriages, "Cargo carriages cannot be null");
        if (cargoCarriages.isEmpty()) throw new IllegalArgumentException("Cargo carriages cannot be empty");
        this.cargoCarriages = new HashMap<>(cargoCarriages);  // Create a copy of the map to ensure immutability
        this.cargoMode = Objects.requireNonNull(cargoMode, "Cargo mode cannot be null");
    }

    /**
     * Returns the station where this point-of-route occurs.
     *
     * @return the associated station
     */
    public Station getStation() {
        return station;
    }

    /**
     * Returns a list of cargo types to be picked up at this point.
     *
     * @return list of cargo types
     */
    public List<CargoType> getCargoes() {
        return new ArrayList<>(cargoCarriages.keySet());
    }

    /** Returns an immutable copy of the cargoes and their respective carriage counts. */
    public Map<CargoType, Integer> getCargoCarriages() {
        return Map.copyOf(cargoCarriages);  // Ensure the map cannot be modified from outside
    }

    /**
     * Returns the cargo loading mode for this point (FULL, HALF, or AVAILABLE).
     *
     * @return the cargo loading mode
     */
    public CargoMode getCargoMode() {
        return cargoMode;
    }

    @Override
    public String toString() {
        // Display the station name and each cargo type with the corresponding carriage count
        String cargoInfo = cargoCarriages.entrySet().stream()
                .map(entry -> entry.getKey().name() + ": " + entry.getValue() + " carriages")
                .collect(Collectors.joining(", "));
        return station.getStationName() + " (" + cargoInfo + ", " + cargoMode + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PointOfRoute that = (PointOfRoute) obj;
        return Objects.equals(station, that.station) &&
                Objects.equals(cargoCarriages, that.cargoCarriages) &&
                cargoMode == that.cargoMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(station, cargoCarriages, cargoMode);
    }

}
