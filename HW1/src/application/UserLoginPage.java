package application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page based on their role.
 */
public class UserLoginPage {
    
    private final DatabaseHelper databaseHelper;
    private final boolean isPostSetup; // Flag to indicate if this is a post-setup login
    
    public UserLoginPage(DatabaseHelper databaseHelper) {
        this(databaseHelper, false);
    }

    public UserLoginPage(DatabaseHelper databaseHelper, boolean isPostSetup) {
        this.databaseHelper = databaseHelper;
        this.isPostSetup = isPostSetup;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Add title label
        Label titleLabel = new Label(isPostSetup ? 
            "Please log in with your new account" : 
            "Login to Your Account");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter username");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 14px;");
        
        // Add "Setup New Account" button for non-post-setup login
        Button setupButton = null;
        if (!isPostSetup) {
            setupButton = new Button("Setup New Account");
            setupButton.setStyle("-fx-font-size: 14px;");
            setupButton.setOnAction(e -> {
                try {
                    // Check if this is the first user
                    boolean isFirstUser = !databaseHelper.hasAnyUsers();
                    new SetupAccountPage(databaseHelper, isFirstUser).show(primaryStage);
                } catch (SQLException ex) {
                    errorLabel.setText("System error. Please try again later.");
                    ex.printStackTrace();
                }
            });
        }

     // In UserLoginPage.java, modify the loginButton.setOnAction handler:

        loginButton.setOnAction(a -> {
            String userName = userNameField.getText().trim();
            String password = passwordField.getText();

            // Basic input validation
            if (userName.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter both username and password");
                return;
            }

            try {
                String role = databaseHelper.getUserRole(userName);
                
                if (role != null) {
                    User user = new User(userName, password, role);
                    
                    if (databaseHelper.login(user)) {
                        // Direct navigation based on role
                        switch (role.toLowerCase()) {
                            case User.ROLE_ADMIN:
                                new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
                                break;
                            case User.ROLE_STUDENT:
                            case User.ROLE_INSTRUCTOR:
                            case User.ROLE_STAFF:
                            case User.ROLE_REVIEWER:
                                new UserHomePage().show(primaryStage);
                                break;
                            default:
                                errorLabel.setText("Invalid role configuration");
                        }
                    } else {
                        errorLabel.setText("Invalid username or password");
                        passwordField.clear();
                    }
                } else {
                    errorLabel.setText("Account not found");
                    passwordField.clear();
                }
                
            } catch (SQLException e) {
                errorLabel.setText("System error. Please try again later.");
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });
        // Build the layout
        layout.getChildren().add(titleLabel);
        if (isPostSetup) {
            Label infoLabel = new Label("Your account has been created successfully.\nPlease log in to continue.");
            infoLabel.setStyle("-fx-text-alignment: center;");
            layout.getChildren().add(infoLabel);
        }
        layout.getChildren().addAll(userNameField, passwordField, loginButton, errorLabel);
        if (setupButton != null) {
            layout.getChildren().add(setupButton);
        }

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }
}