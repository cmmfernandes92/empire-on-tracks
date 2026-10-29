package MDISC.domain;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class providing methods to build and analyze reachability matrices
 * for railway networks (US13 requirements).
 */
public class ConnectivityUtils {

    /* -------------------------------------------------------- */
    /* Matrix construction                                      */
    /* -------------------------------------------------------- */

    /**
     * Builds an initial reachability matrix for the given stations and lines.
     * Each entry {@code m[i][j]} is set to true if there is a direct line
     * between station {@code i} and {@code j} that matches the default train type.
     * Diagonal entries (self-reachability) are also set to true.
     *
     * @param stations   List of stations, in a fixed order, to index the matrix rows/columns.
     * @param lines      List of railway lines connecting pairs of stations.
     * @param defaultType TrainType (ELECTRIC or DIESEL) indicating which lines to include.
     * @return A square boolean matrix of size {@code n x n}, where {@code n = stations.size()},
     *         with true entries for direct connections and self-reachability.
     */
    public static boolean[][] buildReachabilityMatrix(List<Station> stations,
                                                      List<Line> lines,
                                                      TrainType defaultType) {

        int n = stations.size();
        boolean[][] m = new boolean[n][n];
        Map<Station, Integer> idx = new HashMap<>();

        // Map each station to its index
        for (int i = 0; i < n; i++) {
            idx.put(stations.get(i), i);
        }

        // Mark direct connections based on train type
        for (Line l : lines) {
            if (defaultType == TrainType.ELECTRIC && !l.isElectrified()) {
                continue;
            }
            int i = idx.get(l.getA());
            int j = idx.get(l.getB());
            m[i][j] = true;
            m[j][i] = true;
        }

        // Self-reachability
        for (int i = 0; i < n; i++) {
            m[i][i] = true;
        }
        return m;
    }

    /* -------------------------------------------------------- */
    /* Warshall (transitive closure)                            */
    /* -------------------------------------------------------- */

    /**
     * Computes the transitive closure of a reachability matrix using the
     * Floyd-Warshall algorithm. The input matrix is modified in place.
     *
     * @param matrix Square boolean matrix where {@code matrix[i][j]} indicates
     *               direct reachability from {@code i} to {@code j}.
     * @return The same matrix instance after computing its transitive closure,
     *         where {@code matrix[i][j]} is true if there exists any path
     *         from {@code i} to {@code j}.
     */
    public static boolean[][] transitiveClosure(boolean[][] matrix) {
        int n = matrix.length;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (matrix[i][k]) {
                    for (int j = 0; j < n; j++) {
                        matrix[i][j] |= matrix[k][j];
                    }
                }
            }
        }
        return matrix;
    }

    /* -------------------------------------------------------- */
    /* Reachability query + result display                      */
    /* -------------------------------------------------------- */

    /**
     * Checks whether there is a path between two stations and prints the result.
     * First computes the transitive closure, then validates indices, and finally
     * displays a message indicating the presence or absence of a path.
     *
     * @param matrix   Reachability matrix (may be non-closed), which will be
     *                 closed internally.
     * @param stations List of stations corresponding to matrix indices.
     * @param srcIndex Index of the origin station in the stations list.
     * @param dstIndex Index of the destination station in the stations list.
     */
    public static void checkReachability(boolean[][] matrix,
                                         List<Station> stations,
                                         int srcIndex,
                                         int dstIndex) {

        // Compute transitive closure
        boolean[][] closure = transitiveClosure(matrix);

        int n = closure.length;
        // Validate indices
        if (srcIndex < 0 || srcIndex >= n || dstIndex < 0 || dstIndex >= n) {
            System.out.println("Invalid index.");
            return;
        }

        // Display result
        if (closure[srcIndex][dstIndex]) {
            System.out.printf("✅ Path exists between %s and %s.%n",
                    stations.get(srcIndex).getName(),
                    stations.get(dstIndex).getName());
        } else {
            System.out.printf("❌ No path between %s and %s.%n",
                    stations.get(srcIndex).getName(),
                    stations.get(dstIndex).getName());
        }
    }

    /* -------------------------------------------------------- */
    /* Pretty printing                                          */
    /* -------------------------------------------------------- */

    /**
     * Prints a numbered list of station names for user selection.
     *
     * @param stations List of stations to display.
     */
    public static void printStationList(List<Station> stations) {
        for (int i = 0; i < stations.size(); i++) {
            System.out.printf("%2d) %s%n", i, stations.get(i).getName());
        }
    }

    /* -------------------------------------------------------- */
    /* Persist closure matrix                                   */
    /* -------------------------------------------------------- */

    /**
     * Writes the transitive closure matrix to a file, labeling rows and columns
     * with station names. 1 represents reachable, 0 represents not reachable.
     *
     * @param m        Boolean closure matrix of size {@code n x n}.
     * @param stations List of stations in the same order as matrix indices.
     * @param path     File path where the output will be saved.
     */
    public static void writeClosureToFile(boolean[][] m,
                                          List<Station> stations,
                                          String path) {

        int n = stations.size();
        int maxLen = stations.stream()
                .mapToInt(s -> s.getName().length())
                .max()
                .orElse(4);
        int w = maxLen + 2;
        String nameFmt = "%" + w + "s";
        String numFmt  = "%" + w + "d";

        try (PrintWriter out = new PrintWriter(path)) {
            // Header row
            out.printf(nameFmt, "");
            for (Station s : stations) {
                out.printf(nameFmt, s.getName());
            }
            out.println();

            // Matrix rows
            for (int i = 0; i < n; i++) {
                out.printf(nameFmt, stations.get(i).getName());
                for (int j = 0; j < n; j++) {
                    out.printf(numFmt, m[i][j] ? 1 : 0);
                }
                out.println();
            }
            System.out.println("Transitive closure saved to " + path);
        } catch (Exception e) {
            System.err.println("Error writing closure.txt: " + e.getMessage());
        }
    }
}
