package pt.ipp.isep.dei.domain;


import java.io.Serializable;
import java.util.*;
import java.util.Map;

/**
 * Represents a train, composed of a locomotive and carriages,
 * possibly carrying cargo and assigned to a route. Handles simulation of movement and cargo operations.
 */
public class Train implements Serializable {
    private String name;
    private String plateNumber;
    private Locomotive locomotive;
    private List<Carriage> carriages;
    private List<String> currentCargoes;
    private Route assignedRoute;

    // Train simulation
    private final double trainSpeedSimulationFactor = 0.5;
    private double distanceTraveledThisMonth = 0.0;


    /** Index in route for the current stop (station) the train is at. */
    private int currentStopIndex = 0;

    /** How many "simulation months" remain until this train reaches its next stop. */
    private double remainingTravelMonths = 0.0;

    /** The station the train is currently en route to (null if at station). */
    private Station enRouteTo = null;

    /**
     * Constructs a Train with the specified name, locomotive, carriages, and current cargoes.
     *
     * @param name           the name of the train (cannot be null)
     * @param locomotive     the locomotive associated with the train (cannot be null)
     * @param carriages      the list of carriages (cannot be null)
     * @param currentCargoes the list of current cargo types (cannot be null)
     */
    public Train(String name,
                 String plateNumber,
                 Locomotive locomotive,
                 List<Carriage> carriages,
                 List<String> currentCargoes) {
        this.name = Objects.requireNonNull(name);
        this.plateNumber = Objects.requireNonNull(plateNumber);
        this.locomotive = Objects.requireNonNull(locomotive);
        this.carriages = Objects.requireNonNull(carriages);
        this.currentCargoes = Objects.requireNonNull(currentCargoes);
    }

