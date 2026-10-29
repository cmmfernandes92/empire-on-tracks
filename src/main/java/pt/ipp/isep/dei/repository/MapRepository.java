package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Map;
import java.util.*;

/**
 * Repository that stores all created {@link Map} instances
 * and tracks the “current” one.
 *
 * <p><strong>New for Create-Map SD:</strong></p>
 * <ul>
 *   <li>{@link #createMap(String,int,int)} – builds a draft Map;</li>
 *   <li>{@link #isMapNameUnique(String)} – checks for duplicate names.</li>
 * </ul>
 */
public class MapRepository {

    private final List<Map> maps       = new ArrayList<>();
    private Map currentMap;


    /** Initializes with one default map. */
    public MapRepository() {
        this.currentMap = new Map();
        this.maps.add(currentMap);
    }

    /** @return currently active map */
    public Map getCurrentMap() {
        return this.currentMap;
    }

    /** Sets the active map */
    public void setCurrentMap(Map map) {
        this.currentMap = map;
    }

    /** @return all stored maps */
    public List<Map> getAllMaps() {
        return new ArrayList<>(maps);
    }

    /** @return names of all stored maps */
    public List<String> getMapsNames() {
        List<String> names = new ArrayList<>();
        for (Map m : maps) names.add(m.getName());
        return names;
    }

    /** @return map matching name, or null */
    public Map getDetails(String name) {
        for (Map m : maps)
            if (m.getName().equalsIgnoreCase(name)) return m;
        return null;
    }

    /**
     * Phase 1 – build a Map but do NOT persist it yet.
     */
    public Map createMap(String name, int width, int height) {
        return new Map(name, width, height);
    }

    /**
     * Checks that no existing map shares this name (case-insensitive).
     */
    public boolean isMapNameUnique(String name) {
        return maps.stream()
                .noneMatch(m -> m.getName().equalsIgnoreCase(name));
    }

    /**
     * Phase 2 – persist and activate the Map after confirmation.
     */
    public void saveMap(Map map) {
        maps.add(map);
        setCurrentMap(map);
    }

    /** Adds a map without changing the current one. */
    public void addMap(Map map) {
        maps.add(map);
    }
}
