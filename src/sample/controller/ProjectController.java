package sample.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import sample.model.Project;
import sample.repository.MainRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class ProjectController {
    public GridPane root;
    public TableView projectTable;
    public Button addProject;
    public Button deleteProject;
    private Project selectedProject;

    private List<Project> projectList = new ArrayList<>();
    private ObservableList<Project> projectObservableList = FXCollections.observableList(projectList);

    public void initialize() {
        deleteProject.setVisible(false);
        initializeProjectTable();
        requestProjects();
    }

    private void requestProjects() {
        Observable.fromArray(MainRepository.getInstance().getAllProjects())
                .subscribeOn(Schedulers.io())
                .subscribe(projects -> {
                    projectObservableList.clear();
                    projectObservableList.addAll(projects);
                });
    }

    private void initializeProjectTable() {
        TableColumn<Project, String> projectNameCol = new TableColumn<>("Project Name");
        projectNameCol.setMinWidth(60);
        projectNameCol.setCellValueFactory(
                new PropertyValueFactory<Project, String>("pname"));

        TableColumn<Project, String> projectNumCol = new TableColumn<>("Project Number");
        projectNumCol.setMinWidth(40);
        projectNumCol.setCellValueFactory(
                new PropertyValueFactory<Project, String>("pnumber"));

        TableColumn<Project, String> projectLocationCol = new TableColumn<>("Project Location");
        projectLocationCol.setMinWidth(60);
        projectLocationCol.setCellValueFactory(
                new PropertyValueFactory<Project, String>("plocation"));

        TableColumn<Project, String> deptNumberCol = new TableColumn<>("Dept. Number");
        deptNumberCol.setMinWidth(60);
        deptNumberCol.setCellValueFactory(
                new PropertyValueFactory<Project, String>("dnumber"));

        projectTable.setOnMouseClicked(event -> {
            Node node = ((Node) event.getTarget()).getParent();
            TableRow<Project> row;
            if (node instanceof TableRow) {
                row = (TableRow) node;
                System.out.println(row.getItem().getPname() + " selected.");
                deleteProject.setVisible(true);
                selectedProject = row.getItem();
            } else {
                // clicking on text part
                LOGGER.log(Level.INFO, "Clicked outside row");
                deleteProject.setVisible(false);
            }
        });

        projectTable.setItems(projectObservableList);
        projectTable.getColumns().addAll(projectNameCol, projectNumCol, projectLocationCol, deptNumberCol);
    }

    public void addProjectClicked() {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Add Project");
        infoAlert.setHeaderText(null);
        infoAlert.setContentText("Feature is in development.");

        infoAlert.showAndWait();
    }

    public void deleteProjectClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Action");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete " + selectedProject.getPname() + " from the Database?" + "\n\nClick Cancel to abort and OK to continue.");

        Optional<ButtonType> buttonResult = alert.showAndWait();
        if (buttonResult.get() == ButtonType.OK) {
            try {
                MainRepository.getInstance().deleteProject(selectedProject);
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Delete Action");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText(selectedProject + " is removed from the company database.");

                infoAlert.showAndWait();
                deleteProject.setVisible(false);

                requestProjects();

            } catch (SQLException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Delete Action");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Error occured while deleting project.\n\n The project most likely " +
                        "is assigned to one or more employees.\n\nPlease remove employee from project and remove the project.");

                errorAlert.showAndWait();
            }
        } else if (buttonResult.get() == ButtonType.CANCEL || buttonResult.get() == ButtonType.NO) {
            // do nothing
        }
    }
}
