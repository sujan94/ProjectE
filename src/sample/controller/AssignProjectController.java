package sample.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.model.Project;
import sample.model.Report;
import sample.model.WorksOn;
import sample.repository.MainRepository;
import sample.scenes.AddDependentScene;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignProjectController implements AssignItemController.AssignMoreCallback {

    public Button submitButton;
    public VBox vbox;
    public AnchorPane root;
    public Stage prevStage;
    private List<Project> projectLists = new ArrayList<>();
    private Map<String, Project> stringProjectMap = new HashMap<>();
    private List<AssignItemController> controllers = new ArrayList<>();
    private Report report;

    public void initialize() {

    }

    public void setReport(Report report) {
        this.report = report;
        if (report != null && report.getEmployee() != null) {
            projectLists = MainRepository.getInstance().getAllProjectsInDepartment(report.getEmployee().getDno());
            for (Project p : projectLists) {
                stringProjectMap.put(p.getPnumber(), p);
            }
            addAssignProjectController(projectLists);
        }
    }

    private void addAssignProjectController(List<Project> list) {
        AnchorPane project = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/item_assign_project.fxml"));
            project = fxmlLoader.load();
            AssignItemController assignItemController = fxmlLoader.getController();
            assignItemController.setAssignMoreCallback(this);
            controllers.add(assignItemController);
            vbox.getChildren().add(project);
            assignItemController.setEmployee(report.getEmployee());
            assignItemController.setProjectNumList(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }

    @Override
    public void onAssignMoreButtonClicked(AssignItemController assignItemController) {
        if (controllers.size() < 2) {
            ArrayList<Project> newProjectList = (ArrayList<Project>) ((ArrayList) projectLists).clone();
            newProjectList.remove(assignItemController.projectNumberCBox.getValue());
            addAssignProjectController(newProjectList);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error occured.");
            alert.setHeaderText(null);
            alert.setContentText("Too many project.\n\nAn employee can only be assigned to at most 2 projects.");

            alert.showAndWait();
        }
    }

    public void onAssignSubmitClicked() {
        int totalHours = 0;
        boolean isValid = true;
        Map<String, WorksOn> worksOnList = new HashMap<>();
        for (AssignItemController c : controllers) {
            if (!worksOnList.containsKey(c.projectNumberCBox.getValue())) {
                totalHours += Double.parseDouble(c.hoursTextField.getText());
                worksOnList.put(c.projectNumberCBox.getValue(), new WorksOn(c.employeeSSN.getText(), c.projectNumberCBox.getValue(), c.hoursTextField.getText()));
            } else {
                isValid = false;
                break;
            }
        }
        if (totalHours > 40 || !isValid) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Error in entries. \n\n Total hours exceed 40 hours or same project number is entered.");

            alert.showAndWait();
        } else {
            report.getEmployeeProject().clear();
            for (String key : worksOnList.keySet()) {
                try {
                    MainRepository.getInstance().assignProject(worksOnList.get(key));
                    report.getEmployeeProject().add(stringProjectMap.get(worksOnList.get(key).getProjectNumber()));
                    startAddDependentController();
                } catch (SQLException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Operation Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to assign employee to " + worksOnList.get(key));
                    alert.showAndWait();
                    ex.printStackTrace();
                    break;
                }
            }

        }
    }

    private void startAddDependentController() {
        Stage stage = (Stage) root.getScene().getWindow();
        try {
            new AddDependentScene(report).start(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
