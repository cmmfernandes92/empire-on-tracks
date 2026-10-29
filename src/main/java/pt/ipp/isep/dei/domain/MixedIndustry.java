package pt.ipp.isep.dei.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * A mixed industry that can import, export, or transform cargo types on the map.
 * <p>
 * Behavior modes:
 * <ul>
 *   <li><b>IMPORT:</b> spends cost to produce output cargo</li>
 *   <li><b>EXPORT:</b> converts available input cargo into earnings</li>
 *   <li><b>TRANSFORM:</b> consumes input cargo to produce output cargo</li>
 * </ul>
 * <p>
 * Implements {@link Industry} and supports location, production, demand,
 * and cargo storage for both input and output types.
 * </p>
 *
 * @author Team
 * @version 1.0
 */
public class MixedIndustry implements Industry, Serializable {

    private final String industryName;
    private final int industryCost;
    private double production;
    private double demand;
    private Location location;
    private final Cargo inputCargo;
    private final Cargo outputCargo;
    private MixedBehavior behavior = MixedBehavior.TRANSFORM;

    /**
     * Constructs a MixedIndustry with specified parameters and location.
     *
     * @param industryName    name of the industry
     * @param industryCost    cost to operate the industry
     * @param production      amount of cargo produced per cycle
     * @param demand          amount of input cargo required per cycle
     * @param location        position on the map; may be null if unplaced
     * @param inputCargoType  initial cargo type for input; must not be null
     * @param outputCargoType initial cargo type for output; must not be null
     * @throws NullPointerException if any required parameter is null
     */
    public MixedIndustry(String industryName,
                         int industryCost,
                         double production,
                         double demand,
                         Location location,
                         CargoType inputCargoType,
                         CargoType outputCargoType) {
        this.industryName = Objects.requireNonNull(industryName);
        this.industryCost = industryCost;
        this.production = production;
        this.demand = demand;
        this.location = location;
        Objects.requireNonNull(inputCargoType);
        Objects.requireNonNull(outputCargoType);
        this.inputCargo = new Cargo(inputCargoType, 0.0);
        this.outputCargo = new Cargo(outputCargoType, 0.0);
    }

    /**
     * Constructs a MixedIndustry without a preset location.
     *
     * @param industryName    name of the industry
     * @param industryCost    cost to operate the industry
     * @param production      amount of cargo produced per cycle
     * @param demand          amount of input cargo required per cycle
     * @param inputCargoType  initial cargo type for input; must not be null
     * @param outputCargoType initial cargo type for output; must not be null
     * @throws NullPointerException if any required parameter is null
     */
    public MixedIndustry(String industryName,
                         int industryCost,
                         double production,
                         double demand,
                         CargoType inputCargoType,
                         CargoType outputCargoType) {
        this(industryName, industryCost, production, demand, null,
                inputCargoType, outputCargoType);
    }

    /**
     * Returns the industry name.
     *
     * @return industry name
     */
    @Override
    public String getIndustryName() {
        return industryName;
    }

    /**
     * Returns the fixed cost to operate the industry.
     *
     * @return industry cost per cycle
     */
    @Override
    public int getIndustryCost() {
        return industryCost;
    }

    /**
     * Returns the production rate per cycle.
     *
     * @return produced cargo per cycle
     */
    @Override
    public double getProduction() {
        return production;
    }

    /**
     * Returns the input cargo required per cycle.
     *
     * @return input cargo demand per cycle
     */
    @Override
    public double getDemand() {
        return demand;
    }

