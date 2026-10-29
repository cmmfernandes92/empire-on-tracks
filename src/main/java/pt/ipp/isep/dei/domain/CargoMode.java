package pt.ipp.isep.dei.domain;
import java.io.Serializable;
import pt.ipp.isep.dei.domain.ScenarioEvent;

/**
 * Represents the cargo loading mode for a Point-of-Route.
 */
public enum CargoMode implements Serializable {
    FULL,      // Train only departs when fully loaded
    HALF,      // Train departs when half of carriages are loaded
    AVAILABLE  // Train departs with available cargoes in the station
}