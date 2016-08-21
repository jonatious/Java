/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airlinemanagement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
/**
 *
 * @author jonyhero
 */
public class InsertThread implements Runnable {
    private Thread t;
    private String threadName;
    private int cust_id;
    private String travel_date;
    private String flightName;
    private String flightName1 = "";
    private String flightName2 = "";
    private int carrierID;
    private int carrierID1;
    private int carrierID2;
    private int flightID;
    private int flightID1;
    private int flightID2;
    private String PaymentMethod;
    private int PaymentID;
    private String Fname;
    private String Lname;
    private String AisleNo;
    private String SeatNo;
    private String Class;
    private String AisleNo1;
    private String SeatNo1;
    private String Class1;
    private String AisleNo2;
    private String SeatNo2;
    private String Class2;
    private String seat_class;
    private String seat_class1;
    private String seat_class2;
    private int SeatID;
    private int SeatID1;
    private int SeatID2;
    private int sairportid;
    private int dairportid;
    private int sairportid1;
    private int dairportid1;
    private int sairportid2;
    private int dairportid2;
    private double baseAmt = 0.0;
    private double taxAmt = 0.0;
    private double baseAmt1 = 0.0;
    private double taxAmt1 = 0.0;
    private double baseAmt2 = 0.0;
    private double taxAmt2 = 0.0;
    private int tripId;
    private int miles;
    private int tripId1;
    private int miles1;
    private int tripId2;
    private int miles2;
    private int key1;
    private int key2;
    private int stops;
    private boolean inserted = false;
    private String updatetrip = "";
    private String basePriceqry = "";
    private String updatetrip1 = "";
    private String basePriceqry1 = "";
    private String updatetrip2 = "";
    private String basePriceqry2 = "";

