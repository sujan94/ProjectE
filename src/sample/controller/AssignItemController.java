package sample.controller;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sample.model.Employee;
import sample.utils.ChoiceBoxUtils;
import sample.utils.TextFieldUtils;

import java.util.List;

public class AssignItemController {

    public ChoiceBox<String> employeeNameCBox;
    public TextField employeeSSN;
    public ChoiceBox<String> projectNumberCBox;
    public TextField hoursTextField;
    public Button assignMoreProjectButton;
    private AssignMoreCallback assignMoreCallback;

    public void initialize() {
        TextFieldUtils.setInputNumberConstraint(hoursTextField);
    }

    public void setEmployee(Employee e) {
        if (e != null) {
            String name = e.getFullName();
            employeeNameCBox.setValue(name);
            employeeNameCBox.setItems(FXCollections.observableArrayList(name));
            employeeNameCBox.setDisable(true);
            employeeSSN.setText(e.getSsn());
        }
    }

    public void setProjectNumList(List<String> list) {
        projectNumberCBox.setItems(FXCollections.observableArrayList(list));
    }

    public void onAssignMoreClicked() {
        if (validateFields()) {
            assignMoreProjectButton.setVisible(false);
            assignMoreCallback.onAssignMoreButtonClicked(this);
        }
    }

    public boolean validateFields() {
        return TextFieldUtils.isValidInput(hoursTextField)
                && ChoiceBoxUtils.isValidInput(projectNumberCBox);
    }

    public void setAssignMoreCallback(AssignMoreCallback assignMoreCallback) {
        this.assignMoreCallback = assignMoreCallback;
    }

    public interface AssignMoreCallback {
        void onAssignMoreButtonClicked(AssignItemController assignItemController);
    }
}
