package MDISC.UI;

import MDISC.domain.Station;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;

import java.util.List;

/**
 * UI for visualising a railway network using GraphStream.
 *
 * <ul>
 *   <li>Nodes: each station (with label beneath).</li>
 *   <li>Edges: coloured blue if electrified; red if not.</li>
 *   <li>Layout: SpringBox (parameters adjusted to spread nodes further).</li>
 * </ul>
 */
public class GraphUI {
    private final MultiGraph graph;

    /**
     * Constructs a new GraphUI instance with the given identifier.
     * <p>
     * Initialises a {@code MultiGraph} (to allow parallel edges) and sets:
     * <ul>
     *   <li>Stylesheet: grey nodes with labels beneath, edges in CSS classes "electrified" (blue) or "non" (red).</li>
     *   <li>Quality and anti‐aliasing attributes.</li>
     *   <li>SpringBox layout parameters to spread nodes apart.</li>
     * </ul>
     *
     * @param id the identifier for the underlying {@link MultiGraph}
     */
    public GraphUI(String id) {
        // 1) Create MultiGraph (allows parallel edges)
        this.graph = new MultiGraph(id);

        // 2) Define stylesheet:
        //    - node grey, label beneath
        //    - edges with class "electrified" in blue, "non" in red
        String styleSheet =
                "node {                                   \n" +
                        "    fill-color: grey;                    \n" +
                        "    size: 20px;                          \n" +
                        "    text-size: 14;                       \n" +
                        "    text-alignment: under;               \n" +
                        "    text-color: black;                   \n" +
                        "}                                        \n" +
                        "edge.electrified {                       \n" +
                        "    fill-color: blue;                    \n" +
                        "    size: 3px;                           \n" +
                        "}                                        \n" +
                        "edge.non {                               \n" +
                        "    fill-color: red;                     \n" +
                        "    size: 3px;                           \n" +
                        "}                                        \n";
        graph.setAttribute("ui.stylesheet", styleSheet);

        // 3) Enable quality and anti‐aliasing
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        // 4) Adjust SpringBox parameters to spread nodes further
        graph.setAttribute("springbox.idealLength", 200);
        graph.setAttribute("springbox.force", 0.8);
        graph.setAttribute("springbox.quality", 1.0);
    }

    /**
     * Adds each station as a node to the UI.
     * <p>
     * If a node with the same ID already exists, it is not duplicated.
     *
     * @param stations list of {@link Station} instances
     */
    public void addStations(List<Station> stations) {
        for (Station s : stations) {
            String id = s.getName(); // e.g.: "D1", "S2", "T3"
            if (graph.getNode(id) == null) {
                Node n = graph.addNode(id);
                n.setAttribute("ui.label", id);
            }
        }
    }

    /**
     * Adds edges between stations according to the adjacency matrix and electrification matrix.
     * <p>
     * <ul>
     *   <li>If {@code adj[i][j] > 0}, creates that many parallel edges.</li>
     *   <li>Uses {@code ui.class = "electrified"} or {@code "non"} to colour via CSS.</li>
     * </ul>
     *
     * @param stations    indexed list of {@link Station} instances
     * @param adj         int[n][n] adjacency matrix with counts of connections
     * @param electrified boolean[n][n] matrix indicating which connections are electrified
     */
    public void addEdges(List<Station> stations, int[][] adj, boolean[][] electrified) {
        int n = stations.size();
        int edgeCounter = 0;

        // Iterate only for j > i to avoid duplicating undirected edges
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adj[i][j] > 0) {
                    String from = stations.get(i).getName();
                    String to   = stations.get(j).getName();
                    // If there are parallel edges, add multiple edges
                    for (int k = 0; k < adj[i][j]; k++) {
                        String edgeId = from + "_" + to + "_" + (edgeCounter++);
                        Edge e = graph.addEdge(edgeId, from, to);
                        // Set colour: check electrified[i][j]
                        if (electrified[i][j]) {
                            e.setAttribute("ui.class", "electrified");
                        } else {
                            e.setAttribute("ui.class", "non");
                        }
                    }
                }
            }
        }
    }

    /**
     * Displays the graph in a Swing window.
     * <p>
     * If {@code autoLayout} is {@code true}, the SpringBox layout is enabled to reorganise continuously.
     *
     * @param autoLayout if {@code true}, calls {@code v.enableAutoLayout()} on the viewer
     */
    public void display(boolean autoLayout) {
        Viewer v = graph.display();
        if (autoLayout) {
            v.enableAutoLayout();
        }
    }


}
