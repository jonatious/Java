/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertica;
import java.sql.*;
import java.util.Properties;
import java.util.*;

/**
 *
 * @author King Maker
 */
public class Vertica_Connection {
    private static Statement st = null;
    public static Connection conn;
    
    public static boolean Get_Connection()
    {
        // Load JDBC driver
        try {
            Class.forName("com.vertica.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the JDBC driver class." + e);
            return false; 
        }

        Properties myProp = new Properties();
        myProp.put("user", "cosc6340");
        myProp.put("password", "1pmMon-Wed");
        try {
            conn = DriverManager.getConnection(
                       "jdbc:vertica://129.7.242.19:5433/cosc6340", myProp);
        } catch (SQLException e) {
            // Could not connect to database.
            System.err.println("Could not connect to database.");
            System.err.println(e);
            return false;
        }
        System.out.println("Connection Successful!!");
       return true;
    }
    
    public static int Run_Query(String query)
    {
        int count;
        // Load JDBC driver
        try {
            Class.forName("com.vertica.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the JDBC driver class." + e);
            return 0; 
        }
        
       try {
            write_sql(query);
            st = conn.createStatement();
            ResultSet rs = executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                count = rs.getInt(1);
                return count;
            }
        } catch (SQLException e) {
            System.err.println("Not valid count statement");
        }
       return 1;
    }
    
    public static ArrayList String_Query(String query)
    {
        ArrayList table = new ArrayList();
        // Load JDBC driver
        try {
            Class.forName("com.vertica.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the JDBC driver class." + e);
            return null; 
        }
        
       try {
            write_sql(query);
            st = conn.createStatement();
            ResultSet rs = executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                if(!table.contains(rs.getString(1)))
                    table.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println("Not valid count statement");
        }
       return table;
    }
    
    public static void Create_Query(String query)
    {
        // Load JDBC driver
        try {
            Class.forName("com.vertica.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the JDBC driver class." + e);
            return ; 
        }
        
       try {
            write_sql(query);
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            System.err.println(e);
        }
       return ;
    }
    
    public static ResultSet executeQuery(String sqlStatement) {
        try {
            return st.executeQuery(sqlStatement);
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }
    
    public static void write_sql(String query){
        MainProject.sql_txt+= query + "\n\n";
    }
    
}
