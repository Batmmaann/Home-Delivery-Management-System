package HDMS;
import java.util.*;
public class TSP {
    private DeliveryGraphImpl<Location, Integer> graph;

    public TSP(DeliveryGraphImpl<Location, Integer> graph) {
        this.graph = graph;
    }
    // Nearest Neighbor Algorithm for TSP (no cycle completion)
    public List<Location> solve(Location startLocation) {
        List<Location> tour = new ArrayList<>();
        Set<Location> visited = new HashSet<>();
        Location currentLocation = startLocation;

        // Add the starting location to the tour
        tour.add(currentLocation);
        visited.add(currentLocation);

        try {
            while (visited.size() < graph.getNodes().size()) {
                Location nearestNeighbor = getNearestNeighbor(currentLocation, visited);
                tour.add(nearestNeighbor);
                visited.add(nearestNeighbor);
                currentLocation = nearestNeighbor;
            }
        } catch (GraphEmptyException e) {
            System.out.println("Error while accessing the graph: " + e.getMessage());
        }
        return tour;
    }
    // Get the nearest neighbor for the current location
    private Location getNearestNeighbor(Location location, Set<Location> visited) throws GraphEmptyException {
        List<Location> neighbors = graph.getNeighbors(location);
        Location nearestNeighbor = null;
        int minDistance = Integer.MAX_VALUE;

        for (Location neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                int distance = graph.getEdgeWeight(location, neighbor);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestNeighbor = neighbor;
                }
            }
        }
        return nearestNeighbor;
    }
    // Calculate the total distance of the tour
    public int calculateTourDistance(List<Location> tour) throws GraphEmptyException {
        int totalDistance = 0;

        for (int i = 0; i < tour.size() - 1; i++) {
            totalDistance += graph.getEdgeWeight(tour.get(i), tour.get(i + 1));
        }
        return totalDistance;
    }
}