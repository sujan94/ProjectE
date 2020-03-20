package sample.model;

import sample.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private Employee employee;
    private List<Project> employeeProject = new ArrayList<>();
    private Dependent dependent;

    public Report(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public List<Project> getEmployeeProject() {
        return employeeProject;
    }

    public void setEmployeeProject(List<Project> employeeProject) {
        this.employeeProject = employeeProject;
    }

    public Dependent getDependent() {
        return dependent;
    }

    public void setDependent(Dependent dependent) {
        this.dependent = dependent;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Report\n\n");
        stringBuilder.append(employee);
        stringBuilder.append("\n");
        if (!employeeProject.isEmpty()) {
            for (Project project : employeeProject) {
                stringBuilder.append(project);
            }
        } else {
            stringBuilder.append("Project: Not Assigned.");
        }
        stringBuilder.append("\n");
        if (dependent != null) {
            stringBuilder.append(dependent);
        } else {
            stringBuilder.append("Dependent: Not Added.");
        }
        stringBuilder.append("\nAdded to the database on ").append(DateUtils.getCurrentDate());
        return stringBuilder.toString();
    }
}