    InsertThread( Object[] input){
        cust_id = Integer.parseInt((String)input[0]); 
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        travel_date = df.format(input[1]);
        flightName = (String)input[2];
        PaymentMethod = (String)input[3];
        Fname = (String)input[4];
        Lname = (String)input[5];
        AisleNo = (String)input[6];
        SeatNo = (String)input[7];
        Class = (String)input[8];
        stops = (int)input[9];
        seat_class = Class.equals("E")? "Economy": Class.equals("B")? "Business" : "First";
        if(stops == 0)
            threadName = flightName + " - " + travel_date + " - " + Class + "-" + AisleNo + SeatNo;
        if(stops >= 1){
            threadName = "Multi Destination Trip:\n";
            String query = "SELECT name FROM scheduledflights WHERE id = (SELECT stop1id FROM stops JOIN scheduledflights sf ON sf.id = stops.flightid WHERE name = '" + flightName + "')";
            flightName1 = PGConnection.String_Query(query);
            String[] seatdetails = AirlineManagement.CalcSeat(flightName1);
            Class1 = seatdetails[0];
            AisleNo1 = seatdetails[1];
            SeatNo1 = seatdetails[2];
            seat_class1 = Class1.equals("E")? "Economy": Class1.equals("B")? "Business" : "First";
            threadName += flightName + " - " + travel_date + " - " + Class + "-" + AisleNo + SeatNo + "\n";
            threadName += flightName1 + " - " + travel_date + " - " + Class1 + "-" + AisleNo1 + SeatNo1 + "\n";
        }
        if(stops >= 2){
            flightName2 = PGConnection.String_Query("SELECT name FROM scheduledflights WHERE id = (SELECT stop2id FROM stops JOIN scheduledflights sf ON sf.id = stops.flightid WHERE name = '" + flightName + "')");
            String[] seatdetails = AirlineManagement.CalcSeat(flightName2);
            Class2 = seatdetails[0];
            AisleNo2 = seatdetails[1];
            SeatNo2 = seatdetails[2];
            seat_class2 = Class2.equals("E")? "Economy": Class2.equals("B")? "Business" : "First";
            threadName += flightName2 + " - " + travel_date + " - " + Class2 + "-" + AisleNo2 + SeatNo2 + "\n";
        }
            
    }
    public void run() {

        String checktrip = "SELECT sf.carrierid as carrierid,sf.id as sfID ,tr.id as tripId , sf.miles  as miles "+
                "FROM scheduledflights sf INNER JOIN trip tr "+
                "ON (sf.id=tr.flightid) "+
                "WHERE sf.name= '" +flightName+"'  and tr.startdate= '" + travel_date + "'  and tr.status=1";
        String count_query = "SELECT COUNT(*) from (" + checktrip + ") a";
        
        ResultSet rs = PGConnection.executeQuery(checktrip);
        sleep(50);
        try{
            while(rs.next()){
                carrierID = rs.getInt("carrierid");
                flightID = rs.getInt("sfID");
                miles = rs.getInt("miles");
                tripId = rs.getInt("tripId");
            }
        }
        catch(Exception e){
            System.out.println(e + "1");
        }
        if(stops >= 1){
            ResultSet rs1 = PGConnection.executeQuery(checktrip.replace(flightName, flightName1));
            sleep(50);
            try{
                while(rs1.next()){
                    carrierID1 = rs1.getInt("carrierid");
                    flightID1 = rs1.getInt("sfID");
                    miles1 = rs1.getInt("miles");
                    tripId1 = rs1.getInt("tripId");
                }
            }
            catch(Exception e){
                System.out.println(e + "2");
            }
        }
        if(stops >= 2){
            ResultSet rs2 = PGConnection.executeQuery(checktrip.replace(flightName, flightName2));
            sleep(50);
            try{
                while(rs2.next()){
                    carrierID2 = rs2.getInt("carrierid");
                    flightID2 = rs2.getInt("sfID");
                    miles2 = rs2.getInt("miles");
                    tripId2 = rs2.getInt("tripId");
                }
            }
            catch(Exception e){
                System.out.println(e + "3");
            }
        }
        int count =0;
        count = PGConnection.Run_Query(count_query); 
        count += PGConnection.Run_Query(count_query.replace(flightName, flightName1)); 
        count += PGConnection.Run_Query(count_query.replace(flightName, flightName2));       
        sleep(50);
        
        if(count==stops+1){
            boolean seat_available = seats_check();

            if(seat_available){
                Object[] temp = GetVars(flightName, basePriceqry, PaymentMethod, cust_id, miles);
                sairportid = (int)temp[0];
                dairportid = (int)temp[1];
                PaymentID = (int)temp[2];
                baseAmt = (double)temp[3];
                taxAmt = (double)temp[4];
                
            if(baseAmt == 0 && taxAmt == 0)
                System.out.println("Error in price");
                
                if(stops > 0){
                    temp = GetVars(flightName1, basePriceqry1, PaymentMethod, cust_id, miles1);
                    sairportid1 = (int)temp[0];
                    dairportid1 = (int)temp[1];
                    baseAmt1 = (double)temp[3];
                    taxAmt1 = (double)temp[4];
                }
                if(stops > 1){
                    temp = GetVars(flightName2, basePriceqry2, PaymentMethod, cust_id, miles2);
                    sairportid2 = (int)temp[0];
                    dairportid2 = (int)temp[1];
                    baseAmt2 = (double)temp[3];
                    taxAmt2 = (double)temp[4];
                }
                Insert_query();   
            }
        }
        else{
            System.out.println( threadName + ": Flight not found!!");
        }
		
        
        try{
            PrintWriter writer = new PrintWriter("SQLDUMP.sql", "UTF-8");
            writer.println(AirlineManagement.sql_txt);
            writer.close();
        }
        catch(Exception e){
            System.out.println("Writing to file error : " + e);
        }
    }

    public void start ()
    {
        // System.out.println("Starting " +  threadName );
        if (t == null)
        {
           t = new Thread (this, threadName);
           long time = (long)(Math.random()*1000);
           sleep(time);
           t.start ();
        }
    }
    
    public void sleep(long time)
    {
        try{
            Thread.sleep(time);
        }
        catch(Exception e){
            System.out.println("Thread Error = " + e);
        }
    }

