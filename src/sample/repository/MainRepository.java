package sample.repository;

import sample.model.*;
import sample.utils.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import static sample.enums.EmployeeSummaryState.*;

public class MainRepository {

    private static MainRepository instance;

    private MainRepository() {
        DatabaseUtils.startDriverCheck();
    }

    public static MainRepository getInstance() {
        if (instance == null) {
            instance = new MainRepository();
        }
        return instance;
    }

    public List<Department> getAllDepartment() {
        List<Department> departments = new ArrayList<>();
        try {
            String allDepartmentQuery = "SELECT * from DEPARTMENT";
            ResultSet r = DatabaseUtils.dbExecuteQuery(allDepartmentQuery);
            while (r.next()) {
                departments.add(new Department(r.getString(1), r.getString(2), r.getString(3), r.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, e.toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return departments;
    }

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        try {
            String allProjects = "SELECT * from PROJECT";
            ResultSet r = DatabaseUtils.dbExecuteQuery(allProjects);
            while (r.next()) {
                projects.add(new Project(r.getString(1), r.getString(2), r.getString(3), r.getString(4)));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        } catch (NullPointerException n) {
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public List<String> getAllProjectsInDepartment(String departmentNumber) {
        List<String> projects = new ArrayList<>();
        try {
            String allProjects = "SELECT Pnumber from PROJECT where dnum='" + departmentNumber + "'";
            ResultSet r = DatabaseUtils.dbExecuteQuery(allProjects);
            while (r.next()) {
                projects.add(r.getString(1));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        } catch (NullPointerException n) {
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public List<String> getAllLocations() {
        List<String> locations = new ArrayList<>();
        try {
            String allLocations = "SELECT Dlocation from DEPT_LOCATIONS";
            ResultSet r = DatabaseUtils.dbExecuteQuery(allLocations);
            while (r.next()) {
                locations.add(r.getString(1));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        } catch (NullPointerException n) {
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return locations;
    }

    public List<Employee> getAllEmployees(String query) {
        List<Employee> employees = new ArrayList<>();
        try {
            ResultSet r = DatabaseUtils.dbExecuteQuery(query);
            while (r.next()) {
                employees.add(new Employee(r.getString(1),
                        r.getString(2),
                        r.getString(3),
                        r.getString(4),
                        r.getString(5),
                        r.getString(6),
                        r.getString(7),
                        r.getString(8),
                        r.getString(9),
                        r.getString(10)));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        } catch (NullPointerException n) {
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public List<Employee> getAllSupervisors() {
        String query = "select DISTINCT e.fname, e.minit, e.lname, e.ssn, e.bdate, e.address, e.sex, e.salary, e.superssn, e.dno\n" +
                "from  Employee emp, Employee e\n" +
                "where emp.superssn=e.ssn";
        List<Employee> employees = new ArrayList<>();
        try {
            ResultSet r = DatabaseUtils.dbExecuteQuery(query);
            while (r.next()) {
                employees.add(new Employee(r.getString(1),
                        r.getString(2),
                        r.getString(3),
                        r.getString(4),
                        r.getString(5),
                        r.getString(6),
                        r.getString(7),
                        r.getString(8),
                        r.getString(9),
                        r.getString(10)));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        } catch (NullPointerException n) {
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public synchronized String getSummary(int employeeSummaryState) {
        String query = "";
        if (employeeSummaryState == TOTAL_EMPLOYEE.getAction()) {
            query = "select count(fname) from Employee";
        } else if (employeeSummaryState == HIGH_SALARY.getAction()) {
            query = "select max(salary) from Employee";
        } else if (employeeSummaryState == LOW_SALARY.getAction()) {
            query = "select min(salary) from Employee";
        } else {
            return null;
        }
        try {
            ResultSet r = DatabaseUtils.dbExecuteQuery(query);
            while (r.next()) {
                return r.getString(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        } catch (NullPointerException n) {
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized List<ProjectSummaryModel> getProjectSummary() {
        List<ProjectSummaryModel> projectSummaryModels = new ArrayList<>();
        String query = "select Pname, Pno, Count(Pno), sum(hours)\n" +
                "from Works_on, Project\n" +
                "where Pnumber = Pno\n" +
                "group by Pno, Pname";

        try {
            ResultSet r = DatabaseUtils.dbExecuteQuery(query);
            while (r.next()) {
                projectSummaryModels.add(new ProjectSummaryModel(r.getString(1),
                        r.getString(2),
                        r.getString(3),
                        r.getString(4)));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        } catch (NullPointerException n) {
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return projectSummaryModels;
    }

    public boolean isManagerSSN(String ssn) {
        String query = "select ssn \n" +
                "from  Employee\n" +
                "where superssn = '" + ssn + "'";

        try {
            ResultSet r = DatabaseUtils.dbExecuteQuery(query);
            return r.next();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        } catch (NullPointerException n) {
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addEmployee(String fname, String minit, String lname, String ssn, String dob, String address,
                            String sex, String salary, String superSSN, String dno, String email) throws SQLException {
        //Declare a insert statement
        String updateStmt =
                "BEGIN\n" +
                        "INSERT INTO employee\n" +
                        "(fname, minit, lname, ssn, bdate, address, sex, salary, superssn, dno, email)\n" +
                        "VALUES\n" +
                        "('" + fname + "', '" + minit + "','" + lname + "','" + ssn + "', '" + dob + "','" + address + "'," +
                        "'" + sex + "', '" + salary + "','" + superSSN + "','" + dno + "', '" + email + "');\n" +
                        "END;";

        //Execute insert operation
        try {
            DatabaseUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    public void assignProject(WorksOn w) throws SQLException {
        //Declare a insert statement
        String updateStmt =
                "BEGIN\n" +
                        "INSERT INTO works_on\n" +
                        "(essn, pno, hours)\n" +
                        "VALUES\n" +
                        "('" + w.getEmployeeSSN() + "', '" + w.getProjectNumber() + "','" + w.getHours() + "');\n" +
                        "END;";

        //Execute insert operation
        try {
            DatabaseUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    public void addDependent(Dependent d) throws SQLException {
        //Declare a insert statement
        String updateStmt =
                "BEGIN\n" +
                        "INSERT INTO dependent\n" +
                        "(essn, dependent_name, sex , bdate, relationship)\n" +
                        "VALUES\n" +
                        "('" + d.getEmployeeSSN() + "', '" + d.getDependentName() + "','" + d.getSex()
                        + "','" + d.getbDate() + "','" + d.getRelationship() + "');\n" +
                        "END;";

        //Execute insert operation
        try {
            DatabaseUtils.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    public void deleteEmployee(Employee employee) throws SQLException {
        //Declare a insert statement
        String deleteSmt = "DELETE FROM EMPLOYEE where ssn='" + employee.getSsn() + "'";

        //Execute insert operation
        try {
            DatabaseUtils.dbExecuteUpdate(deleteSmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    public void deleteProject(Project project) throws SQLException {
        //Declare a insert statement
        String deleteSmt = "DELETE FROM Project where Pnumber='" + project.getPnumber() + "'";

        //Execute insert operation
        try {
            DatabaseUtils.dbExecuteUpdate(deleteSmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

}
