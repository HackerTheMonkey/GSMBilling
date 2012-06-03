package com.asiacell.billing.prm.idd;

// the application finshed updating 10,000 rates in 8,35 Min.
import java.sql.*;
import javax.sql.*;
import oracle.jdbc.driver.*;
import oracle.jdbc.pool.*;
import java.util.Date;
public class ReRator {
    // Instance Variables Declaration
    private static dbConnection myConn=null;
    private static RateFinder ratef = null;
    private static String called = "";
    private static String called_section_code = "";
    private static double timelong = 0.00;
    private static double charge_rate = 0.00;
    private static double toll_fee = 0.00;
    private static String trunkout_manager = "";
    private static  int i=0;
    
    // Main Method (program entry point)
    public static void main(String args[])
    throws Exception {
        //Local Variables Declaration
        Date currentDate = new Date();
        String URL = "jdbc:oracle:thin:@//192.168.117.12:1521/boss1";
        String user="prm";
        String pass="acprm";
        int count = 0;
        
        myConn = new dbConnection(URL,user,pass);
        ratef= new RateFinder(myConn.getConnection());
        Statement stmtGetCalled=(Statement) myConn.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE,ResultSet.CLOSE_CURSORS_AT_COMMIT);
        //ResultSet getCalledNoRS =(ResultSet) stmtGetCalled.executeQuery("select called,called_section_code,timelong,charge_rate,toll_fee,trunkout_manager from" + args[0] + "a where a.trunkout_manager in ('INCLA','DECOV','GLOBE') and to_char(a.start_time,'yyyymm')='200709' and a.toll_type='VOICE'");        
        ResultSet getCalledNoRS =(ResultSet) stmtGetCalled.executeQuery("select called,called_section_code,timelong,charge_rate,toll_fee,trunkout_manager from " + args[0]) ;
        
        // Main Method Body
        System.out.println(currentDate);
        currentDate = null;
//        for (int h=0;h<699173;h++)
//            getCalledNoRS.next();
        while (getCalledNoRS.next()) {
            called = getCalledNoRS.getString("called");
            timelong = getCalledNoRS.getDouble("timelong");
            trunkout_manager = getCalledNoRS.getString("trunkout_manager");
            
            String result[] = ratef.getRate(called,trunkout_manager,"1-OCT-2007");
            
            charge_rate = Double.parseDouble(result[0].trim());
            called_section_code = result[1].trim();
            
            toll_fee = TollFeeCalculator.calTollFee(timelong,charge_rate);
            
            getCalledNoRS.updateDouble("charge_rate",charge_rate);
            getCalledNoRS.updateString("called_section_code",called_section_code);
            getCalledNoRS.updateDouble("toll_fee",toll_fee);
            getCalledNoRS.updateRow();
            //System.out.println(charge_rate + ":" + called_section_code + ":" + toll_fee);
            myConn.getConnection().commit();
            
            System.gc();            
            System.out.println(++count + ":" + Runtime.getRuntime().totalMemory()/1024/1024 + ":" + Runtime.getRuntime().freeMemory()/1024/1024);
        }
        
        getCalledNoRS.close();
        stmtGetCalled.close();
        getCalledNoRS=null;
        stmtGetCalled=null;
        currentDate = new Date();
        
        System.out.println(currentDate);
        
    }
}