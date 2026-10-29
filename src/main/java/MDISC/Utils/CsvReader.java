package MDISC.Utils;

import MDISC.domain.RailwayGraph;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Utility class for reading a railway graph from a CSV file.
 * <p>
 * Expected CSV format (one line per railway connection):
 * <pre>
 * StationA;StationB;Electrified;Distance
 * </pre>
 * Where:
 * <ul>
 *   <li>StationA and StationB are the station names (e.g., "D8", "S2", "T1").</li>
 *   <li>Electrified is "1" for true; any other value is treated as false.</li>
 *   <li>Distance is a number (double) representing the length in kilometres.</li>
 * </ul>
 */
public class CsvReader {

    /**
     * Loads a {@link RailwayGraph} from a CSV file located at the given path.
     * <p>
     * Each valid line in the file is parsed as a connection between two stations.
     * Malformed lines or lines with non‐numeric distance values are ignored.
     *
     * @param path the path to the CSV file containing railway connections
     * @return a populated {@code RailwayGraph} object based on the file contents;
     *         if an error occurs during reading, returns an empty graph
     */
    public static RailwayGraph loadGraph(String path) {
        RailwayGraph graph = new RailwayGraph();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 4) {
                    // Malformed line; ignore
                    continue;
                }
                String rawA = parts[0].trim();
                String rawB = parts[1].trim();
                boolean electrified = parts[2].trim().equals("1");
                double distance;
                try {
                    distance = Double.parseDouble(parts[3].trim());
                } catch (NumberFormatException e) {
                    // If distance is not a valid number, ignore this line
                    continue;
                }
                graph.addLine(rawA, rawB, electrified, distance);
            }
        } catch (Exception e) {
            System.out.println("❌ Error reading CSV: " + e.getMessage());
        }
        return graph;
    }
}
