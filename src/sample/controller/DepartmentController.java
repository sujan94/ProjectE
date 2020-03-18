package sample.controller;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import sample.model.Department;
import sample.repository.MainRepository;

import java.util.ArrayList;
import java.util.List;

public class DepartmentController {
    public Button addDepartment;
    public TableView departmentTable;
    public GridPane root;

    private List<Department> departmentList = new ArrayList<>();
    private ObservableList<Department> departmentObservableList = FXCollections.observableList(departmentList);

    public void initialize() {
        initializeDepartmentTable();
        requestDepartments();
    }

    private void requestDepartments() {
        Observable.fromArray(MainRepository.getInstance().getAllDepartment())
                .subscribeOn(Schedulers.io())
                .subscribe(departmentList -> {
                    departmentObservableList.clear();
                    departmentObservableList.addAll(departmentList);
                });
    }

    private void initializeDepartmentTable() {
        TableColumn<Department, String> departNameCol = new TableColumn<>("Dept. Name");
        departNameCol.setMinWidth(60);
        departNameCol.setCellValueFactory(
                new PropertyValueFactory<Department, String>("dname"));

        TableColumn<Department, String> departNumCol = new TableColumn<>("Dept. Number");
        departNumCol.setMinWidth(40);
        departNumCol.setCellValueFactory(
                new PropertyValueFactory<Department, String>("dnumber"));

        TableColumn<Department, String> mgrSSNCol = new TableColumn<>("Manager's SSN");
        mgrSSNCol.setMinWidth(60);
        mgrSSNCol.setCellValueFactory(
                new PropertyValueFactory<Department, String>("mgrssn"));

        TableColumn<Department, String> mgrStartDate = new TableColumn<>("Manager's Start Date");
        mgrStartDate.setMinWidth(60);
        mgrStartDate.setCellValueFactory(
                new PropertyValueFactory<Department, String>("mgrstartdate"));

        departmentTable.setItems(departmentObservableList);
        departmentTable.getColumns().addAll(departNameCol, departNumCol, mgrSSNCol, mgrStartDate);
    }

}
