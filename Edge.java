/**
 * Represents a directed connection between two vertices with a weight.
 */
public class Edge {

    private Vertex source;
    private Vertex destination;
    private int weight;

    public Edge(Vertex source, Vertex destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    // Backward-compatible constructor (weight = 1 by default)
    public Edge(Vertex source, Vertex destination) {
        this(source, destination, 1);
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source + " -(" + weight + ")-> " + destination;
    }
}
