package pt.ipp.isep.dei.domain;

import java.io.Serializable;

/**
 * Common interface for different types of industries in the simulation.
 * <p>
 * Defines the contract for production, consumption, cost, and location tracking.
 * Implementing classes may represent industries that produce, transform,
 * or handle cargo in various ways (e.g., primary, transforming, or mixed industries).
 * </p>
 */
public interface Industry extends Serializable {

    /**
     * Returns the unique name of this industry.
     *
     * @return the industry name
     */
    String getIndustryName();

    /**
     * Returns the fixed operating cost of this industry per cycle.
     *
     * @return the operating cost
     */
    int getIndustryCost();

    /**
     * Returns the quantity of cargo produced or transformed each cycle.
     *
     * @return the production rate (units per cycle)
     */
    double getProduction();

    /**
     * Returns the demand threshold required to enable production or transformation.
     * May be zero for industries with no input requirements.
     *
     * @return the demand amount (units per cycle)
     */
    double getDemand();

    /**
     * Sets the location of this industry on the simulation map.
     *
     * @param location the {@link Location} to place the industry
     */
    void setLocation(Location location);

    /**
     * Returns the current location of this industry on the simulation map.
     *
     * @return the {@link Location} of the industry, or null if not placed
     */
    Location getLocation();

    /**
     * Executes one production or transformation cycle for this industry.
     * <p>
     * Implementations must handle input consumption, output generation,
     * cost processing, and any internal state updates required per simulation tick.
     * The {@code player} parameter is used for industries that interact with player finances or
     * ownership, and may be ignored by some implementations.
     * </p>
     *
     * @param player the {@link Player} affected by the industry's production (can be ignored)
     */
    void produceCargo(Player player);

    /**
     * Consumes a specified quantity of input cargo from internal storage.
     * Only applicable to industries that require input resources; others may ignore this method.
     *
     * @param quantity the amount of cargo to consume
     */
    void consumeCargo(double quantity);

    /**
     * Returns the {@link Cargo} object representing output or current stock.
     * For industries with separate input and output storage, this typically returns output stock.
     *
     * @return the output cargo storage for this industry
     */
    Cargo getCargo();

    /**
     * Returns the current quantity in the cargo storage returned by {@link #getCargo()}.
     * This is a convenience method that delegates to {@code getCargo().getQuantity()}.
     *
     * @return the cargo quantity for the main output/storage
     */
    default double getCargoQuantity() {
        return getCargo().getQuantity();
    }

    /**
     * Returns the cargo type consumed by this industry as input.
     * For producers with no input requirements, may return {@code null}.
     *
     * @return the {@link CargoType} consumed as input, or {@code null} if not applicable
     */
    CargoType getInputCargoType();

    /**
     * Returns the quantity of cargo consumed as input each cycle.
     * For industries that do not require input, may return 0.
     *
     * @return the current input cargo quantity
     */
    double getInputCargoQuantity();

    /**
     * Returns the cargo type produced as output by this industry.
     * For industries with no direct output (e.g., pure consumers), may return {@code null}.
     *
     * @return the {@link CargoType} produced as output, or {@code null} if not applicable
     */
    CargoType getOutputCargoType();

    /**
     * Returns the quantity of cargo produced as output each cycle.
     * For industries with no output (e.g., pure consumers), may return 0.
     *
     * @return the current output cargo quantity
     */
    double getOutputCargoQuantity();

    /**
     * Updates the per-cycle production rate for this industry.
     * Used to dynamically adjust output rates in response to events or upgrades.
     *
     * @param production the new production amount per cycle
     */
    void setProduction(double production);
}
