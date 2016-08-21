/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.io.*;

/**
 *
 * @author jonyhero
 */
public class DataAccessLayer {
    //read file
    public static Object[] read_file(String file_name){

        // This will reference one line at a time
        String line;
        double[][] attr_data = new double[99000][50];
        String[] attr_names = new String[50];
        String class_name = new String();
        String[] class_value = new String [99000];

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(file_name);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            int i=0;
            while((line = bufferedReader.readLine()) != null) {
                if(i==0){
                    attr_names = line.split("[,]");
                    class_name = attr_names[attr_names.length-1];
                    attr_names[attr_names.length-1] = null;
                }
                else
                {
                    String temp_attr_data[] = line.split("[,]");
                    for(int j=0;j<temp_attr_data.length-1;j++)
                        attr_data[i][j] = Float.parseFloat(temp_attr_data[j]);
                    class_value[i] = temp_attr_data[temp_attr_data.length-1];
                }
                i++;
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
        
        return new Object[]{attr_names,class_name,attr_data,class_value};
    }
}
