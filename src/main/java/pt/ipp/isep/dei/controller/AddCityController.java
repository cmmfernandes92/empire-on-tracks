package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.HouseBlock;
import pt.ipp.isep.dei.domain.Location;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;

/**
 * Controller for the "Add City" use case (US03).
 * <p>
 * Responsible for managing the creation of a new {@link City} in a specified
 * XY position on the map. It includes validation for city names (AC01),
 * house block assignment either manually or automatically (AC02),
 * and persistence through the {@link Map} entity.
 */
public class AddCityController {

    private Map map;

    /**
     * Constructs the controller with the given map.
     *
     * @param map The map where the city will be added.
     */
    public AddCityController(Map map) {
        this.map = map;
    }

    /**
     * Gets the currently selected map.
     *
     * @return the map instance associated with this controller.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Checks if the given city name already exists in the map.
     *
     * @param name the name of the city to validate.
     * @return true if the name is already in use; false otherwise.
     */
    public boolean isCityNameDuplicate(String name) {
        return map.isCityNameDuplicate(name);
    }

    /**
     * Creates a new city on the map with the provided data.
     * <p>
     * Validates name duplication and delegates the creation to the map.
     * <p>
     * The house blocks can be assigned:
     * <ul>
     *   <li>Manually, using given coordinate lists.</li>
     *   <li>Automatically, using a normal distribution around the city's position.</li>
     * </ul>
     *
     * @param name             the name of the city (must not contain digits or special characters - AC01)
     * @param positionX        the X coordinate of the city location
     * @param positionY        the Y coordinate of the city location
     * @param houseBlockCount  number of house blocks to assign
     * @param assignmentMethod method of house block assignment ("manual" or "auto") - AC02
     * @param manualPosX       list of X positions (used only if method is "manual")
     * @param manualPosY       list of Y positions (used only if method is "manual")
     * @return the created {@link City} object
     */
    public City createCity(String name, int positionX, int positionY, int houseBlockCount, String assignmentMethod,
                           List<Integer> manualPosX, List<Integer> manualPosY) {
        if (isCityNameDuplicate(name)) {
            System.out.println("City name already exists.");
        }

        return map.create(name, positionX, positionY, houseBlockCount, assignmentMethod, manualPosX, manualPosY);
    }

    /**
     * Removes a city from the map by its name.
     *
     * @param name the name of the city to remove
     * @return true if the city was successfully removed; false otherwise
     */
    public boolean removeCity(String name) {
        return map.removeCity(name);
    }
}