    /**
     * Returns the industry’s current location on the map.
     *
     * @return location, or {@code null} if unplaced
     */
    @Override
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the industry’s location on the map.
     *
     * @param location the new location to set
     */
    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Performs the industry’s operation based on its current behavior mode.
     * <ul>
     *   <li>IMPORT: spends cost to produce output cargo</li>
     *   <li>EXPORT: removes all input cargo and awards earnings to the player</li>
     *   <li>TRANSFORM: consumes input cargo to produce output cargo</li>
     * </ul>
     *
     * @param player player performing the operation (budget is updated in IMPORT/EXPORT)
     */
    @Override
    public void produceCargo(Player player) {
        switch (behavior) {
            case IMPORT:
                if (player.spend(industryCost)) {
                    outputCargo.addQuantity(production);
                }
                break;
            case EXPORT:
                double availableInput = inputCargo.getQuantity();
                if (availableInput > 0) {
                    inputCargo.removeQuantity(availableInput);
                    player.earn(1000 * (availableInput / production));
                }
                break;
            case TRANSFORM:
                if (inputCargo.getQuantity() >= demand) {
                    inputCargo.removeQuantity(demand);
                    outputCargo.addQuantity(production);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown behavior: " + behavior);
        }
    }

    /**
     * Adds the specified quantity to the input cargo storage.
     *
     * @param quantity amount to add to input cargo
     */
    @Override
    public void consumeCargo(double quantity) {
        inputCargo.addQuantity(quantity);
    }

    /**
     * Returns the output cargo storage.
     *
     * @return the output {@link Cargo} object
     */
    @Override
    public Cargo getCargo() {
        return outputCargo;
    }

    /**
     * Returns the input cargo type.
     *
     * @return input cargo type
     */
    @Override
    public CargoType getInputCargoType() {
        return inputCargo.getType();
    }

    /**
     * Returns the input cargo quantity currently stored.
     *
     * @return input cargo quantity
     */
    @Override
    public double getInputCargoQuantity() {
        return inputCargo.getQuantity();
    }

    /**
     * Returns the output cargo type.
     *
     * @return output cargo type
     */
    @Override
    public CargoType getOutputCargoType() {
        return outputCargo.getType();
    }

    /**
     * Returns the output cargo quantity currently stored.
     *
     * @return output cargo quantity
     */
    @Override
    public double getOutputCargoQuantity() {
        return outputCargo.getQuantity();
    }

    /**
     * Sets the new production rate for this industry.
     *
     * @param production new production rate per cycle
     */
    @Override
    public void setProduction(double production) {
        this.production = production;
    }

    /**
     * Returns the current behavior mode of this industry.
     *
     * @return the {@link MixedBehavior} (IMPORT, EXPORT, or TRANSFORM)
     */
    public MixedBehavior getBehavior() {
        return behavior;
    }

    /**
     * Sets a new behavior mode for this industry.
     *
     * @param behavior the behavior to set
     */
    public void setBehavior(MixedBehavior behavior) {
        this.behavior = behavior;
    }

    /**
     * Changes the input cargo type and resets its quantity.
     *
     * @param inputCargoType new input cargo type; if null, no change
     */
    public void setInputCargo(CargoType inputCargoType) {
        if (inputCargoType != null) {
            this.inputCargo.setCargoType(inputCargoType);
            this.inputCargo.setQuantity(0);
        }
    }

    /**
     * Changes the output cargo type and resets its quantity.
     *
     * @param outputCargoType new output cargo type; if null, no change
     */
    public void setOutputCargo(CargoType outputCargoType) {
        if (outputCargoType != null) {
            this.outputCargo.setCargoType(outputCargoType);
            this.outputCargo.setQuantity(0);
        }
    }

    /**
     * Sets the quantity of input cargo directly.
     *
     * @param quantity the new quantity for input cargo
     */
    public void setInputCargoQuantity(double quantity) {
        this.inputCargo.setQuantity(quantity);
    }

    /**
     * Returns a string representation of this industry,
     * including its name, behavior, and current cargoes.
     *
     * @return formatted string with current state
     */
    @Override
    public String toString() {
        return String.format(
                "MixedIndustry[%s] Mode=%s | %s:%.2f → %s:%.2f",
                industryName,
                behavior,
                getInputCargoType(), getInputCargoQuantity(),
                getOutputCargoType(), getOutputCargoQuantity()
        );
    }
}
