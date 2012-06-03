package com.asiacell.billing.SmsNotifier;

/* SmsNotifier: the purpose of this application is to send
 * a SMS notification to the postpaid subscribers to inform them
 * that the Bill for the current month has been issued
 *
 * Auhtor: Hasanein Ali
 * Dept. : Core Network Group / Billing System
 */
import java.sql.*;
import java.util.Date;
import java.io.*;
//import javax.swing.JOptionPane;
class OracleConnector
{
    public java.sql.Connection connectMe(String username,String password,String server,String dbName)
        throws ClassNotFoundException, SQLException
    {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //String URL = "jdbc:oracle:thin:@//localhost:1521/hasdb";
        String URL = "jdbc:oracle:thin:@//" + server + ":1521/" + dbName;
        //String URL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST="+ server + ")(PORT=1521))(CONNECT_DATA=(SID=" + dbName +")))";
        Connection conn = DriverManager.getConnection(URL,username,password);
        return conn;
    }
}

class MessageCreator
{
    public String createMessage(String msgType)
    {
        if (msgType.equals("Arabic"))
        {
            char word_01[] = { '\u064A','\u0631','\u062C','\u0649' };
            char word_02[] = { '\u0645','\u0631','\u0627','\u062C','\u0639','\u0629' };            
            char word_03[] = { '\u0645','\u0631','\u0627','\u0643','\u0632','\u0646','\u0627' };            
            char word_04[] = { '\u0644','\u0623','\u0633','\u062A','\u0644','\u0627','\u0645' };
            char word_05[] = { '\u0641','\u0627','\u062A','\u0648','\u0631','\u0629' };
            char word_06[] = { '\u0647','\u0630','\u0627' };
            char word_07[] = { '\u0623','\u0644','\u0634','\u0647','\u0631' };
            char word_08[] = { '\u0648' };
            char word_09[] = { '\u0623','\u0644','\u062F','\u0641','\u0639' };
            char word_10[] = { '\u062E','\u0644','\u0627','\u0644' };
            char word_11[] = { '\u064A','\u0648','\u0645' };
            String word01 = new String(word_01);
            String word02 = new String(word_02);
            String word03 = new String(word_03);
            String word04 = new String(word_04);
            String word05 = new String(word_05);
            String word06 = new String(word_06);
            String word07 = new String(word_07);
            String word08 = new String(word_08);
            String word09 = new String(word_09);
            String word10 = new String(word_10);
            String word11 = new String(word_11);
          	String theMessage = word01 + " " + word02 + " " + word03 + " " + word04 + " " + word05 + " " + word06 + " " + word07 + " " + word08 + " " + word09 + " " + word10 + " 15 " + word11;
            return theMessage;
        }
        if (msgType.equals("Kurdish"))
        {
            char word_01[] = { '\u062A','\u0643','\u0627','\u064A','\u0629' };
            char word_02[] = { '\u0633','\u0629','\u0631','\u062F','\u0627','\u0646','\u064A' };            
            char word_03[] = { '\u0633','\u0629','\u0646','\u062A','\u0629','\u0631','\u0629','\u0643','\u0629','\u0645','\u0627','\u0646' };            
            char word_04[] = { '\u0628','\u0643','\u0629' };
            char word_05[] = { '\u0628','\u0648' };
            char word_06[] = { '\u0648','\u0629','\u0631','\u0643','\u0631','\u062A','\u0646','\u0649' };
            char word_07[] = { '\u0628','\u0633','\u0648','\u0644','\u0629' };
            char word_08[] = { '\u0648' };
            char word_09[] = { '\u0628','\u0627','\u0631','\u0629','\u062F','\u0627','\u0646' };
            char word_10[] = { '\u0644','\u0629' };
            char word_11[] = { '\u0631','\u0624','\u0632','\u062F','\u0627' };
            String word01 = new String(word_01);
            String word02 = new String(word_02);
            String word03 = new String(word_03);
            String word04 = new String(word_04);
            String word05 = new String(word_05);
            String word06 = new String(word_06);
            String word07 = new String(word_07);
            String word08 = new String(word_08);
            String word09 = new String(word_09);
            String word10 = new String(word_10);
            String word11 = new String(word_11);
          	String theMessage = word01 + " " + word02 + " " + word03 + " " + word04 + " " + word05 + " " + word06 + " " + word07 + " " + word08 + " " + word09 + " " + word10 + " 15 " + word11;
          	//JOptionPane.showMessageDialog(null,theMessage);
            return theMessage;
        }
        else if (msgType.equals("English"))
        {
            String stringMsg = "Dear subscriber, Please visit Asiacell shop to receive this month bill and to pay within 15 days";
            return stringMsg;
        }
        else
        {
        	System.out.println("Error: you should specify the message type that you want to create");
        	System.out.println("Usage: MessageCreator.createMessage('Arabic'|'Kurdish'|'English');");
        	return null;
        }
    }
}

