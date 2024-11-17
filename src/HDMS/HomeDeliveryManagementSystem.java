package HDMS;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
public class HomeDeliveryManagementSystem extends JFrame {
    private JTextField nameField, xField, yField;
    private JButton addLocationButton, showRouteButton, clearRouteButton, clearLocationButton;
    private JPanel drawPanel;
    private List<Location> locations;
    private JTextArea infoArea;
    public HomeDeliveryManagementSystem() {
        locations = new ArrayList<>();
        
        // Setup frame
        setTitle("Home Delivery Management System");
        setSize(1100, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel with inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField(10);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("X Coordinate:"));
        xField = new JTextField(5);
        inputPanel.add(xField);

        inputPanel.add(new JLabel("Y Coordinate:"));
        yField = new JTextField(5);
        inputPanel.add(yField);

        addLocationButton = new JButton("Add Location");
        inputPanel.add(addLocationButton);

        showRouteButton = new JButton("Show Route");
        inputPanel.add(showRouteButton);

        clearLocationButton = new JButton("Clear Location");
        inputPanel.add(clearLocationButton);

        clearRouteButton = new JButton("Clear Route");
        inputPanel.add(clearRouteButton);

        add(inputPanel, BorderLayout.NORTH);

        // Panel to draw the route visualization
        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawRoute(g);
            }
        };
        drawPanel.setBackground(Color.LIGHT_GRAY);
        add(drawPanel, BorderLayout.CENTER);

        // Information area, Text Area for displaying route and cost details.
        infoArea = new JTextArea(4, 30);
        infoArea.setEditable(false);
        add(new JScrollPane(infoArea), BorderLayout.SOUTH);

        // Button actions
        addLocationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addLocation();
            }
        });

        showRouteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRoute();
            }
        });

        clearLocationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearLocation();
            }
        });

        clearRouteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearRoute();
            }
        });
    }
    // Add location to the list
    private void addLocation() {
        try {
            String name = nameField.getText();
            int x = Integer.parseInt(xField.getText());
            int y = Integer.parseInt(yField.getText());
            Location location = new Location(x, y, name);
            locations.add(location);

            // Clear the input fields after adding the location
            nameField.setText("");
            xField.setText("");
            yField.setText("");
            
            // Do not draw the dots immediately
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid coordinates. Please enter valid numbers.");
        }
    }
    // Show the route based on TSP solution
    private void showRoute() {
        if (locations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No locations to process. Please add some locations.");
            return;
        }
        // Create a new DeliveryGraphImpl and populate it with locations
        DeliveryGraphImpl<Location, Integer> graph = new DeliveryGraphImpl<>();

        // Add the locations to the graph 
        for (Location location : locations) {
            graph.addVertex(location);
        }
        // Add edges based on the locations 
        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                Location a = locations.get(i);
                Location b = locations.get(j);
                int distance = (int) a.distanceTo(b);
                graph.addEdge(a, b, distance);  // Add edge between locations
            }
        }
        // Now you can use this populated graph with TSP
        TSP tspSolver = new TSP(graph);
        List<Location> tour = tspSolver.solve(locations.get(0));
        
        // Display route information
        StringBuilder sb = new StringBuilder();
        sb.append("Shortest Delivery Route: ");
        
        // Add the ordered list of locations
        for (int i = 0; i < tour.size(); i++) {
            sb.append(i + 1).append(". ").append(tour.get(i).getName()).append(" ");
        }
        sb.append("\n");

        // Calculate the total distance, time, and cost
        int totalDistance = 0;
        try {
            totalDistance = tspSolver.calculateTourDistance(tour); // Assume this gives the distance in meters
        } catch (GraphEmptyException e) {
            JOptionPane.showMessageDialog(this, "Error calculating the tour distance: " + e.getMessage());
        }

        // Time in minutes for 1 meter (0.5 minutes per meter)
        double estimatedTime = totalDistance * 0.5; // Assuming 0.5 minutes per meter

        // Cost in Riyals for 1 meter (1.5 Riyals per meter)
        double cost = totalDistance * 1.5; // Assuming 1.5 Riyals per meter

        sb.append("Total Estimated Distance: ").append(totalDistance).append(" meters\n");
        sb.append("Estimated Time: ").append(estimatedTime).append(" minutes\n");
        sb.append("Estimated Cost: SR").append(cost).append("\n");

        // Display all information in the text area
        infoArea.setText(sb.toString());

        repaint(); // Refresh the drawing panel to show the locations and the route
    }
    // Clear the last location added
    private void clearLocation() {
        if (!locations.isEmpty()) {
            locations.remove(locations.size() - 1); // Remove the last added location
        }
        repaint(); // Refresh the drawing panel to reflect the changes
    }
    // Clear the route (reset the panel)
    private void clearRoute() {
        // Clear the route info in the text area
        infoArea.setText("");

        // Clear the locations (this will empty the graph and remove the drawn dots)
        locations.clear();

        // Repaint the drawing panel to reset the visualization
        repaint();
    }
    // Draw the locations and routes
    private void drawRoute(Graphics g) {
        if (locations.isEmpty()) return;

        // Centering the origin (0,0) in the middle of the panel
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int centerX = panelWidth / 2;
        int centerY = panelHeight / 2;

        // Create the graph and populate it with locations
        DeliveryGraphImpl<Location, Integer> graph = new DeliveryGraphImpl<>();

        // Add the locations to the graph (this will automatically populate the graph)
        for (Location location : locations) {
            graph.addVertex(location);
        }

        // Add edges based on the locations (you can use your custom logic to calculate distances)
        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                Location from = locations.get(i);
                Location to = locations.get(j);
                int distance = (int) from.distanceTo(to); // Use the distance in meters (or any other appropriate calculation)
                graph.addEdge(from, to, distance);  // Add the edge between the two locations
            }
        }

        // Solve TSP to get the correct order of locations
        TSP tspSolver = new TSP(graph);
        List<Location> tour = tspSolver.solve(locations.get(0)); // Get shortest path based on TSP

        // Draw locations as blue circles with updated sequence numbers
        for (int i = 0; i < tour.size(); i++) {
            Location loc = tour.get(i);  // Get location based on TSP order
            
            // Set dot color to blue
            g.setColor(Color.BLUE);
            // Scale the x and y coordinates to the panel center
            int x = centerX + (loc.getX() * 10); // Scaling for visualization (10x scaling)
            int y = centerY - (loc.getY() * 10); // Invert Y axis for proper coordinate system
            g.fillOval(x, y, 10, 10);  // Draw the dot

            // Set text color to black
            g.setColor(Color.BLACK);

            // Draw the sequence number above the dot (1-based index)
            g.drawString(String.valueOf(i + 1), x + 12, y - 5);  // Sequence number above the dot (12 pixels to the right, 5 pixels above)

            // Draw the name of the location slightly away from the dot
            g.drawString(loc.getName(), x + 12, y + 15);  // Move the name slightly below the dot
        }
        // Draw lines based on the shortest route order
        g.setColor(Color.BLACK);
        for (int i = 0; i < tour.size() - 1; i++) {
            Location from = tour.get(i);
            Location to = tour.get(i + 1);
            int x1 = centerX + (from.getX() * 10);
            int y1 = centerY - (from.getY() * 10);
            int x2 = centerX + (to.getX() * 10);
            int y2 = centerY - (to.getY() * 10);
            g.drawLine(x1 + 5, y1 + 5, x2 + 5, y2 + 5); // Line between locations in order of the shortest route
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HomeDeliveryManagementSystem().setVisible(true);
            }
        });
    }
}
