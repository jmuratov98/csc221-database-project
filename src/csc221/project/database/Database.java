package csc221.project.database;

import csc221.project.employees.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Database {
    private static String URL = "jdbc:derby:FinalProject;create=true;";
    private static String username = "MrInquisitor";
    private static String password = "password";

    private Connection con = null;
    private PreparedStatement selectAllEmployee = null;
    private PreparedStatement insertNewEmployee = null;
    private PreparedStatement searchFirstName = null;
    private PreparedStatement searchSales = null;
    private PreparedStatement search30hours = null;

    public Database() {
        createConnection();
    }

    public void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            con = DriverManager.getConnection(URL, username, password);

            selectAllEmployee = con.prepareStatement("SELECT * FROM employees");
            insertNewEmployee = con.prepareStatement("INSERT INTO employees (socialSecurityNumber, firstName, lastName, birthday, employeeType, departmentName) VALUES (?,?,?,?,?,?)");
            searchFirstName = con.prepareStatement("SELECT * FROM employees WHERE firstName = ?");
            searchSales = con.prepareStatement("SELECT * FROM employees WHERE departmentName = ?");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Employee> selectAllEmployees() {
        ObservableList<Employee> res = null;
        try {
            res = FXCollections.observableArrayList();
            ResultSet set = selectAllEmployee.executeQuery();

            while(set.next()) {
                String ssn = set.getString(1); // Social Security number
                String fn = set.getString(2); // First Name
                String ln = set.getString(3); // last name
                String dob = set.getString(4); // birthday
                String type = set.getString(5); // employee type
                String dep = set.getString(6); // department name
                res.add(new Employee(fn, ln, ssn));
            }
            set.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ObservableList<Employee> insert(String ssn, String fn, String ln, String dob, String type, String dep) {
        int result = 0;
        ObservableList list = FXCollections.observableArrayList();

        try {
            insertNewEmployee.setString(1, ssn);
            insertNewEmployee.setString(2, fn);
            insertNewEmployee.setString(3, ln);
            insertNewEmployee.setString(4, dob);
            insertNewEmployee.setString(5, type);
            insertNewEmployee.setString(6, dep);

            result = insertNewEmployee.executeUpdate();
            list.add(new Employee(fn, ln, ssn));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<Employee> searchFirstName(String lastName)  {
        ObservableList<Employee> res = null;
        try {
            searchFirstName.setString(1, lastName);
            ResultSet set = searchFirstName.executeQuery();
            res = FXCollections.observableArrayList();
            while (set.next()) {
                res.add(new Employee(set.getString("firstName"), set.getString("lastName"), set.getString("socialSecurityNumber")));
            }

            set.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ObservableList<Employee> searchSales(String dept)  {
        ObservableList<Employee> res = null;
        try {
            searchFirstName.setString(1, dept);
            ResultSet set = searchFirstName.executeQuery();
            res = FXCollections.observableArrayList();
            while (set.next()) {
                res.add(new Employee(set.getString("firstName"), set.getString("lastName"), set.getString("socialSecurityNumber")));
            }

            set.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void shutdown() {
        try {
            if (con != null)  {
                DriverManager.getConnection(URL + ";shutdown=true");
                con.close();
            }
        }
        catch (SQLException sqlExcept)  {
        }

    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

}
