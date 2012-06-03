package com.asiacell.billing.mds.nrtrde;

import com.objsys.asn1j.runtime.*;
import java.io.*;
import com.asiacell.billing.*;
public class NRTRDE_DECODER_v10
{
    public static void main(String args[]) 
        throws Exception
    {
        FileInputStream inputFile = new FileInputStream("D:\\documents\\MyPrograms\\ASN1C_Compiler\\IRQAC\\NRARETCIRQAC0000009");
        Asn1BerDecodeBuffer buffer_01 = new Asn1BerDecodeBuffer(inputFile);        
        Nrtrde nrt = new Nrtrde();
        nrt.decode(buffer_01);                
        nrt.specificationVersionNumber.print(System.out,"Specification Release Number",0);
        nrt.releaseVersionNumber.print(System.out,"Release Version Number",0);
        System.out.println("Sender = " + hexToChar(nrt.sender.toString()));  
        System.out.println("Sender = " + nrt.sender.toString());        
        System.out.println("Recipient : " + hexToChar(nrt.recipient.toString()));
        System.out.println("Recipient : " + nrt.recipient.toString());
        System.out.println("Sequence Number = " + hexToChar(nrt.sequenceNumber.toString()));
        System.out.println("File Available Time Stamp : " + hexToChar(nrt.fileAvailableTimeStamp.toString()));
        System.out.println("UTC Time Offset : " + hexToChar(nrt.utcTimeOffset.toString()));
        nrt.callEventsCount.print(System.out,"Call Events Count",0);                     
        
        CallEvent call_event = new CallEvent(CallEvent._MTC,nrt.callEvents.elements[0].getElement());
        Mtc mtc_call = new Mtc();        
        call_event.set_mtc(mtc_call);
        System.out.println ("Decoded IMSI is " + mtc_call.utcTimeOffset);
        
//        Asn1Type recordsAsn[] = new Asn1Type[nrt.callEvents.elements.length];
//        for (int i=0;i<nrt.callEvents.elements.length;i++)
//        {
//            recordsAsn[i] = nrt.callEvents.elements[i];            
//        }        
//        CallEvent has = new CallEvent(CallEvent._MOC,recordsAsn[1]);
//        Moc moc = new Moc();
//        System.out.println("AKJAHSKJAHJKHAJHS  " + moc.imei);
    }
    public static String hexToChar(String hexValue)
    {
        String finalSender = "";
        for (int i=0;i<hexValue.length();i++)
        {
            //System.out.println(i + "" + (i + 1));
            char senderString[] = hexValue.toCharArray();
            char senderString1 = (char)Integer.parseInt(String.valueOf(senderString[i] + "" + senderString[i + 1]),16);            
            finalSender = finalSender + String.valueOf(senderString1);
            i++;
        }
        return finalSender;
    }
}