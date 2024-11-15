package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApartmentManager {

    public void testDatabaseConnection() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Database connected successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database:");
            e.printStackTrace();
        }
    }

    public void addResident(String firstName, String lastName, int roomNumber) {
        String sql = "INSERT INTO residents (FirstName, LastName, RoomNumber) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, roomNumber);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Resident added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding resident:");
            e.printStackTrace();
        }
    }

    public String addPackage(int residentId) {
        String countSql = "SELECT COUNT(*) + 1 AS NextPackageNumber FROM packages WHERE ResidentID = ?";
        String insertSql = "INSERT INTO packages (ResidentID, TrackingNumber, Delivered) VALUES (?, ?, 'NO')";
        String trackingNumber = null;

        try (Connection connection = DatabaseConnection.getConnection()) {
            int nextPackageNumber = 0;
            try (PreparedStatement countStatement = connection.prepareStatement(countSql)) {
                countStatement.setInt(1, residentId);
                try (ResultSet resultSet = countStatement.executeQuery()) {
                    if (resultSet.next()) {
                        nextPackageNumber = resultSet.getInt("NextPackageNumber");
                    }
                }
            }

            trackingNumber = String.format("PKG-%05d", nextPackageNumber);
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                insertStatement.setInt(1, residentId);
                insertStatement.setString(2, trackingNumber);
                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Package added successfully for ResidentID: " + residentId + " with TrackingNumber: " + trackingNumber);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding package:");
            e.printStackTrace();
        }

        return trackingNumber;
    }

    public ResultSet checkPackages(int residentId) throws SQLException {
        String sql = "SELECT TrackingNumber, Delivered FROM packages WHERE ResidentID = ?";
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, residentId);
        return statement.executeQuery();
    }

    public void deliverPackage(String trackingNumber) {
        String sql = "UPDATE packages SET Delivered = 'YES' WHERE TrackingNumber = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, trackingNumber);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Package delivered: " + trackingNumber);
            }
        } catch (SQLException e) {
            System.out.println("Error updating package delivery status:");
            e.printStackTrace();
        }
    }

    public ResultSet searchResident(String searchText) throws SQLException {
        String sql = "SELECT * FROM residents WHERE FirstName LIKE ? OR LastName LIKE ? OR ResidentID = ?";
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, "%" + searchText + "%");
        statement.setString(2, "%" + searchText + "%");
        statement.setString(3, searchText);
        return statement.executeQuery();
    }
}