    /**
     * Returns the name of the train.
     *
     * @return the train name
     */
    public String getName() {
        return name;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    /**
     * Returns the type of the locomotive.
     *
     * @return the locomotive type as a string, or an empty string if locomotive is null
     */
    public String getLocomotiveType() {
        return locomotive != null ? locomotive.getType() : "";
    }

    /**
     * Returns the list of current cargo types carried by the train.
     *
     * @return the list of current cargo types
     */
    public List<String> getCurrentCargoes() {
        return currentCargoes;
    }

    /**
     * Assigns a route to the train and resets the current stop index and travel state.
     * Re-creates carriages for the new route.
     *
     * @param route the route to assign (cannot be null)
     */
    public void assignRoute(Route route) {
        this.assignedRoute = Objects.requireNonNull(route);
        List<Carriage> newCarriages = new ArrayList<>();

        // Loop through each point of route (POR) in the assigned route
        for (PointOfRoute por : route.getPointsOfRoute()) {
            // Get the cargo types and their respective carriage counts
            Map<CargoType, Integer> cargoCarriages = por.getCargoCarriages();

            // Loop through each cargo type and create the specified number of carriages
            for (Map.Entry<CargoType, Integer> entry : cargoCarriages.entrySet()) {
                CargoType cargoType = entry.getKey();
                int carriageCount = entry.getValue();

                // Create the specified number of carriages for this cargo type
                for (int i = 0; i < carriageCount; i++) {
                    newCarriages.add(new Carriage(cargoType));
                }
            }
        }
        // Set the new list of carriages
        this.carriages = newCarriages;

        // Reset other properties
        this.currentStopIndex = 0;
        this.enRouteTo = null;
        this.remainingTravelMonths = 0.0;
    }

    /**
     * Returns the route assigned to the train.
     *
     * @return the assigned route, or null if none
     */
    public Route getAssignedRoute() {
        return assignedRoute;
    }

    /**
     * Returns true if the train is currently "in transit" (moving between stations).
     *
     * @return true if the train is traveling, false if at station
     */
    public boolean isInTransit() {
        return enRouteTo != null;
    }

    /**
     * Returns the next station in the route (circular), or null if no route.
     *
     * @return the next station in the route
     */
    public Station getNextStationOnRoute() {
        if (assignedRoute == null) return null;
        int n = assignedRoute.getNumberOfStops();
        int nextIndex = (currentStopIndex + 1) % n;
        return assignedRoute.getPointsOfRoute().get(nextIndex).getStation();
    }

    /**
     * Initiates travel to the specified station, setting the travel time based on total distance and locomotive speed.
     *
     * @param nextStation the next station to travel to
     * @param distanceKm  the total distance to travel (km)
     */
    public void startTravelTo(Station nextStation, double distanceKm, boolean lineIsDouble) {

        this.enRouteTo = nextStation;

        // Calculate speed: locomotive top speed in km/h × 24 × 30 (days/month) = km/month
        double speedKmPerHr = locomotive.getTopSpeed();

        // Adjust speed based on track type (single or double)
        if (lineIsDouble) {
            // Full speed on double track
            speedKmPerHr = locomotive.getTopSpeed();
        } else {
            // 80% speed on single track
            speedKmPerHr *= 0.8;
        }

        // Calculate speed reduction due to carriages (max 30% speed reduction)
        double carriageSpeedReduction = Math.min(0.3, carriages.size() * 0.05);  // 5% reduction per carriage, max 30%
        speedKmPerHr *= (1 - carriageSpeedReduction);  // Apply the reduction

        // Calculate effective speed in km/month with Sim factor for better gameplay
        double speedKmPerMonth = speedKmPerHr * trainSpeedSimulationFactor;
        //         double speedKmPerMonth = speedKmPerHr * 24.0 * 30.0;


        double monthsNeeded = distanceKm / speedKmPerMonth;
        this.remainingTravelMonths = monthsNeeded;

        // REMOVER
        // Debug output to verify speed and calculation
        /* System.out.println("Train '" + name + "' starts travel to " + nextStation.getStationName() +
               " with speed: " + String.format("%.2f", speedKmPerHr) + " km/h, " +
               "months needed: " + String.format("%.2f", monthsNeeded)); */
    }

    /**
     * Advances the train's travel by one simulation month.
     * If in transit, decrements remaining travel months. When reaching the destination,
     * updates current stop index, clears enRouteTo, and returns true to indicate arrival.
     *
     * @return true if the train has just arrived at a stop, false otherwise
     */
    public boolean advanceTravelByOneMonth() {
        if (enRouteTo == null) return false; // Already at a station
        remainingTravelMonths -= 1.0;
        //System.out.println("DEBUG: Train '" + name + "' remaining travel months: " + remainingTravelMonths);

        if (remainingTravelMonths <= 0.0) {
            // Arrived at destination: update stop index and travel state
            List<PointOfRoute> porList = assignedRoute.getPointsOfRoute();
            for (int i = 0; i < porList.size(); i++) {
                if (porList.get(i).getStation().equals(enRouteTo)) {
                    this.currentStopIndex = i;
                    break;
                }
            }
            enRouteTo = null;
            remainingTravelMonths = 0.0;
            return true;
        }
        return false;
    }

    /**
     * Gets the list of cargo types assigned to the train's current stop,
     * if the train is at a station (not in transit).
     *
     * @return list of cargo types to pick up at the current stop,
     *         or empty list if not at a station or no route
     */
    public List<CargoType> getAssignedCargoTypesForCurrentStop() {
        if (assignedRoute == null || enRouteTo != null) {
            return List.of();  // No route or train is in transit
        }
        // Retrieve cargo types for the current stop from the assigned route
        PointOfRoute currentPOR = assignedRoute.getPointsOfRoute().get(currentStopIndex);
        // Get the keys (cargo types) from the map
        return new ArrayList<>(currentPOR.getCargoCarriages().keySet());
    }

    /**
     * Returns a string representation of the train, including its name,
     * locomotive type, and route information.
     *
     * @return a string representation of the train
     */
    @Override
    public String toString() {
        return String.format("Train[name=%s, loco=%s, route=%s]",
                name,
                locomotive.getType(),
                assignedRoute != null ? assignedRoute.toString() : "none");
    }

    /**
     * Returns true if this train currently has an assigned route.
     *
     * @return true if assignedRoute is not null
     */
    public boolean hasRoute() {
        return assignedRoute != null;
    }

    /**
     * Checks whether the train's locomotive can traverse a given route.
     * <p>
     * For electric locomotives, it ensures that every segment of the route is covered
     * by an electrified rail line between consecutive stations. For non-electric
     * locomotives, traversal is always allowed.
     *
     * @param points the list of route points representing the path (stations in order)
     * @param lines  the list of available rail lines on the map
     * @return {@code true} if the train can traverse the entire route; {@code false} otherwise
     */
    public boolean canTraverseRoute(List<PointOfRoute> points, List<RailLine> lines) {
        // Only check electrification for electric locos
        if (!locomotive.isElectric()) return true;
        for (int i = 0; i < points.size() - 1; i++) {
            Station from = points.get(i).getStation();
            Station to = points.get(i + 1).getStation();
            RailLine line = lines.stream()
                    .filter(rl -> (rl.getStart().equals(from) && rl.getEnd().equals(to)) ||
                            (rl.getStart().equals(to) && rl.getEnd().equals(from)))
                    .findFirst().orElse(null);
            if (line == null || !line.isElectrified()) {
                return false;
            }
        }
        return true;
    }


    /**
     * Returns the current stop index (0-based) within the assigned route.
     * @return the index of the stop the train is at or just arrived to
     */
    public int getCurrentStopIndex() {
        return currentStopIndex;
    }

    /**
     * Returns the total available carriage capacity for the given cargo type.
     *
     * @param ctype The cargo type.
     * @return The total available space for that cargo type in all carriages.
     */
    public int getAvailableCapacityFor(CargoType ctype) {
        int total = 0;
        for (Carriage c : carriages) {
            if (c.getCargoType() == ctype) {
                total += c.getAvailableCapacity();
            }
        }
        return total;
    }

    /**
     * Retrieves the list of active carriages assigned to the current stop
     * that are expected to handle the specified cargo type.
     *
     * @param cargoType the cargo type to match
     * @return a list of matching carriages for the current stop
     */
    // Returns the list of carriages assigned for the current stop (PointOfRoute)
    public List<Carriage> getActiveCarriagesForCurrentStop(CargoType cargoType) {
        if (assignedRoute == null) return List.of();
        PointOfRoute por = assignedRoute.getPointsOfRoute().get(currentStopIndex);
        int needed = por.getCargoCarriages().getOrDefault(cargoType, 0);
        List<Carriage> relevant = new ArrayList<>();
        List<Carriage> remaining = new ArrayList<>(carriages);

        int count = 0;
        for (Iterator<Carriage> it = remaining.iterator(); it.hasNext() && count < needed; ) {
            Carriage c = it.next();
            if (c.getCargoType() == cargoType) {
                relevant.add(c);
                it.remove();
                count++;
            }
        }
        return relevant;
    }

    /**
     * Checks whether all active carriages for the given cargo type are full.
     *
     * @param ctype the cargo type to check
     * @return {@code true} if all relevant carriages are full; {@code false} otherwise
     */
    public boolean activeCarriagesFull(CargoType ctype) {
        for (Carriage c : getActiveCarriagesForCurrentStop(ctype)) {
            if (!c.isFull()) return false;
        }
        return true;
    }

    /**
     * Counts how many active carriages for the given cargo type currently have cargo loaded.
     *
     * @param ctype the cargo type
     * @return the number of carriages that have a non-zero load
     */
    public int activeCarriagesLoadedCount(CargoType ctype) {
        int count = 0;
        for (Carriage c : getActiveCarriagesForCurrentStop(ctype)) {
            if (c.getCurrentLoad() > 0) count++;
        }
        return count;
    }

    /**
     * Returns the number of active carriages assigned to the current stop for the specified cargo type.
     *
     * @param ctype the cargo type
     * @return the number of active carriages for that type
     */
    public int activeCarriagesCount(CargoType ctype) {
        return getActiveCarriagesForCurrentStop(ctype).size();
    }

    /**
     * Checks whether any active carriage assigned to the current stop has any cargo loaded.
     *
     * @param ctype the cargo type
     * @return {@code true} if at least one carriage has a non-zero load; {@code false} otherwise
     */
    public boolean anyActiveCarriageLoaded(CargoType ctype) {
        for (Carriage c : getActiveCarriagesForCurrentStop(ctype)) {
            if (c.getCurrentLoad() > 0) return true;
        }
        return false;
    }


    /**
     * Adds the specified distance (in kilometers) to the amount traveled this month.
     *
     * @param km the distance to add
     */
    public void addDistanceTraveled(double km) {
        distanceTraveledThisMonth += km;
    }

    /**
     * Returns the total distance traveled this month and resets the counter to zero.
     *
     * @return the distance traveled this month before resetting
     */
    public double consumeAndResetDistanceTraveledThisMonth() {
        double traveled = distanceTraveledThisMonth;
        distanceTraveledThisMonth = 0.0;
        return traveled;
    }

    /**
     * Loads up to the specified quantity of cargo into available carriages of the given type.
     * Returns the actual amount loaded.
     *
     * @param ctype The cargo type to load.
     * @param qty   The amount to load.
     * @return The amount actually loaded.
     */
    public int loadCargo(CargoType ctype, int qty) {
        int leftToLoad = qty;
        for (Carriage c : carriages) {
            if (c.getCargoType() == ctype && leftToLoad > 0) {
                int loaded = c.loadCargo(leftToLoad);
                leftToLoad -= loaded;
            }
        }
        return qty - leftToLoad; // amount actually loaded
    }
    public List<Carriage> getCarriages() {
        return carriages;
    }

    public double getRemainingTravelMonths() {
        return remainingTravelMonths;
    }

    public Locomotive getLocomotive() {
        return locomotive;
    }
}

