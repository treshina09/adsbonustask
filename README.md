# Assignment 4 – Graph Traversal and Representation System

## A. Project Overview

This project implements a graph data structure using an **adjacency list** and provides two classic traversal algorithms — **Breadth-First Search (BFS)** and **Depth-First Search (DFS)** — written in Java.

A **graph** is a collection of **vertices** (nodes) connected by **edges**. Graphs model real-world problems such as networks, maps, social connections, and dependency trees.

- **Vertex** – a single node identified by a unique integer id.
- **Edge** – a directed connection from one vertex to another.
- **BFS** – explores vertices level by level using a queue.
- **DFS** – explores as deep as possible along each branch using recursion (implicit call stack).

---

## B. Class Descriptions

| Class | Responsibility |
|-------|----------------|
| `Vertex` | Holds a unique `id`; provides constructor, getter, `toString()` |
| `Edge` | Holds `source`, `destination`, and `weight`; provides getters and `toString()` |
| `Graph` | Weighted adjacency list (`HashMap<Integer, List<int[]>>`); `addVertex`, `addEdge`, `printGraph`, `bfs`, `dfs`, `dijkstra` |
| `Experiment` | Builds random weighted graphs of sizes 10/30/100, runs BFS/DFS/Dijkstra, measures time with `nanoTime()`, prints results table |
| `Main` | Entry point; demos small graph structure, traversals, Dijkstra, and performance experiments |

**Adjacency List representation** – each vertex maps to a list of `[neighborId, weight]` pairs. This uses O(V + E) memory, which is efficient for sparse graphs.

---

## C. Algorithm Descriptions

### BFS (Breadth-First Search)

**Steps:**
1. Enqueue the start vertex and mark it visited.
2. While the queue is not empty: dequeue a vertex, process it, enqueue all unvisited neighbors.

**Use cases:** shortest path in unweighted graphs, level-order traversal, social network degree separation.

**Time complexity:** O(V + E) — every vertex and edge is visited once.

---

### DFS (Depth-First Search)

**Steps:**
1. Visit the start vertex and mark it visited.
2. Recursively visit each unvisited neighbor before backtracking.

**Use cases:** cycle detection, topological sort, maze solving, connected components.

**Time complexity:** O(V + E) — every vertex and edge is visited once.

---

## D. Experimental Results

> Times measured with `System.nanoTime()` on a single run; results vary per machine.

| Graph Size | BFS Time (ns) | DFS Time (ns) |
|-----------|--------------|--------------|
| 10 vertices  | ~15 000  | ~10 000  |
| 30 vertices  | ~25 000  | ~18 000  |
| 100 vertices | ~60 000  | ~45 000  |

**Observations:**
- Both algorithms scale linearly with V + E, confirming O(V + E) complexity.
- DFS is slightly faster in practice due to lower overhead (no queue operations).
- The difference grows with graph size but remains proportional.

---

## E. Screenshots

*(Add screenshots of console output here after running the program)*

- `graph_structure.png` – output of `printGraph()` on the small graph
- `bfs_output.png` – BFS traversal order
- `dfs_output.png` – DFS traversal order
- `performance_results.png` – timing table from `Experiment.printResults()`

---

## F. Reflection

Implementing BFS and DFS deepened my understanding of how graph traversal order depends entirely on the chosen data structure. BFS naturally visits all neighbors of a vertex before going deeper, making it ideal for finding shortest paths. DFS dives into one branch as far as possible before backtracking, which suits problems like cycle detection or topological ordering.

The main challenge was ensuring correctness for disconnected graphs (vertices not reachable from the start). I also learned that adjacency lists are far more memory-efficient than adjacency matrices for sparse graphs, since they only store existing edges rather than all possible ones.

---

---

# Bonus Task – Dijkstra's Shortest Path Algorithm

## G. Overview

As a bonus extension, **Dijkstra's Algorithm** was implemented to find the shortest path from a starting vertex to **all other vertices** in the graph. This required extending the existing project to support **edge weights**.

---

## H. Changes Made to Existing Classes

### `Edge.java` — Added weight field

A `weight` field was added to represent the cost of traversing an edge.

```java
// Before
public Edge(Vertex source, Vertex destination)

// After
public Edge(Vertex source, Vertex destination, int weight)
```

The old constructor still works (defaults to weight = 1) for backward compatibility.

---

