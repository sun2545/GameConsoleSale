///////////////////////////////////////////////////////////////////
//  JdbcHelper.java
// ==================
//a simple, light-weight JDBC utility class to interact will
//It supports both static statement and perparedStatement
//
// AUTHOR : Sunhye Kwon (kwonsunhye@gmail.com)
//CREATED :2017 - 012-10
//UPDATE :2017 -12-15
package sunhye;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JdbcHelper {
    //instance vars

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String errorMessage;
    private String activeSql;
    private PreparedStatement activeStatement;

//ctor
    public JdbcHelper() {
        connection = null;
        statement = null;
        resultSet = null;
        errorMessage = "no error";
    }
    ///////////////////////////////////////////////////////
    // try to connect to DB with 3 param
    ////////////////////////////

    public boolean connect(String url, String user, String pass) {
        boolean connected = false;// defalut is not connected

        //validation
        if (url == null || url.isEmpty()) {
            return connected;
        }
        if (user == null || user.isEmpty()) {
            return connected;
        }
        if (pass == null || pass.isEmpty()) {
            return connected;
        }

        //try to connect
        try {

            initJdbcDriver(url);
            connection = DriverManager.getConnection(url, user, pass);

            //try to create statement object automatically
            statement = connection.createStatement();
            connected = true;
        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getSQLState() + ":" + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return connected;

    }

    //clear JDBC resource
    public void disconnect() {

        try {
            resultSet.close();
        } catch (Exception e) {
        }
        try {
            statement.close();
        } catch (Exception e) {
        }
        try {
            connection.close();
        } catch (Exception e) {
        }

    }

    public ResultSet query(String sql) {
        //initialize return value
        resultSet = null;

        //validate input 
        if (sql == null || sql.isEmpty()) {
            System.err.println("[WARNING] SQL string is null or empty in query()");
            return resultSet;
        }

        //check connection before executing SQL query       
        try {
            if (connection == null || connection.isClosed()) {
                System.err.println("Connection is NOT established. Make connection "
                        + "to DB before calling query()");
                return resultSet;
            }
            // PreparedStatement ps = connection.prepareStatement(sql);  //  adding code

            resultSet = statement.executeQuery(sql);

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getSQLState() + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        //PreparedStatement statement2 = connection.prepareStatement(sql);
        return resultSet;
    }

       private void initJdbcDriver(String url) {
        // test various databases
        try {
            if (url.contains("jdbc:mysql")) {
                Class.forName("com.mysql.jdbc.Driver");
            } else if (url.contains("jdbc:oracle")) {
                Class.forName("oracle.jdbc.OracleDriver");
            } else if (url.contains("jdbc:derby")) {
                Class.forName("org.apache.derby.jdbc.ClientDriver");
            } else if (url.contains("jdbc:db2")) {
                Class.forName("com.ibm.db2.jcc.DB2Driver");
            } else if (url.contains("jdbc:postgresql")) {
                Class.forName("org.postgresql.Driver");
            } else if (url.contains("jdbc:sqlite")) {
                Class.forName("org.sqlite.JDBC");
            } else if (url.contains("jdbc:sqlserver")) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } else if (url.contains("jdbc:sybase")) {
                Class.forName("sybase.jdbc.sqlanywhere.IDriver");
            }

            // add other databases here
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

}
