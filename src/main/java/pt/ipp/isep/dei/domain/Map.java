package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.mapper.dto.IndustryDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Represents the simulation map containing stations, industries,
 * rail lines, cities, and locomotives on a 2-D grid.
 *
 * <p>Only the methods invoked by the “Add Industry” sequence diagram
 * were touched; every other member is left intact.</p>
 *
 * <ul>
 *   <li>{@link #createIndustry(Industry, Location)} –
 *       receives the freshly-built {@link Industry} and places it
 *       on the grid;</li>
 *   <li>{@link #isEmpty(int, int)} –
 *       queried beforehand by the controller to validate coordinates.</li>
 * </ul>
 *
 * All other behaviour, loops and collections stay exactly as before.
 */
public class Map implements Serializable {

    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------------
     * Fields
     * ---------------------------------------------------------------- */
    /** Unique name of the map. */
    private String name;

    /** 2-D grid holding objects placed on the map. */
    private final Object[][] map;

    /** Collection of rail lines on the map. */
    private final List<RailLine> railLines = new ArrayList<>();

    /** Collection of stations on the map. */
    private final List<Station> stations = new ArrayList<>();

    /** Collection of industries on the map. */
    private final List<Industry> industries = new ArrayList<>();

    /** Collection of locomotives on the map. */
    private final List<Locomotive> locomotives = new ArrayList<>();

    /** Optional scenario for applying dynamic events to the map. */
    private List<Scenario> scenarios = new ArrayList<>();

    private List<City> cities = new ArrayList<>();

    private Scenario currentScenario;
    /* ------------------------------------------------------------------
     * Construction
     * ---------------------------------------------------------------- */
    /** Creates a 10 × 10 map named “name”. */
    public Map() {
        this("name", 10, 10);
    }

    /**
     * Constructs a map with the given name and dimensions.
     *
     * @param name   the name of the map
     * @param width  the width of the grid (columns)
     * @param height the height of the grid (rows)
     */
    public Map(String name, int width, int height) {
        if (!isPositive(width) || !isPositive(height))           // NEW validation
            throw new IllegalArgumentException("Width and height must be positive");
        this.name = name;
        this.map = new Object[height][width];
    }

    // Método para verificar se a localização está ocupada
    public boolean isLocationOccupied(Location location) {
        // Percorrer todas as estações no mapa
        for (Station station : stations) {
            if (station.getLocation().equals(location)) {
                return true; // Se a localização for igual a uma estação existente, retorna verdadeiro
            }
        }
        return false; // Caso contrário, a localização está livre
    }


    /**
     * Helper to ensure dimensions are positive.
     */
    public static boolean isPositive(int value) {
        return value > 0;
    }


    /* ------------------------------------------------------------------
     * STATIONS  (unchanged)
     * ---------------------------------------------------------------- */
    // Adiciona uma nova estação se o local não estiver ocupado
    // Método para adicionar uma estação
    public boolean addStation(Station station) {
        Location stationLocation = station.getLocation();

        // Verifica se o local está ocupado
        if (isLocationOccupied(stationLocation)) {
            System.out.println("❌ Location is already occupied by another station.");
            return false; // Se o local estiver ocupado, retorna false
        }

        // Adiciona a estação ao mapa
        stations.add(station);
        return true; // Retorna true se a estação foi adicionada com sucesso
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<String> getAllNamesStation() {
        List<String> names = new ArrayList<>();
        for (Station s : stations) names.add(s.getStationName());
        return names;
    }

    public Station getDetails(String stationName) {
        for (Station s : stations)
            if (s.getStationName().equals(stationName)) return s;
        return null;
    }

    /* ------------------------------------------------------------------
     * RAIL LINES  (unchanged)
     * ---------------------------------------------------------------- */
    public void addRailLine(RailLine line) {
        railLines.add(line);
    }

    public List<RailLine> getRailLines() {
        return new ArrayList<>(railLines);
    }

    /* ------------------------------------------------------------------
     * INDUSTRIES  — only JavaDoc was expanded
     * ---------------------------------------------------------------- */

    /**
     * Places a concrete {@link Industry} at the specified {@link Location}.
     *
     * <p>This is the exact call made in the sequence diagram after the
     * controller has converted the chosen template into a real instance:</p>
     *
     * <pre>
     * Controller → Map : addIndustry(industry, location)
     * Map       → Industry : setLocation(...)
     * </pre>
     *
     * @param industry the concrete industry instance
     * @param location the grid position where it should be placed
     * @return {@code true} if the cell was in bounds and free; {@code false} otherwise
     */
    public boolean createIndustry(Industry industry, Location location) {
        int x = location.getX();
        int y = location.getY();

        if (!isEmpty(x, y)) {
            return false;
        }
        industry.setLocation(location);
        map[y][x] = industry;
        industries.add(industry);
        return true;
    }

    public List<Industry> getIndustries() {
        return new ArrayList<>(industries);
    }

    /* ------------------------------------------------------------------
     * CITIES  (unchanged)
     * ---------------------------------------------------------------- */
    public boolean isCityNameDuplicate(String name) {
        return cities.stream().anyMatch(city -> city.getName().equalsIgnoreCase(name));
    }

    public City create(String name, int positionX, int positionY, int houseBlockCount, String assignmentMethod,
                       List<Integer> manualPosX, List<Integer> manualPosY) {

        if (!isEmpty(positionX, positionY)) {
            throw new IllegalArgumentException("Cannot place a city at the given coordinates. Location is already occupied.");
        }
        City city = new City(name, positionX, positionY, houseBlockCount, assignmentMethod);

        city.validateName();
        city.validateHouseBlocks();

        if (assignmentMethod.equals("manual")) {
            for (int i = 0; i < houseBlockCount; i++) {
                int x = manualPosX.get(i);
                int y = manualPosY.get(i);

                if (!isEmpty(x, y)) {
                    throw new IllegalArgumentException("House block position (" + x + ", " + y + ") is already occupied.");
                }
            }
            city.createHouseBlocksManually(manualPosX, manualPosY);
        } else {
            city.createHouseBlocksAutomatically();
        }

        cities.add(city);
        return city;
    }

    public boolean removeCity(String name) {
        return cities.removeIf(city -> city.getName().equalsIgnoreCase(name));
    }

    public List<City> getCities() {
        return cities;
    }

    /* ------------------------------------------------------------------
     * SCENARIOS
     * ---------------------------------------------------------------- */
    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    // Recupera todos os cenários do mapa
    public List<Scenario> getScenarios() {
        return scenarios;
    }
    public Scenario getCurrentScenario() {
        return currentScenario;
    }

/* ------------------------------------------------------------------
     * GRID HELPERS — isEmpty expanded with JavaDoc
     * ---------------------------------------------------------------- */

    /**
     * Verifies whether the given cell is inside bounds and currently empty.
     *
     * <p>Called by the controller <em>before</em> sending the creation
     * request to guarantee that a user-chosen position is available.</p>
     *
     * @param x grid column (0-based)
     * @param y grid row    (0-based)
     * @return {@code true} if the coordinates are valid and the cell is null
     */
    public boolean isEmpty(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return false;
        }
        return map[y][x] == null;
    }

    public boolean validateCoordinates(List<Location> positions) {
        for (Location pos : positions) {
            if (pos.getX() < 0 || pos.getX() >= getWidth()
                    || pos.getY() < 0 || pos.getY() >= getHeight()) {
                return false;
            }
        }
        return true;
    }

    public int getWidth()  { return map[0].length; }
    public int getHeight() { return map.length;    }

    /* ------------------------------------------------------------------
     * SHORTEST-PATH / DIJKSTRA  (unchanged)
     * ---------------------------------------------------------------- */
    public double getShortestDistance(Station stationA, Station stationB) {
        java.util.Map<Station, List<RailLine>> adjLines = new HashMap<>();
        for (RailLine line : this.railLines) {
            Station s1 = line.getStart();
            Station s2 = line.getEnd();
            adjLines.computeIfAbsent(s1, k -> new ArrayList<>()).add(line);
            adjLines.computeIfAbsent(s2, k -> new ArrayList<>()).add(line);
        }

        java.util.Map<Station, Double> dist = new HashMap<>();
        Set<Station> visited = new HashSet<>();
        for (Station s : this.stations) dist.put(s, Double.POSITIVE_INFINITY);
        dist.put(stationA, 0.0);

        PriorityQueue<Station> pq =
                new PriorityQueue<>(java.util.Comparator.comparing(dist::get));
        pq.add(stationA);

        while (!pq.isEmpty()) {
            Station cur = pq.poll();
            if (visited.contains(cur)) continue;
            visited.add(cur);

            if (cur.equals(stationB)) return dist.get(cur);

            List<RailLine> neighbours = adjLines.getOrDefault(
                    cur, java.util.Collections.emptyList());
            for (RailLine line : neighbours) {
                Station nbr = line.getStart().equals(cur)
                        ? line.getEnd() : line.getStart();
                if (visited.contains(nbr)) continue;

                double newDist = dist.get(cur) + line.getDistance();
                if (newDist < dist.get(nbr)) {
                    dist.put(nbr, newDist);
                    pq.add(nbr);
                }
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    /* ------------------------------------------------------------------
     * STATE UPDATES  (unchanged)
     * ---------------------------------------------------------------- */
    public void update(Player player) {
        // 1) Estações coletam/distribuem primeiro
        updateStations();

        // 2) Indústrias produzem (MixedIndustry agora tem input carregado)
        for (Industry ind : industries) {
            ind.produceCargo(player);
        }

        // 3) Cidades também produzem, se for o caso
        for (City city : cities) {
            city.produceCargo(player);
        }
    }



    public void updateStations() {
        for (Station st : stations) {
            // coleta de todas as indústrias (inclui Mixed e Transforming)
            st.collectAndDistribute(new ArrayList<>(industries));
            // coleta de cidades (cada City implementa Industry se você quiser tratá-las igual)
            st.collectAndDistribute(new ArrayList<>(cities));
        }
    }



    /* ------------------------------------------------------------------
     * LOCOMOTIVES  (unchanged)
     * ---------------------------------------------------------------- */
    public void addLocomotive(Locomotive loco) {
        locomotives.add(loco);
    }

    public List<Locomotive> getLocomotives() {
        return new ArrayList<>(locomotives);
    }

    /* ------------------------------------------------------------------
     * NAME ACCESSORS  (unchanged)
     * ---------------------------------------------------------------- */
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }


}
