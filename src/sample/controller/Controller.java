package sample.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.model.Department;
import sample.model.Employee;
import sample.model.Project;
import sample.repository.MainRepository;
import sample.scenes.NewEmployee;
import sample.utils.DatabaseUtils;
import sample.utils.TextFieldUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class Controller extends BaseController {

    private static final String DEFAULT_CHOICE_BOX_STATE = "Any";

    @FXML
    public TableView employeeTable;

    @FXML
    public Button addNewEmployee;

    @FXML
    public AnchorPane rightBodyAnchor;

    @FXML
    public AnchorPane summaryChildBody;

    @FXML
    public GridPane root;

    public Label title;
    public SplitPane splitPane;
    public Button deleteEmployeeButton;


    @FXML
    private ChoiceBox<String> projectChoiceBox;

    @FXML
    private ChoiceBox<String> locationChoiceBox;

    @FXML
    private Button searchButton;

    @FXML
    private ChoiceBox<String> departmentChoiceBox;

    @FXML
    private ListView<String> list;

    private ObservableList<String> listItem = FXCollections.observableArrayList();
    private ObservableList<String> departmentList = FXCollections.observableArrayList();
    private ObservableList<String> projectList = FXCollections.observableArrayList();
    private ObservableList<String> locationList = FXCollections.observableArrayList();
    private ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();

    //TODO fix this.
    private Parent department;
    private Parent project;
    private Parent summary;
    private Parent graph;
    private Employee selectedEmployee;

    public void initialize() {
        splitPane.setVisible(false);
        deleteEmployeeButton.setVisible(false);
        addLoading();
        initializeLeftNav();
        initializeDeptChoiceBox();
        initializeProjectChoiceBox();
        initalizeLocationChoiceBox();
        initializeEmployeeTable();
    }

    private void addLoading() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/loading_scene.fxml"));
            Parent loading = fxmlLoader.load();
            LoadingController loadingController = fxmlLoader.getController();
            root.getChildren().add(loading);
            GridPane.setConstraints(loading, 0, 1);
            Observable.interval(0, 2, TimeUnit.SECONDS)
                    .take(5)
                    .subscribe(aLong -> {
                        float progressValue = (float) (0.35 * aLong);
                        Platform.runLater(() -> {
                            loadingController.setProgress(progressValue);
                            if (progressValue > 1.0f) {
                                if (DatabaseUtils.isIsDriverAvailable()) {
                                    root.getChildren().remove(loading);
                                    splitPane.setVisible(true);
                                }

                            }
                        });
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeEmployeeTable() {
        TableColumn<Employee, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setMinWidth(60);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("fname"));

        TableColumn<Employee, String> mNameCol = new TableColumn<>("Middle Name");
        mNameCol.setMinWidth(40);
        mNameCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("minit"));

        TableColumn<Employee, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setMinWidth(60);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("lname"));

        TableColumn<Employee, String> ssnCol = new TableColumn<>("SSN");
        ssnCol.setMinWidth(60);
        ssnCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("ssn"));

        TableColumn<Employee, String> bdateCol = new TableColumn<>("Date of Birth");
        bdateCol.setPrefWidth(60);
        bdateCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("bdate"));

        TableColumn<Employee, String> addressCol = new TableColumn<>("Address");
        addressCol.setMinWidth(60);
        addressCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("address"));

        TableColumn<Employee, String> sexCol = new TableColumn<>("Sex");
        sexCol.setPrefWidth(30);
        sexCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("sex"));

        TableColumn<Employee, String> salaryCol = new TableColumn<>("Salary");
        salaryCol.setMinWidth(60);
        salaryCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("salary"));

        TableColumn<Employee, String> superssnCol = new TableColumn<>("Supervisor's SSN");
        superssnCol.setMinWidth(60);
        superssnCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("supervisorssn"));

        TableColumn<Employee, String> dnoCol = new TableColumn<>("Department Number");
        dnoCol.setMinWidth(30);
        dnoCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("dno"));

        employeeTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow<Employee> row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                    System.out.println(row.getItem());
                    deleteEmployeeButton.setVisible(true);
                    selectedEmployee = row.getItem();
                } else {
                    // clicking on text part
                    LOGGER.log(Level.INFO, "Clicked outside row");
                    deleteEmployeeButton.setVisible(false);
                }

            }
        });

        employeeTable.setItems(employeeObservableList);
        employeeTable.getColumns().addAll(firstNameCol, mNameCol, lastNameCol, ssnCol, bdateCol, addressCol, sexCol, salaryCol, superssnCol, dnoCol);
        // Load the default employee table without filter
        onSearchButtonClicked();
    }

    private void initalizeLocationChoiceBox() {
        locationList.add(DEFAULT_CHOICE_BOX_STATE);
        Observable.fromArray(MainRepository.getInstance().getAllLocations())
                .subscribeOn(Schedulers.io())
                .subscribe(locations -> {
                    locationList.addAll(locations);
                });

        locationChoiceBox.setItems(locationList);
        locationChoiceBox.setValue(DEFAULT_CHOICE_BOX_STATE);
    }

    private void initializeProjectChoiceBox() {
        projectList.add(DEFAULT_CHOICE_BOX_STATE);
        Observable.fromArray(MainRepository.getInstance().getAllProjects())
                .subscribeOn(Schedulers.io())
                .subscribe(projects -> {
                    for (Project p : projects) {
                        projectList.add(p.getPname());
                    }
                });
        projectChoiceBox.setItems(projectList);
        projectChoiceBox.setValue(DEFAULT_CHOICE_BOX_STATE);
    }

    private void initializeDeptChoiceBox() {
        departmentList.add(DEFAULT_CHOICE_BOX_STATE);
        Observable.fromArray(MainRepository.getInstance().getAllDepartment())
                .subscribeOn(Schedulers.io())
                .subscribe(departments -> {
                    for (Department d : departments) {
                        departmentList.add(d.getDname());
                    }
                });
        departmentChoiceBox.setItems(departmentList);
        departmentChoiceBox.setValue(DEFAULT_CHOICE_BOX_STATE);
    }

    private void initializeLeftNav() {
        listItem.add("Summary");
        listItem.add("Graph");
        listItem.add("Employee");
        listItem.add("Department");
        listItem.add("Project");
        list.setItems(listItem);
    }

    public void onSearchButtonClicked() {
        employeeObservableList.clear();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT Fname, Minit, Lname, Ssn, Bdate, Address, Sex, Salary, Superssn, Dno ");
        String departmentValue = departmentChoiceBox.getValue();
        String projectValue = projectChoiceBox.getValue();
        String locationValue = locationChoiceBox.getValue();
        // if all choice values are any
        if (departmentValue.equals(DEFAULT_CHOICE_BOX_STATE) && projectValue.equals(DEFAULT_CHOICE_BOX_STATE) && locationValue.equals(DEFAULT_CHOICE_BOX_STATE)) {
            stringBuilder.append("from EMPLOYEE");
        }
        // if all choice values are not any
        else if (!departmentValue.equals(DEFAULT_CHOICE_BOX_STATE) && !projectValue.equals(DEFAULT_CHOICE_BOX_STATE) && !locationValue.equals(DEFAULT_CHOICE_BOX_STATE)) {
            stringBuilder.append("from EMPLOYEE, DEPARTMENT, DEPT_LOCATIONS, PROJECT ");
            stringBuilder.append("where Dno = Dnumber and Dnumber = Dnum and Dname ='").append(departmentValue).append("' and Pname='").append(projectValue).append("' and Dlocation='").append(locationValue).append("'");
        }
        //if department and project is  any
        else if (departmentValue.equals(DEFAULT_CHOICE_BOX_STATE) && projectValue.equals(DEFAULT_CHOICE_BOX_STATE) && !locationValue.equals(DEFAULT_CHOICE_BOX_STATE)
        ) {
            stringBuilder.append("from EMPLOYEE, DEPT_LOCATIONS ");
            stringBuilder.append("where Dno = Dnumber and Dlocation='").append(locationValue).append("'");
        }
        //if department and location is any
        else if (departmentValue.equals(DEFAULT_CHOICE_BOX_STATE) && !projectValue.equals(DEFAULT_CHOICE_BOX_STATE) && locationValue.equals(DEFAULT_CHOICE_BOX_STATE)) {
            stringBuilder.append("from EMPLOYEE,PROJECT ");
            stringBuilder.append("where Dno = Dnum and Pname='").append(projectValue).append("'");
        }
        // if only department is any
        else if (departmentValue.equals(DEFAULT_CHOICE_BOX_STATE) && !projectValue.equals(DEFAULT_CHOICE_BOX_STATE) && !locationValue.equals(DEFAULT_CHOICE_BOX_STATE)) {
            stringBuilder.append("from EMPLOYEE, DEPT_LOCATIONS, PROJECT ");
            stringBuilder.append("where Dno = Dnumber and Dnumber = Dnum and Pname='").append(projectValue).append("' and Dlocation='").append(locationValue).append("'");
        }
        // if only project is any
        else if (!departmentValue.equals(DEFAULT_CHOICE_BOX_STATE) && projectValue.equals(DEFAULT_CHOICE_BOX_STATE) && !locationValue.equals(DEFAULT_CHOICE_BOX_STATE)) {
            stringBuilder.append("from EMPLOYEE, DEPARTMENT, DEPT_LOCATIONS ");
            stringBuilder.append("where Dno = Dnumber and Dname ='").append(departmentValue).append("' and Dlocation='").append(locationValue).append("'");
        }
        //if only location is any
        else if (!departmentValue.equals(DEFAULT_CHOICE_BOX_STATE) && !projectValue.equals(DEFAULT_CHOICE_BOX_STATE) && locationValue.equals(DEFAULT_CHOICE_BOX_STATE)) {
            stringBuilder.append("from EMPLOYEE, DEPARTMENT, PROJECT ");
            stringBuilder.append("where Dno = Dnumber and Dnumber = Dnum and Dname ='").append(departmentValue).append("' and Pname='").append(projectValue).append("'");
        } else {
            stringBuilder.append("from EMPLOYEE, DEPARTMENT ");
            stringBuilder.append("where Dno = Dnumber and Dname ='").append(departmentValue).append("'");
        }

        Observable.fromArray(MainRepository.getInstance().getAllEmployees(stringBuilder.toString()))
                .subscribeOn(Schedulers.io())
                .subscribe(employeeList -> {
                    employeeObservableList.clear();
                    employeeObservableList.addAll(employeeList);
                });
    }

    public void onItemSelectedOnLeftNav(MouseEvent arg0) {
        System.out.println("clicked on " + list.getSelectionModel().getSelectedItem());
        switch (list.getSelectionModel().getSelectedIndex()) {
            case 0:
                rightBodyAnchor.getChildren().remove(project);
                rightBodyAnchor.getChildren().remove(department);
                rightBodyAnchor.getChildren().remove(summary);
                rightBodyAnchor.getChildren().remove(graph);
                System.out.println(isSummaryRoot + "11111");
                try {
                    summary = FXMLLoader.load(getClass().getResource("../ui/summary.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                summaryChildBody.setVisible(false);
                rightBodyAnchor.getChildren().add(summary);
                break;
            case 1:
                rightBodyAnchor.getChildren().remove(project);
                rightBodyAnchor.getChildren().remove(department);
                rightBodyAnchor.getChildren().remove(summary);
                rightBodyAnchor.getChildren().remove(graph);
                isSummaryRoot = false;
                System.out.println(isSummaryRoot + "#######");
                try {
                    graph = FXMLLoader.load(getClass().getResource("../ui/graph.fxml"));
                    summaryChildBody.setVisible(false);
                    rightBodyAnchor.getChildren().add(graph);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                summaryChildBody.setVisible(true);
                rightBodyAnchor.getChildren().remove(project);
                rightBodyAnchor.getChildren().remove(department);
                rightBodyAnchor.getChildren().remove(summary);
                rightBodyAnchor.getChildren().remove(graph);
                isSummaryRoot = false;
                System.out.println(isSummaryRoot + "#######");

                break;
            case 3:
                try {
                    rightBodyAnchor.getChildren().remove(project);
                    rightBodyAnchor.getChildren().remove(department);
                    rightBodyAnchor.getChildren().remove(summary);
                    rightBodyAnchor.getChildren().remove(graph);
                    isSummaryRoot = false;
                    System.out.println(isSummaryRoot + "#######");
                    department = FXMLLoader.load(getClass().getResource("../ui/department.fxml"));
                    summaryChildBody.setVisible(false);
                    rightBodyAnchor.getChildren().add(department);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    rightBodyAnchor.getChildren().remove(department);
                    rightBodyAnchor.getChildren().remove(project);
                    rightBodyAnchor.getChildren().remove(summary);
                    rightBodyAnchor.getChildren().remove(graph);
                    isSummaryRoot = false;
                    project = FXMLLoader.load(getClass().getResource("../ui/project.fxml"));
                    summaryChildBody.setVisible(false);
                    rightBodyAnchor.getChildren().add(project);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    public void onAddNewEmployeeClicked() {
        TextInputDialog dialog = new TextInputDialog();
        TextFieldUtils.setSSNConstraint(dialog.getEditor());
        dialog.setTitle("Manager Verification - 1");
        dialog.setHeaderText("Are you a manager?\nPlease enter your social security number.");
        Optional<String> result = dialog.showAndWait();
        String entered = "none.";

        if (result.isPresent()) {
            entered = result.get();
        }
        if (MainRepository.getInstance().isManagerSSN(entered)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Verification Successful!");
            alert.setHeaderText(null);
            alert.setContentText("Verified. \n\nClick OK to continue");

            Optional<ButtonType> buttonResult = alert.showAndWait();
            if (buttonResult.get() == ButtonType.OK) {
                // ... start new scene
                Stage stage = (Stage) root.getScene().getWindow();
                try {
                    new NewEmployee().start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Verification Failed!");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Manager's SSN.\n\n You are not authorize to add/delete action.");

            alert.showAndWait();
        }

    }

    public void onDeleteEmployeeClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Action");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete " + selectedEmployee.getFullName() + " from the Database?" + "\n\nClick Cancel to abort and OK to continue.");

        Optional<ButtonType> buttonResult = alert.showAndWait();
        if (buttonResult.get() == ButtonType.OK) {
            try {
                MainRepository.getInstance().deleteEmployee(selectedEmployee);
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Delete Action");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText(selectedEmployee + " is removed from the company database.");

                infoAlert.showAndWait();
                deleteEmployeeButton.setVisible(false);
                onSearchButtonClicked();

            } catch (SQLException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Delete Action");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Error occured while deleting employee.\n\n The employee most likely " +
                        "is assigned a project or has dependent in the database.\n\nPlease remove employee from project and remove his/her dependent.");

                errorAlert.showAndWait();
            }
        } else if (buttonResult.get() == ButtonType.CANCEL || buttonResult.get() == ButtonType.NO) {

        }
    }

}