    private synchronized void Insert_query() {
        try {
            while(Final_seat_check()){
                int newkey = 0;
                String insert_query = "BEGIN;\n" +
                    "SAVEPOINT my_savepoint;\n" +
                    "INSERT INTO \"public\".reservation\n" +
                    "   (id, bookingid, reservecode, reservedate, traveldate, customerid, tripid, sairportid, dairportid, status, paymentmid)\n" +
                    "    VALUES(" + key1 + ", " + key1 + ", 'R1', Now(), cast('" + travel_date +"' as Date)," + cust_id + ", " + tripId + ", " + sairportid + ", " + dairportid + ", 1," + PaymentID + ");\n";
                if(stops > 0)
                    insert_query += "INSERT INTO \"public\".reservation\n" +
                    "   (id, bookingid, reservecode, reservedate, traveldate, customerid, tripid, sairportid, dairportid, status, paymentmid)\n" +
                    "    VALUES(" + (key1+1) + ", " + (key1+1) + ", 'R1', Now(), cast('" + travel_date +"' as Date)," + cust_id + ", " + tripId1 + ", " + sairportid1 + ", " + dairportid1 + ", 1," + PaymentID + ");\n";
                if(stops > 1)
                    insert_query += "INSERT INTO \"public\".reservation\n" +
                    "   (id, bookingid, reservecode, reservedate, traveldate, customerid, tripid, sairportid, dairportid, status, paymentmid)\n" +
                    "    VALUES(" + (key1+2) + ", " + (key1+2) + ", 'R1', Now(), cast('" + travel_date +"' as Date)," + cust_id + ", " + tripId2 + ", " + sairportid2 + ", " + dairportid2 + ", 1," + PaymentID + ");\n";

                insert_query += "INSERT INTO \"public\".reservecustomer\n" +
                    "		(id, reservid, passfname, passlname, baseamount, tax, seatnumber, isvisarequired, isvisaavailable)\n" +
                    "		VALUES("+ key2 + ", " + key1 + ", '" + Fname + "', '" + Lname + "'," + baseAmt +", '"+ taxAmt+ "'," + SeatID + ", false, false);\n" +
                    updatetrip + ";\n" ;
                if(stops > 0){
                    insert_query += "INSERT INTO \"public\".reservecustomer\n" +
                    "		(id, reservid, passfname, passlname, baseamount, tax, seatnumber, isvisarequired, isvisaavailable)\n" +
                    "		VALUES("+ (key2+1) + ", " + (key1+1) + ", '" + Fname + "', '" + Lname + "'," + baseAmt1 +", '"+ taxAmt1+ "'," + SeatID1 + ", false, false);\n" +
                    updatetrip1 + ";\n" ;
                    newkey = PGConnection.Run_Query("SELECT max(id) from \"public\".reservetrip\n") + 1;
                    insert_query += "INSERT INTO \"public\".reservetrip\n"+
                            "VALUES(" + newkey + ", " + key1 + ", 'M');\n ";
                    insert_query += "INSERT INTO \"public\".reservetrip\n"+
                            "VALUES(" + newkey + ", " + (key1+1) + ", 'M');\n ";
                }
                if(stops > 1){
                    insert_query += "INSERT INTO \"public\".reservecustomer\n" +
                    "		(id, reservid, passfname, passlname, baseamount, tax, seatnumber, isvisarequired, isvisaavailable)\n" +
                    "		VALUES("+ (key2+2) + ", " + (key1+2) + ", '" + Fname + "', '" + Lname + "'," + baseAmt2 +", '"+ taxAmt2+ "'," + SeatID2 + ", false, false);\n" +
                    updatetrip2 + ";\n" ;
                    insert_query += "INSERT INTO \"public\".reservetrip\n"+
                            "VALUES(" + newkey + ", " + (key1+2) + ", 'M');\n ";
                }
                insert_query += "RELEASE my_savepoint;" +
                    "SAVEPOINT my_savepoint;" +
                    "ROLLBACK TO my_savepoint;\n" +
                    "RELEASE my_savepoint;" +
                    "COMMIT;";
                PGConnection.insert_Query(insert_query);
                sleep(50);

            }
        } catch (Exception e) {
                System.out.println("Error creating reservation.Contact Admin");
        }
    }

