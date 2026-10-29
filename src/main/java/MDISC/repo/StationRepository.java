package MDISC.repo;

import MDISC.domain.Station;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Repository responsible for loading stations from a CSV file.
 *
 * Each token in the CSV should have the format:
 *   &lt;Type&gt;&lt;Name&gt;
 * where:
 *   – &lt;Type&gt; is exactly the first character ('D', 'S' or 'T')
 *   – &lt;Name&gt; is the complete identifier (which may contain digits, letters, etc.)
 *
 * Examples of valid tokens: "D8", "S2", "T10", "D12", "S_StationX" (if underscores are desired within the name).
 */
public class StationRepository {
    private final String stationsFile;

    /**
     * Constructs a new StationRepository.
     *
     * @param stationsFile the path to the CSV file containing station tokens; must not be null
     * @throws NullPointerException if {@code stationsFile} is null
     */
    public StationRepository(String stationsFile) {
        this.stationsFile = Objects.requireNonNull(stationsFile, "stationsFile must not be null");
    }

    /**
     * Reads the single line from the station file, splits it into tokens by ';' and, for each token:
     * <ol>
     *   <li>Checks whether the first character is 'D', 'S' or 'T'</li>
     *   <li>Uses the entire token (for example "D8", "S2", "T10") as the station name</li>
     *
     * </ol>
     *
     * @return a list of {@link Station}, sorted first by type (as determined by {@code StationType.fromName}),
     *         then by name in lexicographical order
     * @throws FileNotFoundException if the file {@code stationsFile} cannot be found
     */
    public List<Station> loadAll() throws FileNotFoundException {
        List<Station> stations = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(stationsFile))) {
            if (!sc.hasNextLine()) {
                return stations;
            }
            String line = sc.nextLine();
            for (String token : line.split(";")) {
                token = token.trim();
                if (token.isEmpty()) {
                    continue;
                }
                char prefix = Character.toUpperCase(token.charAt(0));
                if (prefix != 'D' && prefix != 'S' && prefix != 'T') {
                    // Ignore tokens whose first character is not D, S or T
                    continue;
                }
                // Use the entire token as the station name. E.g.: "D8", "S2", "T10".
                Station s = new Station(token);
                stations.add(s);
            }
        }
        // Sort first by type (as detected in StationType.fromName), then by name lexicographically
        stations.sort(Comparator
                .comparing(Station::getType)
                .thenComparing(Station::getName));
        return stations;
    }
}
