package com.asiacell.billing.undertest;

import com.logica.smpp.*;
import com.logica.smpp.util.ByteBuffer;
import java.io.IOException;
public class SMPP_TEST
{
    public static void main(String args[]) throws IOException
    {
        TCPIPConnection has = new TCPIPConnection("192.168.117.1", 5016);               
        has.open();
    }
}