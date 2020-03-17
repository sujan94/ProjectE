package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.model.Department;
import sample.model.Employee;
import sample.repository.MainRepository;
import sample.utils.TextFieldUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NewEmployeeController {
    public Label firstNameLabel;
    public TextField firstNameField;
    public TextField middleNameField;
    public TextField lastNameField;
    public TextField ssnField;
    public DatePicker bdateField;
    public TextField addressField;
    public ChoiceBox<String> sexChoiceBox;
    public TextField salaryField;
    public ChoiceBox<String> supervisorNameChoiceBox;
    public ChoiceBox<String> departNumberField;
    public TextField emailAddressField;
    public Button goBackButton;
    public Button submitButton;

    private List<Department> departments = new ArrayList<>();
    private List<Employee> supervisors = new ArrayList<>();

    Stage prevStage;

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }

    public void initialize() {
        initializeSexChoiceBox();
        initializeSupervisorNameChoiceBox();
        initializeDepartmentNumberChoiceBox();
        addTextFieldConstraint();
        addDateFormatter();
    }

    private void addDateFormatter() {
        bdateField.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    private void addTextFieldConstraint() {
        TextFieldUtils.setSSNConstraint(ssnField);
        TextFieldUtils.setInputNumberConstraint(salaryField);
    }

    private void initializeDepartmentNumberChoiceBox() {
        departments = MainRepository.getInstance().getAllDepartment();
        ObservableList<String> departmentList = FXCollections.observableArrayList();
        for (Department d : departments) {
            departmentList.add(d.getDname());
        }
        departNumberField.setItems(departmentList);
    }

    private void initializeSupervisorNameChoiceBox() {
        supervisors = MainRepository.getInstance().getAllSupervisors();
        ObservableList<String> supervisorList = FXCollections.observableArrayList();
        supervisorList.add("None");
        supervisorNameChoiceBox.setValue("None");
        for (Employee d : supervisors) {
            supervisorList.add(d.getFname() + " " + d.getMinit() + " " + d.getLname());
        }
        supervisorNameChoiceBox.setItems(supervisorList);

    }

    private void initializeSexChoiceBox() {
        List<String> list = new ArrayList<>();
        list.add("M");
        list.add("F");
        sexChoiceBox.setItems(FXCollections.observableArrayList(list));
    }

    public void onBackButtonClicked() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/sample.fxml"));
        Stage stage = new Stage();
        stage.initOwner(goBackButton.getScene().getWindow());
        try {
            stage.setScene(new Scene((Parent) loader.load()));
            prevStage.close();
            // showAndWait will block execution until the window closes...
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onSubmitButtonClicked() {

    }
}
