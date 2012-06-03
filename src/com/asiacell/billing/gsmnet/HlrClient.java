package com.asiacell.billing.gsmnet;
import java.net.*;
import java.io.*;
public class HlrClient
{
    public static void main(String args[])
            throws Exception
    {
        String host = "192.168.105.11";
        int port =  7777;
        String username = "BILLTEST";
        String password = "BILLTEST";
        System.out.println("Socket Client Initialized");
        try
        {
            // open the socket with a server
            InetAddress address = InetAddress.getByName(host);
            Socket connection = new Socket(address, port);
            // Write to the server
            BufferedOutputStream bos =new BufferedOutputStream(connection.getOutputStream());
            OutputStreamWriter osw = new OutputStreamWriter(bos,"US-ASCII");
            String process = "LGI:OPNAME=\"" + username + "\",PWD=\"" + password + "\";";
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
            // Send a query command to the HLR SMU Server
            for (long i = 7701180008L ; i < 7701180500L ; i++)
            {
                BufferedOutputStream bos1 =new BufferedOutputStream(connection.getOutputStream());
                OutputStreamWriter osw1 = new OutputStreamWriter(bos1,"US-ASCII");
                String msisdn = "964" + i;
                String process1 = "LST IMSI:ISDN=\"" + msisdn + "\";";
                osw.write(process1);
	            osw.flush();
	            // Read from the server
	            BufferedInputStream bis1 = new BufferedInputStream(connection.getInputStream());
	            InputStreamReader isr1 = new InputStreamReader(bis1, "US-ASCII");
	            int c1;
	            BufferedReader br1 = new BufferedReader(isr1);
	            String line1 = "";
	            while (true)
	            {
	                line1 = br1.readLine();
	                System.out.println(line1 + "\n");
	                if (line1.startsWith("---"))
	                    break;
	            }
			}
            connection.close();
        }
        catch (Exception e)
        {
            System.out.println("Error Happened");
            System.out.println(e.getMessage());
        }
    }
}