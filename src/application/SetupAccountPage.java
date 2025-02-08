package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import databasePart1.*;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their userName, password, and a valid invitation code to register.
 */
public class SetupAccountPage {
    private final DatabaseHelper databaseHelper;
    
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        // Input fields for userName, password, and invitation code
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter Invitation Code");
        inviteCodeField.setMaxWidth(250);

        // Role selection with checkboxes for multiple roles
        VBox roleSelectionBox = new VBox(5);
        roleSelectionBox.setMaxWidth(250);
        
        Label roleLabel = new Label("Select Role(s):");
        CheckBox studentCheckBox = new CheckBox("Student");
        CheckBox instructorCheckBox = new CheckBox("Instructor");
        CheckBox staffCheckBox = new CheckBox("Staff");
        CheckBox reviewerCheckBox = new CheckBox("Reviewer");
        
        roleSelectionBox.getChildren().addAll(
            roleLabel,
            studentCheckBox,
            instructorCheckBox,
            staffCheckBox,
            reviewerCheckBox
        );
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button setupButton = new Button("Setup");
        setupButton.setOnAction(a -> {
            // Retrieve user input
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String code = inviteCodeField.getText();
            
            // Validate username
            String userNameError = UserNameRecognizer.checkForValidUserName(userName);
            if(!userNameError.isEmpty()) {
                errorLabel.setText("Invalid Username: " + userNameError);
                return;
            }
            
            // Validate password
            String passwordError = PasswordEvaluator.evaluatePassword(password);
            if(!passwordError.isEmpty()) {
                errorLabel.setText("Invalid password: " + passwordError);
                return;
            }
            
            // Check if at least one role is selected
            if (!studentCheckBox.isSelected() && 
                !instructorCheckBox.isSelected() && 
                !staffCheckBox.isSelected() && 
                !reviewerCheckBox.isSelected()) {
                errorLabel.setText("Please select at least one role");
                return;
            }

            try {
                // Check if user already exists
                if(!databaseHelper.doesUserExist(userName)) {
                    // Validate invitation code
                    if(databaseHelper.validateInvitationCode(code)) {
                        // Create new user with initial role (we'll add more roles after)
                        User user = new User(userName, password, "");
                        
                        // Add selected roles
                        if(studentCheckBox.isSelected()) user.addRole("student");
                        if(instructorCheckBox.isSelected()) user.addRole("instructor");
                        if(staffCheckBox.isSelected()) user.addRole("staff");
                        if(reviewerCheckBox.isSelected()) user.addRole("reviewer");
                        
                        // Register user in database
                        databaseHelper.register(user);
                        
                        // Navigate to the Welcome Login Page
                        new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
                    } else {
                        errorLabel.setText("Please enter a valid invitation code");
                    }
                } else {
                    errorLabel.setText("This username is taken! Please use another to setup an account");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
                errorLabel.setText("An error occurred during account setup");
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(
            userNameField, 
            passwordField,
            inviteCodeField, 
            roleSelectionBox,
            setupButton, 
            errorLabel
        );

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}