package MDISC.Controller;

import MDISC.domain.Line;
import MDISC.domain.RailwayGraph;
import MDISC.domain.Station;
import MDISC.domain.TrainType;
import MDISC.repo.LineRepository;
import MDISC.repo.StationRepository;
import MDISC.domain.ConnectivityUtils;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.io.*;
import java.util.*;

/**
 * Main controller for the railway application. Handles loading of station and line data,
 * user interaction, and provides features such as exporting adjacency matrices,
 * checking connectivity, displaying graphs, and finding optimal routes.
 */
public class AppController {

    /** The railway graph representation. */
    private final RailwayGraph graph;

    /** Whether only electrified lines should be considered. */
    private final boolean onlyElectrified;

    /** The default train type based on electrification preference. */
    private final TrainType defaultTrainType;

    /* ────────────────────────────────────────────────────────── */

    /**
     * Constructs the application controller by loading station and line data.
     *
     * @param stationsCsvPath Path to the CSV file containing station definitions.
     * @param linesCsvPath    Path to the CSV file containing railway line definitions.
     * @param onlyElectrified If true, only electrified lines are loaded and used.
     */
    public AppController(String stationsCsvPath,
                         String linesCsvPath,
                         boolean onlyElectrified) {

        this.onlyElectrified  = onlyElectrified;
        this.defaultTrainType = onlyElectrified ? TrainType.ELECTRIC : TrainType.DIESEL;
        this.graph            = new RailwayGraph();

        try {
            StationRepository stationRepo = new StationRepository(stationsCsvPath);
            stationRepo.loadAll();

            LineRepository lineRepo = new LineRepository(linesCsvPath, onlyElectrified);
            lineRepo.loadAll(graph);
        } catch (Exception e) {
            System.err.println("Error loading files: " + e.getMessage());
        }
    }

    /* ────────────────────────────────────────────────────────── */

    /**
     * Result holder for Dijkstra's algorithm, containing the computed path and total distance.
     */
    private static class DijkstraResult {
        final List<Station> path;
        final double distance;

        /**
         * Initializes a DijkstraResult with a path and its associated distance.
         *
         * @param path     Sequence of stations in the shortest path.
         * @param distance Total distance of the path.
         */
        DijkstraResult(List<Station> path, double distance) {
            this.path = path;
            this.distance = distance;
        }
    }

    /* ────────────────────────────────────────────────────────── */

