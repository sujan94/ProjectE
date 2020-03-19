package sample.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controller.BaseController;

public class LoadingScene {

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/loading_scene.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle(ApplicationConstant.APPLICATION_TITLE);
        Scene scene = new Scene(root, ApplicationConstant.SCREEN_WIDTH, ApplicationConstant.SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
        BaseController controller = fxmlLoader.getController();

    }
}
