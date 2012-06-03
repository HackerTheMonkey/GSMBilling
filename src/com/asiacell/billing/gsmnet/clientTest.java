/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.asiacell.billing.gsmnet;

import java.net.*;
import java.io.*;

/**
 *
 * @author hasanein
 */
public class clientTest 
{
    public static void main(String args[]) throws Exception
    {
        Socket clientSocket = new Socket("localhost", 19999);
        InputStream in = clientSocket.getInputStream();
        int c;
        String msg = "";
        while (true)
        {
            c = in.read();
            if (c == 13) break;
            msg = msg + String.valueOf((char) c);
        }
        System.out.println(msg);
    }
}
