import java.util.*;

/**
 * Handles performance experiments on graphs of different sizes.
 */
public class Experiment {

    // Results: "graphLabel_algorithm" -> time in nanoseconds
    private Map<String, Long> results = new LinkedHashMap<>();

    /**
     * Runs BFS, DFS, and Dijkstra on the given graph (from vertex 0)
     * and stores execution times under the provided label.
     */
    public void runTraversals(Graph g, String label) {
        int startVertex = 0;

        // ---- BFS timing ----
        long bfsStart = System.nanoTime();
        g.bfs(startVertex);
        long bfsEnd = System.nanoTime();
        results.put(label + "_BFS", bfsEnd - bfsStart);

        // ---- DFS timing ----
        long dfsStart = System.nanoTime();
        g.dfs(startVertex);
        long dfsEnd = System.nanoTime();
        results.put(label + "_DFS", dfsEnd - dfsStart);

        // ---- Dijkstra timing ----
        long dijkstraStart = System.nanoTime();
        g.dijkstra(startVertex);
        long dijkstraEnd = System.nanoTime();
        results.put(label + "_Dijkstra", dijkstraEnd - dijkstraStart);
    }

    /**
     * Creates and tests graphs of sizes 10, 30, and 100.
     */
    public void runMultipleTests() {
        int[] sizes = {10, 30, 100};
        for (int size : sizes) {
            Graph g = buildRandomGraph(size);
            String label = size + " vertices";
            System.out.println("\n=== Graph: " + label + " ===");
            runTraversals(g, label);
        }
    }

    /**
     * Prints a formatted table of all recorded results.
     */
    public void printResults() {
        System.out.println("\n========== Performance Results ==========");
        System.out.printf("%-30s %20s%n", "Test", "Time (ns)");
        System.out.println("-".repeat(52));
        for (Map.Entry<String, Long> entry : results.entrySet()) {
            System.out.printf("%-30s %20d ns%n", entry.getKey(), entry.getValue());
        }
        System.out.println("=========================================");
    }

    /**
     * Builds a directed weighted graph with 'size' vertices and ~2*size random edges.
     * Edge weights are random integers from 1 to 20.
     */
    private Graph buildRandomGraph(int size) {
        Graph g = new Graph();
        Random rnd = new Random(42);

        for (int i = 0; i < size; i++) {
            g.addVertex(new Vertex(i));
        }

        int edgeCount = 2 * size;
        Set<String> added = new HashSet<>();
        int attempts = 0;
        while (added.size() < edgeCount && attempts < edgeCount * 10) {
            int from   = rnd.nextInt(size);
            int to     = rnd.nextInt(size);
            int weight = rnd.nextInt(20) + 1; // weight 1..20
            String key = from + "-" + to;
            if (from != to && !added.contains(key)) {
                g.addEdge(from, to, weight);
                added.add(key);
            }
            attempts++;
        }
        return g;
    }
}
