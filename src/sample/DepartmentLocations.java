package sample;

public class DepartmentLocations {

    private String dnumber;
    private String dlocation;

    public DepartmentLocations(String dnumber, String dlocation) {
        this.dnumber = dnumber;
        this.dlocation = dlocation;
    }

    public String getDnumber() {
        return dnumber;
    }

    public String getDlocation() {
        return dlocation;
    }
}
