package sample.enums;

public enum EmployeeSummaryState {
    TOTAL_EMPLOYEE(0),
    HIGH_SALARY(1),
    LOW_SALARY(2);

    // declaring private variable for getting values
    private int action;

    // enum constructor - cannot be public or protected
    EmployeeSummaryState(int action) {
        this.action = action;
    }

    // getter method
    public int getAction() {
        return this.action;
    }
}
