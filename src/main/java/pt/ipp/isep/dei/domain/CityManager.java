package pt.ipp.isep.dei.domain;

import java.util.List;
import java.util.Objects;

/**
 * Responsible for managing a list of cities and providing operations such as
 * finding the closest city to a given coordinate.
 * <p>
 * Though not directly used to add cities (US03), it supports scenarios where city data
 * needs to be queried efficiently after being added.
 */
public class CityManager {

    private final List<City> cities;

    /**
     * Constructs a CityManager with a given list of cities.
     *
     * @param cities the list of cities to manage (must not be null)
     * @throws NullPointerException if the cities list is null
     */
    public CityManager(List<City> cities) {
        this.cities = Objects.requireNonNull(cities, "cities cannot be null");
    }

    /**
     * Returns the closest city to the specified (x, y) coordinates,
     * based on Euclidean distance to each city's center.
     *
     * @param x the x-coordinate to compare
     * @param y the y-coordinate to compare
     * @return the closest {@link City}, or {@code null} if the city list is empty
     */
    public City getClosestCity(int x, int y) {
        if (cities.isEmpty()) {
            return null;
        }

        City closest = null;
        double bestDist = Double.MAX_VALUE;

        for (City c : cities) {
            Location center = c.getCenter();
            int dx = x - center.getX();
            int dy = y - center.getY();
            double dist = Math.hypot(dx, dy);

            if (dist < bestDist) {
                bestDist = dist;
                closest = c;
            }
        }

        return closest;
    }

    /**
     * Returns the name of the closest city to the specified (x, y) coordinates.
     *
     * @param x the x-coordinate to compare
     * @param y the y-coordinate to compare
     * @return the name of the closest city, or {@code null} if no cities exist
     */
    public String getClosestCityName(int x, int y) {
        City city = getClosestCity(x, y);
        return city != null ? city.getName() : null;
    }

    /**
     * Returns an unmodifiable copy of the list of cities managed.
     *
     * @return a read-only {@link List} of {@link City} objects
     */
    public List<City> getCities() {
        return List.copyOf(cities);
    }
}
