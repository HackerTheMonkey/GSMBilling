package com.asiacell.billing.postactivation;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class NeConnector
{
    private String hostname;
    private int port;
    private Socket socketConnection;
    private String terminationCharacter;
    private void initComponents(String hostname, int port,String terminationCharacter)
    {
        this.hostname = hostname;
        this.port = port;
        this.terminationCharacter = terminationCharacter;
    }
    public NeConnector(String SMP_SERVER_HOSTNAME,int SMP_PORT,String TERM_CHAR)
    {
        initComponents(SMP_SERVER_HOSTNAME, SMP_PORT,TERM_CHAR);
        try
        {
            socketConnection = establishConnection();
        }
        catch (Exception ex)
        {
            Logger.getLogger(NeConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private Socket establishConnection()
            throws Exception
    {
        InetAddress address = InetAddress.getByName(hostname);
        Socket connection = new Socket(address,port);
        return connection;
    }    
    public void sendMessage(String message)
            throws Exception
    {
        BufferedOutputStream bos =new BufferedOutputStream(socketConnection.getOutputStream());
        OutputStreamWriter osw = new OutputStreamWriter(bos,"US-ASCII");
        osw.write(message);
        osw.flush();
    }
    public String readResponse()
            throws Exception
    {
        BufferedInputStream bis = new BufferedInputStream(socketConnection.getInputStream());
        InputStreamReader isr = new InputStreamReader(bis, "US-ASCII");
        int c;
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while (true)
	            {
	                line = br.readLine();
	                System.out.println(line + "\n");
	                if (line.endsWith(terminationCharacter))
	                    break;
	            }
        return line;
    }
}