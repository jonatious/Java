/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertica;
import java.io.*;
import java.util.*;

/**
 *
 * @author King Maker
 */
public class DataAccessLayer {
    public static boolean dependancy(String column1, String column2, String R){
        String check[] = column1.split(",");
        String check_null;
        check_null = " \nWHERE " + check[0] + " is not null";
        if(check.length > 1)
            check_null += "\nAND " + check[1] + " is not null";
        check_null += "\nAND " + column2 + " is not null";
        String query = "\nSELECT count(*) \nFROM (\nSELECT "+ column1 + ", count(distinct " + column2 + ") as dist_count \nFROM " + R + check_null + "\nGROUP BY " + column1 + ") as temp \nWHERE dist_count >1\n";
        int count = Vertica_Connection.Run_Query(query);
        if (count == 0)
            return true;
        else
            return false;
    }
    
    public static Object[] read_file(String file_name){


        // This will reference one line at a time
        String line;
        String[][] file_data = new String[50][50];
        String[] table = new String[50];

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(file_name);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            int i=0;
            while((line = bufferedReader.readLine()) != null) {
                String[] temp = new String[2];
                temp = line.split("[(]",2);
                table[i] = temp[0];
                temp[1] = temp[1].replace("(k)","$");
                temp[1] = temp[1].replace("(","");
                temp[1] = temp[1].replace(")","");
                file_data[i++] = temp[1].split("[,]");
            }   

            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + file_name + "'");    
            return null;
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + file_name + "'");
            return null;
        }
        catch(Exception ex) {
            System.out.println("Error: " + ex);
            return null;
        }
        
        for(int i=0;file_data[i][0]!=null;i++)
            for(int j=0;j<file_data[i].length;j++)
                file_data[i][j] = file_data[i][j].trim();
        
        return new Object[]{table,file_data};
    }

    public static boolean PreliminaryCheck(String table, String[] table_data) {
        
            //checking validity of table name
            String query = "SELECT *\nFROM cosc6340." + table;
            try {
                int check = Vertica_Connection.Run_Query(query);
                if(check == 0)
                    return false;
            }
            catch(Exception ex){
                System.out.println("Invalid table \"" + table + "\" found!");
                return false;
            }
            //Checking validity of columns
            String columns = "";
            for(int j=0; j<table_data.length;j++){
                columns = columns + table_data[j] + ",";
            }
            columns = columns.substring(0, columns.length()-1);
            columns = columns.replace("$", "");
            query = "SELECT " + columns + " \nFROM cosc6340." + table;
            try {
                int check = Vertica_Connection.Run_Query(query);
                if(check == 0)
                    return false;
                
            }
            catch(Exception ex){
                System.out.println("Invalid Columns found!");
                return false;
            }
        return true;
    }
    
    //1st Normal Form - Nulls
    public static boolean No_Nulls(String table, String[] table_data){
        
            for(int j=0; j<table_data.length;j++){
                if (table_data[j].charAt(table_data[j].length()-1) == '$'){
                    String query = "SELECT count(*)" + " \nFROM cosc6340." + table + "\nWHERE " + table_data[j].replace("$","") + " isnull";
                    try {
                        int count = Vertica_Connection.Run_Query(query);
                        if (count > 0)
                            return false;
                    }
                    catch(Exception ex){}
                }
            }
            return true;
        }
    
    //1st Normal Form - Duplicates
    public static boolean No_Duplicates(String table, String[] table_data){
        
            String columns = "";
            for(int j=0; j<table_data.length;j++){
                if (table_data[j].charAt(table_data[j].length()-1) == '$')
                    columns = columns + table_data[j] + ",";
            }
            columns = columns.substring(0, columns.length()-1);
            columns = columns.replace("$", "");
            
            //Count of total rows
            String query = "SELECT count(*)" + " \nFROM cosc6340." + table;
            int tot_count = Vertica_Connection.Run_Query(query);
            
            //Count of distinct keys
            query = "SELECT count(*) \nFROM (\nSELECT " + columns + " \nFROM cosc6340." + table + "\nGROUP BY " + columns + ") as temp";
            int count_key = Vertica_Connection.Run_Query(query);
            
            if(tot_count != count_key)
                return false;
        return true;
    }
    
    public static ArrayList subsetOfKey(ArrayList nonkeyOnly){
    	
        ArrayList subSet  = new ArrayList();
    	int j=0;
        if(nonkeyOnly.size() >1)
            for(int k=0;k<nonkeyOnly.size();k++)
                subSet.add(nonkeyOnly.get(k));
        if(nonkeyOnly.size() >2)
            for(int k=0;k<nonkeyOnly.size()-1;k++)
                for(int l=k+1;l<nonkeyOnly.size();l++)
                    subSet.add(nonkeyOnly.get(k)+","+nonkeyOnly.get(l));
    	return subSet;
    }//subsetOfKey end
    
    public static ArrayList nonKeyOnly(String[] table_data){
   	
    	ArrayList nonkeyOnly = new ArrayList();
    	//find only keys
        int j = 0;
    	for(int i=0; i<table_data.length;i++){
            if (table_data[i].charAt(table_data[i].length()-1) != '$'){
                nonkeyOnly.add(table_data[i]);
            }
    	}
    	return nonkeyOnly;
    }
    public static ArrayList canKeyOnly(String[]table_data_temp){
   	
    	ArrayList keyOnly = new ArrayList();
    	//find only keys
    	for(int i=0; i<table_data_temp.length;i++){
    		if (table_data_temp[i].charAt(table_data_temp[i].length()-1) == '$'){
    			keyOnly.add(table_data_temp[i].replace("$", ""));  			  	
            }
    	}
    	return keyOnly;
    }
    
}
