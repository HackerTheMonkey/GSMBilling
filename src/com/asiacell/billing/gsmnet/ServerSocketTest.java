package com.asiacell.billing.gsmnet;
import java.net.*;
import java.io.*;
public class ServerSocketTest
{
    static ServerSocket socket1;
    static int port = 19999;
    static Socket connection;
    static boolean first;
    static StringBuffer process;
    static String TimeStamp;
    
    public static void main(String args[])
            throws Exception
    {
        try
        {
            socket1 = new ServerSocket(port);
            System.out.println("SingleServerSocket initialized");
            while(true)
            {
                connection = socket1.accept();
                // reading the input stream from the socket client
                BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
                InputStreamReader isr = new InputStreamReader(is,"US-ASCII");
                process = new StringBuffer();
                int c;
                while ((c = isr.read()) != 13)
                {
                    process.append((char) c);
                }
                System.out.println(process);
                // process the recieved data
                Thread.sleep(1000);
                // Write data to the client socket
                TimeStamp = new java.util.Date().toString();
                //String returnCode = "Operation is Successful;" + "\n";
                String returnCode = "+++     HLR9820        2003-11-21 08:54:14" + "\n" + "SMU    #000324" + "\n" + "SENDSMS:ISDN=9647701105701,TEXT=\"HELLO WORLD\";" + "\n" + "SUCCESS0001: Operation is successful" + "\n" + "There is together 1 report" + "\n" + "---    END" + "\n";
//                BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
//                OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
                OutputStream osw = connection.getOutputStream();
                osw.write(returnCode.getBytes());
                //osw.write(returnCode);    
                osw.flush();
            }
        }
        catch (Exception e)
        {
            System.out.println("Error Happened");
            System.out.println(e.getMessage());
        }
    }
}