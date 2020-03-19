package sample.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.model.Dependent;
import sample.model.Employee;
import sample.model.Report;
import sample.repository.MainRepository;
import sample.utils.ChoiceBoxUtils;
import sample.utils.DateUtils;
import sample.utils.TextFieldUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AddDependentController {
    public ChoiceBox<String> employeeNameCBox;
    public TextField dependentName;
    public ChoiceBox<String> dependentSex;
    public TextField relationship;
    public Button submitButton;
    public AnchorPane root;
    public Button cancelButton;
    public DatePicker dependentDOB;
    public TextField employeeSSN;
    private Stage prevStage;
    private Report report;

    public void initialize() {
        dependentSex.setItems(FXCollections.observableArrayList("M", "F"));
        dependentDOB.setConverter(new StringConverter<LocalDate>() {
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

    public void setReport(Report report) {
        this.report = report;
        if (report != null && report.getEmployee() != null) {
            Employee e = report.getEmployee();
            employeeSSN.setText(e.getSsn());
            employeeNameCBox.setDisable(true);
            employeeNameCBox.setValue(e.getFname());
            employeeNameCBox.setItems(FXCollections.observableArrayList(e.getFname()));
        }
    }

    public void setPrevStage(Stage primaryStage) {
        this.prevStage = primaryStage;
    }

    public void onSubmitButtonClicked() {
        if (validateFields()) {
            if (!TextFieldUtils.isValidInput(dependentDOB.getEditor())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incomplete Form");
                alert.setHeaderText(null);
                alert.setContentText("Please enter dependent's date of birth");
                alert.showAndWait();
            } else {
                String formattedDateString = DateUtils.getFormattedDOB(dependentDOB.getEditor().getText());
                try {
                    Dependent dependent = new Dependent(report.getEmployee().getSsn(), dependentName.getText(),
                            dependentSex.getValue(), formattedDateString, relationship.getText());
                    MainRepository.getInstance().addDependent(dependent);
                    report.setDependent(dependent);
                    onCancelButtonClicked();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Occured!");
                    alert.setHeaderText(null);
                    alert.setContentText("Error occured while adding dependent.");
                    alert.showAndWait();
                }

            }
        }
    }

    private boolean validateFields() {
        return ChoiceBoxUtils.isValidInput(employeeNameCBox) && TextFieldUtils.isValidInput(dependentName)
                && ChoiceBoxUtils.isValidInput(dependentSex) && TextFieldUtils.isValidInput(relationship);
    }

    public void onCancelButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Submission successful!");
        alert.setHeaderText(null);
        alert.setContentText(report.toString());
        Optional<ButtonType> buttonResult = alert.showAndWait();
        if (buttonResult.isPresent() && buttonResult.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/sample.fxml"));
            Stage stage = new Stage();
            stage.initOwner(root.getScene().getWindow());
            try {
                stage.setScene(new Scene(loader.load()));
                prevStage.close();
                // showAndWait will block execution until the window closes...
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
