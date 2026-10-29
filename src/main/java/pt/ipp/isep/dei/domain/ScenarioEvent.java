package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.repository.IndustryRepository;
import pt.ipp.isep.dei.repository.LocomotiveRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a scenario event that can modify the simulation state,
 * such as updating maps, industries, or locomotives.
 * <p>
 * Implementations encapsulate actions that alter the repositories
 * or the in-memory simulation entities.
 * </p>
 */
public interface ScenarioEvent extends Serializable {

    /**
     * Applies this event to the given map and list of locomotives.
     *
     * @param map         the {@link Map} instance to modify
     * @param locomotives the list of {@link Locomotive} instances to update
     */
    void apply(Map map, LocomotiveCatalog locomotives);

/**
 * Executes this event using the provided repositories to update templates.
 *
 * @param industryRepo   the {@link IndustryRepository} to modify industry data
 * @param locomotiveRepo the {@link LocomotiveRepository} to modify locomotive data
 */
}