    private boolean Final_seat_check() {
        long time = (long)(Math.random()*1000);
        sleep(time);
        key1 = PGConnection.Run_Query("SELECT max(id) from \"public\".reservation\n") + 1;
        key2 = PGConnection.Run_Query("SELECT max(id) from \"public\".reservecustomer\n") + 1;
        
        return temp_seatcheck();
    }
    
    private boolean temp_seatcheck() {
       
        int isSeatAvail = PGConnection.Run_Query("SELECT count(*) from (SELECT rc.id FROM reservecustomer RC "+
                            "INNER JOIN reservation R ON (rc.reservid=r.id) " +
                            " WHERE seatnumber="+ SeatID +"  and r.tripid="+ tripId + ") a");
        int isSeatBook = PGConnection.Run_Query("SELECT count(*) from (SELECT rc.id FROM reservecustomer RC "+
                            "INNER JOIN reservation R ON (rc.reservid=r.id) " +
                            " WHERE seatnumber="+ SeatID +"  and r.tripid="+ tripId + " and RC.passfname = '" + Fname +"' and RC.passlname = '" + Lname +"') a");
        
        int isSeatAvail1 = 0;
        int isSeatBook1 = 0;
        int isSeatAvail2 = 0;
        int isSeatBook2 = 0;
        if(stops > 0){
            isSeatAvail1 = PGConnection.Run_Query("SELECT count(*) from (SELECT rc.id FROM reservecustomer RC "+
                            "INNER JOIN reservation R ON (rc.reservid=r.id) " +
                            " WHERE seatnumber="+ SeatID1 +"  and r.tripid="+ tripId1 + ") a");
            isSeatBook1 = PGConnection.Run_Query("SELECT count(*) from (SELECT rc.id FROM reservecustomer RC "+
                            "INNER JOIN reservation R ON (rc.reservid=r.id) " +
                            " WHERE seatnumber="+ SeatID1 +"  and r.tripid="+ tripId1 + " and RC.passfname = '" + Fname +"' and RC.passlname = '" + Lname +"') a");
            if(stops > 1){
                isSeatAvail2 = PGConnection.Run_Query("SELECT count(*) from (SELECT rc.id FROM reservecustomer RC "+
                            "INNER JOIN reservation R ON (rc.reservid=r.id) " +
                            " WHERE seatnumber="+ SeatID2 +"  and r.tripid="+ tripId2 + ") a");
                isSeatBook2 = PGConnection.Run_Query("SELECT count(*) from (SELECT rc.id FROM reservecustomer RC "+
                            "INNER JOIN reservation R ON (rc.reservid=r.id) " +
                            " WHERE seatnumber="+ SeatID2 +"  and r.tripid="+ tripId2 + " and RC.passfname = '" + Fname +"' and RC.passlname = '" + Lname +"') a");
            }
        }
         if(isSeatAvail==0 && isSeatAvail1==0 && isSeatAvail2==0){
            return true;
         }
        else{
             String temp_name ="";
            if(isSeatBook == 0){
                temp_name = threadName + "\n" + flightName + " - " + travel_date + " - " + Class + "-" + AisleNo + SeatNo;
                System.out.println(temp_name + ": Seat has been taken. Please try again!");
            }
            if(stops > 0){
                if(isSeatBook1 == 0){
                    temp_name = threadName + "\n" + flightName1 + " - " + travel_date + " - " + Class1 + "-" + AisleNo1 + SeatNo1;
                    System.out.println(temp_name + ": Seat has been taken. Please try again!");
                }
                if(stops > 1)
                    if(isSeatBook2 == 0){
                        temp_name = threadName + "\n" + flightName2 + " - " + travel_date + " - " + Class2 + "-" + AisleNo2 + SeatNo2;
                        System.out.println(temp_name + ": Seat has been taken. Please try again!");
                    }
            }
            if(stops == 0)
                if(isSeatBook != 0)
                    System.out.println(threadName + ": Seat has been successfully booked!\n");
            if(stops == 1)
                if(isSeatBook != 0 && isSeatBook1 != 0)
                    System.out.println(threadName + "Seats have been successfully booked!\n");
            if(stops == 2)
                if(isSeatBook != 0 && isSeatBook1 != 0 && isSeatBook2 != 0)
                    System.out.println(threadName + "Seats have been successfully booked!\n");
            return false;
        }
    }

