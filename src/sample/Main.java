package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MainRepository mainRepository = MainRepository.getInstance();
        try {
            mainRepository.initiateConnection();
        }catch (SQLException e){
            System.out.println(e.toString());
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Controller controller = fxmlLoader.getController();
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 900, 575));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
