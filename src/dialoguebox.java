package src;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

// This file contains the whole GUI for this application.

public class dialoguebox extends JFrame {
    private JTextField residentSearchField, trackingNumberField, residentIdField, tempCardResidentIdField;
    private JTextArea residentInfoArea, packageInfoArea, cardStatus;
    private ApartmentManager apartmentManager;
    // This is the constructor for the dialoguebox class.
    public dialoguebox() {
                apartmentManager = new ApartmentManager();
                setTitle("Apartment Community Management System");
                setSize(800, 600);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                initComponents();
            }
    // This method initializes the components for the dialoguebox.
    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();
            trackingNumberField = new JTextField();
            tabbedPane.addTab("Residents", createResidentPanel());
            tabbedPane.addTab("Packages", createPackagePanel());
            tabbedPane.addTab("Temporary Cards", createTempCardPanel());
            tabbedPane.addTab("Lockouts", createLockoutPanel());
    
            add(tabbedPane);
    }
               
    // This method creates the resident panel for the dialoguebox.
    private JPanel createResidentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());

        residentSearchField = new JTextField(20);
        JButton searchButton = new JButton("Search Resident");

        searchPanel.add(new JLabel("Resident Name/ID:"));
        searchPanel.add(residentSearchField);
        searchPanel.add(searchButton);

        residentInfoArea = new JTextArea();
        residentInfoArea.setEditable(false);

        searchButton.addActionListener(e -> searchResident());

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(residentInfoArea), BorderLayout.CENTER);
        return panel;
    }
    // This method creates the package panel for the dialoguebox.
    private JPanel createPackagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(4, 2));

        residentIdField = new JTextField(15);

        formPanel.add(new JLabel("Resident ID:"));
        formPanel.add(residentIdField);

        JButton addButton = new JButton("Add Package");
        JButton checkButton = new JButton("Check Packages");
        JButton markDeliveredButton = new JButton("Mark as Delivered");

        addButton.addActionListener(e -> addPackage());
        checkButton.addActionListener(e -> checkPackages());
        markDeliveredButton.addActionListener(e -> markPackageDelivered());

        formPanel.add(addButton);
        formPanel.add(checkButton);
        formPanel.add(markDeliveredButton);

        packageInfoArea = new JTextArea();
        packageInfoArea.setEditable(false);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(packageInfoArea), BorderLayout.CENTER);
        return panel;
    }
    // This method creates the lockout panel for the dialoguebox.
    private JPanel createLockoutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout());
        
        JTextField lockoutResidentIdField = new JTextField(10);
        JButton recordButton = new JButton("Record Lockout");
        JButton checkHistoryButton = new JButton("Check History");
        JTextArea lockoutDisplay = new JTextArea();
        lockoutDisplay.setEditable(false);

        inputPanel.add(new JLabel("Resident ID:"));
        inputPanel.add(lockoutResidentIdField);
        inputPanel.add(recordButton);
        inputPanel.add(checkHistoryButton);

        recordButton.addActionListener(e -> {
            try {
                int residentId = Integer.parseInt(lockoutResidentIdField.getText());
                handleLockout(residentId, lockoutDisplay);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Resident ID.");
            }
        });

        checkHistoryButton.addActionListener(e -> {
            try {
                int residentId = Integer.parseInt(lockoutResidentIdField.getText());
                displayLockoutHistory(residentId, lockoutDisplay);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Resident ID.");
            }
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(lockoutDisplay), BorderLayout.CENTER);
        return panel;
    }
    // This method handles the lockout for the dialoguebox.
    private void handleLockout(int residentId, JTextArea display) {
        try {
            List<String> lockouts = apartmentManager.readLines("src/lockouts.txt");
            int lockoutCount = 0;
            
            // Count existing lockouts for this resident
            for (String lockout : lockouts) {
                if (lockout.contains("ResidentID: " + residentId)) {
                    lockoutCount++;
                }
            }
            
            // Calculate charge
            int charge = 0;
            if (lockoutCount >= 2) {
                charge = Math.min(25, (lockoutCount - 1) * 5);
                JOptionPane.showMessageDialog(this, 
                    "Warning: This is lockout #" + (lockoutCount + 1) + "\nCharge: $" + charge);
            }
            
            // Add new lockout record
            String newLockout = String.format("ResidentID: %d, LockoutNumber: %d, Charge: $%d, Date: %s",
                residentId, lockoutCount + 1, charge, java.time.LocalDate.now());
            lockouts.add(newLockout);
            
            apartmentManager.writeLines("src/lockouts.txt", lockouts);
            displayLockoutHistory(residentId, display);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error processing lockout.");
            e.printStackTrace();
        }
    }
    // This method displays the lockout history for the dialoguebox.
    private void displayLockoutHistory(int residentId, JTextArea display) {
        try {
            List<String> lockouts = Files.readAllLines(Paths.get("src/lockouts.txt"));
            StringBuilder history = new StringBuilder();
            history.append("Lockout History for Resident ID: ").append(residentId).append("\n\n");
            
            boolean foundHistory = false;
            for (String lockout : lockouts) {
                if (lockout.startsWith("ResidentID: " + residentId + ",")) {
                    foundHistory = true;
                    String[] parts = lockout.split(", ");
                    history.append("Lockout Number: ").append(parts[1].split(": ")[1]).append("\n");
                    history.append("Charge: ").append(parts[2].split(": ")[1]).append("\n");
                    history.append("Date: ").append(parts[3].split(": ")[1]).append("\n");
                    history.append("------------------------\n");
                }
            }
            
            if (!foundHistory) {
                history.append("No lockout history found for this resident.");
            }
            
            display.setText(history.toString());
            
        } catch (IOException e) {
            display.setText("Error retrieving lockout history: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // This method searches the resident for the dialoguebox.
    private void searchResident() {
        String searchText = residentSearchField.getText().toLowerCase().trim();
        try {
            List<String> results = apartmentManager.searchResident(searchText);
            StringBuilder displayText = new StringBuilder();
            
            for (String resident : results) {
                // Parse the resident string
                String[] parts = resident.split(", ");
                String id = parts[0].split(": ")[1];
                String firstName = parts[1].split(": ")[1];
                String lastName = parts[2].split(": ")[1];
                String roomNumber = parts[3].split(": ")[1];
                
                // Check if search matches ID or name
                if (id.equals(searchText) || 
                    firstName.toLowerCase().contains(searchText) || 
                    lastName.toLowerCase().contains(searchText) || 
                    (firstName + " " + lastName).toLowerCase().contains(searchText)) {
                    
                    displayText.append("ID: ").append(id).append("\n");
                    displayText.append("Name: ").append(firstName).append(" ")
                            .append(lastName).append("\n");
                    displayText.append("Room Number: ").append(roomNumber).append("\n\n");
                }
            }
            residentInfoArea.setText(displayText.length() > 0 ? displayText.toString() : "No resident found.");
        } catch (Exception e) {
            residentInfoArea.setText("Error searching resident.");
            e.printStackTrace();
        }
    }
    // This method adds the package for the dialoguebox.
    private void addPackage() {
        try {
            int residentId = Integer.parseInt(residentIdField.getText());
            String trackingNumber = apartmentManager.addPackage(residentId);
            
            if (trackingNumber != null) {
                trackingNumberField.setText(trackingNumber);
                JOptionPane.showMessageDialog(this, "Package added successfully with Tracking Number: " + trackingNumber);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add package.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Resident ID.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding package: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // This method checks the packages for the dialoguebox.
    private void checkPackages() {
        try {
            int residentId = Integer.parseInt(residentIdField.getText());
            List<String> packages = apartmentManager.checkPackages(residentId);
            StringBuilder displayText = new StringBuilder();
            
            for (String pkg : packages) {
                // Parse the package string
                String[] parts = pkg.split(", ");
                // Extract resident ID and check for exact match
                int pkgResidentId = Integer.parseInt(parts[0].split(": ")[1]);
                if (pkgResidentId == residentId) {  // Only show packages for exact ID match
                    String trackingNum = parts[1].split(": ")[1];
                    String delivered = parts[2].split(": ")[1];
                    
                    displayText.append("Tracking Number: ").append(trackingNum).append("\n");
                    displayText.append("Delivered: ").append(delivered).append("\n\n");
                }
            }
            packageInfoArea.setText(displayText.length() > 0 ? displayText.toString() : "No packages found.");
        } catch (NumberFormatException e) {
            packageInfoArea.setText("Please enter a valid Resident ID.");
        } catch (Exception e) {
            packageInfoArea.setText("Error checking packages.");
            e.printStackTrace();
        }
    }
    // This method handles the temp card checkout for the dialoguebox.
    private void handleTempCardCheckout() {
        try {
            int residentId = Integer.parseInt(tempCardResidentIdField.getText());
            
            if (isCardAlreadyCheckedOut(residentId)) {
                cardStatus.setText("Cannot issue temp card - Resident already has a card checked out");
                return;
            }
            
            String tempCardId = "TC-" + String.format("%05d", residentId);
            
            List<String> tempCards = apartmentManager.readLines("src/tempCards.txt");
            List<String> updatedCards = new ArrayList<>();
            for (String card : tempCards) {
                if (card.contains("ResidentID: " + residentId)) {
                    String tempId = card.split(",")[0].trim();
                    card = tempId + ", ResidentID: " + residentId + ", TempCardCheckedOut: Yes";
                }
                updatedCards.add(card);
            }
            
            apartmentManager.writeLines("src/tempCards.txt", updatedCards);
            cardStatus.setText("Temp Card Issued\nCard ID: " + tempCardId);
            
        } catch (NumberFormatException e) {
            cardStatus.setText("Please enter a valid Resident ID.");
        } catch (Exception e) {
            cardStatus.setText("Error processing temp card request.");
            e.printStackTrace();
        }
    }
    // This method handles the temp card return for the dialoguebox.
    private void handleTempCardReturn() {
        try {
            int residentId = Integer.parseInt(tempCardResidentIdField.getText());
            
            // First check if they actually have a card checked out
            if (!isCardAlreadyCheckedOut(residentId)) {
                cardStatus.setText("No temp card found checked out for Resident ID: " + residentId);
                return;
            }
            
            // Update the file to mark card as returned
            List<String> tempCards = apartmentManager.readLines("src/tempCards.txt");
            List<String> updatedCards = new ArrayList<>();
            for (String card : tempCards) {
                if (card.contains("ResidentID: " + residentId)) {
                    // Preserve the TempID when updating the line
                    String tempId = card.split(",")[0].trim();
                    card = tempId + ", ResidentID: " + residentId + ", TempCardCheckedOut: No";
                }
                updatedCards.add(card);
            }
            
            apartmentManager.writeLines("src/tempCards.txt", updatedCards);
            cardStatus.setText("Temp Card Successfully Returned for Resident ID: " + residentId);
            
        } catch (NumberFormatException e) {
            cardStatus.setText("Please enter a valid Resident ID.");
        } catch (Exception e) {
            cardStatus.setText("Error processing temp card return.");
            e.printStackTrace();
        }
    }
    // This method creates the temp card panel for the dialoguebox.
    private JPanel createTempCardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout());
        
        // Create and use new dedicated field for temp cards
        tempCardResidentIdField = new JTextField(10);
        inputPanel.add(new JLabel("Resident ID:"));
        inputPanel.add(tempCardResidentIdField);
        
        JButton checkoutButton = new JButton("Checkout Card");
        JButton returnButton = new JButton("Return Card");
        cardStatus = new JTextArea();
        cardStatus.setEditable(false);
    
        inputPanel.add(checkoutButton);
        inputPanel.add(returnButton);
    
        // Update the handlers to use residentIdField instead of residentIdInput
        checkoutButton.addActionListener(e -> handleTempCardCheckout());
        returnButton.addActionListener(e -> handleTempCardReturn());
    
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(cardStatus), BorderLayout.CENTER);
        return panel;
    }
    // This method checks if the card is already checked out for the dialoguebox.
    private boolean isCardAlreadyCheckedOut(int residentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/tempCards.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ResidentID: " + residentID) && 
                    line.contains("TempCardCheckedOut: Yes")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    // This method marks the package as delivered for the dialoguebox.
    private void markPackageDelivered() {
        try {
            String trackingNumber = JOptionPane.showInputDialog(this, "Enter tracking number to mark as delivered:");
            if (trackingNumber != null && !trackingNumber.trim().isEmpty()) {
                boolean success = apartmentManager.markDelivered(trackingNumber.trim());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Package marked as delivered successfully.");
                    // Refresh the package display
                    checkPackages();
                } else {
                    JOptionPane.showMessageDialog(this, "Package not found or already delivered.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error marking package as delivered: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

