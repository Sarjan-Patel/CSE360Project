package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import databasePart1.DatabaseHelper;

/**
 * AdminPage class represents the user interface for the admin user.
 * This page provides administrative functions including user and role management.
 */
public class AdminHomePage {
    private final DatabaseHelper databaseHelper;

    public AdminHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(15);  // 15 pixels spacing between elements
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        // Admin Dashboard Header
        Label adminLabel = new Label("Administrator Dashboard");
        adminLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // User Management Section
        Label userManagementLabel = new Label("User Management");
        userManagementLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button manageRolesButton = new Button("Manage User Roles");
        manageRolesButton.setOnAction(e -> {
            new UserRoleManagementPage(databaseHelper).show(primaryStage);
        });
        
        // Invitation Management Section
        Label invitationLabel = new Label("Invitation Management");
        invitationLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button generateInviteButton = new Button("Generate Invitation Code");
        generateInviteButton.setOnAction(e -> {
            new InvitationPage().show(databaseHelper, primaryStage);
        });
        
        // System Management Section
        Label systemLabel = new Label("System Management");
        systemLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button viewSystemLogsButton = new Button("View System Logs");
        Button systemSettingsButton = new Button("System Settings");
        
        // Back button to return to role selection
        Button backButton = new Button("Back to Role Selection");
        backButton.setOnAction(e -> {
            // Note: You'll need to pass the current user object here
            // This is just a placeholder - modify according to your needs
            new WelcomeLoginPage(databaseHelper).show(primaryStage, null);
        });
        
        // Add some padding between sections
        VBox.setMargin(userManagementLabel, new Insets(20, 0, 10, 0));
        VBox.setMargin(invitationLabel, new Insets(20, 0, 10, 0));
        VBox.setMargin(systemLabel, new Insets(20, 0, 10, 0));
        VBox.setMargin(backButton, new Insets(30, 0, 0, 0));
        
        // Add all components to layout
        layout.getChildren().addAll(
            adminLabel,
            userManagementLabel,
            manageRolesButton,
            invitationLabel,
            generateInviteButton,
            systemLabel,
            viewSystemLogsButton,
            systemSettingsButton,
            backButton
        );
        
        Scene adminScene = new Scene(layout, 800, 600);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Dashboard");
    }
}