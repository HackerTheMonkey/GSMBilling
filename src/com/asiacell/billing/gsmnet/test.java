/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.asiacell.billing.gsmnet;

/**
 *
 * @author hasanein
 */
public class test 
{
    public static void main(String args[])
    {
        StringBuffer sb = new StringBuffer("hasanein");
        String bin = "";
        for (int i = 0 ; i < sb.length() ; i++)
        {
            bin = bin + Integer.toBinaryString(sb.charAt(i));
        }
        System.out.println(bin + ":" + bin.length());
    }
}
