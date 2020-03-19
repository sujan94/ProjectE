package sample.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Graph {

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/graph.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle(ApplicationConstant.APPLICATION_TITLE);
        Scene scene = new Scene(root, ApplicationConstant.SCREEN_WIDTH, ApplicationConstant.SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
