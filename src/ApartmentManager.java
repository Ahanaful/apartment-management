package src;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.io.FileReader;
import java.io.IOException;
// This is the main class for the ApartmentManager. This class contains the methods for adding residents, packages, and lockouts.
public class ApartmentManager {
    // This is the file path for the residents.txt and packages.txt files.
    private static final String RESIDENTS_FILE = "src/residents.txt";
    private static final String PACKAGES_FILE = "src/packages.txt";
    private static final String LOCKOUTS_FILE = "src/lockouts.txt";
    
    // This method adds a package to the packages.txt file. 
    public String addPackage(int residentId) {
        try {
            List<String> packages = readLines(PACKAGES_FILE);
            int nextPackageNumber = packages.size() + 1;
            String trackingNumber = String.format("PKG-%05d", nextPackageNumber);
            
            String newPackage = String.format("ResidentID: %d, TrackingNumber: %s, Delivered: NO",
                    residentId, trackingNumber);
            appendToFile(PACKAGES_FILE, newPackage);
            
            System.out.println("Package added successfully for ResidentID: " + residentId +
                    " with TrackingNumber: " + trackingNumber);
            return trackingNumber;
        } catch (Exception e) {
            System.out.println("Error adding package:");
            e.printStackTrace();
            return null;
        }
    }
    // This method checks the packages for a resident.
    public List<String> checkPackages(int residentId) {
        try {
            List<String> packages = readLines(PACKAGES_FILE);
            List<String> residentPackages = new ArrayList<>();
            
            for (String pkg : packages) {
                if (pkg.startsWith("ResidentID: " + residentId)) {
                    residentPackages.add(pkg);
                }
            }
            return residentPackages;
        } catch (Exception e) {
            System.out.println("Error checking packages:");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    // This method delivers a package to the packages.txt file.
    public void deliverPackage(String trackingNumber) {
        try {
            List<String> packages = readLines(PACKAGES_FILE);
            List<String> updatedPackages = new ArrayList<>();
            
            for (String pkg : packages) {
                if (pkg.contains("TrackingNumber: " + trackingNumber)) {
                    pkg = pkg.replace("Delivered: NO", "Delivered: YES");
                    System.out.println("Package delivered: " + trackingNumber);
                }
                updatedPackages.add(pkg);
            }
            
            writeLines(PACKAGES_FILE, updatedPackages);
        } catch (Exception e) {
            System.out.println("Error updating package delivery status:");
            e.printStackTrace();
        }
    }
    // This method searches the resident for the residents.txt file.
    public List<String> searchResident(String searchText) {
        try {
            List<String> residents = readLines(RESIDENTS_FILE);
            List<String> matches = new ArrayList<>();
            searchText = searchText.toLowerCase();
            
            for (String resident : residents) {
                String[] parts = resident.split(", ");
                String id = parts[0].split(": ")[1];
                String firstName = parts[1].split(": ")[1].toLowerCase();
                String lastName = parts[2].split(": ")[1].toLowerCase();
                
                if (id.equals(searchText) || 
                    firstName.contains(searchText) || 
                    lastName.contains(searchText) || 
                    (firstName + " " + lastName).contains(searchText)) {
                    matches.add(resident);
                }
            }
            return matches;
        } catch (Exception e) {
            System.out.println("Error searching residents:");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    // This method marks the package as delivered for the packages.txt file.
    public boolean markDelivered(String trackingNumber) {
        try {
            List<String> packages = Files.readAllLines(Paths.get("src/packages.txt"));
            List<String> updatedPackages = new ArrayList<>();
            boolean found = false;

            for (String pkg : packages) {
                if (pkg.contains("TrackingNumber: " + trackingNumber)) {
                    pkg = pkg.replace("Delivered: NO", "Delivered: YES");
                    found = true;
                }
                updatedPackages.add(pkg);
            }

            if (found) {
                Files.write(Paths.get("src/packages.txt"), updatedPackages);
            }
            return found;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Utility methods for file operations
    public List<String> readLines(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            e.printStackTrace();
        }
        return lines;
    }
    // This method appends to the file for the residents.txt file.
    private void appendToFile(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error appending to file: " + filename);
            e.printStackTrace();
        }
    }
    // This method writes the lines to the file for the residents.txt file. 
    public void writeLines(String filename, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filename);
            e.printStackTrace();
        }
    }
    public List<String> viewLockouts() throws IOException {
        return readLines("src/lockouts.txt");
    }

    
    public String recordLockout(int residentId) {
       
            // Read all lockouts
            List<String> lockouts = readLines(LOCKOUTS_FILE);
            List<String> updatedLockouts = new ArrayList<>();
            boolean residentFound = false;
            String costMessage = "";

            for (String record : lockouts) {
                if (record.startsWith("ResidentID: " + residentId)) {
                    residentFound = true;

                    // Parse and update the lockouts count
                    String[] parts = record.split(", ");
                    int lockoutCount = Integer.parseInt(parts[1].split(": ")[1]) + 1;

                    // Calculate cost
                    int cost = 0;
                    if (lockoutCount > 3) {
                        cost = Math.min(25, (lockoutCount - 3) * 5);
                    }

                    costMessage = "Lockout recorded. Total Lockouts: " + lockoutCount + ". Due Payments: $" + cost;

                    // Update the record
                    record = "ResidentID: " + residentId + ", Lockouts: " + lockoutCount + ", Due Payments: $" + cost;
                }
                updatedLockouts.add(record);
            }

            // If resident not found, return an error message
            if (!residentFound) {
                return "Resident ID " + residentId + " not found. Please check the lockouts file.";
            }

            // Write the updated list back to the file
            writeLines(LOCKOUTS_FILE, updatedLockouts);
            return costMessage;
    }

    public String getLockouts(int residentId) throws IOException {
        List<String> lockoutData = readLines("src/lockouts.txt");
        for (String line : lockoutData) {
            if (line.contains("ResidentID: " + residentId)) {
                return line;
            }
        }
        return "ResidentID: " + residentId + " not found.";
    }
    
    
    public int recordLockoutWithCost(int residentId) throws IOException {
        List<String> lockoutData = readLines("src/lockouts.txt");
        List<String> updatedLockoutData = new ArrayList<>();
        int lockoutCount = 0;
        int duePayments = 0;

        boolean found = false;
        for (String line : lockoutData) {
            if (line.contains("ResidentID: " + residentId)) {
                String[] parts = line.split(", ");
                lockoutCount = Integer.parseInt(parts[1].split(": ")[1]) + 1;

                // Calculate the due payments
                duePayments = calculateLockoutCost(lockoutCount);

                // Update the line with the new lockout count and due payments
                line = "ResidentID: " + residentId + ", Lockouts: " + lockoutCount + ", Due Payments: $" + duePayments;
                found = true;
            }
            updatedLockoutData.add(line);
        }

        if (!found) {
            // If the resident doesn't exist, add a new entry
            lockoutCount = 1;
            duePayments = calculateLockoutCost(lockoutCount);
            updatedLockoutData.add("ResidentID: " + residentId + ", Lockouts: " + lockoutCount + ", DuePayments: $" + duePayments);
        }

        // Write the updated data back to the file
        writeLines("src/lockouts.txt", updatedLockoutData);

        // Return the cost of the current lockout
        return duePayments;
    }

    

	
		
    public int calculateLockoutCost(int lockoutCount) {
        if (lockoutCount < 3) return 0;      // No charge for 2 or fewer lockouts
        if (lockoutCount == 3) return 5;     // $5 for 3 lockouts
        if (lockoutCount == 4) return 10;    // $10 for 4 lockouts
        if (lockoutCount == 5) return 15;    // $15 for 5 lockouts
        if (lockoutCount == 6) return 20;    // $20 for 6 lockouts
        return 25;                           // $25 for 7 or more lockouts
    }




}

		
		
	