    /**
     * Starts the interactive console menu for user commands.
     */
    public void startInteractive() {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n========= MAIN MENU =========");
            System.out.println("| Electrified only: " + (onlyElectrified ? "YES" : "NO") + "         |");
            System.out.println("-----------------------------");
            System.out.println(" 1) Export adjacency matrix to file");
            System.out.println(" 2) Check reachability A→B and save closure");
            System.out.println(" 3) Display full network graph");
            System.out.println(" 4) Find maintenance route");
            System.out.println(" 5) Find shortest path between two stations");
            System.out.println(" 0) Exit");
            System.out.print("Select an option: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1  -> exportMatrix();
                case 2  -> checkReachabilityUS13(sc);
                case 3  -> displayGraph();
                case 4  -> findMaintenanceRoute();
                case 5  -> findFastestPath(sc);
                case 0  -> System.out.println("Exiting...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);

        sc.close();
    }

    /**
     * Exports the adjacency matrix of the current graph to a file.
     */
    private void exportMatrix() {
        graph.exportAdjacencyMatrix();
    }

    /**
     * Performs connectivity check between stations, measures execution time, saves results,
     * and optionally checks reachability between two user-specified stations.
     *
     * @param sc Scanner for reading user input.
     */
    private void checkReachabilityUS13(Scanner sc) {
        System.out.println("\n--- Connectivity Check (US13) ---");

        long startTime = System.nanoTime();

        List<Station> stations = graph.getSortedStations();
        ConnectivityUtils.printStationList(stations);

        boolean[][] matrix = ConnectivityUtils.buildReachabilityMatrix(
                stations,
                graph.getAllLines(),
                defaultTrainType
        );

        long endTime = System.nanoTime();
        double seconds = (endTime - startTime) / 1_000_000_000.0;
        saveExecutionTimeToFile(
                "us13_execution_times.txt",
                stations.size(),
                seconds
        );

        System.out.printf("Execution time for connectivity check (US13): %.10f seconds.%n", seconds);

        ConnectivityUtils.writeClosureToFile(matrix, stations, "closure.txt");

        System.out.print("Origin (index): ");
        int src = sc.nextInt();
        System.out.print("Destination (index): ");
        int dst = sc.nextInt();
        sc.nextLine();

        ConnectivityUtils.checkReachability(matrix, stations, src, dst);
    }

    /**
     * Displays the entire railway network as a graph using GraphStream.
     */
    private void displayGraph() {
        MultiGraph gs = new MultiGraph("Railway");

        String styleSheet =
                "node { fill-color: grey; size: 20px; text-size: 14; text-alignment: under; text-color: black; }" +
                        "edge.electrified { fill-color: blue; size: 3px; }" +
                        "edge.non { fill-color: red; size: 3px; }";
        gs.setAttribute("ui.stylesheet", styleSheet);
        gs.setAttribute("ui.quality");
        gs.setAttribute("ui.antialias");
        gs.setAttribute("springbox.idealLength", 200);
        gs.setAttribute("springbox.force", 0.8);
        gs.setAttribute("springbox.quality", 1.0);

        int edgeCounter = 0;
        for (Line l : graph.getAllLines()) {
            String a = l.getA().getName();
            String b = l.getB().getName();

            if (gs.getNode(a) == null) {
                Node nA = gs.addNode(a);
                nA.setAttribute("ui.label", a);
            }
            if (gs.getNode(b) == null) {
                Node nB = gs.addNode(b);
                nB.setAttribute("ui.label", b);
            }

            String id = a + "_" + b + "_" + edgeCounter++;
            Edge e = gs.addEdge(id, a, b);
            e.setAttribute("ui.class", l.isElectrified() ? "electrified" : "non");
        }
        gs.display(true);
    }

    /**
     * Displays the railway network highlighting a specific route.
     *
     * @param route List of stations representing the path to highlight.
     */
    private void displayGraphRoute(List<Station> route) {
        MultiGraph gs = new MultiGraph("Railway");

        String styleSheet =
                "node { fill-color: grey; size: 20px; text-size: 14; " +
                        "text-alignment: under; text-color: black; }" +
                        "edge.electrified { fill-color: blue;  size: 3px; }" +
                        "edge.non         { fill-color: red;   size: 3px; }" +
                        "edge.path        { fill-color: #00c000; size: 5px; }";
        gs.setAttribute("ui.stylesheet", styleSheet);
        gs.setAttribute("ui.quality");
        gs.setAttribute("ui.antialias");
        gs.setAttribute("springbox.idealLength", 200);
        gs.setAttribute("springbox.force", 0.8);
        gs.setAttribute("springbox.quality", 1.0);

        int edgeCounter = 0;
        for (Line l : graph.getAllLines()) {
            String a = l.getA().getName();
            String b = l.getB().getName();

            if (gs.getNode(a) == null) {
                Node nA = gs.addNode(a);
                nA.setAttribute("ui.label", a);
            }
            if (gs.getNode(b) == null) {
                Node nB = gs.addNode(b);
                nB.setAttribute("ui.label", b);
            }

            String id = a + "_" + b + "_" + edgeCounter++;
            Edge e = gs.addEdge(id, a, b);
            e.setAttribute("ui.class", l.isElectrified() ? "electrified" : "non");
        }

        if (route != null && route.size() > 1) {
            int id = 0;
            for (int i = 0; i < route.size() - 1; i++) {
                String a = route.get(i).getName();
                String b = route.get(i + 1).getName();

                if (gs.getNode(a) == null) {
                    Node nA = gs.addNode(a);
                    nA.setAttribute("ui.label", a);
                }
                if (gs.getNode(b) == null) {
                    Node nB = gs.addNode(b);
                    nB.setAttribute("ui.label", b);
                }

                String edgeId = "path_" + (id++);
                Edge e = gs.addEdge(edgeId, a, b, false);
                e.setAttribute("ui.class", "path");
            }
        }

        gs.display(true);
    }

    /**
     * Displays only the stations in a given trail as a simplified graph.
     *
     * @param trail List of stations defining the route.
     */
    private void displayRouteGraph(List<Station> trail) {
        MultiGraph gs = new MultiGraph("RouteOnly");

        String css = """
        node { fill-color: grey; size:20px; text-alignment: under; text-size:14; }
        edge.electrified { fill-color: blue; size:4px; }
        edge.non { fill-color: red; size:4px; }
        """;
        gs.setAttribute("ui.stylesheet", css);
        gs.setAttribute("ui.quality");
        gs.setAttribute("ui.antialias");
        gs.setAttribute("springbox.idealLength", 200);
        gs.setAttribute("springbox.force", 0.8);
        gs.setAttribute("springbox.quality", 1.0);

        for (Station s : trail) {
            if (gs.getNode(s.getName()) == null) {
                Node n = gs.addNode(s.getName());
                n.setAttribute("ui.label", s.getName());
            }
        }

        int id = 0;
        for (int i = 0; i < trail.size() - 1; i++) {
            String a = trail.get(i).getName();
            String b = trail.get(i + 1).getName();

            boolean electrified = false;
            for (Line l : graph.getAllLines()) {
                boolean same = (l.getA().getName().equals(a) && l.getB().getName().equals(b))
                        || (l.getA().getName().equals(b) && l.getB().getName().equals(a));
                if (same) { electrified = l.isElectrified(); break; }
            }
            Edge e = gs.addEdge("r" + (id++), a, b, false);
            e.setAttribute("ui.class", electrified ? "electrified" : "non");
        }
        gs.display(true);
    }

    /**
     * Finds and displays an Eulerian maintenance route over all reachable edges.
     * Also prints execution time for the operation.
     */
    private void findMaintenanceRoute() {
        long startTime = System.nanoTime();

        List<Station> stations = graph.getSortedStations();
        boolean[][] adj = ConnectivityUtils.buildReachabilityMatrix(
                stations,
                graph.getAllLines(),
                onlyElectrified ? TrainType.ELECTRIC : TrainType.DIESEL
        );

        if (!graph.buildMaintenanceRoute(adj)) return;

        List<Station> trail = graph.findEulerRoute(adj, stations);
        if (trail == null) {
            System.out.println("Error.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < trail.size(); i++) {
            if (i > 0) sb.append(" → ");
            sb.append(trail.get(i).getName());
        }

        System.out.println("🔧 Maintenance route:");
        System.out.println(sb.toString());
        displayRouteGraph(trail);

        long endTime = System.nanoTime();
        double seconds = (endTime - startTime) / 1_000_000_000.0;
        System.out.printf("Execution time for maintenance route (US14): %.10f seconds.%n", seconds);
    }

    /**
     * Prompts the user for an integer input with a custom prompt message.
     *
     * @param sc     Scanner instance for input.
     * @param prompt Message to display before reading input.
     * @return The integer entered by the user.
     */
    private int askInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        int v = sc.nextInt(); sc.nextLine();
        return v;
    }

    /**
     * Prints a numbered list of stations for user selection.
     *
     * @param st List of stations to display.
     */
    private void printStationMenu(List<Station> st) {
        for (int i = 0; i < st.size(); i++)
            System.out.printf("%2d) %s%n", i, st.get(i).getName());
    }

    /**
     * Builds a list of intermediate stops including origin and destination.
     *
     * @param sc  Scanner for input.
     * @param st  List of all stations.
     * @param src Index of origin station.
     * @param dst Index of destination station.
     * @param k   Number of additional intermediate stops.
     * @return List of stations representing the full route including stops.
     */
    private List<Station> buildStops(Scanner sc, List<Station> st,
                                     int src, int dst, int k) {
        List<Station> pts = new ArrayList<>();
        pts.add(st.get(src));
        for (int i = 0; i < k; i++)
            pts.add(st.get(askInt(sc, "Stop " + (i + 1) + " (index): ")));
        pts.add(st.get(dst));
        return pts;
    }

    /**
     * Appends a segment path to the full path, avoiding duplication of the start station.
     *
     * @param full The full path being constructed.
     * @param seg  Segment path to append.
     */
    private void appendSegment(List<Station> full, List<Station> seg) {
        if (full.isEmpty()) full.addAll(seg);
        else full.addAll(seg.subList(1, seg.size()));
    }

    /**
     * Prints a route in a semicolon-separated format and shows its total distance.
     *
     * @param path List of stations in the route.
     * @param km   Total distance of the route in kilometers.
     * @return String representation of the route for logging or further use.
     */
    private String printRoute(List<Station> path, double km) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) sb.append(';');
            sb.append(path.get(i).getName());
        }
        String route = sb.toString();
        System.out.printf("Route [%s] with length %.0f km;%n", route, km);
        return route;
    }

    /**
     * Finds the fastest path between two stations, optionally with intermediate stops,
     * using Dijkstra's algorithm, and displays the result.
     *
     * @param sc Scanner for reading user selections.
     */
    private void findFastestPath(Scanner sc) {
        List<Station> stations = graph.getSortedStations();
        printStationMenu(stations);

        int src = askInt(sc, "Origin (index): ");
        int dst = askInt(sc, "Destination (index): ");
        int k   = askInt(sc, "How many intermediate stops would you like to add? ");

        List<Station> stops = buildStops(sc, stations, src, dst, k);

        int n = stations.size();
        boolean[][] allowed = new boolean[n][n];
        double[][] weight  = new double[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(weight[i], Double.POSITIVE_INFINITY);
        }
        for (Line l : graph.getAllLines()) {
            if (defaultTrainType == TrainType.ELECTRIC && !l.isElectrified()) continue;
            int a = stations.indexOf(l.getA());
            int b = stations.indexOf(l.getB());
            allowed[a][b] = allowed[b][a] = true;
            weight[a][b]  = weight[b][a]  = l.getDistance();
        }

        List<Station> fullPath = new ArrayList<>();
        double totalDist = 0;

        for (int i = 0; i < stops.size() - 1; i++) {
            int iSrc = stations.indexOf(stops.get(i));
            int iDst = stations.indexOf(stops.get(i + 1));
            DijkstraResult seg = dijkstra(iSrc, iDst, allowed, weight, stations);
            if (seg == null) {
                System.out.printf("❌ No path between %s and %s%n",
                        stops.get(i).getName(), stops.get(i + 1).getName());
                return;
            }
            appendSegment(fullPath, seg.path);
            totalDist += seg.distance;
        }

        if (fullPath.isEmpty()) {
            System.out.println("❌ Unable to determine the path.");
            return;
        }
        printRoute(fullPath, totalDist);
        displayGraphRoute(fullPath);
    }


    /**
     * Executes Dijkstra's algorithm on the given graph matrices to find the shortest path.
     *
     * @param src      Index of the source station.
     * @param dst      Index of the destination station.
     * @param allowed  Adjacency matrix indicating allowable edges.
     * @param weight   Matrix of edge weights (distances).
     * @param stations List of all stations.
     * @return DijkstraResult containing the path and distance, or null if unreachable.
     */
    private DijkstraResult dijkstra(int src,
                                    int dst,
                                    boolean[][] allowed,
                                    double[][] weight,
                                    List<Station> stations) {
        int n = allowed.length;
        double[] dist  = new double[n];
        int[]    prev  = new int[n];
        boolean[] used = new boolean[n];

        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);
        dist[src] = 0;

        for (int it = 0; it < n; it++) {
            int u = -1;
            double best = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                if (!used[i] && dist[i] < best) {
                    best = dist[i];
                    u = i;
                }
            }
            if (u == -1 || u == dst) break;
            used[u] = true;

            for (int v = 0; v < n; v++) {
                if (allowed[u][v]) {
                    double alt = dist[u] + weight[u][v];
                    if (alt < dist[v]) {
                        dist[v] = alt;
                        prev[v] = u;
                    }
                }
            }
        }

        if (Double.isInfinite(dist[dst])) return null;

        List<Station> path = new ArrayList<>();
        for (int at = dst; at != -1; at = prev[at]) {
            path.add(stations.get(at));
        }
        Collections.reverse(path);
        return new DijkstraResult(path, dist[dst]);
    }

    /**
     * Appends execution time data to a log file for performance tracking.
     *
     * @param filename Name of the file to append to.
     * @param size     Problem size (e.g., number of stations).
     * @param seconds  Elapsed time in seconds.
     */
    private void saveExecutionTimeToFile(String filename, int size, double seconds) {
        try (PrintWriter w = new PrintWriter(new FileWriter(filename, true))) {
            w.printf("%d\t%.10f%n", size, seconds);
        } catch (IOException e) {
            System.err.println("Error writing execution times: " + e.getMessage());
        }
    }
}
