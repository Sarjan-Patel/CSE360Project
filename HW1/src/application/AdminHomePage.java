package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import databasePart1.DatabaseHelper;

public class AdminHomePage {
    private final DatabaseHelper databaseHelper;

    public AdminHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Welcome label
        Label adminLabel = new Label("Hello, Admin!");
        adminLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Create table
        TableView<User> table = new TableView<>();
        table.setMinWidth(600);

        // Username column
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        usernameCol.setPrefWidth(200);

        // Role column
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(200);

        // Action column with buttons
        TableColumn<User, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(200);
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button otpButton = new Button("Set OTP");
            private final HBox buttonBox = new HBox(5);
            
            {
                deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
                otpButton.setStyle("-fx-background-color: #e0e0e0;");
                buttonBox.getChildren().addAll(deleteButton, otpButton);
                
                deleteButton.setOnAction(event -> {
                    User user = getTableRow().getItem();
                    if (user != null) {
                        System.out.println("Delete clicked for: " + user.getUserName());
                        // Add delete functionality here
                    }
                });
            
            
            otpButton.setOnAction(event -> {
                User user = getTableRow().getItem();
                if (user != null) {
                    // Generate and set OTP
                    String otp = generateOTP(); // You'll need to implement this
                    try {
                        databaseHelper.setOneTimePassword(user.getUserName(), otp);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("OTP Set");
                        alert.setHeaderText(null);
                        alert.setContentText("OTP for " + user.getUserName() + ": " + otp);
                        alert.showAndWait();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

            private String generateOTP() {
                // Generate a random 6-digit number
                int randomPIN = (int)(Math.random()*900000)+100000;
                return String.valueOf(randomPIN);
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);  // Change from deleteButton to buttonBox
            }
        });

        table.getColumns().addAll(usernameCol, roleCol, actionCol);

        // Load data into table
        try {
            ResultSet rs = databaseHelper.getAllUsers();
            while (rs.next()) {
                table.getItems().add(new User(
                    rs.getString("userName"),
                    "",  // We don't show password
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #4285f4; -fx-text-fill: white;");
        backButton.setOnAction(e -> new WelcomeLoginPage(databaseHelper).show(primaryStage, new User("admin", "", "admin")));

        layout.getChildren().addAll(adminLabel, table, backButton);
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Admin Page");
    }
}
