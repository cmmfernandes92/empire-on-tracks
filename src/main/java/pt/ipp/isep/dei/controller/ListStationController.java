package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;

import java.util.List;

/**
 * Controller responsible for managing and accessing stations on the map.
 *
 * Provides methods to retrieve station names and obtain details about specific stations.
 */
public class ListStationController {
    private final Map map;

    public ListStationController(Map map) {
        this.map = map;
    }

    /**
     * Retrieves the names of all stations on the map.
     *
     * @return a list of station names.
     */
    public List<String> getAllStations() {
        return map.getAllNamesStation();
    }

    /**
     * Gets the details of a specific station by its name.
     *
     * @param stationName The name of the station.
     * @return the corresponding Station object, or null if not found.
     */
    public Station getStationDetails(String stationName) {
        Station station = getStationByName(stationName);

        if (station == null) {
            System.out.println("Station not found.");
            return null;
        }

        if (station.getBuildings() == null || station.getBuildings().isEmpty()) {
            System.out.println("Station found, but it has no buildings.");
            return station;
        }

        return station;
    }


    /**
     * Finds a station by its name.
     *
     * @param name The name of the station.
     * @return the Station object if found, or null otherwise.
     */
    private Station getStationByName(String name) {
        for (Station s : map.getStations()) {
            if (s.getStationName().equals(name)) {
                return s;
            }
        }
        return null;
    }
}
