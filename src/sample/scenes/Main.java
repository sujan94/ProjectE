package sample.scenes;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.controller.Controller;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    Controller controller;
    @FXML
    MenuBar menuBar = new MenuBar();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/main.fxml"));
        controller = fxmlLoader.getController();
        Parent root = fxmlLoader.load();
        Menu menu = new Menu("Menu");
        MenuItem menuItem = new MenuItem("Scan");
        menu.getItems().add(menuItem);
        menuBar.getMenus().add(menu);
        menuItem.setOnAction(e -> {
            controller.onMenuClicked();
        });
        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuBar, root);
        primaryStage.setTitle(ApplicationConstant.APPLICATION_TITLE);
        Scene scene = new Scene(vBox, ApplicationConstant.SCREEN_WIDTH, ApplicationConstant.SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
