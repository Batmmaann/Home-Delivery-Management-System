package HDMS;
import java.util.List;
public interface DeliveryGraph<V, E> {
    // This method Adds a delivery location (node)
    boolean addVertex(V node);
    // This method Adds a route between two locations with a specified distance
    boolean addEdge(V node1, V node2, E weight);
    // This method Removes a location (node)
    boolean removeVertex(V node) throws GraphEmptyException;
    // This method Removes a route between two locations
    boolean removeEdge(V node1, V node2) throws GraphEmptyException;
    // This method Retrieves the neighbors (connected locations) for a given location
    List<V> getNeighbors(V node) throws GraphEmptyException;
    // This method Retrieves the distance (weight) of a route between two locations
    E getEdgeWeight(V node1, V node2) throws GraphEmptyException;
    // This method Retrieves all locations (nodes)
    List<V> getNodes() throws GraphEmptyException;
    // This method Clears all locations and routes
    boolean deleteAll() throws GraphEmptyException;
}