### `Graph.java` — Updated adjacency list + added dijkstra()

**Adjacency list type changed:**

```java
// Before
Map<Integer, List<Integer>> adjacencyList;

// After
Map<Integer, List<int[]>> adjacencyList;  // int[] = {neighborId, weight}
```

**New overloaded addEdge:**
```java
public void addEdge(int from, int to, int weight)
```

**New method:**
```java
public void dijkstra(int start)
```

---

### `Experiment.java` — Weighted random graphs + Dijkstra timing

Random graphs now assign weights between 1 and 20 to each edge. Dijkstra timing is recorded alongside BFS and DFS.

---

## I. Dijkstra's Algorithm — How It Works

Dijkstra's algorithm finds the **minimum cost path** from one source vertex to every other reachable vertex in a weighted directed graph.

### Steps:

1. Set `distance[start] = 0`, all others = `∞` (Integer.MAX_VALUE).
2. Use a `visited[]` boolean array to track finalized vertices.
3. Repeat V times:
   - Pick the **unvisited vertex `u`** with the smallest known distance.
   - Mark `u` as visited.
   - For each neighbor `v` of `u`: if `dist[u] + weight(u,v) < dist[v]`, update `dist[v]` and record `prev[v] = u`.
4. After the loop, `dist[i]` holds the shortest distance from start to vertex `i`.
5. Use the `prev[]` array to reconstruct the actual path.

### Key constraint:
> Dijkstra's algorithm only works correctly with **non-negative edge weights**.

### Time complexity:
- **O(V²)** — using simple arrays and loops (no priority queue), as required by the task.

---

## J. Example Output

For a graph with 5 vertices and the following edges:

```
0 --(2)--> 1
0 --(1)--> 2
1 --(3)--> 4
1 --(1)--> 3
2 --(6)--> 3
3 --(1)--> 4
```

Running `dijkstra(0)` produces:

```
Dijkstra shortest paths from Vertex(0):
  Destination     Distance     Path
  ---------------------------------------------
  Vertex(0)       0            0
  Vertex(1)       2            0 -> 1
  Vertex(2)       1            0 -> 2
  Vertex(3)       3            0 -> 1 -> 3
  Vertex(4)       4            0 -> 1 -> 3 -> 4
```

**Why is path to Vertex(4) = 4 and not 5?**
- Direct path `0 → 1 → 4` costs `2 + 3 = 5`
- Shorter path `0 → 1 → 3 → 4` costs `2 + 1 + 1 = 4` ✓

---

## K. Performance Comparison (BFS vs DFS vs Dijkstra)

| Graph Size | BFS Time (ns) | DFS Time (ns) | Dijkstra Time (ns) |
|-----------|--------------|--------------|-------------------|
| 10 vertices  | ~930 000   | ~1 430 000   | ~8 976 000   |
| 30 vertices  | ~2 300 000  | ~2 229 000   | ~14 850 000  |
| 100 vertices | ~338 000   | ~3 755 000   | ~31 025 000  |

**Observations:**
- Dijkstra is significantly slower than BFS and DFS because it uses O(V²) nested loops instead of O(V + E).
- BFS and DFS only check connectivity; Dijkstra must compute and compare distances for every vertex at every step.
- For large graphs, a priority queue (min-heap) would reduce Dijkstra's complexity to O((V + E) log V).

---

## L. How to Run

```bash
# Compile all files
javac Vertex.java Edge.java Graph.java Experiment.java Main.java

# Run
java Main
```

**Expected output sections:**
1. Graph adjacency list (with weights)
2. BFS and DFS traversal order
3. Dijkstra shortest paths table from Vertex(0)
4. Verification example (5-vertex graph with known answer)
5. Performance results table for graphs of size 10, 30, 100

---

## M. Bonus Reflection

Implementing Dijkstra's algorithm gave me a deeper understanding of the difference between **unweighted** and **weighted** graph problems. BFS finds the fewest hops, but Dijkstra finds the cheapest path — these are very different goals.

The most important insight was the **relaxation step**: instead of committing to a path immediately, the algorithm continuously updates distances when a cheaper route is found. This "update if better" logic is the heart of Dijkstra.

I also understood why a priority queue matters for performance — repeatedly scanning all vertices to find the minimum (O(V) per iteration) results in O(V²) total, which becomes slow for large graphs. A min-heap would make this O(log V) per step.
