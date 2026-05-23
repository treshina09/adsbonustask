import java.util.*;

/**
 * Graph represented as an adjacency list.
 * Supports directed weighted edges, BFS, DFS, and Dijkstra's algorithm.
 */
public class Graph {

    // Maps each vertex id to the vertex object
    private Map<Integer, Vertex> vertices;

    // Adjacency list: vertex id -> list of [neighborId, weight]
    private Map<Integer, List<int[]>> adjacencyList;

    public Graph() {
        vertices = new HashMap<>();
        adjacencyList = new HashMap<>();
    }

    /**
     * Adds a vertex to the graph.
     */
    public void addVertex(Vertex v) {
        vertices.put(v.getId(), v);
        adjacencyList.putIfAbsent(v.getId(), new ArrayList<>());
    }

    /**
     * Adds a directed unweighted edge (weight = 1 by default).
     */
    public void addEdge(int from, int to) {
        addEdge(from, to, 1);
    }

    /**
     * Adds a directed weighted edge from vertex 'from' to vertex 'to'.
     */
    public void addEdge(int from, int to, int weight) {
        if (!vertices.containsKey(from) || !vertices.containsKey(to)) {
            throw new IllegalArgumentException(
                "Both vertices must exist. from=" + from + ", to=" + to);
        }
        adjacencyList.get(from).add(new int[]{to, weight});
    }

    /**
     * Prints the adjacency list representation of the graph.
     */
    public void printGraph() {
        System.out.println("Graph (Adjacency List):");
        List<Integer> sortedIds = new ArrayList<>(vertices.keySet());
        Collections.sort(sortedIds);
        for (int id : sortedIds) {
            System.out.print("  " + vertices.get(id) + " -> ");
            List<String> neighbors = new ArrayList<>();
            for (int[] entry : adjacencyList.get(id)) {
                neighbors.add(entry[0] + "(w=" + entry[1] + ")");
            }
            System.out.println(neighbors);
        }
    }

    /**
     * Breadth-First Search starting from vertex with given id.
     * Time complexity: O(V + E)
     */
    public void bfs(int start) {
        if (!vertices.containsKey(start)) {
            System.out.println("BFS: start vertex " + start + " not found.");
            return;
        }

        Set<Integer> visited = new LinkedHashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);

        System.out.print("BFS from " + start + ": ");

        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.print(current + " ");

            for (int[] entry : adjacencyList.get(current)) {
                int neighbor = entry[0];
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        System.out.println();
    }

    /**
     * Depth-First Search starting from vertex with given id.
     * Time complexity: O(V + E)
     */
    public void dfs(int start) {
        if (!vertices.containsKey(start)) {
            System.out.println("DFS: start vertex " + start + " not found.");
            return;
        }

        Set<Integer> visited = new LinkedHashSet<>();
        System.out.print("DFS from " + start + ": ");
        dfsHelper(start, visited);
        System.out.println();
    }

    private void dfsHelper(int current, Set<Integer> visited) {
        visited.add(current);
        System.out.print(current + " ");

        for (int[] entry : adjacencyList.get(current)) {
            int neighbor = entry[0];
            if (!visited.contains(neighbor)) {
                dfsHelper(neighbor, visited);
            }
        }
    }

    /**
     * Dijkstra's Algorithm — finds the shortest path from 'start' to all other vertices.
     *
     * How it works:
     *  1. Set distance to start = 0, all others = infinity.
     *  2. Repeatedly pick the unvisited vertex with the smallest known distance.
     *  3. For each neighbor, check if going through the current vertex gives a shorter path.
     *  4. Repeat until all vertices are visited.
     *
     * Time complexity: O(V^2) with simple arrays (no priority queue).
     */
    public void dijkstra(int start) {
        if (!vertices.containsKey(start)) {
            System.out.println("Dijkstra: start vertex " + start + " not found.");
            return;
        }

        int n = vertices.size();
        // Get sorted list of vertex ids for consistent indexing
        List<Integer> ids = new ArrayList<>(vertices.keySet());
        Collections.sort(ids);

        // Map vertex id -> index in arrays
        Map<Integer, Integer> idToIndex = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            idToIndex.put(ids.get(i), i);
        }

        int startIdx = idToIndex.get(start);

        // dist[i] = shortest known distance from start to vertex at index i
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[startIdx] = 0;

        // visited[i] = true if we have finalized the shortest path to vertex i
        boolean[] visited = new boolean[n];

        // prev[i] = index of previous vertex on the shortest path (for path reconstruction)
        int[] prev = new int[n];
        Arrays.fill(prev, -1);

        // Main loop: repeat V times
        for (int iter = 0; iter < n; iter++) {

            // Step 1: find unvisited vertex with minimum distance
            int u = -1;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && (u == -1 || dist[i] < dist[u])) {
                    u = i;
                }
            }

            // If no reachable unvisited vertex exists, stop
            if (u == -1 || dist[u] == Integer.MAX_VALUE) break;

            visited[u] = true;
            int uId = ids.get(u);

            // Step 2: relax all neighbors of u
            for (int[] entry : adjacencyList.get(uId)) {
                int neighborId = entry[0];
                int weight     = entry[1];
                int v = idToIndex.get(neighborId);

                if (!visited[v] && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    prev[v] = u;
                }
            }
        }

        // Print results
        System.out.println("\nDijkstra shortest paths from Vertex(" + start + "):");
        System.out.printf("  %-15s %-12s %s%n", "Destination", "Distance", "Path");
        System.out.println("  " + "-".repeat(45));

        for (int i = 0; i < n; i++) {
            int destId = ids.get(i);
            String distStr = (dist[i] == Integer.MAX_VALUE) ? "unreachable" : String.valueOf(dist[i]);
            String path    = (dist[i] == Integer.MAX_VALUE) ? "-" : buildPath(prev, ids, i, start);
            System.out.printf("  %-15s %-12s %s%n", "Vertex(" + destId + ")", distStr, path);
        }
    }

    /**
     * Reconstructs the path from start to vertex at index 'targetIdx'.
     */
    private String buildPath(int[] prev, List<Integer> ids, int targetIdx, int startId) {
        List<Integer> path = new ArrayList<>();
        for (int cur = targetIdx; cur != -1; cur = prev[cur]) {
            path.add(ids.get(cur));
        }
        Collections.reverse(path);

        // If path doesn't begin with start, it's unreachable
        if (path.isEmpty() || path.get(0) != startId) return "-";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) sb.append(" -> ");
            sb.append(path.get(i));
        }
        return sb.toString();
    }

    /** Returns the set of all vertex ids (used by Experiment). */
    public Set<Integer> getVertexIds() {
        return vertices.keySet();
    }
}
