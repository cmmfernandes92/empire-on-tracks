# Algorithms Documentation - US13 AND US14


---
# Graph Theory Concepts

**Connected Graph**: A graph is connected if there is at least one path between every pair of vertices. In our context, it ensures you can travel (possibly through intermediate stations) from any station to any other.

**Eulerian Trail**: A trail (walk without repeating edges) that uses each edge exactly once. An undirected graph has an Eulerian trail if and only if it is connected (when ignoring isolated vertices) and has exactly 0 or 2 vertices of odd degree.

**Eulerian Circuit**: A special case of an Eulerian trail that starts and ends on the same vertex. It exists when the graph is connected and every vertex has an even degree.

**Transitive Closure**: The transitive closure of a graph’s adjacency relation captures reachability: if vertex A can reach B, and B can reach C, then A can reach C. Computing this closure lets us answer “can I get from A to C?” in one lookup.

## 1. Adjacency Matrix Construction

```java
public int[][] buildAdjacency(List<Station> stations, List<Line> lines) {
    int n = stations.size();
    int[][] adj = new int[n][n];  // initialized to zeros
    for (Line l : lines) {
        int i = stations.indexOf(l.getFrom());
        int j = stations.indexOf(l.getTo());
        adj[i][j]++;
        adj[j][i]++;
    }
    return adj;
}
```

**Concept:**

* The adjacency matrix represents the graph’s structure: each station is a vertex, each track is an edge.
* Entry `adj[i][j]` equals the number of parallel edges between vertices `i` and `j`.
* Symmetry (`adj[i][j] == adj[j][i]`) reflects the undirected nature of the network.
* This matrix serves as the basis for reachability and Eulerian computations.

---

## 2. Electrification Matrix Construction

```java
public boolean[][] buildElectrification(List<Station> stations, List<Line> lines) {
    int n = stations.size();
    boolean[][] elec = new boolean[n][n];  // false by default
    for (Line l : lines) {
        if (l.isElectrified()) {
            int i = stations.indexOf(l.getFrom());
            int j = stations.indexOf(l.getTo());
            elec[i][j] = true;
            elec[j][i] = true;
        }
    }
    return elec;
}
```

**Concept:**

* Creates a boolean overlay on the adjacency structure highlighting electrified edges.
* `elec[i][j] = true` indicates at least one electrified connection between `i` and `j`.
* Useful for filtering algorithms or coloring electrified routes in visualizations without altering the numeric adjacency counts.

---

## 3. Exporting Adjacency to Text

```java
private void exportMatrix(LineRepository repo) {
    List<Station> stations = stationRepo.loadAll();
    List<Line> lines = repo.loadAll(stations);
    int[][] m = buildAdjacency(stations, lines);
    int n = m.length;
    int w = computeColumnWidth(stations, m);  // choose width for alignment
    String labelFmt = "%" + w + "s";
    String numFmt   = "%" + w + "d";
    try (PrintWriter out = new PrintWriter("matrix.txt")) {
        out.printf(labelFmt, "");
        for (Station s : stations) out.printf(labelFmt, s.toString());
        out.println();
        for (int i = 0; i < n; i++) {
            out.printf(labelFmt, stations.get(i).toString());
            for (int j = 0; j < n; j++) {
                out.printf(numFmt, m[i][j]);
            }
            out.println();
        }
    }
}
```

**Concept:**

* Converts the numeric adjacency matrix into a readable grid, aligning columns so that station identifiers and counts line up.
* Relies on computing a uniform column width to maintain table structure.
* Bridges the gap between raw data structures and end-user consumption via a text report.

---

## 4. Reachability (Transitive Closure)

```java
private void computeClosure(int[][] m, boolean[][] r) {
    int n = r.length;
    // Initialization: direct reachability
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            r[i][j] = (i == j) || (m[i][j] > 0);
        }
    }
    // Propagate reachability through intermediates
    for (int k = 0; k < n; k++) {
        for (int i = 0; i < n; i++) {
            if (r[i][k]) {
                for (int j = 0; j < n; j++) {
                    r[i][j] = r[i][j] || r[k][j];
                }
            }
        }
    }
}
```

**Concept:**

* Starts with direct connections: each station trivially reaches itself and any station with a direct edge.
* The triple loop checks all stations `k` as potential intermediates: if `i` can reach `k` and `k` can reach `j`, then `i` can reach `j`.
* Produces a full reachability map, enabling queries about indirect connectivity without repeated searches.

---

## 5. Maintenance Route Feasibility

```java
private boolean checkEulerian(int[][] adj) {
    // 1) Remove stations with no connections
    List<Integer> keep = new ArrayList<>();
    for (int i = 0; i < adj.length; i++) {
        int sum = 0;
        for (int v : adj[i]) sum += v;
        if (sum > 0) keep.add(i);
    }
    // 2) Build reduced adjacency for connected stations
    int n = keep.size();
    int[][] m = new int[n][n];
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            m[i][j] = adj[keep.get(i)][keep.get(j)];
        }
    }
    // 3) Check connectivity using closure
    boolean[][] r = new boolean[n][n];
    computeClosure(m, r);
    boolean connected = true;
    for (int i = 0; i < n && connected; i++) {
        for (int j = 0; j < n; j++) {
            if (!r[i][j]) { connected = false; break; }
        }
    }
    // 4) Count how many stations have an odd number of tracks
    int odd = 0;
    for (int i = 0; i < n; i++) {
        int deg = 0;
        for (int v : m[i]) deg += v;
        if (deg % 2 != 0) odd++;
    }
    // 5) Eulerian if connected and odd count is 0 or 2
    return connected && (odd == 0 || odd == 2);
}
```

**Concept:**

* An **Eulerian trail** passes each edge exactly once; here, edges are the railway tracks.
* We first discard stations with no tracks (isolated vertices) since they are irrelevant to connectivity.
* **Connectivity check:** by computing the reachability closure, we ensure all remaining stations lie in one component.
* **Odd-degree count:** we sum the number of tracks at each station; graph theory dictates an Eulerian trail exists if and only if exactly 0 or 2 stations have an odd number of tracks.
* This logic identifies whether maintenance crews can traverse every track without lifting equipment or retracing steps.

---