    public static Object[] GetVars(String flightname, String basepriceQry, String paymentmethod, int custid, int mile) {
        int startid = 0, destid = 0, paymentid =0;
        double base = 0, tax = 0;
        try {
            String query = "SELECT startcityid FROM \"public\".scheduledflights\nWHERE name = '" + flightname + "'";
            startid = PGConnection.Run_Query(query);
            //sleep(50);
            query = "SELECT destcityid FROM \"public\".scheduledflights\nWHERE name = '" + flightname + "'";
            destid = PGConnection.Run_Query(query);
            //sleep(50);
            query = "SELECT id FROM \"public\".paymodes\nWHERE name = '" + paymentmethod + "'";
            paymentid = PGConnection.Run_Query(query);
            //sleep(50);

            double[] prices = CalcCost(destid, basepriceQry, mile);
            base = prices[0];
            tax = prices[1];

            } 
        catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("Error getting customer details. Please try again");
        }
        
        return new Object[]{startid,destid,paymentid,base,tax};
    }
    
    private static double[] CalcCost(int dairportid, String basepriceQry, int mile){
        double base =0,tax =0;
    	try {
            int stateID = -1;
            int countryID = -1;

            String countryQry = "SELECT countrycode FROM \"public\".countrycity \nWHERE citycode = " + dairportid;
            countryID = PGConnection.Run_Query(countryQry);

            String stateQry = "SELECT statecode FROM \"public\".statecity \nWHERE citycode = " + dairportid ;
            stateID = PGConnection.Run_Query(stateQry);

            String basetaxQryr = "";
            String localtaxQry="";

            if(stateID>0 && countryID==0){
                    basetaxQryr = basepriceQry + "  stateid=" + stateID;
                    localtaxQry = "SELECT SUM(tax) FROM localtaxes WHERE stateID =" +  stateID + " Group By " + stateID;
            }
            else if(stateID==0 && countryID>0){
                    basetaxQryr = basepriceQry + "  countryid=" + countryID;
                    localtaxQry = "SELECT SUM(tax) FROM localtaxes WHERE countryID =" +  countryID + " Group By " + countryID;
            }
            else if((stateID>0 && countryID>0)||(stateID==0 && countryID==0)){
                    System.out.println("");
            }

            double basePrice = (double)PGConnection.Run_Query(basetaxQryr);
            base = mile * (basePrice/1000);

            tax = (double)PGConnection.Run_Query(basetaxQryr);
        } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("Error Calculating the price. Contact Admin." + e);
        }
        return new double[]{base,tax};
    }

    private boolean seats_check() {
        String temp_name;
        boolean avail = false;
        String totalSeatqry = "";
        String bookedSeatsqry = "";
        int totalSeats = 0;
        int bookedSeats = 0;
        if(Class.equals("E")){
            totalSeatqry = "SELECT ecoclassseats FROM carrierinventory  WHERE Id=" + carrierID;
            bookedSeatsqry = "SELECT ecseatbooked FROM trip WHERE flightid=" +flightID+ "  AND startdate ='"+ travel_date+"'  and status=1";
            updatetrip = "UPDATE trip SET ecseatbooked = ecseatbooked + 1 WHERE flightid = " + flightID ;
            basePriceqry = "SELECT ecbp FROM baseprice WHERE isactive='true' AND ";
        }else if(Class.equals("B")){
            totalSeatqry = "SELECT businessseats FROM carrierinventory  WHERE Id=" + carrierID;
            bookedSeatsqry = "SELECT bcseatbooked FROM trip WHERE flightid=" +flightID+ "  AND startdate ='"+ travel_date+"'  and status=1";
            updatetrip = "UPDATE trip SET bcseatbooked = bcseatbooked + 1 WHERE flightid = " + flightID ;
            basePriceqry = "SELECT bsbp FROM baseprice WHERE isactive='true' AND ";
        }else if(Class.equals("F")){
            totalSeatqry = "SELECT firstclaseats FROM carrierinventory  WHERE Id=" + carrierID;
            bookedSeatsqry = "SELECT fcseatbooked FROM trip WHERE flightid=" +flightID+ "  AND startdate ='"+ travel_date+"'  and status=1";
            updatetrip = "UPDATE trip SET fcseatbooked = fcseatbooked + 1 WHERE flightid = " + flightID ;
            basePriceqry = "SELECT fcbp FROM baseprice WHERE isactive='true' AND ";
        }

        totalSeats = PGConnection.Run_Query(totalSeatqry);
        sleep(50);
        bookedSeats = PGConnection.Run_Query(bookedSeatsqry);
        sleep(50);
        temp_name = threadName + "\n" + flightName + " - " + travel_date + " - " + Class + "-" + AisleNo + SeatNo;
        if(bookedSeats < totalSeats){
            avail = true;
            String getSeatIDqry =  "SELECT SY.id as SeatID FROM seatarray sy INNER JOIN seataisle sa ON sy.aisleid=sa.id "+
                                    " INNER JOIN seatnumber sn ON sn.id = sy.seatid "+ 
                                    " WHERE SA.aislecode='" + AisleNo +"' AND SN.seatcode=" + SeatNo + " AND SY.carrierid=" + carrierID +"  AND category='"+ Class +"'";
            SeatID=PGConnection.Run_Query(getSeatIDqry);
            
            if(SeatID == 0)
            {
                System.out.println(temp_name + ": Invalid Seat Number!!");
                return false;
            }
            sleep(50);

            String isSeatAvailqry = "SELECT count(*) from (SELECT rc.id FROM reservecustomer RC "+
                                 "INNER JOIN reservation R ON (rc.reservid=r.id) " +
                                 " WHERE seatnumber="+ SeatID +"  and r.tripid="+ tripId + ") a";
            int isSeatAvail = PGConnection.Run_Query(isSeatAvailqry);
            if(isSeatAvail != 0){
                System.out.println(temp_name + ": Seat not Available. Choose another seat and try again!");  
                return false;
            }
            sleep(50);
        }
        else if(bookedSeats >= totalSeats){
        
            totalSeatqry = "SELECT ecoclassseats + businessseats + firstclaseats FROM carrierinventory  WHERE Id=" + carrierID;
            bookedSeatsqry = "SELECT ecseatbooked + fcseatbooked + bcseatbooked FROM trip WHERE flightid=" +flightID+ "  AND startdate ='"+ travel_date+"'  and status=1";

            totalSeats = PGConnection.Run_Query(totalSeatqry);
            sleep(50);
            bookedSeats = PGConnection.Run_Query(bookedSeatsqry);
            sleep(50);

            if(totalSeats > bookedSeats)
                System.out.println(temp_name +": " + seat_class + " class seats are full. Try again with another class");
            else
                System.out.println(temp_name +": Flight is full.");
            return false;
        }
        
        if(stops > 0){
            if(Class1.equals("E")){
                totalSeatqry = "SELECT ecoclassseats FROM carrierinventory  WHERE Id=" + carrierID1;
                bookedSeatsqry = "SELECT ecseatbooked FROM trip WHERE flightid=" +flightID1+ "  AND startdate ='"+ travel_date+"'  and status=1";
                updatetrip1 = "UPDATE trip SET ecseatbooked = ecseatbooked + 1 WHERE flightid = " + flightID1 ;
                basePriceqry1 = "SELECT ecbp FROM baseprice WHERE isactive='true' AND ";
            }else if(Class1.equals("B")){
                totalSeatqry = "SELECT businessseats FROM carrierinventory  WHERE Id=" + carrierID1;
                bookedSeatsqry = "SELECT bcseatbooked FROM trip WHERE flightid=" +flightID1+ "  AND startdate ='"+ travel_date+"'  and status=1";
                updatetrip1 = "UPDATE trip SET bcseatbooked = bcseatbooked + 1 WHERE flightid = " + flightID1 ;
                basePriceqry1 = "SELECT bsbp FROM baseprice WHERE isactive='true' AND ";
            }else if(Class1.equals("F")){
                totalSeatqry = "SELECT firstclaseats FROM carrierinventory  WHERE Id=" + carrierID1;
                bookedSeatsqry = "SELECT fcseatbooked FROM trip WHERE flightid=" +flightID1+ "  AND startdate ='"+ travel_date+"'  and status=1";
                updatetrip1 = "UPDATE trip SET fcseatbooked = fcseatbooked + 1 WHERE flightid = " + flightID1 ;
                basePriceqry1 = "SELECT fcbp FROM baseprice WHERE isactive='true' AND ";
            }

            totalSeats = PGConnection.Run_Query(totalSeatqry);
            sleep(50);
            bookedSeats = PGConnection.Run_Query(bookedSeatsqry);
            sleep(50);
            temp_name = threadName + "\n" + flightName1 + " - " + travel_date + " - " + Class1 + "-" + AisleNo1 + SeatNo1;
            if(bookedSeats < totalSeats){
                String getSeatIDqry =  "SELECT SY.id as SeatID FROM seatarray sy INNER JOIN seataisle sa ON sy.aisleid=sa.id "+
                                        " INNER JOIN seatnumber sn ON sn.id = sy.seatid "+ 
                                        " WHERE SA.aislecode='" + AisleNo1 +"' AND SN.seatcode=" + SeatNo1 + " AND SY.carrierid=" + carrierID1 +"  AND category='"+ Class1 +"'";
                SeatID1=PGConnection.Run_Query(getSeatIDqry);
                
                if(SeatID1 == 0)
                {
                    System.out.println(temp_name + ": Invalid Seat Number!!");
                    return false;
                }
                sleep(50);

                String isSeatAvailqry = "SELECT count(*) from (SELECT rc.id FROM reservecustomer RC "+
                                     "INNER JOIN reservation R ON (rc.reservid=r.id) " +
                                     " WHERE seatnumber="+ SeatID1 +"  and r.tripid="+ tripId1 + ") a";
                int isSeatAvail = PGConnection.Run_Query(isSeatAvailqry);
                if(isSeatAvail != 0){
                    System.out.println(temp_name + ": Seat not Available. Choose another seat and try again!");   
                    return false;
                }
                sleep(50);
            }
            else if(bookedSeats >= totalSeats){

                totalSeatqry = "SELECT ecoclassseats + businessseats + firstclaseats FROM carrierinventory  WHERE Id=" + carrierID1;
                bookedSeatsqry = "SELECT ecseatbooked + fcseatbooked + bcseatbooked FROM trip WHERE flightid=" +flightID1+ "  AND startdate ='"+ travel_date+"'  and status=1";

                totalSeats = PGConnection.Run_Query(totalSeatqry);
                sleep(50);
                bookedSeats = PGConnection.Run_Query(bookedSeatsqry);
                sleep(50);

                if(totalSeats > bookedSeats)
                    System.out.println(temp_name +": " + seat_class1 + " class seats are full. Try again with another class");
                else
                    System.out.println(temp_name +": Flight is full.");
                return false;
            }

            if(stops > 1){
                if(Class2.equals("E")){
                    totalSeatqry = "SELECT ecoclassseats FROM carrierinventory  WHERE Id=" + carrierID2;
                    bookedSeatsqry = "SELECT ecseatbooked FROM trip WHERE flightid=" +flightID2+ "  AND startdate ='"+ travel_date+"'  and status=1";
                    updatetrip2 = "UPDATE trip SET ecseatbooked = ecseatbooked + 1 WHERE flightid = " + flightID2 ;
                    basePriceqry2 = "SELECT ecbp FROM baseprice WHERE isactive='true' AND ";
                }else if(Class2.equals("B")){
                    totalSeatqry = "SELECT businessseats FROM carrierinventory  WHERE Id=" + carrierID2;
                    bookedSeatsqry = "SELECT bcseatbooked FROM trip WHERE flightid=" +flightID2+ "  AND startdate ='"+ travel_date+"'  and status=1";
                    updatetrip2 = "UPDATE trip SET bcseatbooked = bcseatbooked + 1 WHERE flightid = " + flightID2 ;
                    basePriceqry2 = "SELECT bsbp FROM baseprice WHERE isactive='true' AND ";
                }else if(Class2.equals("F")){
                    totalSeatqry = "SELECT firstclaseats FROM carrierinventory  WHERE Id=" + carrierID2;
                    bookedSeatsqry = "SELECT fcseatbooked FROM trip WHERE flightid=" +flightID2+ "  AND startdate ='"+ travel_date+"'  and status=1";
                    updatetrip2 = "UPDATE trip SET fcseatbooked = fcseatbooked + 1 WHERE flightid = " + flightID2 ;
                    basePriceqry2 = "SELECT fcbp FROM baseprice WHERE isactive='true' AND ";
                }

                totalSeats = PGConnection.Run_Query(totalSeatqry);
                sleep(50);
                bookedSeats = PGConnection.Run_Query(bookedSeatsqry);
                sleep(50);
                temp_name = threadName + "\n" + flightName2 + " - " + travel_date + " - " + Class2 + "-" + AisleNo2 + SeatNo2;
                if(bookedSeats < totalSeats){
                    String getSeatIDqry =  "SELECT SY.id as SeatID FROM seatarray sy INNER JOIN seataisle sa ON sy.aisleid=sa.id "+
                                        " INNER JOIN seatnumber sn ON sn.id = sy.seatid "+ 
                                        " WHERE SA.aislecode='" + AisleNo2 +"' AND SN.seatcode=" + SeatNo2 + " AND SY.carrierid=" + carrierID2 +"  AND category='"+ Class2 +"'";
                    SeatID2=PGConnection.Run_Query(getSeatIDqry);

                    if(SeatID2 == 0)
                    {
                        System.out.println(temp_name + ": Invalid Seat Number!!");
                        return false;
                    }
                    sleep(50);

                    String isSeatAvailqry = "SELECT count(*) from (SELECT rc.id FROM reservecustomer RC "+
                                         "INNER JOIN reservation R ON (rc.reservid=r.id) " +
                                         " WHERE seatnumber="+ SeatID2 +"  and r.tripid="+ tripId2 + ") a";
                    int isSeatAvail = PGConnection.Run_Query(isSeatAvailqry);
                    if(isSeatAvail != 0){
                        System.out.println(temp_name + ": Seat not Available. Choose another seat and try again!");   
                        return false;
                    }
                    sleep(50);
                }
                else if(bookedSeats >= totalSeats){
                    totalSeatqry = "SELECT ecoclassseats + businessseats + firstclaseats FROM carrierinventory  WHERE Id=" + carrierID2;
                    bookedSeatsqry = "SELECT ecseatbooked + fcseatbooked + bcseatbooked FROM trip WHERE flightid=" +flightID2+ "  AND startdate ='"+ travel_date+"'  and status=1";

                    totalSeats = PGConnection.Run_Query(totalSeatqry);
                    sleep(50);
                    bookedSeats = PGConnection.Run_Query(bookedSeatsqry);
                    sleep(50);

                    if(totalSeats > bookedSeats)
                        System.out.println(temp_name +": " + seat_class2 + " class seats are full. Try again with another class");
                    else
                        System.out.println(temp_name +": Flight is full.");
                    return false;
                }
            }
        }
        return true;
    }
    
}
