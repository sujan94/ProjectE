package sample.utils;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class DBUtil {
    //Declare JDBC Driver
    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";

    //Connection
    private static Connection conn = null;

    private static boolean isDriverAvailable;

    public static void startDriverCheck() {
        //Setting Oracle JDBC Driver
        try {
            Class.forName(JDBC_DRIVER);
            isDriverAvailable = true;
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, "Driver not found");
            e.printStackTrace();
            isDriverAvailable = false;
        }
    }

    //Connect to DB
    public static void dbConnect() throws SQLException, ClassNotFoundException {
        if (isDriverAvailable) {
            //Establish the Oracle Connection using Connection String
            try {
                // TODO define connection string and credential here
                String connectionString = "@connectionString";
                conn = DriverManager.getConnection(connectionString, "@username", "@password");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Connection Failed! Check output console" + e);
                e.printStackTrace();
                throw e;
            }
        }
    }

    //Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();

            //Create statement
            stmt = conn.createStatement();

            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);

            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        //Return CachedRowSet
        return crs;
    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();
            //Create Statement
            stmt = conn.createStatement();
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
    }
}
