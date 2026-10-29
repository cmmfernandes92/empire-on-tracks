package MDISC.domain;

import java.io.PrintWriter;
import java.util.*;

/**
 * Represents the railway network as a graph structure.
 * <p>
 * Internally maintains:
 * <ul>
 *   <li><b>stationsMap</b>: normalized station-name → Station object</li>
 *   <li><b>allLines</b>: list of all Line connections added</li>
 * </ul>
 * <p>
 * Provides operations for:
 * <ul>
 *   <li>Adding new lines (auto-creating stations)</li>
 *   <li>Exporting the adjacency matrix to a file</li>
 *   <li>Retrieving sorted station and line collections</li>
 *   <li>Computing Eulerian or semi-Eulerian maintenance routes (Fleury's algorithm)</li>
 *   <li>Performing quick feasibility checks for maintenance routes</li>
 * </ul>
 * </p>
 */
public class RailwayGraph {

    /** Map from normalized station name to Station instance. */
    private final Map<String, Station> stationsMap = new HashMap<>();

    /** List of all line segments in the network. */
    private final List<Line> allLines = new ArrayList<>();

    /**
     * Normalizes a raw station token by removing an underscore after the first
     * character (e.g., "D_8" → "D8").
     *
     * @param token Raw station token to normalize
     * @return Normalized station name without leading underscores
     */
    private String normalize(String token) {
        token = token.trim();
        if (token.length() > 1 && token.charAt(1) == '_') {
            return token.substring(0, 1) + token.substring(2);
        }
        return token;
    }

    /**
     * Adds a new line to the graph. Creates Station objects if they do not
     * already exist in the internal map.
     *
     * @param rawA         Raw name of the first station endpoint
     * @param rawB         Raw name of the second station endpoint
     * @param electrified  True if the line is electrified; false otherwise
     * @param distance     Distance of the line segment in kilometers
     */
    public void addLine(String rawA, String rawB,
                        boolean electrified, double distance) {
        String aName = normalize(rawA);
        String bName = normalize(rawB);
        Station a = stationsMap.computeIfAbsent(aName, Station::new);
        Station b = stationsMap.computeIfAbsent(bName, Station::new);
        allLines.add(new Line(a, b, electrified, distance));
    }

    /**
     * Exports the adjacency matrix of the current graph to "matrix.txt".
     * <p>
     * Rows and columns are labeled by station names. Cell values indicate
     * the number of parallel connections between stations.
     * </p>
     */
    public void exportAdjacencyMatrix() {
        List<Station> st = new ArrayList<>(stationsMap.values());
        st.sort(Comparator.comparing(Station::getName));

        int n = st.size();
        Map<Station,Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) {
            idx.put(st.get(i), i);
        }

        int[][] mat = new int[n][n];
        for (Line l : allLines) {
            int i = idx.get(l.getA());
            int j = idx.get(l.getB());
            mat[i][j]++;
            mat[j][i]++;
        }

        int nameWidth = st.stream()
                .mapToInt(s -> s.getName().length())
                .max().orElse(4) + 2;
        int valueWidth = Arrays.stream(mat)
                .flatMapToInt(Arrays::stream)
                .map(v -> String.valueOf(v).length())
                .max().orElse(1) + 1;
        int w = Math.max(nameWidth, valueWidth + 1);

        String nameFmt = "%" + w + "s";
        String valFmt  = "%" + w + "d";

