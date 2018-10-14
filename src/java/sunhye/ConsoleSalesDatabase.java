package sunhye;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConsoleSalesDatabase {

    // member vars
    private JdbcHelper jdbc;

    //connect DB
    public ConsoleSalesDatabase() {
        final String DB_URL = "jdbc:mysql://localhost:3306/ejd";
        final String DB_USER = "ejd";
        final String DB_PASS = "PROG32758";
        jdbc = new JdbcHelper();
        jdbc.connect("jdbc:mysql://localhost:3306/ejd", "ejd", "PROG32758");

    }

    //query all names and values from DB for getting JSON string
    public String getConsoleSales(String category) {
        String sql = "";
        if (category == null || category.isEmpty()) {
            category = "hardwareSales";
        }
        sql = "SELECT consoleName," + category + "  FROM ConsoleSales";
        ResultSet rs = jdbc.query(sql);
        String json = "[";
        
        try {
            while (rs.next()) {
                
                String cn = rs.getString(1);
                Double cv = rs.getDouble(2);
                
                json += "{\"name\":\"" + cn + "\", \"value\":" + cv + "},";
            }
        } catch (SQLException e) {
            System.err.println(e.getSQLState() + ":" + e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        int index = json.lastIndexOf(",");
        if(index>=0){
        json = json.substring(0, index);
        }
        jdbc.disconnect();

        json += "]";

        return json;
    }
}
