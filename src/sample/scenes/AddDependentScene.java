package sample.scenes;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import sample.controller.AddDependentController;
import sample.model.Report;

import java.util.Optional;

public class AddDependentScene {

    private Report report;

    public AddDependentScene(Report report) {
        this.report = report;
    }

    public AddDependentScene() {
    }

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/add_dependent.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle(ApplicationConstant.APPLICATION_TITLE);
        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Close Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to close the application?");

            Optional<ButtonType> buttonResult = alert.showAndWait();
            if (buttonResult.get() == ButtonType.OK) {
                Platform.exit();
            } else if (buttonResult.get() == ButtonType.CANCEL) {
                event.consume();
            }
        });
        Scene scene = new Scene(root, ApplicationConstant.SCREEN_WIDTH, ApplicationConstant.SCREEN_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("../css/TextField.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../css/ChoiceBox.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        AddDependentController controller = fxmlLoader.getController();
        controller.setReport(report);
        controller.setPrevStage(primaryStage);
    }
}
