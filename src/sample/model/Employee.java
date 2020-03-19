package sample.model;

public class Employee {
    private String fname;
    private String minit;
    private String lname;
    private String ssn;
    private String bdate;
    private String address;
    private String sex;
    private String salary;
    private String supervisorssn;
    private String dno;

    public Employee(String fname, String minit, String lname, String ssn, String bdate, String address, String sex, String salary, String supervisorssn, String dno) {
        this.fname = fname;
        this.minit = minit;
        this.lname = lname;
        this.ssn = ssn;
        this.bdate = bdate;
        this.address = address;
        this.sex = sex;
        this.salary = salary;
        this.supervisorssn = supervisorssn;
        this.dno = dno;
    }

    public String getFname() {
        return fname;
    }

    public String getMinit() {
        return minit;
    }

    public String getLname() {
        return lname;
    }

    public String getSsn() {
        return ssn;
    }

    public String getBdate() {
        return bdate;
    }

    public String getAddress() {
        return address;
    }

    public String getSex() {
        return sex;
    }

    public String getSalary() {
        return salary;
    }

    public String getSupervisorssn() {
        return supervisorssn;
    }

    public String getDno() {
        return dno;
    }

    public String getFullName() {
        return this.fname + " " + this.minit + " " + this.lname;
    }

    @Override
    public String toString() {
        return "Employee:\n\n" +
                "Name = " + getFullName() + '\n' +
                "SSN = " + ssn + '\n' +
                "Date of Birth= " + bdate + '\n' +
                "address= " + address + '\n' +
                "working in department number " + dno + '\n';
    }
}
