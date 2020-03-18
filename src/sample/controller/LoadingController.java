package sample.controller;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;

public class LoadingController {
    public ProgressBar progress;
    public GridPane root;

    public void setProgress(float progressValue) {
        progress.setProgress(progressValue);
    }

}
