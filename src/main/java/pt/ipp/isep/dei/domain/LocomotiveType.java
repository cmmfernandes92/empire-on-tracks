package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.repository.LocomotiveRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.io.Serializable;

/**
 * Represents a type of locomotive.
 * <p>
 * A locomotive type is defined by its name (e.g., "Steam", "Diesel", "Electric").
 * The class provides functionality to retrieve a list of available locomotive types.
 * </p>
 */
public class LocomotiveType implements Serializable {

    private String name;

    /**
     * Constructs a LocomotiveType with the specified name.
     *
     * @param name The name of the locomotive type (e.g., "Steam", "Diesel", "Electric").
     */
    public LocomotiveType(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the locomotive type.
     *
     * @return The name of the locomotive type.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves a list of available locomotive types.
     * <p>
     * This method returns a predefined list of locomotive types:
     * "Steam", "Diesel", and "Electric".
     * </p>
     *
     * @return A list containing the available locomotive types.
     */
    public static List<LocomotiveType> getLocomotiveTypes() {
        List<LocomotiveType> types = new ArrayList<>();
        types.add(new LocomotiveType("Steam"));
        types.add(new LocomotiveType("Diesel"));
        types.add(new LocomotiveType("Electric"));
        return types;
    }

    /**
     * Returns a string representation of the locomotive type.
     * <p>
     * This method returns the name of the locomotive type as a string.
     * </p>
     *
     * @return A string representation of the locomotive type.
     */
    @Override
    public String toString() {
        return name;
    }
}
