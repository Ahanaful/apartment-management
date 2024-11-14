package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

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

            // Set the parameters for the query
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, roomNumber);

            // Execute the insertion
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Resident added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding resident:");
            e.printStackTrace();
        }
    }
    
    public void reassignResidentIDs() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Start transaction
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                // Create temporary table
                statement.executeUpdate("CREATE TEMPORARY TABLE temp_residents AS SELECT * FROM residents ORDER BY RoomNumber");
                
                // Clear original table
                statement.executeUpdate("DELETE FROM residents");
                
                // Reset auto increment
                statement.executeUpdate("ALTER TABLE residents AUTO_INCREMENT = 1");
                
                // Reinsert data in ordered fashion
                statement.executeUpdate("""
                    INSERT INTO residents (FirstName, LastName, RoomNumber)
                    SELECT FirstName, LastName, RoomNumber
                    FROM temp_residents
                    ORDER BY RoomNumber
                """);
                
                // Drop temporary table
                statement.executeUpdate("DROP TEMPORARY TABLE temp_residents");
                
                // Commit transaction
                connection.commit();
                
                System.out.println("ResidentIDs have been successfully reassigned based on RoomNumber.");
            } catch (SQLException e) {
                // Rollback on error
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Error reassigning ResidentIDs:");
            e.printStackTrace();
        }
    }
    
    
    public void displayResident(){
        String sql = "SELECT * FROM Residents ORDER BY RoomNumber";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            // Print the data rows
            while (resultSet.next()) {
                int id = resultSet.getInt("ResidentID");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                int roomNumber = resultSet.getInt("RoomNumber");
                System.out.println("ID: " + id + ", Name: " + firstName + " " + lastName + ", Room: " + roomNumber);
            }
        } catch (SQLException e) {
            System.out.println("Error displaying residents:");
            e.printStackTrace();
        }
    }
}

