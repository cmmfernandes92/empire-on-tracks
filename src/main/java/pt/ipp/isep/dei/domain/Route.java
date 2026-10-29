package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.io.Serializable;
/**
 * Represents a route as an ordered sequence of Points-of-Route (station, cargoes, cargoMode).
 * The route must have at least two stops (Points-of-Route).
 * The route is circular by default.
 */
public class Route implements Serializable {
    private final List<PointOfRoute> pointsOfRoute;

    /**
     * Constructs a route with the given list of Points-of-Route.
     * @param pointsOfRoute list of PointOfRoute, must have size >= 2
     * @throws IllegalArgumentException if pointsOfRoute is null or has fewer than 2 elements
     */
    public Route(List<PointOfRoute> pointsOfRoute) {
        Objects.requireNonNull(pointsOfRoute, "pointsOfRoute cannot be null");
        if (pointsOfRoute.size() < 2) {
            throw new IllegalArgumentException("The route must have at least two Points-of-Route.");
        }
        this.pointsOfRoute = List.copyOf(pointsOfRoute);
    }

    /**
     * Returns the first Point-of-Route in this route.
     * @return initial PointOfRoute
     */
    public PointOfRoute getInitialStop() {
        return pointsOfRoute.get(0);
    }

    /**
     * Returns the last Point-of-Route in this route.
     * @return final PointOfRoute
     */
    public PointOfRoute getFinalStop() {
        return pointsOfRoute.get(pointsOfRoute.size() - 1);
    }

    /**
     * Returns a list of all cargo types to be picked up in this route,
     * in the order of stops (may contain duplicates).
     * @return list of cargo types (flattened)
     */
    public List<CargoType> getCargoes() {
        return pointsOfRoute.stream()
                .flatMap(por -> por.getCargoes().stream())
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns a list of all stations visited in this route,
     * in the order of stops (may contain duplicates).
     * @return list of stations
     */
    public List<Station> getStations() {
        return pointsOfRoute.stream()
                .map(PointOfRoute::getStation)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns an unmodifiable list of the Points-of-Route in this route.
     * @return unmodifiable list of Points-of-Route
     */
    public List<PointOfRoute> getPointsOfRoute() {
        return Collections.unmodifiableList(pointsOfRoute);
    }

    /**
     * Returns the number of stops (Points-of-Route) in this route.
     * @return number of stops
     */
    public int getNumberOfStops() {
        return pointsOfRoute.size();
    }

    /**
     * Returns a list where each entry is the number of cargo types to be picked up at each stop,
     * i.e., number of carriages needed per stop.
     * @return list of cargo counts per stop
     */
    public List<Integer> getCarriageCountsPerStop() {
        List<Integer> result = new ArrayList<>();
        for (PointOfRoute por : pointsOfRoute) {
            result.add(por.getCargoes().size());
        }
        return result;
    }

    /**
     * Returns a string representation of the route as a sequence of Points-of-Route.
     * @return string representation
     */
    @Override
    public String toString() {
        return pointsOfRoute.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" -> "));
    }
}
