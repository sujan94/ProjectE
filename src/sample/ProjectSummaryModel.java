package sample;

public class ProjectSummaryModel {
    private String projectName;
    private String projectNumber;
    private String employeesInProject;
    private String totalHoursInProject;

    public ProjectSummaryModel(String projectName, String projectNumber, String employeesInProject, String totalHoursInProject) {
        this.projectName = projectName;
        this.projectNumber = projectNumber;
        this.employeesInProject = employeesInProject;
        this.totalHoursInProject = totalHoursInProject;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public String getEmployeesInProject() {
        return employeesInProject;
    }

    public String getTotalHoursInProject() {
        return totalHoursInProject;
    }
}
