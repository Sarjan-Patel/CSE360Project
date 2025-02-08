package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import java.sql.SQLException;
import java.util.Set;
import databasePart1.DatabaseHelper;

/**
 * UserRoleManagementPage provides an interface for administrators to manage user roles.
 * Administrators can view users, add/remove roles, and update user permissions.
 */
public class UserRoleManagementPage {
    private final DatabaseHelper databaseHelper;
    private ComboBox<String> userSelect;
    private ListView<String> currentRolesView;
    private ListView<String> availableRolesView;
    private Label statusLabel;
    
    // Define all possible roles
    private final String[] ALL_ROLES = {"admin", "student", "instructor", "staff", "reviewer"};

    public UserRoleManagementPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));

        // Header
        Label headerLabel = new Label("User Role Management");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // User selection section
        Label userSelectLabel = new Label("Select User:");
        userSelect = new ComboBox<>();
        refreshUserList();

        // Current roles section
        Label currentRolesLabel = new Label("Current Roles:");
        currentRolesView = new ListView<>();
        currentRolesView.setPrefHeight(150);

        // Available roles section
        Label availableRolesLabel = new Label("Available Roles:");
        availableRolesView = new ListView<>();
        availableRolesView.setPrefHeight(150);

        // Role management buttons
        Button addRoleButton = new Button("Add Selected Role");
        Button removeRoleButton = new Button("Remove Selected Role");
        
        // Status label for feedback
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");

        // Action handlers
        userSelect.setOnAction(e -> refreshUserRoles());
        
        addRoleButton.setOnAction(e -> {
            String selectedUser = userSelect.getValue();
            String selectedRole = availableRolesView.getSelectionModel().getSelectedItem();
            
            if (selectedUser == null || selectedRole == null) {
                statusLabel.setText("Please select both a user and a role");
                return;
            }

            try {
                databaseHelper.addUserRole(selectedUser, selectedRole);
                refreshUserRoles();
                statusLabel.setText("Role added successfully");
            } catch (SQLException ex) {
                statusLabel.setText("Error adding role: " + ex.getMessage());
            }
        });

        removeRoleButton.setOnAction(e -> {
            String selectedUser = userSelect.getValue();
            String selectedRole = currentRolesView.getSelectionModel().getSelectedItem();
            
            if (selectedUser == null || selectedRole == null) {
                statusLabel.setText("Please select both a user and a role");
                return;
            }

            // Prevent removing the last role
            try {
                Set<String> currentRoles = databaseHelper.getUserRoles(selectedUser);
                if (currentRoles.size() <= 1) {
                    statusLabel.setText("Cannot remove the last role from a user");
                    return;
                }

                databaseHelper.removeUserRole(selectedUser, selectedRole);
                refreshUserRoles();
                statusLabel.setText("Role removed successfully");
            } catch (SQLException ex) {
                statusLabel.setText("Error removing role: " + ex.getMessage());
            }
        });

        // Back button
        Button backButton = new Button("Back to Admin Page");
        backButton.setOnAction(e -> new AdminHomePage(databaseHelper).show(primaryStage));

        // Layout organization
        HBox roleButtonsBox = new HBox(10, addRoleButton, removeRoleButton);
        roleButtonsBox.setPadding(new Insets(10, 0, 10, 0));
        
        // Add components to main layout
        mainLayout.getChildren().addAll(
            headerLabel,
            userSelectLabel,
            userSelect,
            currentRolesLabel,
            currentRolesView,
            availableRolesLabel,
            availableRolesView,
            roleButtonsBox,
            statusLabel,
            backButton
        );

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("User Role Management");
    }

    // Helper method to refresh the user list
    private void refreshUserList() {
        try {
            Set<String> users = databaseHelper.getAllUsers();
            userSelect.setItems(FXCollections.observableArrayList(users));
        } catch (SQLException e) {
            statusLabel.setText("Error loading users: " + e.getMessage());
        }
    }

    // Helper method to refresh the roles lists
    private void refreshUserRoles() {
        String selectedUser = userSelect.getValue();
        if (selectedUser == null) return;

        try {
            // Get current roles for the selected user
            Set<String> userRoles = databaseHelper.getUserRoles(selectedUser);
            
            // Update current roles list
            currentRolesView.setItems(FXCollections.observableArrayList(userRoles));
            
            // Update available roles list (roles not currently assigned)
            Set<String> availableRoles = FXCollections.observableArrayList(ALL_ROLES).stream()
                .filter(role -> !userRoles.contains(role))
                .collect(java.util.stream.Collectors.toSet());
            availableRolesView.setItems(FXCollections.observableArrayList(availableRoles));
            
        } catch (SQLException e) {
            statusLabel.setText("Error loading user roles: " + e.getMessage());
        }
    }
}