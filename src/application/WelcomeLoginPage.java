package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import databasePart1.*;
import java.util.Set;

/**
 * The WelcomeLoginPage class displays a welcome screen for authenticated users.
 * It allows users to select from their assigned roles and navigate to appropriate pages.
 */
public class WelcomeLoginPage {
    private final DatabaseHelper databaseHelper;

    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        // Welcome message
        Label welcomeLabel = new Label("Welcome " + user.getUserName() + "!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Get user's roles and create role selection combobox
        Set<String> userRoles = user.getRoles();
        ComboBox<String> roleSelect = new ComboBox<>(
            FXCollections.observableArrayList(userRoles)
        );
        roleSelect.setPromptText("Select Role");
        
        Label roleLabel = new Label("Select role to continue as:");
        
        // Error label for validation messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        // Button to navigate to the selected role's page
        Button continueButton = new Button("Continue to Selected Role");
        continueButton.setOnAction(e -> {
            String selectedRole = roleSelect.getValue();
            if (selectedRole == null) {
                errorLabel.setText("Please select a role to continue");
                return;
            }
            
            navigateToRolePage(primaryStage, selectedRole, user);
        });
        
        // Add role management button for admin users
        if (user.hasRole("admin")) {
            Button manageUsersButton = new Button("Manage Users");
            manageUsersButton.setOnAction(e -> new UserRoleManagementPage(databaseHelper).show(primaryStage));
            
            Button inviteButton = new Button("Generate Invitation");
            inviteButton.setOnAction(e -> new InvitationPage().show(databaseHelper, primaryStage));
            
            layout.getChildren().addAll(manageUsersButton, inviteButton);
        }
        
        // Quit button
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> {
            databaseHelper.closeConnection();
            Platform.exit();
        });
        
        // Add all components to layout
        layout.getChildren().addAll(
            welcomeLabel,
            roleLabel,
            roleSelect,
            continueButton,
            errorLabel,
            quitButton
        );
        
        Scene welcomeScene = new Scene(layout, 800, 400);
        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Welcome Page");
    }
    
    private void navigateToRolePage(Stage primaryStage, String role, User user) {
        switch (role.toLowerCase()) {
            case "admin":
                new AdminHomePage().show(primaryStage);
                break;
            case "student":
                new StudentHomePage().show(primaryStage);
                break;
            case "instructor":
                new InstructorHomePage().show(primaryStage);
                break;
            case "staff":
                new StaffHomePage().show(primaryStage);
                break;
            case "reviewer":
                new ReviewerHomePage().show(primaryStage);
                break;
            default:
                new UserHomePage().show(primaryStage);
                break;
        }
    }
}