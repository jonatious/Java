/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airlinemanagement;

import java.sql.Connection;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.text.*;

/**
 *
 * @author jonyhero
 */
public class AirlineManagement {
    
    public static String sql_txt = "";
    
    public static void main(String[] args) throws ParseException {
        Connection connect = null;
        connect = PGConnection.Get_Connection(connect);// Connecting to vertica jdbc server
        
        if(connect != null){
            System.out.println("Connection success");
        
            
            if(args[0].equals("test")){
                
                InsertThread.GetVars("AB466","SELECT bsbp FROM baseprice WHERE isactive='true' AND ","Cash",149,3245);
            }
            if(args[0].equals("input")){
                String file_name = "C:\\Users\\jonyhero\\Desktop\\input.csv";
                Object[] input = fileread(file_name);
                String[] cust_id = (String[])input[0];
                Date[] travel_date = (Date[])input[1];
                String[] flightName = (String[])input[2];
                String[] PaymentMethod = (String[])input[3];
                String[] Fname = (String[])input[4];
                String[] Lname = (String[])input[5];
                String[] AisleNo = (String[])input[6];
                String[] SeatNo = (String[])input[7];
                String[] Class = (String[])input[8];
                int size = cust_id.length;
                for(int i=0; i< size; i++){
                    if(cust_id[i]!=null){
                    InsertThread a = new InsertThread(new Object[]{cust_id[i],travel_date[i],flightName[i],PaymentMethod[i],Fname[i],Lname[i],AisleNo[i],SeatNo[i],Class[i]});
                    a.start();
                    }
                }
            }
            else if(args[0].substring(0,8).equals("nthreads")){
                int nthreads = Integer.parseInt(args[0].substring(9,args[0].length()));
                while(true){
                    for(int i=0;i<nthreads;i++){
                        Object[] input = random_selects();
                        InsertThread a = new InsertThread(input);
                        a.start();
                    }
                    try{
                        Thread.sleep(10000);
                        System.out.println("A set of " + nthreads + " threads is started");
                    }
                    catch(Exception e){
                        System.out.println("Thread sleep error" + e);
                    }
                    try{
                        PrintWriter write_sql = new PrintWriter("OLTP.sql", "UTF-8");
                        write_sql.println(sql_txt);
                        write_sql.close();
                    }
                    catch(Exception e){
                        System.out.println("Writing to file error : " + e);
                    }
                }
            }
            else
                System.out.println("Invalid argument! Please use argument as 'input' or 'nthreads=XX' where XX is the number of threads!");
        }
        else
            System.out.println("Connection failed");
    }
    
    static Object[] fileread(String file_name){
         // This will reference one line at a time
        String line;
        String[] cust_id = new String[50];
        Date[] travel_date = new Date[50];
        String[] flightName = new String[50];
        String[] PaymentMethod = new String[50];
        String[] Fname = new String[50];
        String[] Lname = new String[50];
        String[] AisleNo = new String[50];
        String[] SeatNo = new String[50];
        String[] Class = new String[50];
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(file_name);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            int i=-1;
            while((line = bufferedReader.readLine()) != null) {
                if(i>=0){
                    String[] temp = line.split("[,]");
                    cust_id[i] = temp[0];
                    DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                    travel_date[i] = format.parse(temp[1]);
                    flightName[i] = temp[2];
                    PaymentMethod[i] = temp[3];
                    Fname[i] = temp[4];
                    Lname[i] = temp[5];
                    AisleNo[i] = temp[6].substring(0,1);
                    SeatNo[i] = temp[6].substring(1,temp[6].length());
                    Class[i] = temp[7].substring(0,1);
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
        
        return new Object[]{cust_id,travel_date,flightName,PaymentMethod,Fname,Lname,AisleNo,SeatNo,Class};
    }

    private static Object[] random_selects() throws ParseException {
        
        //customer id
        int max = PGConnection.Run_Query("SELECT max(id) FROM customer");
        String cust_id = Integer.toString(Math.round((float)Math.random()*(max-1) + 1));
        
        //no of stops
        int stops = Math.round((float)Math.random()*2);
        
        //Flight name
        String flightName;
        if(stops == 0){
            ArrayList flight_list = PGConnection.ArrayList_Query("SELECT distinct name FROM scheduledflights");
            flightName = (String)flight_list.get(Math.round((float)Math.random()*(flight_list.size()-1)));
        }
        else{
            ArrayList flight_list = PGConnection.ArrayList_Query("SELECT distinct name FROM scheduledflights WHERE nostops = " + stops);
            flightName = (String)flight_list.get(Math.round((float)Math.random()*(flight_list.size()-1)));
        }
            
        //Travel date
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList date_list = PGConnection.ArrayList_Query("SELECT distinct startdate FROM trip WHERE flightid = (SELECT id from scheduledflights WHERE name = '" + flightName + "')");
        Date travel_date = df.parse((String)date_list.get(Math.round((float)Math.random()*(date_list.size()-1))));
        
        //Payment method
        String[] payment_list = {"Cash","Credit Card","Check"};
        String PaymentMethod = payment_list[Math.round((float)Math.random()*(payment_list.length - 1))];
        
        //First and Last names
        String Fname = "F" + cust_id;
        String Lname = "L" + cust_id;
        
        //Seat details
        String[] seat_details = CalcSeat(flightName);
        String Class = seat_details[0];
        String AisleNo = seat_details[1];
        String SeatNo = seat_details[2];
        
        return new Object[]{cust_id,travel_date,flightName,PaymentMethod,Fname,Lname,AisleNo,SeatNo,Class,stops};
    }

    public static String[] CalcSeat(String flightName) {
        int max;
        String[] Class_list = {"E","B","F"};
        String Class = Class_list[Math.round((float)Math.random()*(Class_list.length-1))];
        
        max = PGConnection.Run_Query("SELECT max(aisleid) FROM seatarray WHERE category = '" + Class +
                "' AND carrierid = (SELECT carrierid FROM scheduledflights WHERE name = '" + flightName + "')");
        int aisle_id = Math.round((float)Math.random()*(max-1) + 1);
        String AisleNo = PGConnection.String_Query("SELECT aislecode FROM seataisle WHERE id = " + aisle_id);
        AisleNo = AisleNo.trim();
        
        max = PGConnection.Run_Query("SELECT max(seatid) FROM seatarray WHERE category = '" + Class +
                "' AND aisleid = " + aisle_id +
                " AND carrierid = (SELECT carrierid FROM scheduledflights WHERE name = '" + flightName + "')");
        String SeatNo = Integer.toString(Math.round((float)Math.random()*(max-1) + 1));
        return new String[]{Class,AisleNo,SeatNo};
    }
    
}
