package sample;

public class Department {
    private String dname;
    private String dnumber;
    private String mgrssn;
    private String mgrstartdate;

    public Department(String dname, String dnumber, String mgrssn, String mgrstartdate) {
        this.dname = dname;
        this.dnumber = dnumber;
        this.mgrssn = mgrssn;
        this.mgrstartdate = mgrstartdate;
    }

    public String getDname() {
        return dname;
    }

    public String getDnumber() {
        return dnumber;
    }

    public String getMgrssn() {
        return mgrssn;
    }

    public String getMgrstartdate() {
        return mgrstartdate;
    }
}
