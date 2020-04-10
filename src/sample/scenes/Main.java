package sample.scenes;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
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

    Controller controller;
    @FXML
    MenuBar menuBar = new MenuBar();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/main.fxml"));
        Parent root = fxmlLoader.load();
        Menu menu = new Menu("Menu");
        MenuItem menuItem = new MenuItem("Scan");
        menu.getItems().add(menuItem);
        menuBar.getMenus().add(menu);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuBar, root);
        primaryStage.setTitle(ApplicationConstant.APPLICATION_TITLE);
        Scene scene = new Scene(vBox, ApplicationConstant.SCREEN_WIDTH, ApplicationConstant.SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        controller = fxmlLoader.getController();
        Observable.just("").subscribeOn(Schedulers.computation()).subscribe(s -> controller.onMenuClicked(), error -> System.out.println(error.toString()));
        menuItem.setOnAction(e -> {
            menuItem.setVisible(false);
            menuItem.setVisible(true);
            Observable.just("").subscribeOn(Schedulers.computation()).subscribe(s -> controller.onMenuClicked());
        });
        primaryStage.show();
    }
}
