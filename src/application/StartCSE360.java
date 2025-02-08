package application;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.SQLException;
import databasePart1.DatabaseHelper;

public class StartCSE360 extends Application {

    private static final DatabaseHelper databaseHelper = new DatabaseHelper();
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Modified to handle ClassNotFoundException
            try {
                databaseHelper.connectToDatabase(); // Connect to the database
            } catch (ClassNotFoundException e) {
                System.err.println("SQLite JDBC Driver not found: " + e.getMessage());
                e.printStackTrace();
                return;
            }

            if (databaseHelper.isDatabaseEmpty()) {
                new FirstPage(databaseHelper).show(primaryStage);
            } else {
                new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}