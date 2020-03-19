package sample.model;

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
        stringBuilder.append("Report\n\n\n");
        stringBuilder.append(employee);
        if (!employeeProject.isEmpty()) {
            stringBuilder.append("\n\n");
            for (Project project : employeeProject) {
                stringBuilder.append(project);
            }
        }
        if (dependent != null) {
            stringBuilder.append("\n\n");
            stringBuilder.append(dependent);
        }
        stringBuilder.append("\n added to the database.");
        return stringBuilder.toString();
    }
}