public class SmsNotifier
{
    public static void main(String args[])
    {
        try
        {           		
            Date currentDate = new Date();
            String logFileName = "SmsNotifier_" + currentDate.toString().substring(8,10) + currentDate.toString().substring(4,7) + currentDate.toString().substring(24,28) + ".log";//this filename will be used as the logfile name
            FileWriter logFile = new FileWriter("/Scripts/JavaApps/log/" + logFileName);
            System.out.println(logFileName);
            OracleConnector sendSmsConnector = new OracleConnector();//connection to sendsms
            OracleConnector bossConnector = new OracleConnector();//connection to BOSS1
            Connection mySendSmsConn = sendSmsConnector.connectMe("USRHASANEIN","billing","192.168.165.36","sendsms");
            System.out.println();
            System.out.println("connected to Sendsms");
            Connection myBossConn = bossConnector.connectMe("ccare","acccare","192.168.117.12","BOSS1");
            System.out.println("connected to BOSS");
            Statement retrieveMsisdns  = myBossConn.createStatement();
            ResultSet msisdns = retrieveMsisdns.executeQuery("Select Distinct (msisdn),t.local_id From inf_subscriber_all t,inf_acct v,inf_acct_relation r Where sub_state In ('B01','B04') And v.acct_id = r.acct_id And r.sub_id = t.sub_id And v.bill_cycle_type='01' and t.dun_flag = 1 and msisdn='07701530001' Order By msisdn");
            MessageCreator msg = new MessageCreator();
            String sql = "{call sendsms.send_single_sms(?,?,?,?,?,?,?,?,?)}";
            CallableStatement sendSingleSms = mySendSmsConn.prepareCall(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            sendSingleSms.setString(1,"Asiacell");
            //sendSingleSms.setString(3,msg.createMessage("Arabic"));
            sendSingleSms.setInt(4,0);
            sendSingleSms.setInt(5,1);
            sendSingleSms.setInt(6,5000);
            sendSingleSms.setString(7,"BS");
            sendSingleSms.registerOutParameter(8,java.sql.Types.VARCHAR,100);
            sendSingleSms.registerOutParameter(9,java.sql.Types.INTEGER);
            while (msisdns.next())
            {
                //sendSingleSms.setString(2,msisdns.getString(1));                
                sendSingleSms.setString(2,"9647701103622");
                if (msisdns.getString(2).equals("SULY"))
                {
                	sendSingleSms.setString(3,msg.createMessage("Kurdish"));
                	sendSingleSms.execute();
                	logFile.write(currentDate.toString() + " >> " + "Kurdish Notification Message Sent to " + msisdns.getString(1) + " ( " + msisdns.getString(2) + " Subscriber )" + '\n');                	
                	sendSingleSms.setString(3,msg.createMessage("English"));
                	sendSingleSms.execute();
                	logFile.write(currentDate.toString() + " >> " + "English Notification Message Sent to " + msisdns.getString(1) + " ( " + msisdns.getString(2) + " Subscriber )" + '\n');                	                	
                	sendSingleSms.setString(3,msg.createMessage("Arabic"));
                	sendSingleSms.execute();                	
                	logFile.write(currentDate.toString() + " >> " + "Arabic Notification Message Sent to " + msisdns.getString(1) + " ( " + msisdns.getString(2) + " Subscriber )" + '\n');
                }
                else if (msisdns.getString(2).equals("KIRKUK"))
                {
                	sendSingleSms.setString(3,msg.createMessage("Arabic"));
                	sendSingleSms.execute();                	
                	logFile.write(currentDate.toString() + " >> " + "Arabic Notification Message Sent to " + msisdns.getString(1) + " ( " + msisdns.getString(2) + " Subscriber )" + '\n');
                	sendSingleSms.setString(3,msg.createMessage("Kurdish"));
                	sendSingleSms.execute();                	
                	logFile.write(currentDate.toString() + " >> " + "Kurdish Notification Message Sent to " + msisdns.getString(1) + " ( " + msisdns.getString(2) + " Subscriber )" + '\n');
                	sendSingleSms.setString(3,msg.createMessage("English"));
                	sendSingleSms.execute();                	
                	logFile.write(currentDate.toString() + " >> " + "English Notification Message Sent to " + msisdns.getString(1) + " ( " + msisdns.getString(2) + " Subscriber )" + '\n');
                }
                else
                {
                	sendSingleSms.setString(3,msg.createMessage("Arabic"));
                	sendSingleSms.execute();
                	logFile.write(currentDate.toString() + " >> " + "Arabic Notification Message Sent to " + msisdns.getString(1) + " ( " + msisdns.getString(2) + " Subscriber )" + '\n');
                	sendSingleSms.setString(3,msg.createMessage("English"));
                	sendSingleSms.execute();
                	logFile.write(currentDate.toString() + " >> " + "English Notification Message Sent to " + msisdns.getString(1) + " ( " + msisdns.getString(2) + " Subscriber )" + '\n');
                }                                
            }
            logFile.close();
        }
        catch (Exception e)
        {
            System.out.println("An error has occurred while trying to connect to the database");
            System.out.println(e.toString());
        }
    }
}