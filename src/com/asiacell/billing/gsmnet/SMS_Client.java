/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.asiacell.billing.gsmnet;
import java.io.*;
import java.net.*;
/**
 *
 * @author hasanein
 */
public class SMS_Client 
{
    public static void main(String args[])
    {
        try
        {
            Socket clientSocket = new Socket("localhost", 19999);
            OutputStream out =  clientSocket.getOutputStream();
            InputStream in = clientSocket.getInputStream();
            // Send the command to the server
            out.write("SENDSMS:OPNAME=\"hasanein\",PWD=\"taekwondo\",ISDN=\"9647701105701\",TEXT=\"HELLO WORLD\";".getBytes());
            out.flush();
            // Get the response from the server and display it on the console screen
            int c;
            String msg ="";
            while ((c = in.read()) != 59)
            {
                msg = msg + String.valueOf((char) c);
            }
            System.out.println(msg);
            clientSocket.close();
        }
        catch (Exception e) {}
    }
}
