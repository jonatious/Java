/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertica;
import java.io.PrintWriter; 

/**
 *
 * @author King Maker
 */
public class MainProject {
    public static String sql_txt = "";
    public static String output_txt = "Table\t\t\tForm\tComplies\tExplanation\n";
    public static String error = "";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("No input database provided!!");
            return;
        }
        boolean connection = Vertica_Connection.Get_Connection();// Connecting to vertica jdbc server
        
        
        if(connection){
           String file_Name = args[0].substring(9);// The name of the file to open.
        	//String file_Name = "C:\\Users\\harish\\Desktop\\DB project\\testschema.txt";
            Object[] obj = DataAccessLayer.read_file(file_Name);
            if(obj==null)
                return;
            String[] table = (String[])obj[0];
            String[][] table_data = (String[][])obj[1];

            for(int i=0;table[i]!= null;i++){
                output_txt += table[i] + "\t\t\t";
                if(table[i].length()<5)
                    output_txt += "\t";
                sql_txt += "--Preliminary check SQL Queries :\n\n";
                boolean preliminary = DataAccessLayer.PreliminaryCheck(table[i],table_data[i]);
                if(preliminary){
                    System.out.println("Preliminary test passed for table '" + table[i] + "'");
                    sql_txt += "--1NF certify SQL Queries :\n\n";
                    boolean emptyTableTest = false, oneRecordTableTest = false;
                    boolean oneNFTrue = false, twoNFTrue = false, threeNFTrue = false;
                    
                    String emptyTableCount = "SELECT COUNT(*) \nFROM "+ table[i];
                    int check = Vertica_Connection.Run_Query(emptyTableCount);
                    if(check == 0){
                    	emptyTableTest = true;
                    	error = "No records found.Nulls are ambigous hence relation is not normalized";
                        System.out.println(error);
                    	output_txt += "1NF\t\tF\t\t\t" + error + "\n";
                	}
                    if(check == 1){
                    	oneRecordTableTest = true;
                    	error = "Only one record. Hence table is completely normalized";
                        System.out.println(error);
                    	output_txt += "3NF\t\tT\t\t\t" + error + "\n";
                	}
                    if(emptyTableTest==false && oneRecordTableTest==false){
		                    oneNFTrue = Certify1NF.Certify1NF(table[i],table_data[i]);                 
		
		                    if(oneNFTrue){
		                        //dropping table if exists
		                        String Drop_Query = "DROP TABLE IF EXISTS temp_dependancy";
		                        Vertica_Connection.Create_Query(Drop_Query);
		
		                        sql_txt += "--Creating temp table SQL query:\n\n";
		                        String query = "CREATE TABLE temp_dependancy(\n\tid AUTO_INCREMENT,\n\tcolumn1 varchar(50),\n\tcolumn2 varchar(50),\n\ttable_name varchar(50)\n);";
		                        Vertica_Connection.Create_Query(query);
		                        twoNFTrue = Certify2NF.Certify2NF(table[i], table_data[i]);
		
		                        //deleting recors from temp table if exists
		                        Drop_Query = "delete from temp_dependancy";
		                        Vertica_Connection.Create_Query(Drop_Query);
		
		                        if(twoNFTrue){
		                            sql_txt += "--Certify 3NF SQL Queries :\n\n";
		                            threeNFTrue = Certify3NF.Certify3NF(table[i],table_data[i]);
		                            query = "DROP TABLE temp_dependancy";
		                            Vertica_Connection.Create_Query(query);
		                        }
	                     }
		                    if(!oneNFTrue)
		                        output_txt += "1NF\t\tF\t\t\t" + error + "\n";
		                    else if(!twoNFTrue)
		                        output_txt += "2NF\t\tF\t\t\t" + error.substring(0,error.length()-2) + "\n";
		                    else if(!threeNFTrue)
		                        output_txt += "3NF\t\tF\t\t\t" + error.substring(0,error.length()-2) + "\n";
		                    else
		                    	output_txt += "3NF\t\tT\t\n";
                   }
              }
              else{
                   	System.out.println("Preliminary test failed  for table '" + table[i] + "'");
                    output_txt += "Preliminary test failed!\n";
               }
            }
            
            try{
                PrintWriter write_sql = new PrintWriter("NF.sql", "UTF-8");
                write_sql.println(sql_txt);
                write_sql.close();
                PrintWriter write_output = new PrintWriter("NF.txt", "UTF-8");
                write_output.println(output_txt);
                write_output.close();
            }
            catch(Exception e){
                System.out.println("Writing to file error : " + e);
            }
        }
        System.out.println("Output files NF.txt and NF.sql are generated successfully!");
    }
}
