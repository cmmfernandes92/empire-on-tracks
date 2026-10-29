package pt.ipp.isep.dei.domain;
import java.io.Serializable;
import pt.ipp.isep.dei.domain.ScenarioEvent;

/**
 * Defines the various types of cargo handled within the simulation.
 * <p>
 * CargoType enumerates all supported resource categories that industries,
 * stations, and locomotives can consume, produce, or transport.
 * </p>
 */
public enum CargoType implements Serializable {



    /** Coal resource cargo. */
    COAL,

    /** Steel resource cargo. */
    STEEL,

    /** Food resource cargo. */
    FOOD,

    /** Technology goods cargo. */
    TECHNOLOGY,

    /** Oil resource cargo. */
    OIL,

    /** Gas resource cargo. */
    GAS,

    /** Plastic resource cargo. */
    PLASTIC,

    /** Passenger transport cargo. */
    PASSENGERS,

    /** Mail and parcel transport cargo. */
    MAIL,

    WHEAT;
}