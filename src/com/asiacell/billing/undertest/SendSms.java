package com.asiacell.billing.undertest;

// Import the required SMPP class libraries         
import com.logica.smpp.*;
import com.logica.smpp.TCPIPConnection;
import com.logica.smpp.pdu.*;
import com.logica.smpp.pdu.Unbind;
import com.logica.smpp.debug.Debug;
import com.logica.smpp.debug.Event;
import com.logica.smpp.debug.FileDebug;
import com.logica.smpp.debug.FileEvent;
import com.logica.smpp.util.Queue;

/** This Application is a test application and it will be used 
 * to send a single SMS via the SMSC-GW by implementing
 * the SMPP connection to it.
 */
public class SendSms
{            
    /**
     * This is the starting point (program entry point) for the SendSMS application
     * and it will call the sendsms method along with it's appropriate
     * arguments to send a single SMS via the SMSC-GW
     */
    public static void main(String args[])
        throws Exception
    {
        SendSms smsSender = new SendSms();
        smsSender.sendSingleSMS("billing","1234","10.73.100.27",5016,"Asiacell","96477701105701","Just For Test");        
    }
    
    /**
     * To establish a SMPP connection with the SMSC-GW and send a short message
     */
    public void sendSingleSMS(String smscUsername,String password,String smscIPAddress,int port,String sourceAddress,String destAddress,String msgText)
        throws Exception
    {
        Connection smppConn = new TCPIPConnection(smscIPAddress,port);
        Session smppSession = new Session(smppConn);
        BindRequest boundReq = new BindTransmitter();
        boundReq.setSystemId(smscUsername);
        boundReq.setPassword(password);
        Response boundRes = smppSession.bind(boundReq);
        
        if (boundRes.getCommandStatus() == Data.ESME_ROK)
        {
            SubmitSM msg = new SubmitSM();
            msg.setSourceAddr(sourceAddress);
            msg.setDestAddr(destAddress);
            msg.setShortMessage(msgText);
            SubmitSMResp submitResp = smppSession.submit(msg);
            if (submitResp.getCommandStatus() == Data.ESME_ROK)
            {
                System.out.println("Message Submission Success");
            }
            else
                System.out.println("Message Sending Failure, Status=" + submitResp.getCommandStatus());
            smppSession.unbind();
        }
        else
        { 
            System.out.println("Can not bound to the SMSC-GW, Status=" + boundRes.getCommandStatus());
        }
    }
}