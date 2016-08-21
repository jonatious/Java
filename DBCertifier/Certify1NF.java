/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertica;

/**
 *
 * @author jonyhero
 */
public class Certify1NF {
    public static boolean Certify1NF(String table, String[] table_data){
        boolean no_nulls = DataAccessLayer.No_Nulls(table, table_data);
        boolean no_duplicates = true;
        if(no_nulls)
            no_duplicates = DataAccessLayer.No_Duplicates(table, table_data);
        else{
            System.out.println("There exists null keys in the table '" + table + "' and it is not in 1NF");
            MainProject.error = "Nulls exist";
            return false;
        }
        if(!no_duplicates){
            System.out.println("There exists duplicate keys in the table '" + table + "' and it is not in 1NF");
            MainProject.error = "Duplicates exist";
            return false;
        }
        else
            System.out.println("The table '" + table + "' is in 1NF");
        return true;
    }
    
}
