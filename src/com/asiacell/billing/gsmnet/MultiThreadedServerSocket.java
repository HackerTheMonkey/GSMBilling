/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.asiacell.billing.gsmnet;
import java.net.*;
import java.io.*;

public class MultiThreadedServerSocket implements Runnable
{
    private Socket connection;
    private static int openedSocketsNumber = 0;
    
    public MultiThreadedServerSocket(Socket s,int i) 
    {
        this.connection = s;
        this.openedSocketsNumber = i;
    }
    
    public static void main(String args[])
            throws Exception
    {
        int port = 19999;
        ServerSocket srvSocket = new ServerSocket(port);
        while (true)
        {
            Socket connection = srvSocket.accept();
            openedSocketsNumber ++;            
            Runnable runnable = new MultiThreadedServerSocket(connection, openedSocketsNumber);
            Thread thread = new Thread(runnable);
            thread.start();            
            System.out.println("A thread has been spawned to serve a client connection...");
            System.out.println("The number of opened sockets are: " + openedSocketsNumber);                
        }
        
    }
    
    public void run()
    {        
        try
        {
            // Variables Definition
            InputStream in = null;
            OutputStream out = null;
            String cmdStr_01 = "";
            String cmdStr_02 = "";
            String parameters[] = null;
            String username,password,text,msisdn;
            int c;
            // Create the input stream and the output stream
            in = connection.getInputStream();
            out = connection.getOutputStream();
            // get the command string sent by the client and extracting the parameters
            while ((c = in.read()) != 59)
            {
                cmdStr_01 = cmdStr_01 + String.valueOf((char) c);
            }
            cmdStr_02 = cmdStr_01.replaceFirst("SENDSMS:", "");
            parameters = cmdStr_02.split(",");
            username = parameters[0].replaceFirst("OPNAME=", "").replaceAll("\"", "");
            password = parameters[1].replaceFirst("PWD=", "").replaceAll("\"", "");
            msisdn = parameters[2].replaceFirst("ISDN=", "").replaceAll("\"", "");
            text = parameters[3].replaceFirst("TEXT=", "").replaceAll("\"", "");                    
            // If the client supply the correct username and password, then process the request
            if (username.equals("hasanein") && password.equals("taekwondo"))
            {
                SendSms smsSender = new SendSms();
                //smsSender.sendSingleSMS(username, password,"192.168.117.1",7777,"111",msisdn,text);
                out.write(("Request Completed for " + username + ";").getBytes());
                out.flush();
            }
            else
            {
                out.write(("Error username or password;").getBytes());
                out.flush();                
            }
            connection.close();
            openedSocketsNumber--;
        }
        catch (Exception e) {}
    }    
}
