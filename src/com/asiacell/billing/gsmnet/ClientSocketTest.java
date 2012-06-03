package com.asiacell.billing.gsmnet;
import java.net.*;
import java.io.*;
public class ClientSocketTest
{
    public static void main(String args[])
            throws Exception
    {
        String host = "192.168.105.11";
        int port =  7777;
        StringBuffer instr = new StringBuffer();
        String TimeStamp;
        System.out.println("Socket Client Initialized");
        try
        {
            // open the socket with a server
            InetAddress address = InetAddress.getByName(host);
            Socket connection = new Socket(address, port);
            // Write to the server
            BufferedOutputStream bos =new BufferedOutputStream(connection.getOutputStream());
            OutputStreamWriter osw = new OutputStreamWriter(bos,"US-ASCII");
            TimeStamp = new java.util.Date().toString();
            String process = "LGI:OPNAME=\"BILLTEST\",PWD=\"BILLTEST\";";
            osw.write(process);
            osw.flush();
            // Read from the server
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            InputStreamReader isr = new InputStreamReader(bis, "US-ASCII");            
            int c;                      
            BufferedReader br = new BufferedReader(isr);    
            String line = "";
            while (true)
            {
                line = br.readLine();
                System.out.println(line + "\n");
                if (line.startsWith("---"))
                    break;
            }
//            while ((c = isr.read()) != 68)
//            {
//                instr.append((char) c);
//            }
            // Close the socket connection
            connection.close();
            System.out.println(instr);
        }
        catch (Exception e)
        {
            System.out.println("Error Happened");
            System.out.println(e.getMessage());
        }
    }
}