        try (PrintWriter out = new PrintWriter("matrix.txt")) {
            out.printf(nameFmt, "");
            for (Station s : st) {
                out.printf(nameFmt, s.getName());
            }
            out.println();

            for (int i = 0; i < n; i++) {
                out.printf(nameFmt, st.get(i).getName());
                for (int j = 0; j < n; j++) {
                    out.printf(valFmt, mat[i][j]);
                }
                out.println();
            }
            System.out.println("Matrix exported to matrix.txt");
        } catch (Exception e) {
            System.err.println("Error exporting matrix: " + e.getMessage());
        }
    }

    /**
     * Returns a list of all stations sorted by custom rules:
     * <ol>
     *   <li>Stations starting with 'D'</li>
     *   <li>Stations starting with 'S'</li>
     *   <li>Other stations</li>
     * </ol>
     * Within each group, sorts by numeric suffix or lexicographically.
     *
     * @return Sorted list of Station objects
     */
    public List<Station> getSortedStations() {
        List<Station> list = new ArrayList<>(stationsMap.values());
        list.sort((a, b) -> {
            char ca = Character.toUpperCase(a.getName().charAt(0));
            char cb = Character.toUpperCase(b.getName().charAt(0));
            int oa = (ca == 'D') ? 0 : (ca == 'S' ? 1 : 2);
            int ob = (cb == 'D') ? 0 : (cb == 'S' ? 1 : 2);
            if (oa != ob) return oa - ob;
            try {
                return Integer.parseInt(a.getName().substring(1))
                        - Integer.parseInt(b.getName().substring(1));
            } catch (NumberFormatException e) {
                return a.getName().compareTo(b.getName());
            }
        });
        return list;
    }

    /**
     * Returns an unmodifiable view of all lines in the network.
     *
     * @return List of Line objects
     */
    public List<Line> getAllLines() {
        return Collections.unmodifiableList(allLines);
    }

    /*-----------------------------------------------------------------------------*/
    /* Fleury's algorithm helpers                                                   */
    /*-----------------------------------------------------------------------------*/

    /**
     * Finds the first vertex index with degree > 0.
     *
     * @param deg Array of vertex degrees
     * @return Index of first non-isolated vertex
     * @throws IllegalStateException if there are no edges
     */
    private static int firstVertexWithDeg(int[] deg) {
        for (int i = 0; i < deg.length; i++) {
            if (deg[i] > 0) return i;
        }
        throw new IllegalStateException("Graph has no edges");
    }

    /**
     * Performs a depth-first traversal on a boolean adjacency matrix.
     *
     * @param v   Starting vertex index
     * @param adj Adjacency matrix
     * @param vis Visited marker array
     */
    private static void dfsMatrix(int v, boolean[][] adj, boolean[] vis) {
        vis[v] = true;
        for (int u = 0; u < adj.length; u++) {
            if (adj[v][u] && !vis[u]) {
                dfsMatrix(u, adj, vis);
            }
        }
    }

    /**
     * Determines if edge (u,v) is a bridge by temporarily removing it and
     * checking connectivity.
     *
     * @param u   Vertex u index
     * @param v   Vertex v index
     * @param adj Adjacency matrix
     * @param deg Degree array
     * @return True if edge is a bridge
     */
    private static boolean isBridge(int u, int v, boolean[][] adj, int[] deg) {
        if (deg[u] == 1) {
            return false; // Single edge from u can't be a bridge
        }
        adj[u][v] = adj[v][u] = false;
        boolean[] vis = new boolean[adj.length];
        dfsMatrix(u, adj, vis);
        boolean result = !vis[v];
        adj[u][v] = adj[v][u] = true;
        return result;
    }

    /*-----------------------------------------------------------------------------*/
    /* Fleury's algorithm: Eulerian trail/circuit                                    */
    /*-----------------------------------------------------------------------------*/

    /**
     * Computes an Eulerian or semi-Eulerian trail using Fleury's algorithm.
     * Modifies the adjacency matrix by removing edges as they are traversed.
     *
     * @param adj      Pre-filtered boolean adjacency matrix (size n×n)
     * @param stations Ordered list of Station objects matching matrix indices
     * @return List of stations in traversal order, or null if no trail exists
     */
    public static List<Station> findEulerRoute(boolean[][] adj,
                                               List<Station> stations) {
        if (adj == null || adj.length != stations.size()) {
            throw new IllegalArgumentException("Matrix and station list mismatch.");
        }
        int n = adj.length;
        int[] degree = new int[n];
        int edges = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adj[i][j]) {
                    degree[i]++;
                    degree[j]++;
                    edges++;
                }
            }
        }
        if (edges == 0) {
            return null;
        }
        if (!buildMaintenanceRoute(adj.clone())) {
            return null;
        }
        int start = firstVertexWithDeg(degree);
        for (int i = 0; i < n; i++) {
            if (degree[i] % 2 != 0) {
                start = i;
                break;
            }
        }
        List<Station> trail = new ArrayList<>();
        int u = start;
        while (edges > 0) {
            int next = -1;
            for (int v = 0; v < n; v++) {
                if (adj[u][v] && !isBridge(u, v, adj, degree)) {
                    next = v;
                    break;
                }
                if (adj[u][v] && next == -1) {
                    next = v;
                }
            }
            adj[u][next] = adj[next][u] = false;
            degree[u]--;
            degree[next]--;
            edges--;
            trail.add(stations.get(u));
            u = next;
        }
        trail.add(stations.get(u));
        return trail;
    }

    /**
     * Quickly checks if a maintenance route is possible by verifying
     * connectivity and the Eulerian degree condition (0 or 2 odd vertices).
     *
     * @param adj Boolean adjacency matrix (n×n)
     * @return True if route is possible; false otherwise
     */
    public static boolean buildMaintenanceRoute(boolean[][] adj) {
        if (adj == null || adj.length == 0 || adj.length != adj[0].length) {
            throw new IllegalArgumentException("Matrix must be non-empty and square.");
        }
        int n = adj.length;
        int[] degree = new int[n];
        int edges = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adj[i][j]) {
                    degree[i]++;
                    degree[j]++;
                    edges++;
                }
            }
        }
        if (edges == 0) {
            System.out.println("Graph has no edges → maintenance route not possible.");
            return false;
        }
        ConnectivityUtils.transitiveClosure(adj);
        int root = -1;
        for (int i = 0; i < n; i++) {
            if (degree[i] > 0) {
                root = i;
                break;
            }
        }
        for (int i = 0; i < n; i++) {
            if (degree[i] > 0 && !adj[root][i]) {
                System.out.println("Graph is disconnected → maintenance route not possible.");
                return false;
            }
        }
        int oddCount = 0;
        for (int d : degree) {
            if (d % 2 != 0) {
                oddCount++;
            }
        }
        if (oddCount == 0) {
            System.out.println("Graph is Eulerian (0 odd vertices) → route possible.");
            return true;
        } else if (oddCount == 2) {
            System.out.println("Graph is Semi-Eulerian (2 odd vertices) → route possible.");
            return true;
        } else {
            System.out.printf("Graph is not Eulerian (%d odd vertices) → route not possible.%n", oddCount);
            return false;
        }
    }
}

