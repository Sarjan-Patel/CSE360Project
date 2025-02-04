package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * WelcomePage class represents the next screen after account setup.
 */
public class WelcomePage {

    public void show(Stage primaryStage) {
        VBox layout = new VBox(15); // Added spacing between elements
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label welcomeLabel = new Label("Account Setup Complete! Welcome!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Back button to return to the SetupAccountPage
        Button backButton = new Button("Back to Login");
        backButton.setOnAction(e -> new SetupAccountPage().show(primaryStage));

        layout.getChildren().addAll(welcomeLabel, backButton);
        Scene scene = new Scene(layout, 800, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome Page");
        primaryStage.show();
    }
}
