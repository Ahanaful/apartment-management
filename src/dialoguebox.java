package src;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dialoguebox extends JFrame {
    private JTextField residentSearchField, trackingNumberField, residentIdField;
    private JTextArea residentInfoArea, packageInfoArea;
    private ApartmentManager apartmentManager;

    public dialoguebox() {
        apartmentManager = new ApartmentManager();
        setTitle("Apartment Community Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Residents", createResidentPanel());
        tabbedPane.addTab("Packages", createPackagePanel());
        tabbedPane.addTab("Temporary Cards", createPlaceholderPanel("Temporary Card Management"));
        tabbedPane.addTab("Lockouts", createPlaceholderPanel("Lockout Management"));

        add(tabbedPane);
    }

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

    private JPanel createPackagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(5, 2));

        residentIdField = new JTextField(15);
        trackingNumberField = new JTextField(15);

        formPanel.add(new JLabel("Resident ID:"));
        formPanel.add(residentIdField);
        formPanel.add(new JLabel("Tracking Number:"));
        formPanel.add(trackingNumberField);

        JButton addButton = new JButton("Add Package");
        JButton checkButton = new JButton("Check Packages");

        addButton.addActionListener(e -> addPackage());
        checkButton.addActionListener(e -> checkPackages());

        formPanel.add(addButton);
        formPanel.add(checkButton);

        packageInfoArea = new JTextArea();
        packageInfoArea.setEditable(false);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(packageInfoArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPlaceholderPanel(String labelText) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void searchResident() {
        String searchText = residentSearchField.getText();
        try {
            StringBuilder results = new StringBuilder();
            ResultSet resultSet = apartmentManager.searchResident(searchText);
            while (resultSet.next()) {
                results.append("ID: ").append(resultSet.getInt("ResidentID")).append("\n");
                results.append("Name: ").append(resultSet.getString("FirstName")).append(" ")
                        .append(resultSet.getString("LastName")).append("\n");
                results.append("Room Number: ").append(resultSet.getInt("RoomNumber")).append("\n\n");
            }
            residentInfoArea.setText(results.length() > 0 ? results.toString() : "No resident found.");
        } catch (SQLException e) {
            residentInfoArea.setText("Error searching resident.");
            e.printStackTrace();
        }
    }

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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding package: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void checkPackages() {
        try {
            int residentId = Integer.parseInt(residentIdField.getText());
            StringBuilder results = new StringBuilder();
            ResultSet resultSet = apartmentManager.checkPackages(residentId);
            while (resultSet.next()) {
                results.append("Tracking Number: ").append(resultSet.getString("TrackingNumber")).append("\n");
                results.append("Delivered: ").append(resultSet.getString("Delivered")).append("\n\n");
            }
            packageInfoArea.setText(results.length() > 0 ? results.toString() : "No packages found.");
        } catch (SQLException e) {
            packageInfoArea.setText("Error checking packages.");
            e.printStackTrace();
        }
    }

    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> new dialoguebox().setVisible(true));
    // }
}
