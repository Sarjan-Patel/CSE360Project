package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

/**
 * This page displays a simple welcome message for the instructor.
 */
public class InstructorHomePage {

    private final DatabaseHelper databaseHelper;

    // Constructor to initialize database helper
    public InstructorHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label to display welcome message
        Label instructorLabel = new Label("Welcome, Instructor!");
        instructorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> {
            new SetupLoginSelectionPage(databaseHelper).show(primaryStage); // Redirect to login page
        });

        // Add components to the layout
        layout.getChildren().addAll(instructorLabel, logoutButton);

        // Create scene and set it to primary stage
        Scene instructorScene = new Scene(layout, 800, 400);
        primaryStage.setScene(instructorScene);
        primaryStage.setTitle("Instructor Page");
    }
}
