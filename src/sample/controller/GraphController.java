package sample.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import sample.model.ProjectSummaryModel;
import sample.repository.MainRepository;

public class GraphController {
    public AnchorPane root;
    public BarChart barChart;

    public void initialize() {
        requestProjectSummary();
    }

    private void requestProjectSummary() {
        Observable.fromArray(MainRepository.getInstance().getProjectSummary())
                .subscribeOn(Schedulers.io())
                .subscribe(projectSummaryModels -> Platform.runLater(() -> {
                    for (ProjectSummaryModel p : projectSummaryModels) {
                        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
                        series1.getData().add(new XYChart.Data<>(p.getProjectName(), Double.parseDouble(p.getTotalHoursInProject())));
                        series1.setName(p.getProjectName());
                        barChart.getData().add(series1);
                    }

                }));
    }


}
