package pt.ipp.isep.dei.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an industry that transforms one type of cargo into another.
 * <p>
 * A transforming industry consumes input cargo when sufficient demand is met,
 * produces output cargo each cycle, and tracks its location on the map.
 * </p>
 */
public class TransformingIndustry implements Industry, Serializable {

    /** Name of the industry. */
    private final String industryName;

    /** Fixed operating cost of the industry. */
    private final int industryCost;

    /** Quantity of output cargo produced per cycle. */
    private double production;

    /** Quantity of input cargo required per cycle. */
    private double demand;

    /** Location of this industry on the map grid. */
    private Location location;

    /** Internal cargo storage for input type. */
    private final Cargo inputCargo;

    /** Internal cargo storage for output type. */
    private final Cargo outputCargo;

    /**
     * Constructs a transforming industry without an assigned location.
     *
     * @param industryName  the unique name of the industry
     * @param industryCost  the fixed operating cost
     * @param production    the amount of cargo produced per cycle
     * @param demand        the amount of input cargo required per cycle
     * @param inputType     the type of cargo consumed
     * @param outputType    the type of cargo produced
     * @throws NullPointerException if {@code inputType} or {@code outputType} is null
     */
    public TransformingIndustry(String industryName,
                                int industryCost,
                                double production,
                                double demand,
                                CargoType inputType,
                                CargoType outputType) {
        this(industryName, industryCost, production, demand, null, inputType, outputType);
    }

    /**
     * Constructs a transforming industry with an assigned location.
     *
     * @param industryName  the unique name of the industry
     * @param industryCost  the fixed operating cost
     * @param production    the amount of cargo produced per cycle
     * @param demand        the amount of input cargo required per cycle
     * @param location      the {@link Location} where the industry is placed
     * @param inputType     the type of cargo consumed
     * @param outputType    the type of cargo produced
     * @throws NullPointerException if {@code inputType} or {@code outputType} is null
     */
    public TransformingIndustry(String industryName,
                                int industryCost,
                                double production,
                                double demand,
                                Location location,
                                CargoType inputType,
                                CargoType outputType) {
        Objects.requireNonNull(inputType,  "Input cargo type cannot be null");
        Objects.requireNonNull(outputType, "Output cargo type cannot be null");
        this.industryName = industryName;
        this.industryCost = industryCost;
        this.production   = production;
        this.demand       = demand;
        this.location     = location;
        this.inputCargo   = new Cargo(inputType,  0.0);
        this.outputCargo  = new Cargo(outputType, 0.0);
    }

    /**
     * Returns the name of the industry.
     *
     * @return the industry name
     */
    @Override
    public String getIndustryName() {
        return industryName;
    }

    /**
     * Returns the fixed operating cost of the industry.
     *
     * @return the industry cost per cycle
     */
    @Override
    public int getIndustryCost() {
        return industryCost;
    }

    /**
     * Returns the amount of output cargo produced per cycle.
     *
     * @return production rate per cycle
     */
    @Override
    public double getProduction() {
        return production;
    }

    /**
     * Returns the amount of input cargo required per cycle.
     *
     * @return input demand per cycle
     */
    @Override
    public double getDemand() {
        return demand;
    }

    /**
     * Sets the location of this industry on the map.
     *
     * @param location new location (can be null)
     */
    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Returns the current map location of this industry.
     *
     * @return the location, or {@code null} if not set
     */
    @Override
    public Location getLocation() {
        return location;
    }

    /**
     * Produces output cargo if enough input cargo is available.
     * <p>
     * Consumes {@code demand} units of input cargo and adds {@code production}
     * units to the output cargo storage.
     * </p>
     *
     * @param player (ignored, present for interface compatibility)
     */
    @Override
    public void produceCargo(Player player) {
        if (inputCargo.getQuantity() >= demand) {
            outputCargo.addQuantity(production);
            inputCargo.removeQuantity(demand);
        }
    }

    /**
     * Consumes a specified quantity of input cargo.
     *
     * @param quantity the amount of input cargo to consume
     */
    @Override
    public void consumeCargo(double quantity) {
        inputCargo.removeQuantity(quantity);
    }

    /**
     * Returns the output cargo produced by this industry.
     *
     * @return the {@link Cargo} instance representing output storage
     */
    @Override
    public Cargo getCargo() {
        return outputCargo;
    }

    /**
     * Returns the type of input cargo this industry consumes.
     *
     * @return the {@link CargoType} of input cargo
     */
    public CargoType getInputCargoType() {
        return inputCargo.getType();
    }

    /**
     * Returns the quantity of input cargo required for each cycle.
     * <p>Note: In this implementation, always returns 0 (used only for interface compatibility).</p>
     *
     * @return always 0
     */
    @Override
    public double getInputCargoQuantity() {
        return 0;
    }

    /**
     * Returns the type of output cargo this industry produces.
     *
     * @return the {@link CargoType} of output cargo
     */
    public CargoType getOutputCargoType() {
        return outputCargo.getType();
    }

    /**
     * Returns the quantity of output cargo currently stored.
     *
     * @return output cargo quantity
     */
    public double getOutputCargoQuantity() {
        return outputCargo.getQuantity();
    }

    /**
     * Sets the per-cycle production rate.
     *
     * @param production the new production amount per cycle
     */
    public void setProduction(double production) {
        this.production = production;
    }

    /**
     * Sets the per-cycle demand rate.
     *
     * @param demand the new input demand per cycle
     */
    public void setDemand(double demand) {
        this.demand = demand;
    }

    /**
     * Updates the input cargo type. Resets stored quantity if type changes.
     *
     * @param newType the new {@link CargoType} for input
     * @throws NullPointerException if {@code newType} is null
     */
    public void setInputCargo(CargoType newType) {
        Objects.requireNonNull(newType, "Input cargo type cannot be null");
        this.inputCargo.setCargoType(newType);
    }

    /**
     * Updates the output cargo type. Resets stored quantity if type changes.
     *
     * @param newType the new {@link CargoType} for output
     * @throws NullPointerException if {@code newType} is null
     */
    public void setOutputCargo(CargoType newType) {
        Objects.requireNonNull(newType, "Output cargo type cannot be null");
        this.outputCargo.setCargoType(newType);
    }

    /**
     * Sets the current quantity of input cargo.
     *
     * @param quantity the new input cargo quantity
     */
    public void setInputCargoQuantity(double quantity) {
        inputCargo.setQuantity(quantity);
    }

    /**
     * Returns a string representation of this transforming industry,
     * including current cargo levels.
     *
     * @return a formatted string summarizing consumption and production
     */
    @Override
    public String toString() {
        return String.format(
                "TransformingIndustry[%s] Consumes %s:%.2f -> Produces %s:%.2f",
                industryName,
                getInputCargoType(), inputCargo.getQuantity(),
                getOutputCargoType(), outputCargo.getQuantity()
        );
    }
}
