import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class dialoguebox extends JFrame {

    private JTextField residentSearchField, trackingNumberField, recipientField;
    private JTextArea residentInfoArea;

    public ApartmentManagementApp() {
        setTitle("Apartment Community Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add each panel directly within the main class
        tabbedPane.addTab("Residents", createResidentPanel());
        tabbedPane.addTab("Packages", createPackagePanel());
        tabbedPane.addTab("Temporary Cards", createPlaceholderPanel("Temporary Card Management"));
        tabbedPane.addTab("Lockouts", createPlaceholderPanel("Lockout Management"));
        tabbedPane.addTab("Equipment Checkout", createPlaceholderPanel("Equipment Checkout"));

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
        JPanel formPanel = new JPanel(new GridLayout(4, 2));

        trackingNumberField = new JTextField(15);
        recipientField = new JTextField(15);

        formPanel.add(new JLabel("Tracking Number:"));
        formPanel.add(trackingNumberField);
        formPanel.add(new JLabel("Recipient:"));
        formPanel.add(recipientField);

        JButton addButton = new JButton("Add Package");
        JButton retrieveButton = new JButton("Retrieve Package");

        addButton.addActionListener(e -> addPackage());
        retrieveButton.addActionListener(e -> retrievePackage());

        formPanel.add(addButton);
        formPanel.add(retrieveButton);

        panel.add(formPanel, BorderLayout.CENTER);
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
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM residents WHERE name LIKE ? OR id = ?")) {
            stmt.setString(1, "%" + searchText + "%");
            stmt.setString(2, searchText);
            ResultSet rs = stmt.executeQuery();

            residentInfoArea.setText("");
            while (rs.next()) {
                residentInfoArea.append("ID: " + rs.getInt("id") + "\n");
                residentInfoArea.append("Name: " + rs.getString("name") + "\n");
                residentInfoArea.append("Apartment: " + rs.getString("apartment") + "\n");
                residentInfoArea.append("Phone: " + rs.getString("phone") + "\n\n");
            }

            if (residentInfoArea.getText().isEmpty()) {
                residentInfoArea.setText("No resident found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPackage() {
        String trackingNumber = trackingNumberField.getText();
        String recipient = recipientField.getText();
        // For demonstration, we'll just display a message
        JOptionPane.showMessageDialog(this, "Package for " + recipient + " added with tracking number: " + trackingNumber);

        // Clear fields after adding package
        trackingNumberField.setText("");
        recipientField.setText("");
    }

    private void retrievePackage() {
        // Placeholder for retrieve package functionality
        JOptionPane.showMessageDialog(this, "Retrieve package functionality to be implemented");
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment_management", "user", "password");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ApartmentManagementApp().setVisible(true));
    }
}
