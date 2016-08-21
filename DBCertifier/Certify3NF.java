/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertica;

import java.util.ArrayList;

public class Certify3NF {
    public static boolean Certify3NF(String table, String[] table_data){
        MainProject.error = "";
        int x=0,y=0;
        String[][] new_tables = new String[50][50];
        ArrayList nonKeys = DataAccessLayer.nonKeyOnly(table_data);
        ArrayList subsetsNonKeys = DataAccessLayer.subsetOfKey(nonKeys);
        ArrayList subTableColumns = new ArrayList();
        ArrayList nonTableColumns = new ArrayList();

        for(int j=0;j<subsetsNonKeys.size();j++){
            String LHS = subsetsNonKeys.get(j).toString();
            String[] check = LHS.split(",");
            for(int k=0;k<nonKeys.size();k++){
                boolean dependancy = false;
                String RHS = nonKeys.get(k).toString();
                if(check.length<1){
                    System.out.println("Error: No subsets found");
                }
                else{
                    if(!RHS.equals(check[0])){
                        if(check.length>1){
                            if(!RHS.equals(check[1])){
                                dependancy = DataAccessLayer.dependancy(LHS,RHS,table);
                            }
                        }
                        else
                            dependancy = DataAccessLayer.dependancy(LHS,RHS,table);
                    }
                }
                if(dependancy){
                    String insert_query = "INSERT INTO temp_dependancy(column1,column2,table_name) \nVALUES('" + LHS + "','" + RHS + "','" + table + "');";
                    Vertica_Connection.Create_Query(insert_query);
                }
            }
        }

        //DECOMPOSITION IN 3NF
        int table_count = 1;
        String query = "SELECT count(*) \nFROM temp_dependancy";
        int count = Vertica_Connection.Run_Query(query);
        if(count==0){
            System.out.println("the table '" + table + "' is in 3NF.");
            return true;
        }
        else{
            System.out.println("the table '" + table + "' is not in 3NF.");
            //extracting LHS columns
            query = "SELECT column1, id \nFROM temp_dependancy GROUP BY column1,id \nORDER BY id ASC";
            ArrayList LHS = Vertica_Connection.String_Query(query);
            for(int j=0;j<LHS.size();j++){
                String columns = LHS.get(j).toString();
                query = "SELECT column2 \nFROM temp_dependancy \nWHERE column1 = '" + columns + "' \nGROUP BY column2";
                ArrayList RHS = Vertica_Connection.String_Query(query);

                String temp[] = columns.split(",");
                subTableColumns.add(temp[0]);
                if(temp.length>1)
                    subTableColumns.add(temp[1]);
                
                //Creating new table
                for(int k=0;k<RHS.size();k++){
                    MainProject.error += LHS.get(j).toString() + " --> " + RHS.get(k).toString() + ", ";
                    columns = columns + "," + RHS.get(k);
                    nonTableColumns.add(RHS.get(k));
                }
                
                //dropping table if exists
                String Drop_Query = "DROP TABLE IF EXISTS " + table + table_count;
		Vertica_Connection.Create_Query(Drop_Query);
                
                String create_query = "CREATE TABLE " + table + table_count + " as(\nSELECT " + columns + " \nFROM " + table + " GROUP BY " + columns + ")";
                Vertica_Connection.Create_Query(create_query);

                //storing new table name and its key
                new_tables[x][y++] = table + table_count++;//table name
                new_tables[x][y++] = LHS.get(j).toString();
                new_tables[x++][y] = columns;
                y=0;

                //deleting dependancy rows
                for(int k=0;k<RHS.size();k++){
                    query = "DELETE FROM temp_dependancy \nWHERE (column1 like '%"  + LHS.get(j) + "%' AND column2 = '" + RHS.get(k) + "') \nOR \n(column1 = '"  + RHS.get(k) + "' AND column2 = '" + LHS.get(j) + "')\nOR \n(column2 = '"  + RHS.get(k) + "')";
                    Vertica_Connection.Create_Query(query);
                }

                //regenerating LHS
                query = "SELECT column1,id \nFROM temp_dependancy \nGROUP BY column1,id \nORDER BY id ASC";
                LHS = Vertica_Connection.String_Query(query);
                j=-1;
            }
            // Creating main table
            String columns = "";
            for(int t=0;t< table_data.length;t++){
                String temp_column = table_data[t];
                temp_column = temp_column.replace("$", "");
                if(!nonTableColumns.contains(temp_column) || subTableColumns.contains(temp_column) || table_data[t].charAt(table_data[t].length()-1) == '$')
                    columns += temp_column + ",";
            }
            columns = columns.substring(0, columns.length()-1);
            
            //dropping table if exists
            String Drop_Query = "DROP TABLE IF EXISTS " + table + "main";
            Vertica_Connection.Create_Query(Drop_Query);

            
            String create_query = "CREATE TABLE " + table + "main as(\nSELECT " + columns + " \nFROM " + table + " \nGROUP BY " + columns + ")";
            Vertica_Connection.Create_Query(create_query);

            System.out.println("\nDecomposition of table " + table + ":\n");
            System.out.println(table + "main(" + columns + ")");
            String temp_tables = "";
            
            String check_query = "SELECT count(*) \nFROM ( \nSELECT * \nFROM " + table + "main  ";
            for(int t=0;new_tables[t][0]!=null;t++){
                System.out.println( new_tables[t][0] + "(" + new_tables[t][2] + ")");
                check_query += "\nJOIN " + new_tables[t][0] + " \nON ";
                String[] sub_key = new_tables[t][1].split(",");
                for(int k=0;k<sub_key.length;k++){
                    check_query += table + "main." + sub_key[k] + " = " + new_tables[t][0] + "." + sub_key[k];
                    if(k+1 < sub_key.length){
                        check_query += " \nAND ";
                    }
                }
                temp_tables += "," + new_tables[t][0];
            }
            System.out.println("Verification : \n" + table + " = join(" + table + "main" + temp_tables + ")? ");
            check_query += ") as temp";
            int dec_count = Vertica_Connection.Run_Query(check_query);
            int tot_count = Vertica_Connection.Run_Query("SELECT count(*) \nFROM " + table);
            if(dec_count == tot_count)
                System.out.println("Yes\n");
            else
                System.out.println("No! There is an error. The table is not decomposed properly.\n");

            //Dropping the derived tables
            MainProject.sql_txt += "--DROP temp table SQL Query :\n\n";
            for(int t=0;new_tables[t][0]!=null;t++){
                Vertica_Connection.Create_Query("DROP TABLE " + new_tables[t][0]);
            }
            Vertica_Connection.Create_Query("DROP TABLE " + table + "main");
        }
        return false;
    }
    
}
