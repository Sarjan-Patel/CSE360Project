package application;

import databasePart1.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InvitationPage {
    public void show(DatabaseHelper databaseHelper, Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label titleLabel = new Label("Generate Invitation Code");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Role selection
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("user", "admin");
        roleComboBox.setPromptText("Select Role");
        roleComboBox.setMaxWidth(250);

        Label inviteCodeLabel = new Label();
        inviteCodeLabel.setStyle("-fx-font-size: 14px;");

        Button generateButton = new Button("Generate Invitation Code");
        generateButton.setOnAction(e -> {
            String selectedRole = roleComboBox.getValue();
            
            if (selectedRole == null) {
                inviteCodeLabel.setText("Please select a role");
                inviteCodeLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            String invitationCode = databaseHelper.generateInvitationCode(selectedRole);
            
            inviteCodeLabel.setText("Invitation Code: " + invitationCode);
            inviteCodeLabel.setStyle("-fx-text-fill: green;");
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new WelcomeLoginPage(databaseHelper).show(primaryStage, 
            new User("admin", "", "admin")));

        layout.getChildren().addAll(
            titleLabel, 
            roleComboBox, 
            generateButton, 
            inviteCodeLabel,
            backButton
        );

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Generate Invitation");
        primaryStage.show();
    }
}