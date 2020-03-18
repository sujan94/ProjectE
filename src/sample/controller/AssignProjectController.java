package sample.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.model.Employee;
import sample.model.WorksOn;
import sample.repository.MainRepository;
import sample.scenes.AddDependentScene;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssignProjectController implements AssignItemController.AssignMoreCallback {

    public Button submitButton;
    public VBox vbox;
    public AnchorPane root;
    Stage prevStage;
    List<String> projectNumList = new ArrayList<>();
    List<AssignItemController> controllers = new ArrayList<>();
    private Employee e;

    public void initialize() {

    }

    public void setEmployee(Employee e) {
        this.e = e;
        if (e != null) {
            projectNumList = MainRepository.getInstance().getAllProjectsInDepartment(e.getDno());
            addAssignProjectController(projectNumList);
        }
    }

    private void addAssignProjectController(List<String> list) {
        AnchorPane project = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/item_assign_project.fxml"));
            project = fxmlLoader.load();
            AssignItemController assignItemController = fxmlLoader.getController();
            assignItemController.setAssignMoreCallback(this);
            controllers.add(assignItemController);
            vbox.getChildren().add(project);
            assignItemController.setEmployee(e);
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
        ArrayList<String> newProjectList = (ArrayList<String>) ((ArrayList) projectNumList).clone();
        newProjectList.remove(assignItemController.projectNumberCBox.getValue());
        addAssignProjectController(newProjectList);
    }

    public void onAssignSubmitClicked() {
        int totalHours = 0;
        List<WorksOn> worksOnList = new ArrayList<>();
        for (AssignItemController c : controllers) {
            totalHours += Double.parseDouble(c.hoursTextField.getText());
            worksOnList.add(new WorksOn(c.employeeSSN.getText(), c.projectNumberCBox.getValue(), c.hoursTextField.getText()));
        }
        if (totalHours > 40) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Total hours must not exceed 40 hours.");

            alert.showAndWait();
        } else {
            for (WorksOn w : worksOnList) {
                try {
                    MainRepository.getInstance().assignProject(w);
                    startAddDependentController();
                } catch (SQLException | ClassNotFoundException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Operation Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to assign employee to " + w.getProjectNumber() + "\n\n");
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
            new AddDependentScene(e).start(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
