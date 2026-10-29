package pt.ipp.isep.dei.domain;
import java.io.Serializable;
import pt.ipp.isep.dei.domain.ScenarioEvent;

/**
 * Enum representing types of buildings for station upgrades.
 */
public enum BuildingType implements Serializable {
    TELEGRAPH,
    TELEPHONE,
    SMALL_CAFE,
    LARGE_CAFE,
    CUSTOMS,
    POST_OFFICE,
    SMALL_HOTEL,
    LARGE_HOTEL,
    SILO,
    LIQUID_STORAGE
}
