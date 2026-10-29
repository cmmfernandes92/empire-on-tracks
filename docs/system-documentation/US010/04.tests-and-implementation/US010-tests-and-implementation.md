# US010 - Assign a Train to a Route

## 4. Tests 

**Test 1:** Check that the Route constructor throws a NullPointerException if the stops list is null.

	@Test
    void constructor_throwsExceptionIfStopsIsNull() {
        assertThrows(NullPointerException.class, () -> new Route(null));
    }
	

## 5. Construction (Implementation)

### Class AssignTrainToRouteController 

```java

/**
 * Controller used to assign a route to a player's train.
 */
public class AssignTrainToRouteController {

    private final Player player;
    private final List<Station> availableStations;
    private final List<CargoType> availableCargoTypes;

    public AssignTrainToRouteController(Player player,
                                        List<Station> availableStations,
                                        List<CargoType> availableCargoTypes) {
        this.player = Objects.requireNonNull(player);
        this.availableStations = Objects.requireNonNull(availableStations);
        this.availableCargoTypes = Objects.requireNonNull(availableCargoTypes);
    }

    /** Returns the player's trains. */
    public List<Train> getOwnedTrains() {
        return player.getOwnedTrains();
    }

    public List<Station> getAvailableStations() {
        return availableStations;
    }

    public List<CargoType> getAvailableCargoTypes() {
        return availableCargoTypes;
    }

    /**
     * Represents a stop: station + cargo type.
     */
    public static class StationCargo {
        private final Station station;
        private final CargoType cargoType;

        public StationCargo(Station station, CargoType cargoType) {
            this.station = Objects.requireNonNull(station);
            this.cargoType = Objects.requireNonNull(cargoType);
        }

        public Station getStation() {
            return station;
        }

        public CargoType getCargoType() {
            return cargoType;
        }

        @Override
        public String toString() {
            return station.getStationName() + " (" + cargoType + ")";
        }
    }

    /**
     * Builds a Route from user input.
     * Example: "Central, COAL, Porto, STEEL, Industrial, FOOD"
     */
    public Route parseRouteFromInput(String input) {
        if (input == null || input.isBlank()) return null;
        String[] parts = input.split(",");
        if (parts.length % 2 != 0) return null;

        List<StationCargo> routeList = new ArrayList<>();
        for (int i = 0; i < parts.length; i += 2) {
            String stationName = parts[i].trim();
            String cargoName   = parts[i + 1].trim();

            Station station = availableStations.stream()
                    .filter(s -> s.getStationName().equalsIgnoreCase(stationName))
                    .findFirst().orElse(null);
            if (station == null) return null;

            CargoType ct = availableCargoTypes.stream()
                    .filter(c -> c.name().equalsIgnoreCase(cargoName))
                    .findFirst().orElse(null);
            if (ct == null) return null;

            routeList.add(new StationCargo(station, ct));
        }

        try {
            return new Route(routeList);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Assigns the route to the train at the specified index.
     */
    public boolean assignRouteToTrain(int index, Route route) {
        if (route == null) return false;
        List<Train> trains = player.getOwnedTrains();
        if (index < 0 || index >= trains.size()) return false;
        trains.get(index).assignRoute(route);
        return true;
    }
}
```

### Class Route

```java
/**
 * Represents a route as an ordered sequence of station+cargo (StationCargo) pairs.
 * The route must have at least two stops: initial and final.
 */
public class Route {
    private final List<StationCargo> stops;

    /**
     * Constructs a route with the given list of stops.
     * @param stops list of StationCargo, must have size >= 2
     * @throws IllegalArgumentException if stops is null or has fewer than 2 elements
     */
    public Route(List<StationCargo> stops) {
        Objects.requireNonNull(stops, "stops cannot be null");
        if (stops.size() < 2) {
            throw new IllegalArgumentException("The route must have at least initial and final stations");
        }
        this.stops = List.copyOf(stops);
    }

    /** Returns the station+cargo pairs in this route (immutable). */
    public List<StationCargo> getStops() {
        return Collections.unmodifiableList(stops);
    }

    /** First stop (station + cargo). */
    public StationCargo getInitialStop() {
        return stops.get(0);
    }

    /** Last stop (station + cargo). */
    public StationCargo getFinalStop() {
        return stops.get(stops.size() - 1);
    }

    /** Sequence of cargo types transported, in the order of the stops. */
    public List<CargoType> getCargoTypes() {
        return stops.stream()
                .map(StationCargo::getCargoType)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return stops.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" -> "));
    }
}
```


## 6. Integration and Demo 

* A new option on the Player menu options to "Assign Train to a Route" was added.


## 7. Observations

n/a