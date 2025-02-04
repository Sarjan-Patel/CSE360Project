package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class StartCSE360 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Directly show SetupAccountPage on launch
        new SetupAccountPage().show(primaryStage);
    }
}
