package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.repository.LocomotiveRepository;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Catalog that stores and manages locomotives owned or available in the simulation.
 *
 * It allows adding locomotives and retrieving either:
 * - all locomotives directly added to the catalog, or
 * - available locomotive templates (from the repository).
 */
public class LocomotiveCatalog implements Serializable {

    private List<Locomotive> allLocomotives = new ArrayList<>();

    /**
     * Adds a locomotive to the internal catalog.
     *
     * @param loco the locomotive to add
     */
    public void addLocomotive(Locomotive loco) {
        allLocomotives.add(loco);
    }

    /**
     * Returns a list of locomotives available to the user, as defined
     * by the global {@link LocomotiveRepository}.
     *
     * @return list of available locomotive templates
     */
    public List<Locomotive> getAvailableLocomotives() {
        // In future: filter based on scenario
        return LocomotiveRepository.getInstance().getAllTemplates();
    }

    /**
     * Returns the list of all locomotives added to the catalog.
     *
     * @return list of owned locomotives
     */
    public List<Locomotive> getAllLocomotives() {
        return allLocomotives;
    }
}
