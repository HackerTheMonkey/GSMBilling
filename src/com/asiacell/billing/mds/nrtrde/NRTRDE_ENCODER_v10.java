package com.asiacell.billing.mds.nrtrde;

import com.asiacell.billing.*;
import com.objsys.asn1j.runtime.*;
import java.io.*;
import com.asiacell.billing.AsciiString;
import com.asiacell.billing.Sender;
public class NRTRDE_ENCODER_v10
{
    public static void main(String args[]) 
        throws Exception
    {
        // Set the NRTRDE encoding variables and values
        Nrtrde nrt = new Nrtrde();
        SpecificationVersionNumber sp_ver_no = new SpecificationVersionNumber(2);
        ReleaseVersionNumber rel_no = new ReleaseVersionNumber(1);
        Sender sndr = new Sender("IRQAC");
        Recipient rcpt = new Recipient("IRQHAS");
        SequenceNumber sq  = new SequenceNumber("00000010");
        FileAvailableTimeStamp ftime_stamp = new FileAvailableTimeStamp("20071210132429");
        UtcTimeOffset utc_off = new UtcTimeOffset("+0400");
        CallEventsCount call_count = new CallEventsCount(1); 
        CallEvent call_event[] = new CallEvent[1]; 
        //call_event[0] = null;
        Moc moc = new Moc();
        
        // Start Setting the MOC details
        "01923472134729374 12347912349723947 200804231200 0400 120 0 11 24".substring(0,17).trim();
        moc.callEventDuration = new CallEventDuration(0);
        moc.callEventStartTimeStamp = new CallEventStartTimeStamp("20071210132429");
        moc.callReference = new CallReference(1827333);
        moc.causeForTermination = new CauseForTermination(0);
        moc.chargeAmount = new ChargeAmount(12);
        //moc.connectedNumber = new ConnectedNumber("");
        //moc.dialledDigits = new DialledDigits("");
        moc.imei = new Imei("49");
        moc.imsi = new Imsi("'418054100002903'H");
        moc.recEntityId = new RecEntityId("25250960102");                
        //moc.serviceCode.set_teleServiceCode(new TeleServiceCode("11"));(
        ServiceCode servic_code = new ServiceCode();
        servic_code.set_teleServiceCode(new TeleServiceCode("11"));
        moc.serviceCode = servic_code;
        //moc.supplementaryServiceCode = new SupplementaryServiceCode("");
        //moc.thirdPartyNumber = new ThirdPartyNumber("");
        moc.utcTimeOffset = new UtcTimeOffset("+0400");        
        
        // End Setting the MOC details
        
        call_event[0] = new CallEvent();
        call_event[0].set_moc(moc);
        com.asiacell.billing.CallEventList call_event_list = new com.asiacell.billing.CallEventList(call_event);
        // Initialize the NRTRDE object instance
        nrt.utcTimeOffset = utc_off;
        nrt.specificationVersionNumber = sp_ver_no;
        nrt.sequenceNumber = sq;
        nrt.sender = sndr;
        nrt.releaseVersionNumber = rel_no;
        nrt.recipient = rcpt;
        nrt.fileAvailableTimeStamp = ftime_stamp;
        nrt.callEventsCount = call_count;  
        nrt.callEvents = call_event_list;
        //  Create the Encode Bufffer
        Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();
        // Do the Encoding
        try
        {
            nrt.encode(buffer,true);
            //buffer.binDump();
            //buffer.hexDum();
        }        
        catch (Exception e)
        {
            
        }
        // Write the Encoded File in Binary Format
        buffer.write(new FileOutputStream("D:\\documents\\MyPrograms\\ASN1C_Compiler\\IRQAC\\NRIRQACARTEC0000001"));
        // Write the hex dump for checking
        buffer.hexDump(new PrintStream(new FileOutputStream("D:\\NRIRQACARTEC0000001_check")));
    }
}