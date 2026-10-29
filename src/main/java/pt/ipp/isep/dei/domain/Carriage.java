package pt.ipp.isep.dei.domain;

import java.util.Objects;
import java.io.Serializable;
import pt.ipp.isep.dei.domain.ScenarioEvent;
/**
 * Represents a train carriage specialized for a specific cargo type.
 * Carriages are composed by trains - they cannot exist independently.
 */
public class Carriage implements Serializable {
    private final CargoType cargoType;
    private final int capacity;
    private int currentLoad;
    private static final int DEFAULT_CAPACITY = 100;

    Carriage(CargoType cargoType, int capacity) {
        this.cargoType = Objects.requireNonNull(cargoType, "Cargo type cannot be null");
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        this.capacity = capacity;
        this.currentLoad = 0;
    }

    Carriage (CargoType cargoType) {
        this.cargoType = Objects.requireNonNull(cargoType, "Cargo type cannot be null");
        this.capacity = DEFAULT_CAPACITY;
        this.currentLoad = 0;
    }

    public CargoType getCargoType() { return cargoType; }
    public int getCapacity() { return capacity; }
    public int getCurrentLoad() { return currentLoad; }
    public int getAvailableCapacity() { return capacity - currentLoad; }
    public boolean isFull() { return currentLoad >= capacity; }
    public boolean isHalfFull() { return currentLoad >= capacity / 2; }

    /**
     * Load cargo into this carriage.
     * @param amount amount to load
     * @return actual amount loaded (may be less if carriage becomes full)
     */
    public int loadCargo(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        int actualLoad = Math.min(amount, getAvailableCapacity());
        currentLoad += actualLoad;
        return actualLoad;
    }

    /**
     * Unload all cargo from this carriage.
     * @return amount unloaded
     */
    public int unloadCargo() {
        int unloaded = currentLoad;
        currentLoad = 0;
        return unloaded;
    }

    // [ADDED] Helper to completely empty carriage
    public void unloadAll() {
        this.currentLoad = 0;
    }

    @Override
    public String toString() {
        return String.format("Carriage[%s: %d/%d]", cargoType, currentLoad, capacity);
    }
}
