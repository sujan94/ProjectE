package sample.model;

import sample.enums.ScanResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScanReport {

    private ScanResult scanResult;
    private List<Employee> employeesWorkingOnMoreProject = new ArrayList<>();
    private List<Employee> employeeWithNoProjectInOwnDept = new ArrayList<>();
    private String scanTimeStamp;

    public ScanReport() {
        scanTimeStamp = new SimpleDateFormat("yyyy-MM-dd:HH:mm").format(new Date());
    }

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public List<Employee> getEmployeesWorkingOnMoreProject() {
        return employeesWorkingOnMoreProject;
    }

    public void setEmployeesWorkingOnMoreProject(List<Employee> employeesWorkingOnMoreProject) {
        this.employeesWorkingOnMoreProject = employeesWorkingOnMoreProject;
    }

    public List<Employee> getEmployeeWithNoProjectInOwnDept() {
        return employeeWithNoProjectInOwnDept;
    }

    public void setEmployeeWithNoProjectInOwnDept(List<Employee> employeeWithNoProjectInOwnDept) {
        this.employeeWithNoProjectInOwnDept = employeeWithNoProjectInOwnDept;
    }

    public String getScanTimeStamp() {
        return scanTimeStamp;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Scan Completed:\n" + "Time: ").append(getScanTimeStamp()).append("\nScan Result: ").append(getScanResult()).append("\n");
        if (!employeesWorkingOnMoreProject.isEmpty()) {
            result.append(result + "Contraints violated: Too many project assigned." + "\n");
            for (Employee e : employeesWorkingOnMoreProject) {
                result.append(e.toString()).append("\n");
            }
        }

        if (!employeeWithNoProjectInOwnDept.isEmpty()) {
            result.append(result + "Constraints violated: No project assigned from his/her department.\n");
            for (Employee e : employeeWithNoProjectInOwnDept) {
                result.append(e.toString()).append("\n");
            }
        }
        result.append("\n\n");
        return result.toString();
    }
}
