package com.asiacell.billing.prm.idd;

// the application finshed updating 10,000 rates in 8,35 Min.
import java.sql.*;
import javax.sql.*;
import oracle.jdbc.driver.*;
import oracle.jdbc.pool.*;
import java.util.Date;
public class ReRatorTest {
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
    public void doReRating(String username,String password,String dbName,String hostIP,int portNo,String applyTime,String[] iddPartners,String month)
    throws Exception {
        //Local Variables Declaration
        Date currentDate = new Date();
        String URL = "jdbc:oracle:thin:@//"+ hostIP + ":"+ portNo +"/"+ dbName +"";
        int count = 0;
        String selectedPartners = "";
        
        myConn = new dbConnection(URL,username,password);
        ratef= new RateFinder(myConn.getConnection());
        Statement stmtGetCalled=(Statement) myConn.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE,ResultSet.CLOSE_CURSORS_AT_COMMIT);
        
        for (int j=0;j<iddPartners.length;j++) {
            selectedPartners = selectedPartners + "'" + iddPartners[j] + "',";
        }
        selectedPartners = selectedPartners.substring(0,selectedPartners.length() - 1);
        
        ResultSet getCalledNoRS =(ResultSet) stmtGetCalled.executeQuery("select called,called_section_code,timelong,charge_rate,toll_fee,trunkout_manager from bill_s_idd a where a.trunkout_manager in (" + selectedPartners + ") and to_char(a.start_time,'yyyymm')='" + month + "' and a.toll_type='VOICE'");
        
        // Main Method Body
        System.out.println(currentDate);
        currentDate = null;
        while (getCalledNoRS.next()) {
            called = getCalledNoRS.getString("called");
            timelong = getCalledNoRS.getDouble("timelong");
            trunkout_manager = getCalledNoRS.getString("trunkout_manager");
            
            String result[] = ratef.getRate(called,trunkout_manager,applyTime);
            
            charge_rate = Double.parseDouble(result[0].trim());
            called_section_code = result[1].trim();
            
            toll_fee = TollFeeCalculator.calTollFee(timelong,charge_rate);
            
            getCalledNoRS.updateDouble("charge_rate",charge_rate);
            getCalledNoRS.updateString("called_section_code",called_section_code);
            getCalledNoRS.updateDouble("toll_fee",toll_fee);
            getCalledNoRS.updateRow();
            
            myConn.getConnection().commit();
            
            System.gc();
            //System.out.println(++count + ":" + Runtime.getRuntime().totalMemory()/1024/1024 + ":" + Runtime.getRuntime().freeMemory()/1024/1024);
        }
        
        getCalledNoRS.close();
        stmtGetCalled.close();
        getCalledNoRS=null;
        stmtGetCalled=null;
        currentDate = new Date();
        
        //System.out.println(currentDate);
        
    }
}