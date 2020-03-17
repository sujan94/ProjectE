package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.model.Department;
import sample.model.Employee;
import sample.repository.MainRepository;

import java.util.ArrayList;
import java.util.List;

public class NewEmployeeController {
    public Label firstNameLabel;
    public TextField firstNameField;
    public TextField middleNameField;
    public TextField lastNameField;
    public TextField ssnField;
    public TextField bdateField;
    public TextField addressField;
    public ChoiceBox<String> sexChoiceBox;
    public TextField salaryField;
    public ChoiceBox<String> supervisorNameChoiceBox;
    public ChoiceBox<String> departNumberField;
    public TextField emailAddressField;

    private List<Department> departments = new ArrayList<>();
    private List<Employee> supervisors = new ArrayList<>();

    public void initialize(){
        initializeSexChoiceBox();
        initializeSupervisorNameChoiceBox();
        initializeDepartmentNumberChoiceBox();
    }

    private void initializeDepartmentNumberChoiceBox() {
        departments =  MainRepository.getInstance().getAllDepartment();
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
            supervisorList.add(d.getFname()+" "+d.getMinit()+" "+d.getLname());
        }
        supervisorNameChoiceBox.setItems(supervisorList);

    }

    private void initializeSexChoiceBox() {
        List<String > list = new ArrayList<>();
        list.add("M");
        list.add("F");
        sexChoiceBox.setItems(FXCollections.observableArrayList(list));
    }

    public void onBackButtonClicked(){

    }

    public void onSubmitButtonClicked(){
        
    }
}
