/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airlinemanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author jonyhero
 */
public class PGConnection {
    private static Statement st = null;
    
    public static Connection Get_Connection(Connection connect)
    {
        // Load JDBC driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the JDBC driver class." + e);
            return null; 
        }

        try {
            connect = DriverManager.getConnection(
					"jdbc:postgresql://129.7.243.243:5432/team18", "team18",
					"deadlock");
        } catch (SQLException e) {
            // Could not connect to database.
            System.err.println("Could not connect to database.");
            System.err.println(e);
            return null;
        }
        //System.out.println("Connection Successful!!");
       return connect;
    }
    
    public static int Run_Query(String query)
    {
        Connection connect = null;
        int count;
        // Load JDBC driver
        connect = Get_Connection(connect);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the JDBC driver class." + e);
            return 0; 
        }
        
       try {
            write_sql(query);
            st = connect.createStatement();
            ResultSet rs = executeQuery(query);
            while (rs.next()) {
                count = rs.getInt(1);
                try{
                    connect.close();
                }
                catch(Exception e){
                    System.err.println(e);
                }
                return count;
            }
        } catch (SQLException e) {
            System.err.println("Not valid count statement");
        }
        
       return 0;
    }
    
    public static String String_Query(String query)
    {
        Connection connect = null;
        String val;
        // Load JDBC driver
        connect = Get_Connection(connect);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the JDBC driver class." + e);
            return ""; 
        }
        
       try {
            write_sql(query);
            st = connect.createStatement();
            ResultSet rs = executeQuery(query);
            while (rs.next()) {
                val = rs.getString(1);
                try{
                    connect.close();
                }
                catch(Exception e){
                    System.err.println(e);
                }
                return val;
            }
        } catch (SQLException e) {
            System.err.println("Not valid count statement");
        }
        
       return "";
    }
    
    public static ArrayList ArrayList_Query(String query)
    {
        Connection connect = null;
        ArrayList table = new ArrayList();
        connect = Get_Connection(connect);
        // Load JDBC driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the JDBC driver class." + e);
            return null; 
        }
        
       try {
            write_sql(query);
            st = connect.createStatement();
            ResultSet rs = executeQuery(query);
            while (rs.next()) {
                if(!table.contains(rs.getString(1)))
                    table.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println("Not valid count statement");
        }
        try{
            connect.close();
        }
        catch(Exception e){
            System.err.println(e);
        }
       return table;
    }
    
    public static void Create_Query(String query)
    {
        Connection connect = null;
        // Load JDBC driver
        connect = Get_Connection(connect);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the JDBC driver class." + e);
            return ; 
        }
        
       try {
            write_sql(query);
            st = connect.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            System.err.println(e);
        }
        try{
            connect.close();
        }
        catch(Exception e){
            System.err.println(e);
        }
       return ;
    }
    
    public static void insert_Query(String query)
    {
        Connection connect = null;
        // Load JDBC driver
        connect = Get_Connection(connect);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find the JDBC driver class." + e);
            return ; 
        }
        
       try {
            write_sql(query);
            st = connect.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            //System.err.println(e);
        }
        try{
            connect.close();
        }
        catch(Exception e){
            System.err.println(e);
        }
       return ;
    }
    
    public static ResultSet executeQuery(String sqlStatement) {
        
        Connection connect = null;
        // Load JDBC driver
        connect = Get_Connection(connect);
        try {
        	write_sql(sqlStatement);
                st = connect.createStatement();
                ResultSet rs = st.executeQuery(sqlStatement);
                connect.close();
                return rs;
            } catch (SQLException e) {
                //System.out.println(e);
            return null;
        }
    }
    
    public static void write_sql(String query){
        AirlineManagement.sql_txt+= query + "\n\n";
    }
 
}
