package sample.model;

public class Project {
    private String pname;
    private String pnumber;
    private String plocation;
    private String dnumber;

    public Project(String pname, String pnumber, String plocation, String dnumber) {
        this.pname = pname;
        this.pnumber = pnumber;
        this.plocation = plocation;
        this.dnumber = dnumber;
    }

    public String getPname() {
        return pname;
    }

    public String getPnumber() {
        return pnumber;
    }

    public String getPlocation() {
        return plocation;
    }

    public String getDnumber() {
        return dnumber;
    }
}
