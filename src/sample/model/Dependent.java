package sample.model;

public class Dependent {

    private String employeeSSN;
    private String dependentName;
    private String sex;
    private String bDate;
    private String relationship;

    public Dependent(String employeeSSN, String dependentName, String sex, String bDate, String relationship) {
        this.employeeSSN = employeeSSN;
        this.dependentName = dependentName;
        this.sex = sex;
        this.bDate = bDate;
        this.relationship = relationship;
    }

    public String getEmployeeSSN() {
        return employeeSSN;
    }

    public String getDependentName() {
        return dependentName;
    }

    public String getSex() {
        return sex;
    }

    public String getbDate() {
        return bDate;
    }

    public String getRelationship() {
        return relationship;
    }
}
