package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.model.Department;
import sample.model.Employee;
import sample.repository.MainRepository;
import sample.scenes.AssignProject;
import sample.utils.ChoiceBoxUtils;
import sample.utils.TextFieldUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sample.utils.DateUtils.getFormattedDOB;

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
    public AnchorPane root;
    Stage prevStage;
    private List<Department> departments = new ArrayList<>();
    private List<Employee> supervisors = new ArrayList<>();

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
            String pattern = "dd-MM-yy";
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
        for (Employee e : supervisors) {
            supervisorList.add(e.getFname());
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
        // Validate all fields
        if (validateInput()) {
            // validate there are values for depart num and supervisor ssn.
            String departmentNum = getDepartNum();
            String supervisorSSN = getSupervisorSSN();
            String birthDate = getFormattedDOB(bdateField.getEditor().getText());
            if (departmentNum != null && supervisorSSN != null) {
                try {
                    MainRepository.getInstance().addEmployee(firstNameField.getText(),
                            middleNameField.getText(), lastNameField.getText(),
                            ssnField.getText(), birthDate, addressField.getText(),
                            sexChoiceBox.getValue(), salaryField.getText(), supervisorSSN,
                            departmentNum, emailAddressField.getText());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Submission successful!");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee with" + firstNameField.getText() + " " + lastNameField.getText() + " is added to the datebase");

                    Optional<ButtonType> buttonResult = alert.showAndWait();
                    if (buttonResult.get() == ButtonType.OK) {
                        // ... start new scene
                        Employee employee = new Employee(firstNameField.getText(),
                                middleNameField.getText(), lastNameField.getText(),
                                ssnField.getText(), birthDate, addressField.getText(),
                                sexChoiceBox.getValue(), salaryField.getText(), supervisorSSN,
                                departmentNum);
                        Stage stage = (Stage) root.getScene().getWindow();
                        try {
                            new AssignProject(employee).start(stage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid inputs. Employee cannot be added.");

                    alert.showAndWait();
                }
            }
        }
    }


    private String getSupervisorSSN() {
        // get correct supervisor ssn num;
        for (Employee e : supervisors) {
            if (supervisorNameChoiceBox.getValue().contains(e.getFname()) &&
                    supervisorNameChoiceBox.getValue().contains(e.getMinit()) &&
                    supervisorNameChoiceBox.getValue().contains(e.getLname())) {
                return e.getSsn();
            }
        }
        return null;
    }

    private String getDepartNum() {
        // Get correct  depart num
        for (Department d : departments) {
            if (d.getDname().equals(departNumberField.getValue())) {
                return d.getDnumber();
            }
        }
        return null;
    }

    private boolean validateInput() {
        return TextFieldUtils.isValidInput(ssnField) &&
                TextFieldUtils.isValidInput(firstNameField) &&
                TextFieldUtils.isValidInput(lastNameField) &&
                TextFieldUtils.isValidInput(salaryField) &&
                TextFieldUtils.isValidInput(emailAddressField) &&
                ChoiceBoxUtils.isValidInput(departNumberField) &&
                ChoiceBoxUtils.isValidInput(supervisorNameChoiceBox);
    }
}
