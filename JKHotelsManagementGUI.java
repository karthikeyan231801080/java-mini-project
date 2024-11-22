
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JKHotelsManagementGUI extends JFrame {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/HotelManagementSystem";
    private static final String USER = "root";
    private static final String PASSWORD = "Karthi@641113"; // Replace with your actual password

    private Connection connection;

    private JTable table;
    private JTextField customerIdField, roomIdField, checkInField, checkOutField;

    // Constructor
    public JKHotelsManagementGUI() {
        setTitle("JK Hotels Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel for actions
        JPanel buttonPanel = new JPanel();
        JButton connectButton = new JButton("Connect to Database");
        JButton viewRoomsButton = new JButton("View Rooms");
        JButton bookRoomButton = new JButton("Book Room");
        JButton checkInButton = new JButton("Check In");
        JButton checkOutButton = new JButton("Check Out");

        buttonPanel.add(connectButton);
        buttonPanel.add(viewRoomsButton);
        buttonPanel.add(bookRoomButton);
        buttonPanel.add(checkInButton);
        buttonPanel.add(checkOutButton);

        // Table for displaying data
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        // Booking details panel
        JPanel bookingPanel = new JPanel(new GridLayout(5, 2));
        bookingPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        bookingPanel.add(new JLabel("Customer ID:"));
        customerIdField = new JTextField();
        bookingPanel.add(customerIdField);
        bookingPanel.add(new JLabel("Room ID:"));
        roomIdField = new JTextField();
        bookingPanel.add(roomIdField);
        bookingPanel.add(new JLabel("Check-In Date (YYYY-MM-DD):"));
        checkInField = new JTextField();
        bookingPanel.add(checkInField);
        bookingPanel.add(new JLabel("Check-Out Date (YYYY-MM-DD):"));
        checkOutField = new JTextField();
        bookingPanel.add(checkOutField);

        // Layout
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bookingPanel, BorderLayout.SOUTH);

        // Button actions
        connectButton.addActionListener(e -> connectToDatabase());
        viewRoomsButton.addActionListener(e -> displayRooms());
        bookRoomButton.addActionListener(e -> bookRoom());
        checkInButton.addActionListener(e -> checkIn());
        checkOutButton.addActionListener(e -> checkOut());
    }

    // Method to connect to the database
    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            JOptionPane.showMessageDialog(this, "Connected to the database successfully!");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "JDBC Driver not found. Add the MySQL JDBC library to your project.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database. Check your credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to display room details
    private void displayRooms() {
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Please connect to the database first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "SELECT * FROM Rooms";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            DefaultTableModel model = new DefaultTableModel(new String[]{"RoomID", "RoomType", "BedCount", "PricePerNight", "IsAvailable"}, 0);
            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getInt("RoomID"),
                        resultSet.getString("RoomType"),
                        resultSet.getInt("BedCount"),
                        resultSet.getDouble("PricePerNight"),
                        resultSet.getBoolean("IsAvailable")
                });
            }
            table.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error while retrieving rooms.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to book a room
    private void bookRoom() {
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Please connect to the database first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int customerId = Integer.parseInt(customerIdField.getText());
            int roomId = Integer.parseInt(roomIdField.getText());
            String checkInDate = checkInField.getText();
            String checkOutDate = checkOutField.getText();

            String query = "INSERT INTO Bookings (CustomerID, RoomID, CheckInDate, CheckOutDate, BookingStatus) VALUES (?, ?, ?, ?, 'Pending')";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, customerId);
                statement.setInt(2, roomId);
                statement.setDate(3, java.sql.Date.valueOf(checkInDate)); // Correct usage of java.sql.Date
                statement.setDate(4, java.sql.Date.valueOf(checkOutDate)); // Correct usage of java.sql.Date
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Room booked successfully!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error while booking room.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to handle check-in
    private void checkIn() {
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Please connect to the database first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int roomId = Integer.parseInt(roomIdField.getText());
            String query = "UPDATE Bookings SET BookingStatus = 'Confirmed' WHERE RoomID = ? AND BookingStatus = 'Pending'";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, roomId);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Welcome to JK Hotels!");
                } else {
                    JOptionPane.showMessageDialog(this, "No pending bookings found for the given Room ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error while checking in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to handle check-out
    private void checkOut() {
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Please connect to the database first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int roomId = Integer.parseInt(roomIdField.getText());
            String query = "UPDATE Bookings SET BookingStatus = 'Cancelled' WHERE RoomID = ? AND BookingStatus = 'Confirmed'";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, roomId);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Thank you for staying with us at JK Hotels!");
                } else {
                    JOptionPane.showMessageDialog(this, "No active bookings found for the given Room ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error while checking out.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JKHotelsManagementGUI gui = new JKHotelsManagementGUI();
            gui.setVisible(true);
        });
    }
}
