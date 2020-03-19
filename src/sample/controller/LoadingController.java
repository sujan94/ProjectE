package sample.controller;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;

public class LoadingController {
    public ProgressBar progress;
    public GridPane root;
    public Label label;

    public void setProgress(float progressValue) {
        progress.setProgress(progressValue);
    }

    public void setText(String newMessage, boolean isError) {
        label.setText(newMessage);
        if (isError) {
            label.getStyleClass().add("error");
        }
    }

}
