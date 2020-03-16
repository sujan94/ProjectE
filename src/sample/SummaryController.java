package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryController {

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

    public void setConnectionAvailable(boolean connectionAvailable) {
        connectionStatus.setVisible(!connectionAvailable);
        summaryTilePane.setVisible(connectionAvailable);
    }

    Map<Integer, String> employeeSummary = new HashMap<>();

    public void initialize() {
        fetchEmployeeSummary();
        fetchProjectSummary();
    }

    private void fetchProjectSummary() {
        List<ProjectSummaryModel> projectSummaryModelList = MainRepository.getInstance().getProjectSummary();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (true) {
                    int finalCount = count;
                    setConnectionAvailable(projectSummaryModelList != null && !projectSummaryModelList.isEmpty());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(projectSummaryModelList != null && !projectSummaryModelList.isEmpty()) {
                                projectSummaryfield.setText(projectSummaryModelList.get(finalCount % projectSummaryModelList.size()).getTotalHoursInProject());
                                projectSummaryLabel.setText(projectSummaryModelList.get(finalCount % projectSummaryModelList.size()).getProjectName());
                                departmentSummaryfield.setText(projectSummaryModelList.get(finalCount % projectSummaryModelList.size()).getEmployeesInProject());
                                departmentSummaryLabel.setText(projectSummaryModelList.get(finalCount % projectSummaryModelList.size()).getProjectName());
                            }
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

        Thread thread = new Thread(runnable);
        thread.start();

    }

    private void fetchEmployeeSummary() {
        employeeSummary.put(1, "");
        employeeSummary.put(2, "");
        employeeSummary.put(3, "");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (true) {
                    int finalCount = count;
                    String value = MainRepository.getInstance().getSummary(finalCount % employeeSummary.size());
                    setConnectionAvailable(value != null);
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

        Thread thread = new Thread(runnable);
        thread.start();
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
