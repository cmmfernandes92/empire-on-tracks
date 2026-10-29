package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Locomotive;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Singleton repository class for storing locomotive templates.
 * <p>
 * This class is responsible for managing a list of predefined locomotive templates,
 * which can be cloned to create new locomotive instances. It maintains a collection of
 * different locomotive types such as Steam, Diesel, and Electric, which can be used
 * throughout the simulation.
 * </p>
 */
public class LocomotiveRepository {
    private static LocomotiveRepository instance;
    private final List<Locomotive> templates = new ArrayList<>();

    /**
     * Private constructor to initialize default templates for locomotives.
     * It adds three templates: Steam, Diesel, and Electric locomotives.
     */
    private LocomotiveRepository() {
        // Default templates
        // Steam loco
        templates.add(new Locomotive(
                "S100", "Steam", 75000.0, false,
                2000, 0.5, 80.0, 1804,
                2.0, 500.0
        ));
        // Diesel loco
        templates.add(new Locomotive(
                "D200", "Diesel", 120000.0, false,
                3000, 0.8, 120.0, 1804,
                3.0, 700.0
        ));
        // Electric loco
        templates.add(new Locomotive(
                "E300", "Electric", 200000.0, true,
                4000, 1.0, 160.0, 1804,
                4.0, 600.0
        ));
    }

    /**
     * Returns the singleton instance of the LocomotiveRepository.
     *
     * @return The singleton instance of the LocomotiveRepository.
     */
    public static synchronized LocomotiveRepository getInstance() {
        if (instance == null) {
            instance = new LocomotiveRepository();
        }
        return instance;
    }

    /**
     * Returns a list of all registered locomotive templates.
     * This method returns a copy of the list to prevent external modification.
     *
     * @return A list of all locomotive templates.
     */
    public List<Locomotive> getAllTemplates() {
        return new ArrayList<>(templates);
    }

    /**
     * Returns a new Locomotive object based on the specified template index.
     * The returned locomotive is a clone of the selected template.
     *
     * @param index The index of the template to clone.
     * @return A clone of the locomotive template, or null if the index is invalid.
     */
    public Locomotive getTemplate(int index) {
        if (index < 0 || index >= templates.size()) {
            return null;
        }
        Locomotive tpl = templates.get(index);
        // Assuming Locomotive has a constructor that accepts all fields
        return new Locomotive(
                tpl.getId(),
                tpl.getType(),
                tpl.getPrice(),
                tpl.requiresElectrification(),
                tpl.getPower(),
                tpl.getAcceleration(),
                tpl.getTopSpeed(),
                tpl.getStartYear(),
                tpl.getFuelCostPerKm(),
                tpl.getMaintenancePerYear()
        );
    }

    /**
     * Adds a new locomotive template to the repository.
     *
     * @param loco The locomotive template to add.
     */
    public void addTemplate(Locomotive loco) {
        templates.add(Objects.requireNonNull(loco));
    }

    /**
     * Updates the locomotive template at the specified index with a new locomotive object.
     *
     * @param index The index of the template to update.
     * @param loco  The new locomotive template to replace the existing one.
     */
    public void updateTemplate(int index, Locomotive loco) {
        if (index >= 0 && index < templates.size()) {
            templates.set(index, Objects.requireNonNull(loco));
        }
    }

    /**
     * Removes the locomotive template at the specified index.
     *
     * @param index The index of the template to remove.
     */
    public void removeTemplate(int index) {
        if (index >= 0 && index < templates.size()) {
            templates.remove(index);
        }
    }
}
