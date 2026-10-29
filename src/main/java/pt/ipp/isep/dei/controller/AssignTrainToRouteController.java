package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.Map;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller used to assign a route to a player's train.
 * Now includes a method to find all stations reachable from a given station
 * (using BFS over the Map's railLines).
 */
public class AssignTrainToRouteController {

    private final Player player;
    private final Map map;                      // Reference to the simulation map (domain.Map)
    private final List<Station> availableStations;
    private final List<CargoType> availableCargoTypes;
    private List<PointOfRoute> currentRoute = new ArrayList<>();

    /**
     * @param player              the current player
     * @param map                 the Map object (so we can inspect its railLines)
     * @param availableStations   all stations the player can choose from
     * @param availableCargoTypes all cargo types the player can choose from
     */
    public AssignTrainToRouteController(
            Player player,
            Map map,
            List<Station> availableStations,
            List<CargoType> availableCargoTypes) {

        this.player = Objects.requireNonNull(player, "player cannot be null");
        this.map = Objects.requireNonNull(map, "map cannot be null");
        this.availableStations = Objects.requireNonNull(availableStations, "availableStations cannot be null");
        this.availableCargoTypes = Objects.requireNonNull(availableCargoTypes, "availableCargoTypes cannot be null");
    }

    /** Returns the player's owned trains. */
    public List<Train> getOwnedTrains() {
        return player.getOwnedTrains();
    }

    /** Returns all stations the player is allowed to pick from. */
    public List<Station> getAvailableStations() {
        return availableStations;
    }

    /** Returns all cargo types the player is allowed to pick from. */
    public List<CargoType> getAvailableCargoTypes() {
        return availableCargoTypes;
    }

    /**
     * Gets the distinct cargo types that this station currently supplies.
     */
    public List<CargoType> getAvailableCargoTypesForStation(Station station) {
        return station.getCargoes().stream()
                .map(Cargo::getType)
                .distinct()
                .collect(Collectors.toList());
    }

    /** Start a new, empty route in progress. */
    public void startNewRoute() {
        currentRoute.clear();
    }

    /** Add a point of route (stop) to the current route in progress. */
    public void addPointOfRoute(Station station, java.util.Map<CargoType, Integer> cargoCarriages, CargoMode mode) {
        currentRoute.add(new PointOfRoute(station, cargoCarriages, mode));
    }

    /** Returns an immutable copy of the current route in progress. */
    public List<PointOfRoute> getCurrentRoute() {
        return List.copyOf(currentRoute);
    }

    /**
     * Validate and assign the route in progress to the given train.
     * @return false if fewer than 2 points have been added; true otherwise.
     */
    public boolean validateAndAssignRoute(Train train) {
        if (currentRoute.size() < 2) {
            return false;
        }
        Route route = new Route(currentRoute);

        if (!train.canTraverseRoute(currentRoute, map.getRailLines())) {
            return false;
        }

        train.assignRoute(route);
        return true;
    }

    // ────────────────────────────────────────────────────────────────────────────────────────
    //           ◈ NEW: Find all stations reachable from a given start station (using BFS) ◈
    // ────────────────────────────────────────────────────────────────────────────────────────

    /**
     * Returns a list of all stations reachable (via one or more railLines) from the given startStation.
     * Uses a breadth-first search (BFS) over the Map's railLines graph.
     * Does NOT include startStation itself in the returned list.
     *
     * @param startStation the station from which to begin the search
     * @return all distinct reachable stations, excluding the startStation
     */
    public List<Station> getAllReachableStations(Station startStation) {
        // 1) Build an adjacency list from each RailLine (treating lines as undirected edges)
        java.util.Map<Station, List<Station>> adjacency = new java.util.HashMap<>();
        for (RailLine line : map.getRailLines()) {
            Station s1 = line.getStart();
            Station s2 = line.getEnd();

            adjacency.computeIfAbsent(s1, k -> new ArrayList<>()).add(s2);
            adjacency.computeIfAbsent(s2, k -> new ArrayList<>()).add(s1);
        }

        // 2) BFS initialization
        Set<Station> visited = new HashSet<>();
        Queue<Station> queue = new LinkedList<>();

        visited.add(startStation);
        queue.add(startStation);

        List<Station> reachable = new ArrayList<>();

        // 3) Perform BFS
        while (!queue.isEmpty()) {
            Station current = queue.poll();
            List<Station> neighbors = adjacency.getOrDefault(current, Collections.emptyList());

            for (Station neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    reachable.add(neighbor);
                }
            }
        }

        return reachable;
    }
}
