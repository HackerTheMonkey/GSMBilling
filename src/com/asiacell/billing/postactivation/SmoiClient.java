package com.asiacell.billing.postactivation;
import java.net.*;
import java.io.*;
public class SmoiClient
{
    public static void main(String args[])
            throws Exception
    {
        String host = "10.76.105.40";
        int port =  19999;
        String username = "billing";
        String password = "123456";
        System.out.println("Socket Client Initialized");
        try
        {
            // open the socket with a server
            InetAddress address = InetAddress.getByName(host);
            Socket connection = new Socket(address, port);
            // Write to the server
            BufferedOutputStream bos =new BufferedOutputStream(connection.getOutputStream());
            OutputStreamWriter osw = new OutputStreamWriter(bos,"US-ASCII");

            // Composing the SMP message
            String messageStartFlag = "`SC`";
            String messageLength = "005C";
            // Compile the messageHeader
            String versionNumber = "1.00";
            String terminalIdentifier = "internal";
            String serviceName = "PPS" + " ";
            String messageHeader = versionNumber + terminalIdentifier + serviceName;
            // Compile the sessionHeader
            String sessionID = "00001322";
            String sessionControlCharacter = "DLGCON";
            String reservedField = "00000087";
            String sessionHeader = sessionID + sessionControlCharacter + reservedField;
            // Compile the transactionHeader
            String transactionID = "29C3";
            String transactionControlCharacter = "TXBEG ";
            String tranReservedField = "0000";
            String transactionHeader = transactionID + transactionControlCharacter + tranReservedField;
            // rest of the fields
            String mmlCommand = "DISP PPS ACNTINFO:MSISDN=7701105701" + " ";
            String msgCheckSum = "BBAFCCC5";
            // Compile the final SMP Message
            //String smpMessage = messageStartFlag + messageLength + messageHeader + sessionHeader + transactionHeader + mmlCommand + msgCheckSum;
            String smpMessage = "`SC`00581.00internalSRVM    00001322DLGLGN000000943BFBTXBEG 0000LOGIN:USER=billing,PSWD=billing 81EDD9DF";
            byte smpMessageByte[] = "0".getBytes();
            StringBuilder binary = new StringBuilder();
            for (int j = 0 ; j < smpMessageByte.length ; j++)
            {
            	System.out.print(smpMessageByte[j] + ":");
            	int val = smpMessageByte[j];
            	for (int i = 0 ; i < 8 ; i++)
            	{
            		binary.append((val & 128) == 0 ? 0 : 1);
            		val <<= 1;
            	}
            }
            System.out.print("smpMessage: " + binary + ":");
            System.out.println("smpMessage: " + binary.length());
            osw.write(smpMessage + ";");
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
                if (line.endsWith(";"))
                {
                	break;
                }
            }
            // Send a query command to the HLR SMU Server
            connection.close();
        }
        catch (Exception e)
        {
            System.out.println("Error Happened");
            System.out.println(e.getMessage());
        }
    }
}