package sample.repository;

import sample.model.Employee;
import sample.model.ProjectSummaryModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import static sample.enums.EmployeeSummaryState.*;

public class MainRepository {

    private static MainRepository instance;
    private Connection conn;

    private MainRepository() {
    }

    public static MainRepository getInstance() {
        if (instance == null) {
            instance = new MainRepository();
        }
        return instance;
    }
    public void initiateConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException x) {
            System.out.println("Driver could not be loaded");
        }

        // TODO create connection here...
        String stmt1 = "select Lname, Salary from EMPLOYEE";
        PreparedStatement p = conn.prepareStatement(stmt1);
        p.clearParameters();
        ResultSet r = p.executeQuery();
    }

    public List<String> getAllDepartment() {
        List<String> departments = new ArrayList<>();
        try {
            String allDepartmentQuery = "SELECT Dname from DEPARTMENT";
            PreparedStatement p = conn.prepareStatement(allDepartmentQuery);
            p.clearParameters();
            ResultSet r = p.executeQuery();
            while (r.next()) {
                departments.add(r.getString(1));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        } catch (NullPointerException n){
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        }
        return departments;
    }

    public List<String> getAllProjects() {
        List<String> projects = new ArrayList<>();
        try {
            String allProjects = "SELECT Pname from PROJECT";
            PreparedStatement p = conn.prepareStatement(allProjects);
            p.clearParameters();
            ResultSet r = p.executeQuery();
            while (r.next()) {
                projects.add(r.getString(1));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        }catch (NullPointerException n){
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        }
        return projects;
    }

    public List<String> getAllLocations() {
        List<String> locations = new ArrayList<>();
        try {
            String allLocations = "SELECT Dlocation from DEPT_LOCATIONS";
            PreparedStatement p = conn.prepareStatement(allLocations);
            p.clearParameters();
            ResultSet r = p.executeQuery();
            while (r.next()) {
                locations.add(r.getString(1));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        }catch (NullPointerException n){
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        }
        return locations;
    }

    public List<Employee> getAllEmployees(String query){
        List<Employee> employees = new ArrayList<>();
        try {
            PreparedStatement p = conn.prepareStatement(query);
            p.clearParameters();
            ResultSet r = p.executeQuery();
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
        }catch (NullPointerException n){
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        }
        return employees;
    }

    public String getSummary(int employeeSummaryState){
        String query="";
        if(employeeSummaryState== TOTAL_EMPLOYEE.getAction()){
            query = "select count(fname) from Employee";
        }else if(employeeSummaryState== HIGH_SALARY.getAction()){
            query = "select max(salary) from Employee";
        }
        else if(employeeSummaryState== LOW_SALARY.getAction()){
            query = "select min(salary) from Employee";
        }else{
            return null;
        }
        try {
            PreparedStatement p = conn.prepareStatement(query);
            p.clearParameters();
            ResultSet r = p.executeQuery();
            while (r.next()) {
                return r.getString(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        }catch (NullPointerException n){
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        }
        return null;
    }

    public List<ProjectSummaryModel> getProjectSummary(){
        List<ProjectSummaryModel> projectSummaryModels = new ArrayList<>();
        String query = "select Pname, Pno, Count(Pno), sum(hours)\n" +
                "from Works_on, Project\n" +
                "where Pnumber = Pno\n" +
                "group by Pno, Pname";

        try {
            PreparedStatement p = conn.prepareStatement(query);
            p.clearParameters();
            ResultSet r = p.executeQuery();
            while (r.next()) {
                projectSummaryModels.add(new ProjectSummaryModel(r.getString(1),
                        r.getString(2),
                        r.getString(3),
                        r.getString(4)));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.toString());
        }catch (NullPointerException n){
            LOGGER.log(Level.WARNING, "Database connection not initiated.");
        }
        return projectSummaryModels;
    }

}
