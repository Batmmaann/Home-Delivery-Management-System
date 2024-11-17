package HDMS;
public class Location implements Comparable<Location> {
    private int x;
    private int y;
    private String name;

    public Location(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getName() {
        return name;
    }
    @Override
    public int compareTo(Location o) {
        return this.name.compareTo(o.name); // Compare based on the name of the location
    }
    // Calculate the Euclidean distance between two locations
    public double distanceTo(Location other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
    @Override
    public String toString() {
        return name + " (" + x + ", " + y + ")";
    }
}

