package application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import databasePart1.*;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * For the first user, it sets up an admin account without requiring an invitation code.
 */
public class SetupAccountPage {
    
    private final DatabaseHelper databaseHelper;
    private final boolean isFirstUser;
    
    public SetupAccountPage(DatabaseHelper databaseHelper, boolean isFirstUser) {
        this.databaseHelper = databaseHelper;
        this.isFirstUser = isFirstUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Title label changes based on whether this is first user or not
        Label titleLabel = new Label(isFirstUser ? 
            "Administrator Account Setup" : 
            "User Account Setup");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter username");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        // Only show invitation code field for non-admin users
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter InvitationCode");
        inviteCodeField.setMaxWidth(250);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        Button setupButton = new Button("Complete Setup");
        
        setupButton.setOnAction(a -> {
            String userName = userNameField.getText();
            String password = passwordField.getText();
            
            String userNameError = UserNameRecognizer.checkForValidUserName(userName);
            if(!userNameError.isEmpty()) {
                errorLabel.setText("Invalid Username: " + userNameError);
                return;
            }
            
            String passwordError = PasswordEvaluator.evaluatePassword(password);
            if(!passwordError.isEmpty()) {
                errorLabel.setText("Invalid password: " + passwordError);
                return;
            }
            
            try {
                if(!databaseHelper.doesUserExist(userName)) {
                    if (isFirstUser || databaseHelper.validateInvitationCode(inviteCodeField.getText())) {
                        // Create user with appropriate role
                        User user = new User(userName, password, isFirstUser ? "admin" : "student");
                        databaseHelper.register(user);
                        
                        // Show success message and redirect to login
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Account Created");
                        alert.setHeaderText(null);
                        alert.setContentText("Account successfully created. Please log in.");
                        alert.showAndWait();
                        
                        // Redirect to login page
                        new UserLoginPage(databaseHelper).show(primaryStage);
                    } else {
                        errorLabel.setText("Please enter a valid invitation code");
                    }
                } else {
                    errorLabel.setText("This username is already taken. Please choose another.");
                }
                
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                errorLabel.setText("An error occurred while creating your account.");
                e.printStackTrace();
            }
        });

        // Add components to layout based on user type
        layout.getChildren().add(titleLabel);
        layout.getChildren().addAll(userNameField, passwordField);
        if (!isFirstUser) {
            layout.getChildren().add(inviteCodeField);
        }

        layout.getChildren().addAll(setupButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle(isFirstUser ? "Administrator Setup" : "Account Setup");
        primaryStage.show();
    }
}