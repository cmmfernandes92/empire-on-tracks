package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import pt.ipp.isep.dei.domain.ScenarioEvent;

/**
 * Represents a cargo item with a specific type and quantity.
 * Provides methods to manipulate the quantity and behaviors of the cargo.
 */
public class Cargo implements Serializable {
    private CargoType type;
    private double quantity = 0;
    private final List<String> behaviors = new ArrayList<>();

    /**
     * Constructs a new Cargo with the specified type and initial quantity.
     *
     * @param type     the cargo type
     * @param quantity the initial quantity of cargo
     */
    public Cargo(CargoType type, double quantity) {
        this.type = type;
    }

    /**
     * Returns the cargo type.
     *
     * @return the type of this cargo
     */
    public CargoType getType() {
        return type;
    }

    /**
     * Returns the current quantity of this cargo.
     *
     * @return the quantity of cargo
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Returns a string representation of this cargo,
     * indicating its quantity and type.
     *
     * @return a string representation of the cargo
     */
    @Override
    public String toString() {
        return quantity + " units of " + type.name();
    }

    /**
     * Adds the specified value to the current quantity,
     * only if the value is positive.
     *
     * @param value the amount to add
     */
    public void addQuantity(double value) {
        if (value > 0) {
            this.quantity += value;
        }
    }

    /**
     * Removes the specified value from the current quantity,
     * only if the value is positive and does not exceed the current quantity.
     *
     * @param value the amount to remove
     * @return true if the removal was successful, false otherwise
     */
    public boolean removeQuantity(double value) {
        if (value > 0 && this.quantity >= value) {
            this.quantity -= value;
            return true;
        }
        return false;
    }

    /**
     * Sets the quantity of this cargo to the specified value.
     *
     * @param value the new quantity
     */
    public void setQuantity(double value) {
        this.quantity = value;
    }

    /**
     * Sets the cargo type.
     *
     * @param type the new cargo type
     */
    public void setCargoType(CargoType type) {
        this.type = type;
    }

    /**
     * Returns the list of behaviors associated with this cargo.
     *
     * @return the list of behaviors
     */
    public List<String> getBehaviors() {
        return behaviors;
    }

    /**
     * Adds a new behavior to the cargo if it does not already exist.
     *
     * @param behavior the behavior to add
     */
    public void setBehavior(String behavior) {
        if (!behaviors.contains(behavior)) {
            behaviors.add(behavior);
        }
    }


}
