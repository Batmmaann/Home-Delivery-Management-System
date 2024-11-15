package HDMS;
import java.util.List;

public interface DeliveryGraph<V, E> {
    // Adds a delivery location (node)
    boolean addVertex(V node);

    // Adds a route between two locations with a specified distance
    boolean addEdge(V node1, V node2, E weight);

    // Removes a location (node)
    boolean removeVertex(V node) throws GraphEmptyException;

    // Removes a route between two locations
    boolean removeEdge(V node1, V node2) throws GraphEmptyException;

    // Retrieves the neighbors (connected locations) for a given location
    List<V> getNeighbors(V node) throws GraphEmptyException;

    // Retrieves the distance (weight) of a route between two locations
    E getEdgeWeight(V node1, V node2) throws GraphEmptyException;

    // Retrieves all locations (nodes)
    List<V> getNodes() throws GraphEmptyException;

    // Clears all locations and routes
    boolean deleteAll() throws GraphEmptyException;
}
