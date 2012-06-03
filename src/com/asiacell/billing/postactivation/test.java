package com.asiacell.billing.postactivation;
public class test
{
    public static void main(String args[])
            throws Exception
    {
        String msg = new String("`SC`00581.00internalSRVM    00000001DLGLGN000000000001TXBEG 0000LOGIN:USER=Wang,PSWD=123456  f09ac0f6;");
        NeConnector sulySMP = new NeConnector("10.76.105.40", 19999, ";");
        sulySMP.sendMessage(msg);
        System.out.println(sulySMP.readResponse());
    }
}