package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

public class ReviewerHomePage {
    
    private final DatabaseHelper databaseHelper;

    public ReviewerHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label label = new Label("Welcome, Reviewer!");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> {
            new SetupLoginSelectionPage(databaseHelper).show(primaryStage); // Redirect to login page
        });

        layout.getChildren().addAll(label, logoutButton);
        Scene scene = new Scene(layout, 800, 500);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Reviewer Home Page");
    }
}
