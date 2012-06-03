package com.asiacell.billing.mds.nrtrde;

import com.asiacell.billing.*;
import com.objsys.asn1j.runtime.*;
import java.io.*;
public class NRTRDE_ENCODER_v12
{
    // Static & Private Member Variables Declaration
    private static File roamingCdrSource = new File("D:\\temp\\NR_SOURCE");
    private static File nrFilesDest = new File("D:\\temp\\NR_DEST");   
    private static String destPartner = null;
    public static void main(String args[])
        throws Exception
    {
        String roamFileList[] = roamingCdrSource.list();
        for (int i=0;i<roamFileList.length;i++)
        {
            String roamFileFullPath = roamingCdrSource.getParent() + "\\" + roamingCdrSource.getName() + "\\" + roamFileList[i];
            FileReader roamFileReader = new FileReader(roamFileFullPath);
            BufferedReader roamFileBufReader = new BufferedReader(roamFileReader);
            SpecificationVersionNumber sp_ver_no = new SpecificationVersionNumber(2);
            ReleaseVersionNumber rel_no = new ReleaseVersionNumber(1);
            Sender sndr = new Sender("IRQAC");
            Recipient rcpt = new Recipient(roamFileList[i].substring(0,5));
            destPartner = roamFileList[i].substring(0,5);
            SequenceNumber sq  = new SequenceNumber("00000010");
            FileAvailableTimeStamp ftime_stamp = new FileAvailableTimeStamp("20071210132429");
            UtcTimeOffset utc_off = new UtcTimeOffset("+0400");
            long recCount = getNoOfRecs(new File(roamFileFullPath));
            CallEventsCount call_count = new CallEventsCount(recCount);   
            com.asiacell.billing.CallEventList call_event_list = new com.asiacell.billing.CallEventList(generateCallEventArray(roamFileBufReader,(int)recCount));
            roamFileReader.close();
            roamFileReader.close();
            //Set the NRTRDE object
            Nrtrde nrt = new Nrtrde();
            nrt.callEvents = call_event_list;
            nrt.callEventsCount = call_count;
            nrt.fileAvailableTimeStamp = ftime_stamp;
            nrt.recipient = rcpt;
            nrt.sender = sndr;
            nrt.sequenceNumber = sq;
            nrt.releaseVersionNumber = rel_no;
            nrt.specificationVersionNumber = sp_ver_no;
            nrt.utcTimeOffset = utc_off;
            Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();
            try
            {
                nrt.encode(buffer,true);
            }
            catch (Exception e)
            {
                
            }
            buffer.write(new FileOutputStream(nrFilesDest + "\\NRIRQAC" + destPartner + "0000001"));
        }
    }      
    private static long getNoOfRecs(File myFile)   throws Exception
    {
        FileReader testFr = new FileReader(myFile);
        BufferedReader bufr = new BufferedReader(testFr);
        String line = null;
        long count = 0;
        while ((line = bufr.readLine()) != null)
        {
            count++;
        }
        bufr.close();
        testFr.close();
        return count;
    }
    private static CallEvent[] generateCallEventArray(BufferedReader bufReader,int NoOfRecords)
        throws Exception
    {
        CallEvent call_event[] = new CallEvent[NoOfRecords];
        String cdrLine = null;  
        for (int j=0;j<NoOfRecords;j++)
        {
            call_event[j] = new CallEvent();
            cdrLine = bufReader.readLine();
            String cdrType = cdrLine.substring(0,1);    
            Moc moc = new Moc();
            Mtc mtc = new Mtc();
            switch(Integer.parseInt(cdrType))
            {
                case 0://MOC                    
                    moc.callEventDuration = new CallEventDuration(Integer.parseInt(cdrLine.substring(87,94).trim()));
                    moc.callEventStartTimeStamp = new CallEventStartTimeStamp(cdrLine.substring(64,80).trim());
                    moc.callReference = new CallReference(1827333);
                    moc.causeForTermination = new CauseForTermination(Integer.parseInt(cdrLine.substring(94,100).trim()));
                    moc.chargeAmount = new ChargeAmount(Integer.parseInt(cdrLine.substring(107,117).trim()));
                    moc.connectedNumber = new ConnectedNumber(cdrLine.substring(117).trim());
                    moc.imei = new Imei("'" + cdrLine.substring(43,60).trim() + "'H");
                    moc.imsi = new Imsi("'" + cdrLine.substring(3,19).trim() + "'H");
                    moc.recEntityId = new RecEntityId("25250960102");  
                    ServiceCode servic_code = new ServiceCode();
                    servic_code.set_teleServiceCode(new TeleServiceCode(cdrLine.substring(100,107).trim()));
                    moc.serviceCode = servic_code;
                    moc.utcTimeOffset = new UtcTimeOffset("+" + cdrLine.substring(80,87).trim());                    
                    call_event[j].set_moc(moc);                    
                    break;
                case 1://MTC
                    mtc.callEventDuration = new CallEventDuration(Integer.parseInt(cdrLine.substring(87,94).trim()));
                    mtc.callEventStartTimeStamp = new CallEventStartTimeStamp(cdrLine.substring(64,80).trim());
                    mtc.callReference = new CallReference(1827333);
                    mtc.causeForTermination = new CauseForTermination(Integer.parseInt(cdrLine.substring(94,100).trim()));
                    mtc.chargeAmount = new ChargeAmount(Integer.parseInt(cdrLine.substring(107,117).trim()));
                    mtc.callingNumber = new CallingNumber(cdrLine.substring(117).trim());
                    mtc.imei = new Imei("'" + cdrLine.substring(43,60).trim() + "'H");
                    mtc.imsi = new Imsi("'" + cdrLine.substring(3,19).trim() + "'H");
                    mtc.recEntityId = new RecEntityId("25250960102");  
                    ServiceCode servic_code_mtc = new ServiceCode();
                    servic_code_mtc.set_teleServiceCode(new TeleServiceCode(cdrLine.substring(100,107).trim()));
                    mtc.serviceCode = servic_code_mtc;
                    mtc.utcTimeOffset = new UtcTimeOffset("+" + cdrLine.substring(80,87).trim());                    
                    call_event[j].set_mtc(mtc);
                    break;
            }
        }
        return call_event;
    }
}