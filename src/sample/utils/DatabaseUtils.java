package sample.utils;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class DatabaseUtils {
    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static Connection connection = null;
    private static boolean isDriverAvailable;

    public static boolean isIsDriverAvailable() {
        return isDriverAvailable;
    }

    public static void startDriverCheck() {
        try {
            Class.forName(JDBC_DRIVER);
            isDriverAvailable = true;
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, "Driver not found");
            e.printStackTrace();
            isDriverAvailable = false;
        }
    }

    public static void dbConnect() throws SQLException {
        if (isDriverAvailable) {
            try {
                // TODO define connection string and credential here
                String connectionString = "@connectionString";
                connection = DriverManager.getConnection(connectionString, "@username", "@password");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Connection Failed! Check output console" + e);
                e.printStackTrace();
                throw e;
            }
        }
    }

    public static void dbDisconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {

            dbConnect();
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(queryStmt);
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
        return crs;
    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException {
        Statement stmt = null;
        try {
            dbConnect();
            stmt = connection.createStatement();
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
    }
}
