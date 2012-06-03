/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.asiacell.billing.undertest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
/**
 *
 * @author hasanein
 */
public class network01 
{
   public static void main(String args[])
           throws Exception
   {
        URL url = new URL("http://www.asiacell.com");
        URLConnection conn = url.openConnection();
        conn.setUseCaches(false);
        java.io.InputStream is = conn.getInputStream();
        java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
        java.io.BufferedReader br = new java.io.BufferedReader(isr);
        String line;
        while((line = br.readLine()) != null)
        {
            System.out.println(line);
        }
   }
}
