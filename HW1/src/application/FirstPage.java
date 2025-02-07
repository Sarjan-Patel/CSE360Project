package application;
import databasePart1.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FirstPage class represents the initial screen for the first user.
 * It prompts the user to set up their account with admin role and navigate to the setup screen.
 */
public class FirstPage {
    
    private final DatabaseHelper databaseHelper;
    
    public FirstPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the first page in the provided primary stage. 
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox(10); // Increased spacing for better layout
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        // Updated welcome message to better reflect the user story
        Label welcomeLabel = new Label("Welcome to the System!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label infoLabel = new Label(
            "As the first user, you will be assigned an administrator role.\n" +
            "You'll need to set up your username, password, and account information.\n" +
            "After setup, you will be required to log in again."
        );
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
        
        Button setupButton = new Button("Begin Account Setup");
        setupButton.setStyle("-fx-font-size: 14px;");
        
        // Changed to route to SetupAccountPage instead of AdminSetupPage
        setupButton.setOnAction(e -> {
            // Pass true to indicate this is the first user (admin)
            new SetupAccountPage(databaseHelper, true).show(primaryStage);
        });
        
        layout.getChildren().addAll(welcomeLabel, infoLabel, setupButton);
        
        Scene firstPageScene = new Scene(layout, 800, 400);
        primaryStage.setScene(firstPageScene);
        primaryStage.setTitle("Welcome - First Time Setup");
        primaryStage.show();
    }
}