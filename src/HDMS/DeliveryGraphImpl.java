package HDMS;

import java.util.*;

public class DeliveryGraphImpl<V extends Comparable<V>, E> implements DeliveryGraph<V, E> {

    // Use HashMap to store neighbors along with edge weights
    private Map<V, Map<V, E>> adjacencyList = new HashMap<>();

    @Override
    public boolean addVertex(V node) {
        if (adjacencyList.containsKey(node)) {
            return false; // Node already exists
        }
        adjacencyList.put(node, new HashMap<>()); // Add new node with an empty map of neighbors and edge weights
        return true;
    }

    @Override
    public boolean addEdge(V node1, V node2, E edge) {
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            return false; // One or both nodes don't exist
        }
        
        // Add the edge from node1 to node2 with the associated weight
        adjacencyList.get(node1).put(node2, edge);
        // Add the edge from node2 to node1 (undirected graph) with the associated weight
        adjacencyList.get(node2).put(node1, edge);

        return true;
    }

    @Override
    public boolean removeVertex(V node) throws GraphEmptyException {
        if (adjacencyList.isEmpty()) {
            throw new GraphEmptyException("The graph is empty. No nodes to remove.");
        }

        if (!adjacencyList.containsKey(node)) {
            return false; // Node doesn't exist
        }

        // Remove all edges pointing to this node by iterating through all other nodes
        for (V neighbor : adjacencyList.get(node).keySet()) {
            adjacencyList.get(neighbor).remove(node); // Remove the node from each neighbor's adjacency map
        }

        // Finally, remove the node itself
        adjacencyList.remove(node);
        return true;
    }

    @Override
    public boolean removeEdge(V node1, V node2) throws GraphEmptyException {
        if (adjacencyList.isEmpty()) {
            throw new GraphEmptyException("The graph is empty. No edges to remove.");
        }

        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            return false; // One or both nodes don't exist
        }

        // Remove the edge from node1 to node2 and from node2 to node1 (undirected graph)
        boolean removedFromNode1 = adjacencyList.get(node1).remove(node2) != null;
        boolean removedFromNode2 = adjacencyList.get(node2).remove(node1) != null;

        return removedFromNode1 || removedFromNode2; // Return true if either removal was successful
    }

    @Override
    public List<V> getNeighbors(V node) throws GraphEmptyException {
        if (adjacencyList.isEmpty()) {
            throw new GraphEmptyException("The graph is empty. No neighbors to retrieve.");
        }

        if (!adjacencyList.containsKey(node)) {
            return Collections.emptyList(); // Node doesn't exist, so no neighbors
        }

        // Return the neighbors as a list (Map<V, E> -> List<V>)
        return new ArrayList<>(adjacencyList.get(node).keySet());
    }

    @Override
    public E getEdgeWeight(V node1, V node2) throws GraphEmptyException {
        if (adjacencyList.isEmpty()) {
            throw new GraphEmptyException("The graph is empty. No edges to retrieve.");
        }

        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            return null; // One or both nodes don't exist, so no edge weight
        }

        // Return the edge weight from node1 to node2, if it exists
        return adjacencyList.get(node1).get(node2);
    }

    @Override
    public List<V> getNodes() throws GraphEmptyException {
        if (adjacencyList.isEmpty()) {
            throw new GraphEmptyException("The graph is empty. No nodes to retrieve.");
        }

        // Return a list of all keys (nodes) in the adjacency list
        return new ArrayList<>(adjacencyList.keySet());
    }

    @Override
    public boolean deleteAll() throws GraphEmptyException {
        if (adjacencyList.isEmpty()) {
            throw new GraphEmptyException("The graph is already empty.");
        }

        // Clear all nodes and edges from the graph
        adjacencyList.clear();
        return true;
    }
}
