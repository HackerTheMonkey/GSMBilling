package com.asiacell.billing.mds.nrtrde;

import com.asiacell.billing.*;
import com.objsys.asn1j.runtime.*;
import java.io.*;
import com.asiacell.billing.AsciiString;
import com.asiacell.billing.Sender;
public class NRTRDE_ENCODER_v11
{
    public static void main(String args[]) 
        throws Exception
    {
        java.io.BufferedReader bf = new java.io.BufferedReader(new FileReader("D:\\source_file.std"));
        String cdr_line = bf.readLine();
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
        moc.callEventDuration = new CallEventDuration(Integer.parseInt(cdr_line.substring(85,90).trim()));
        moc.callEventStartTimeStamp = new CallEventStartTimeStamp(cdr_line.substring(62,78).trim());
        moc.callReference = new CallReference(1827333);
        moc.causeForTermination = new CauseForTermination(Integer.parseInt(cdr_line.substring(92,98).trim()));
        moc.chargeAmount = new ChargeAmount(Integer.parseInt(cdr_line.substring(105,115).trim()));
        moc.connectedNumber = new ConnectedNumber(cdr_line.substring(115).trim());
        //moc.dialledDigits = new DialledDigits("");
        moc.imei = new Imei("'" + cdr_line.substring(41,58).trim() + "'H");
        moc.imsi = new Imsi("'" + cdr_line.substring(1,17).trim() + "'H");
        moc.recEntityId = new RecEntityId("25250960102");                
        //moc.serviceCode.set_teleServiceCode(new TeleServiceCode("11"));(
        ServiceCode servic_code = new ServiceCode();
        servic_code.set_teleServiceCode(new TeleServiceCode("11"));
        moc.serviceCode = servic_code;
        //moc.supplementaryServiceCode = new SupplementaryServiceCode("");
        //moc.thirdPartyNumber = new ThirdPartyNumber("");
        moc.utcTimeOffset = new UtcTimeOffset("+" + cdr_line.substring(78,85).trim());        
        
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