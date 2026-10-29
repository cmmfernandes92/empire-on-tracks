package MDISC.repo;

import MDISC.domain.RailwayGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Repository responsible for loading lines (edges) into a {@link RailwayGraph}.
 * Expects a CSV file where each record has the format:
 *   stationA;stationB;electrifiedFlag;distance
 *
 * Instead of returning a {@code List<Line>}, this repository injects edges directly
 * into the provided {@link RailwayGraph}.
 */
public class LineRepository {
    private final String linesFile;
    private final boolean onlyElectrified;

    /**
     * Constructs a new LineRepository.
     *
     * @param linesFile        the path to the CSV file containing line definitions; must not be null
     * @param onlyElectrified  if {@code true}, only electrified lines will be loaded; if {@code false}, all lines will be loaded
     * @throws NullPointerException if {@code linesFile} is null
     */
    public LineRepository(String linesFile, boolean onlyElectrified) {
        this.linesFile = Objects.requireNonNull(linesFile, "linesFile must not be null");
        this.onlyElectrified = onlyElectrified;
    }

    /**
     * Reads each line from the CSV file and, for each valid record, invokes {@code graph.addLine(...)}.
     * Lines that are malformed or whose distance cannot be parsed as a number are ignored.
     * If {@code onlyElectrified} is {@code true}, non-electrified lines are skipped.
     *
     * @param graph the {@link RailwayGraph} into which edges will be added
     * @throws FileNotFoundException if the CSV file {@code linesFile} cannot be found
     */
    public void loadAll(RailwayGraph graph) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File(linesFile))) {
            while (sc.hasNextLine()) {
                String[] p = sc.nextLine().split(";");
                if (p.length < 4) {
                    continue; // ignore malformed lines
                }
                // --- extract data
                String rawA = p[0].trim();   // e.g. "D8" or "S_8"
                String rawB = p[1].trim();   // e.g. "D7" or "D_7"
                boolean electrified = p[2].trim().equals("1");
                double distance;
                try {
                    distance = Double.parseDouble(p[3].trim());
                } catch (NumberFormatException e) {
                    continue; // ignore if distance is not a valid number
                }
                // --- if only electrified lines are required and this is not, skip
                if (onlyElectrified && !electrified) {
                    continue;
                }
                // --- add the line to the graph
                graph.addLine(rawA, rawB, electrified, distance);
            }
        }
    }
}
