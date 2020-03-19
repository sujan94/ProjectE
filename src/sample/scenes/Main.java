package sample.scenes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controller.Controller;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/sample.fxml"));
        controller = fxmlLoader.getController();
        Parent root = fxmlLoader.load();
        primaryStage.setTitle(ApplicationConstant.APPLICATION_TITLE);
        Scene scene = new Scene(root, ApplicationConstant.SCREEN_WIDTH, ApplicationConstant.SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
