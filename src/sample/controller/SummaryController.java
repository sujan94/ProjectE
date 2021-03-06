package sample.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import sample.model.ProjectSummaryModel;
import sample.repository.MainRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryController extends BaseController {

    @FXML
    public Label employeeSummaryfield;

    @FXML
    public Label employeeSummaryLabel;

    @FXML
    public Label connectionStatus;

    @FXML
    public TilePane summaryTilePane;
    @FXML
    public Label departmentSummaryfield;
    @FXML
    public Label departmentSummaryLabel;
    @FXML
    public Label projectSummaryfield;
    @FXML
    public Label projectSummaryLabel;

    public AnchorPane rootSummary;
    Map<Integer, String> employeeSummary = new HashMap<>();

    public void setConnectionAvailable(boolean connectionAvailable) {
        connectionStatus.setVisible(!connectionAvailable);
        summaryTilePane.setVisible(connectionAvailable);
    }

    public void initialize() {
        isSummaryRoot = true;
        fetchEmployeeSummary();
        fetchProjectSummary();
    }

    private void fetchProjectSummary() {
        List<ProjectSummaryModel> projectSummaryModelList = MainRepository.getInstance().getProjectSummary();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (isSummaryRoot) {
                    int finalCount = count;
                    setConnectionAvailable(projectSummaryModelList != null && !projectSummaryModelList.isEmpty());
                    System.out.println(Thread.currentThread().getId());
                    Platform.runLater(() -> {
                        if (projectSummaryModelList != null && !projectSummaryModelList.isEmpty()) {
                            projectSummaryfield.setText(projectSummaryModelList.get(finalCount % projectSummaryModelList.size()).getTotalHoursInProject());
                            projectSummaryLabel.setText(projectSummaryModelList.get(finalCount % projectSummaryModelList.size()).getProjectName());
                            departmentSummaryfield.setText(projectSummaryModelList.get(finalCount % projectSummaryModelList.size()).getEmployeesInProject());
                            departmentSummaryLabel.setText(projectSummaryModelList.get(finalCount % projectSummaryModelList.size()).getProjectName());
                        }
                    });
                    count++;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread projectThread = new Thread(runnable);
        projectThread.start();

    }

    private void fetchEmployeeSummary() {
        employeeSummary.put(1, "");
        employeeSummary.put(2, "");
        employeeSummary.put(3, "");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (isSummaryRoot) {
                    int finalCount = count;
                    String value = MainRepository.getInstance().getSummary(finalCount % employeeSummary.size());
                    setConnectionAvailable(value != null);
                    System.out.println(Thread.currentThread().getId());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            employeeSummaryfield.setText(value);
                            employeeSummaryLabel.setText(getState(finalCount % employeeSummary.size()));

                        }
                    });
                    count++;
                    try {
                        Thread.sleep(9000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread employeeThread = new Thread(runnable);
        employeeThread.start();
    }

    public String getState(int action) {
        switch (action) {
            case 0:
                return "Total Employees";
            case 1:
                return "Highest Salary";
            case 2:
            default:
                return "Lowest Salary";
        }
    }

}
