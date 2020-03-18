package sample.model;

public class WorksOn {
    private String employeeSSN;
    private String projectNumber;
    private String hours;

    public WorksOn(String employeeSSN, String projectNumber, String hours) {
        this.employeeSSN = employeeSSN;
        this.projectNumber = projectNumber;
        this.hours = hours;
    }

    public String getEmployeeSSN() {
        return employeeSSN;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public String getHours() {
        return hours;
    }
}
