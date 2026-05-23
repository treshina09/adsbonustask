/**
 * Entry point. Demonstrates BFS, DFS, and Dijkstra's Algorithm.
 */
public class Main {

    public static void main(String[] args) {

        // ----------------------------------------------------------------
        // 1. Small graph (10 vertices) – print structure + traversal order
        // ----------------------------------------------------------------
        System.out.println("===== SMALL GRAPH (10 vertices) =====");
        Graph small = new Graph();
        for (int i = 0; i < 10; i++) small.addVertex(new Vertex(i));

        // Directed edges with weights
        int[][] smallEdges = {
            {0,1,4}, {0,2,1}, {1,3,1}, {1,4,5}, {2,5,2},
            {2,6,8}, {3,7,3}, {4,8,2}, {5,9,6}, {6,9,1},
            {7,8,1}, {8,9,2}
        };
        for (int[] e : smallEdges) small.addEdge(e[0], e[1], e[2]);

        small.printGraph();
        System.out.println();

        long t0 = System.nanoTime();
        small.bfs(0);
        long t1 = System.nanoTime();
        System.out.println("  BFS time: " + (t1 - t0) + " ns");

        long t2 = System.nanoTime();
        small.dfs(0);
        long t3 = System.nanoTime();
        System.out.println("  DFS time: " + (t3 - t2) + " ns");

        // ----------------------------------------------------------------
        // 2. Dijkstra's Algorithm demo
        // ----------------------------------------------------------------
        System.out.println("\n===== DIJKSTRA'S ALGORITHM =====");
        long t4 = System.nanoTime();
        small.dijkstra(0);
        long t5 = System.nanoTime();
        System.out.println("\n  Dijkstra time: " + (t5 - t4) + " ns");

        // ----------------------------------------------------------------
        // 3. Simple 5-vertex example to verify correctness
        // ----------------------------------------------------------------
        System.out.println("\n===== VERIFICATION EXAMPLE (5 vertices) =====");
        Graph g = new Graph();
        for (int i = 0; i < 5; i++) g.addVertex(new Vertex(i));

        //       2       3
        //   0 -----> 1 -----> 4
        //   |                 ^
        //   | 1       6       | 1
        //   v        v        |
        //   2 -----> 3 -------+
        g.addEdge(0, 1, 2);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 4, 3);
        g.addEdge(2, 3, 6);
        g.addEdge(3, 4, 1);
        g.addEdge(1, 3, 1); // shortcut through 1->3->4 = 2+1+1 = 4 vs 0->1->4 = 2+3 = 5

        g.dijkstra(0);
        // Expected:
        // Vertex(0) = 0  (start)
        // Vertex(1) = 2  (0->1)
        // Vertex(2) = 1  (0->2)
        // Vertex(3) = 3  (0->1->3)
        // Vertex(4) = 4  (0->1->3->4)

        // ----------------------------------------------------------------
        // 4. Performance experiment
        // ----------------------------------------------------------------
        System.out.println("\n===== PERFORMANCE EXPERIMENT =====");
        Experiment exp = new Experiment();
        exp.runMultipleTests();
        exp.printResults();
    }
}
