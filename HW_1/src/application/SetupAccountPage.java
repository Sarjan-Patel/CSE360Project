package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their username and password. No database connection is used.
 */
public class SetupAccountPage {

    public void show(Stage primaryStage) {
        // Input fields for username and password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Username");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button setupButton = new Button("Setup");

        setupButton.setOnAction(a -> {
            // Retrieve user input
            String userName = userNameField.getText().trim();
            String password = passwordField.getText().trim();

            // Collect all error messages
            StringBuilder errorMessages = new StringBuilder();

            // Validate username
            String usernameError = UserNameRecognizer.checkForValidUserName(userName);
            if (!usernameError.isEmpty()) {
                errorMessages.append("Username Error:\n").append(usernameError).append("\n");
            }

            // Leave one line between username and password errors
            if (!usernameError.isEmpty()) {
                errorMessages.append("\n");
            }

            // Validate password
            String passwordError = PasswordEvaluator.evaluatePassword(password);
            if (!passwordError.isEmpty()) {
                errorMessages.append("Password Error:\n").append(passwordError);
            }

            // If there are errors, display them
            if (errorMessages.length() > 0) {
                errorLabel.setText(errorMessages.toString());
                return;
            }

            // If all validations pass, navigate to Welcome Page
            new WelcomePage().show(primaryStage);
        });

        VBox layout = new VBox(10, userNameField, passwordField, setupButton, errorLabel);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}
