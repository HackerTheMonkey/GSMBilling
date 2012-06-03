package com.asiacell.billing.mds.nrtrde;
import com.asiacell.billing.*;
import com.objsys.asn1j.runtime.*;
import java.io.*;
import java.sql.*;
import java.util.Date;
import com.asiacell.billing.utils.*;
import java.util.Calendar;
public class NRTRDE_ENCODER_v21
{        
    public static void main(String args[])
        throws Exception
    {
        // Initialize Components and load Variable values from DB
        System.out.println("[" + new Date() + "][NrtrdeEngine.NrtrdeMain][INFO]>> Initializing NRTRDE base data");
        XmlReader xmlReader = new XmlReader();
        String scriptLoc = xmlReader.getParameter("ACCU_SCRIPT_LOC", "generic_config.xml");
        String accumulatedCdrsDir = xmlReader.getParameter("ACCU_ROAM_FINAL", "generic_config.xml");
        String encodedCdrsDir = xmlReader.getParameter("ENCODED_CDRS_DIR", "generic_config.xml");
        OracleConnector myConnector = new OracleConnector();
        Connection conn1 = myConnector.connectMe(xmlReader.getParameter("DATABASE_USER_NAME", "generic_config.xml"),xmlReader.getParameter("DATABASE_PASSWORD", "generic_config.xml"),xmlReader.getParameter("DATABASE_IP_ADDRESS", "generic_config.xml"),xmlReader.getParameter("DATABASE_TNS_NAME", "generic_config.xml"));        
        System.out.println("[" + new Date() + "][NrtrdeEngine.NrtrdeMain][INFO]>> Connect to database success");        
        // execute the syncPartners procedure
        System.out.println("[" + new Date() + "][NrtrdeEngine.NrtrdeMain][INFO]>> About to synchronize the roaming partners information");
        CallableStatement syncPartnersInfo = conn1.prepareCall("{call syncPartners()}");
        syncPartnersInfo.execute();
        conn1.close();
        File roamOrigCdrsDir = new File(accumulatedCdrsDir);
        String roamFileList[] = null;
        // Generate and run the roaming CDRs accumulation script.        
        generateAccuScript(scriptLoc);
        System.out.println("[" + new Date() + "][NrtrdeEngine.NrtrdeMain][INFO]>> Run the accumulation script");
        Process proc1 = Runtime.getRuntime().exec(scriptLoc);
        proc1.waitFor();
        // Delete the accumulation script after it completed the accumulation task if it is configured to be deleted
        if (xmlReader.getParameter("IS_DELETE_ACCU_SCRIPT", "generic_config.xml").equals("yes"))
        {
            System.out.println("[" + new Date() + "][NrtrdeEngine.NrtrdeMain][INFO]>> Delete the accumulation script");
            Process proc2 = Runtime.getRuntime().exec("/usr/bin/rm -fr " + scriptLoc);
            proc2.waitFor();            
        }

        // Iterate on the accumulated roaming files to encode them into NRTRDE file format
        roamFileList = roamOrigCdrsDir.list();
        for (int i=0;i<roamFileList.length;i++)
        {
            String imsi = roamFileList[i].substring(0,roamFileList[i].indexOf("_"));
            Connection conn2 = myConnector.connectMe(xmlReader.getParameter("DATABASE_USER_NAME", "generic_config.xml"),xmlReader.getParameter("DATABASE_PASSWORD", "generic_config.xml"),xmlReader.getParameter("DATABASE_IP_ADDRESS", "generic_config.xml"),xmlReader.getParameter("DATABASE_TNS_NAME", "generic_config.xml"));
            Statement stmt1 = conn2.createStatement();
            ResultSet rs1 = stmt1.executeQuery("Select tadig_name,imsi,current_seq From nrt_partners_seq t Where t.imsi='" + imsi + "'" + "And t.status='VALID'");
            if (rs1.next())
            {
                encodeNRTRDE(accumulatedCdrsDir + "/" + roamFileList[i],encodedCdrsDir,rs1.getString(1),rs1.getString(3));
                incrSequence(rs1.getString(1));
                new File(accumulatedCdrsDir + "/" + roamFileList[i]).delete();
            }
            rs1.close();
            stmt1.close();
            conn2.close();
        }
         //Complete the iteration process
        System.out.println("[" + new Date() + "][NrtrdeEngine.NrtrdeMain][INFO]>> ----------------------------------------------------------------------");
        System.out.println("[" + new Date() + "][NrtrdeEngine.NrtrdeMain][INFO]>> One Encoding Round Completed");
        System.out.println("[" + new Date() + "][NrtrdeEngine.NrtrdeMain][INFO]>> Sleeping...");
        System.out.println("[" + new Date() + "][NrtrdeEngine.NrtrdeMain][INFO]>> ----------------------------------------------------------------------");

    }
    
    private static void incrSequence(String tadigName)
        throws Exception
    {
        XmlReader xmlReader = new XmlReader();
        String sql = "select current_seq from nrt_partners_seq where tadig_name='" + tadigName + "'";
        OracleConnector incrSeqConnector = new OracleConnector();
        Connection incrSeqConn = incrSeqConnector.connectMe(xmlReader.getParameter("DATABASE_USER_NAME", "generic_config.xml"),xmlReader.getParameter("DATABASE_PASSWORD", "generic_config.xml"),xmlReader.getParameter("DATABASE_IP_ADDRESS", "generic_config.xml"),xmlReader.getParameter("DATABASE_TNS_NAME", "generic_config.xml"));
        Statement incrSeqStmt = incrSeqConn.createStatement();
        ResultSet incrSeqRs = incrSeqStmt.executeQuery(sql);
        incrSeqRs.next();
        String curSeq = incrSeqRs.getString(1);
        incrSeqRs.close();
        incrSeqStmt.close();
        int curSeqInt = Integer.parseInt(curSeq);
        curSeqInt++;
        String incSeqStr = String.valueOf(curSeqInt);
        for (int i=0;i<9;i++)
        {
            if (incSeqStr.length() >= 7) break;
            incSeqStr = "0" + incSeqStr;
        }
        if (incSeqStr.equals("10000000")) incSeqStr="0000001";
        // Update the sequence into the DB
        Statement updateStmt = incrSeqConn.createStatement();
        updateStmt.executeQuery("update nrt_partners_seq set current_seq='" + incSeqStr + "' where tadig_name='" + tadigName + "'");
        updateStmt.close();
        incrSeqConn.close();
        System.out.println("[" + new Date() + "][NrtrdeEngine.incrSequence][INFO]>> Incrementing NRTRDE file sequence for " + tadigName + ": new seq = " + incSeqStr);
    }

    private static void encodeNRTRDE(String sourceRoamCdrFilePath,String destEncodedNrtrdeDir,String partnerTadigName,String seq)
        throws Exception
    {        
        System.out.println("[" + new Date() + "][NrtrdeEngine.encodeNRTRDE][INFO]>> Initializing basic parameters ");
        File sourceRoamCdrFile = new File(sourceRoamCdrFilePath);
        File destRoamNrtrdeDir = new File(destEncodedNrtrdeDir);
        String destPartner = partnerTadigName;
        String partnerFileSequence = seq;
        FileReader roamSourceFileReader = new FileReader(sourceRoamCdrFile);
        BufferedReader roamSourceFileReaderBufReader = new BufferedReader(roamSourceFileReader);

        System.out.println("[" + new Date() + "][NrtrdeEngine.encodeNRTRDE][INFO]>> NRTRDE format encoding preparation started for " + sourceRoamCdrFilePath);
        System.out.println("[" + new Date() + "][NrtrdeEngine.encodeNRTRDE][INFO]>> Initializing generic NRTRDE file fields ");
        // Start declaring and initializing the NRTRDE file parameters that are not depandable on the CDR records
        SpecificationVersionNumber sp_ver_no = new SpecificationVersionNumber(2);
        ReleaseVersionNumber rel_no = new ReleaseVersionNumber(1);
        Sender sndr = new Sender("IRQAC");
        Recipient rcpt = new Recipient(destPartner);
        SequenceNumber sq  = new SequenceNumber(partnerFileSequence);
        FileAvailableTimeStamp ftime_stamp = new FileAvailableTimeStamp(genTimestamp());
        UtcTimeOffset utc_off = new UtcTimeOffset("+0300");
        long recCount = getNoOfRecs(new File(sourceRoamCdrFilePath));
        CallEventsCount call_count = new CallEventsCount(recCount);
        com.asiacell.billing.CallEventList call_event_list = new com.asiacell.billing.CallEventList(generateCallEventArray(roamSourceFileReaderBufReader,(int)recCount));        
        // End decaring and initializing the NRTRDE file parameters.

        System.out.println("[" + new Date() + "][NrtrdeEngine.encodeNRTRDE][INFO]>> Initializing NRTRDE object for encoding");
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
        System.out.println("[" + new Date() + "][NrtrdeEngine.encodeNRTRDE][INFO]>> NRTRDE format encoding preparation completed for " + sourceRoamCdrFilePath);
        try
        {            
            nrt.encode(buffer,true);
            System.out.println("[" + new Date() + "][NrtrdeEngine.encodeNRTRDE][INFO]>> NRTRDE encoding completed successfully for " + sourceRoamCdrFilePath);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        buffer.write(new FileOutputStream(destRoamNrtrdeDir + "/" + "NRIRQAC" + destPartner + partnerFileSequence));
        System.out.println("[" + new Date() + "][NrtrdeEngine.encodeNRTRDE][INFO]>> Writing the encoded file to disk successfully completed: SRC=" + sourceRoamCdrFilePath + " DST=" + destRoamNrtrdeDir + "/" + "NRIRQAC" + destPartner + partnerFileSequence);
        roamSourceFileReaderBufReader.close();
        roamSourceFileReader.close();
    }

    private static CallEvent[] generateCallEventArray(BufferedReader bufReader,int NoOfRecords)
        throws Exception
    {                
        System.out.println("[" + new Date() + "][NrtrdeEngine.generateCallEventArray][INFO]>> Start generating CallEvents array");
        CallEvent call_event[] = new CallEvent[NoOfRecords];
        String cdrLine = null;
        System.out.println("[" + new Date() + "][NrtrdeEngine.generateCallEventArray][INFO]>> No of CallEvents is " + NoOfRecords);
        for (int j=0;j<NoOfRecords;j++)
        {
            
            call_event[j] = new CallEvent();
            cdrLine = bufReader.readLine();
            String cdrType = cdrLine.substring(3,8);
            int recType = 0;

            // Get the record type basing on the cdrType
            if (cdrType.equals("CSMOC"))
            {
                recType = 0;
            }
            else if (cdrType.equals("CSMTC"))
            {
                recType = 1;
            }
            else if (cdrType.equals("SMSMO"))
            {
                recType = 2;
            }
            else if (cdrType.equals("SMSMT"))
            {
                recType = 3;
            }                        
            // end getting the recType
            Moc moc = new Moc();
            Mtc mtc = new Mtc();
            switch(recType)
            {
                case 0://MOC 
                    System.out.println("[" + new Date() + "][NrtrdeEngine.generateCallEventArray][INFO]>> Start Encoding a CSMOC record...");
                    moc.callEventDuration = new CallEventDuration(Integer.parseInt(cdrLine.substring(151,161).trim()));                    
                    moc.callEventStartTimeStamp = new CallEventStartTimeStamp(cdrLine.substring(137,151).trim());                    
                    //moc.callReference = new CallReference(1827333);//manadatory when available from the network, need to be investigated later                    
                    
                    String terminationCauseCSMOC = "0";
                    if ( ! cdrLine.substring(254,257).trim().equals("")) terminationCauseCSMOC = cdrLine.substring(254,257).trim();
                    moc.causeForTermination = new CauseForTermination(Integer.parseInt(terminationCauseCSMOC));
                    
                    //moc.chargeAmount = new ChargeAmount(Integer.parseInt(cdrLine.substring(107,117).trim())); //This field is an optional field within MOC, MTC and GPRS groups
                    
                    // remove the leading 00, if the caller field is empty, then put the thirdparty number instead
                    if (cdrLine.substring(62,92).trim().length() < 2)
                    {
                        if (cdrLine.substring(107,137).trim().length() < 2)
                        {
                            moc.connectedNumber = new ConnectedNumber("1111");
                        }
                        else
                        {
                            if (cdrLine.substring(107,137).trim().substring(0,2).equals("00"))
                            {
                                moc.connectedNumber = new ConnectedNumber(cdrLine.substring(107,137).trim().replaceFirst("00",""));
                            }
                            else
                            {
                                moc.connectedNumber = new ConnectedNumber(cdrLine.substring(107,137).trim());//represent the called number
                            }                             
                        }
                    }
                    else
                    {
                            if (cdrLine.substring(62,92).trim().substring(0,2).equals("00"))
                            {
                                moc.connectedNumber = new ConnectedNumber(cdrLine.substring(62,92).trim().replaceFirst("00",""));
                            }
                            else
                            {
                                moc.connectedNumber = new ConnectedNumber(cdrLine.substring(62,92).trim());//represent the called number
                            }                        
                    }
                    
                    moc.imei = new Imei("'" + cdrLine.substring(47,62).trim() + "'H");
                    moc.imsi = new Imsi("'" + cdrLine.substring(32,47).trim() + "'H");
                    moc.recEntityId = new RecEntityId(cdrLine.substring(161,176).trim());// as per the TD.35 this field can carry the MSC ID (GT)
                    ServiceCode servic_code_csmoc = new ServiceCode();
                    servic_code_csmoc.set_teleServiceCode(new TeleServiceCode("11"));
                    moc.serviceCode = servic_code_csmoc;
                    moc.utcTimeOffset = new UtcTimeOffset("+0300");
                    call_event[j].set_moc(moc);
                    break;
                case 1://MTC
                    System.out.println("[" + new Date() + "][NrtrdeEngine.generateCallEventArray][INFO]>> Start Encoding a CSMTC record...");
                    mtc.callEventDuration = new CallEventDuration(Integer.parseInt(cdrLine.substring(151,161).trim()));
                    mtc.callEventStartTimeStamp = new CallEventStartTimeStamp(cdrLine.substring(137,151).trim());
                    //mtc.callReference = new CallReference(1827333);
                    
                    String terminationCauseCSMTC = "0";
                    if ( ! cdrLine.substring(254,257).trim().equals("")) terminationCauseCSMTC = cdrLine.substring(254,257).trim();
                    mtc.causeForTermination = new CauseForTermination(Integer.parseInt(terminationCauseCSMTC));
                    
                    //mtc.chargeAmount = new ChargeAmount(Integer.parseInt(cdrLine.substring(107,117).trim())); //This field is an optional field within MOC, MTC and GPRS groups
                    
                    // Replace the leading 00
                    if (cdrLine.substring(62,92).trim().length() < 2)
                    {
                        if (cdrLine.substring(107,137).trim().length() < 2)
                        {
                            mtc.callingNumber = new CallingNumber("1111");
                        }
                        else
                        {
                            if (cdrLine.substring(107,137).trim().substring(0,2).equals("00"))
                            {
                                 mtc.callingNumber = new CallingNumber(cdrLine.substring(107,137).trim().replaceFirst("00",""));
                            }
                            else
                            {
                                 mtc.callingNumber = new CallingNumber(cdrLine.substring(107,137).trim());//represent the called number
                            }                             
                        }
                    }
                    else
                    {
                            if (cdrLine.substring(62,92).trim().substring(0,2).equals("00"))
                            {
                                mtc.callingNumber = new CallingNumber(cdrLine.substring(62,92).trim().replaceFirst("00",""));
                            }
                            else
                            {
                                mtc.callingNumber = new CallingNumber(cdrLine.substring(62,92).trim());//represent the called number
                            }                        
                    }                    
                    
                    
                    mtc.imei = new Imei("'" + cdrLine.substring(47,62).trim() + "'H");
                    mtc.imsi = new Imsi("'" + cdrLine.substring(32,47).trim() + "'H");
                    mtc.recEntityId = new RecEntityId(cdrLine.substring(161,176).trim());// as per the TD.35 this field can carry the MSC ID (GT)
                    ServiceCode servic_code_csmtc = new ServiceCode();
                    servic_code_csmtc.set_teleServiceCode(new TeleServiceCode("11"));
                    mtc.serviceCode = servic_code_csmtc;
                    mtc.utcTimeOffset = new UtcTimeOffset("+0300");
                    call_event[j].set_mtc(mtc);
                    break;
                case 2://SMSMO
                    System.out.println("[" + new Date() + "][NrtrdeEngine.generateCallEventArray][INFO]>> Start Encoding a SMSMO record...");
                    moc.callEventDuration = new CallEventDuration(Integer.parseInt("0"));
                    moc.callEventStartTimeStamp = new CallEventStartTimeStamp(cdrLine.substring(101,115).trim());
                    //moc.callReference = new CallReference(1827333);
                    
                    String terminationCauseSMSMO = "0";
                    if ( ! cdrLine.substring(210,213).trim().equals("")) terminationCauseSMSMO = cdrLine.substring(210,213).trim();
                    moc.causeForTermination = new CauseForTermination(Integer.parseInt(terminationCauseSMSMO));
                    
                    //moc.chargeAmount = new ChargeAmount(Integer.parseInt(cdrLine.substring(107,117).trim())); //This field is an optional field within MOC, MTC and GPRS groups
                    
                    // remove the leading 00
                    if (cdrLine.substring(145,160).trim().length() < 2)
                    {
                        moc.connectedNumber = new ConnectedNumber("11111");
                    }
                    else
                    {
                        if (cdrLine.substring(145,160).trim().substring(0,2).equals("00"))
                        {
                            moc.connectedNumber = new ConnectedNumber(cdrLine.substring(145,160).trim().replaceFirst("00",""));
                        }
                        else
                        {
                            moc.connectedNumber = new ConnectedNumber(cdrLine.substring(145,160).trim());
                        }                        
                    }                                       
                    
                    
                    
                    //moc.imei = new Imei("'" + cdrLine.substring(43,60).trim() + "'H");
                    moc.imsi = new Imsi("'" + cdrLine.substring(56,71).trim() + "'H");
                    moc.recEntityId = new RecEntityId(cdrLine.substring(145,160).trim());
                    ServiceCode servic_code_smsmo = new ServiceCode();
                    servic_code_smsmo.set_teleServiceCode(new TeleServiceCode("22"));
                    moc.serviceCode = servic_code_smsmo;
                    moc.utcTimeOffset = new UtcTimeOffset("+0300");
                    call_event[j].set_moc(moc);
                    break;
                case 3://SMSMT
                    System.out.println("[" + new Date() + "][NrtrdeEngine.generateCallEventArray][INFO]>> Start Encoding a SMSMT record...");
                    mtc.callEventDuration = new CallEventDuration(Integer.parseInt("0"));
                    mtc.callEventStartTimeStamp = new CallEventStartTimeStamp(cdrLine.substring(101,115).trim());
                    //mtc.callReference = new CallReference(1827333);
                    
                    String terminationCauseSMSMT = "0";
                    if ( ! cdrLine.substring(210,213).trim().equals("")) terminationCauseSMSMT = cdrLine.substring(210,213).trim();
                    mtc.causeForTermination = new CauseForTermination(Integer.parseInt(terminationCauseSMSMT));
                    
                    //mtc.chargeAmount = new ChargeAmount(Integer.parseInt(cdrLine.substring(107,117).trim()));
                    
                    // remove the leading 00
                    if (cdrLine.substring(71,101).trim().length() < 2)
                    {
                        mtc.callingNumber = new CallingNumber("11111");
                    }
                    else
                    {
                        if (cdrLine.substring(71,101).trim().substring(0,2).equals("00"))
                        {
                            mtc.callingNumber = new CallingNumber(cdrLine.substring(71,101).trim().replaceFirst("00",""));
                        }
                        else
                        {
                            mtc.callingNumber = new CallingNumber(cdrLine.substring(71,101).trim());
                        }                        
                    }
                    
                    
                    
                    //mtc.imei = new Imei("'" + cdrLine.substring(43,60).trim() + "'H");
                    mtc.imsi = new Imsi("'" + cdrLine.substring(56,71).trim() + "'H");
                    mtc.recEntityId = new RecEntityId(cdrLine.substring(145,160).trim());
                    ServiceCode servic_code_smsmt = new ServiceCode();
                    servic_code_smsmt.set_teleServiceCode(new TeleServiceCode("21"));                    
                    mtc.serviceCode = servic_code_smsmt;
                    mtc.utcTimeOffset = new UtcTimeOffset("+0300");                    
                    call_event[j].set_mtc(mtc);
                    break;
            }
        }
        System.out.println("[" + new Date() + "][NrtrdeEngine.generateCallEventArray][INFO]>> End generating CallEvents array");
        return call_event;
    }

    private static void generateAccuScript(String scriptLocation)
        throws Exception
    {
        XmlReader xmlReader = new XmlReader();        
        // Get the uniq IMSI's from the DB
        OracleConnector myConnector = new OracleConnector();
        Connection conn  = myConnector.connectMe(xmlReader.getParameter("DATABASE_USER_NAME", "generic_config.xml"),xmlReader.getParameter("DATABASE_PASSWORD", "generic_config.xml"),xmlReader.getParameter("DATABASE_IP_ADDRESS", "generic_config.xml"),xmlReader.getParameter("DATABASE_TNS_NAME", "generic_config.xml"));
        System.out.println("[" + new Date() + "][NrtrdeEngine.generateAccuScript][INFO]>> Connect to database success ");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select imsi from nrt_partners_seq where status='VALID'");
        String uniqImsis = "";
        while (rs.next())
        {
            uniqImsis = uniqImsis + " " + rs.getString(1);
        }
        System.out.println("[" + new Date() + "][NrtrdeEngine.generateAccuScript][INFO]>> Getting uniq IMSI's from DB success ");
        rs.close();
        stmt.close();
        conn.close();
        uniqImsis.trim();
        System.out.println("[" + new Date() + "][NrtrdeEngine.generateAccuScript][INFO]>> Start writing the accumulation script to disk");
        FileWriter accuScriptWriter = new FileWriter(scriptLocation);
        BufferedWriter accuScriptBufferedWriter = new BufferedWriter(accuScriptWriter);
        accuScriptBufferedWriter.write("#!" + xmlReader.getParameter("ACCU_SCRIPT_SHELL", "generic_config.xml"));
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("SMS_ORIG_DIR=" + xmlReader.getParameter("SMS_ORIG_DIR", "generic_config.xml"));
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("VOICE_ORIG_DIR=" + xmlReader.getParameter("VOICE_ORIG_DIR", "generic_config.xml"));
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("ACCU_ORIG_SMS=" + xmlReader.getParameter("ACCU_ORIG_SMS", "generic_config.xml"));
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("ACCU_ORIG_VOICE=" + xmlReader.getParameter("ACCU_ORIG_VOICE", "generic_config.xml"));
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("ACCU_SMS=" + xmlReader.getParameter("", "generic_config.xml"));
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("ACCU_VOICE=" + xmlReader.getParameter("ACCU_VOICE", "generic_config.xml"));
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("PRE_ACCU_ROAM_FINAL=" + xmlReader.getParameter("PRE_ACCU_ROAM_FINAL", "generic_config.xml"));
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("ACCU_ROAM_FINAL=" + xmlReader.getParameter("ACCU_ROAM_FINAL", "generic_config.xml"));
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("NRT_BACKUP=" + xmlReader.getParameter("NRT_BACKUP", "generic_config.xml"));
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("mkdir ${NRT_BACKUP}/SMS_ORIG_BAK/$(date +\"%Y%m%d\")");
        accuScriptBufferedWriter.newLine(); 
        accuScriptBufferedWriter.write("mkdir ${NRT_BACKUP}/VOICE_ORIG_BAK/$(date +\"%Y%m%d\")");
        accuScriptBufferedWriter.newLine();      
        accuScriptBufferedWriter.write("mkdir ${NRT_BACKUP}/ACCU_FINAL_BAK/$(date +\"%Y%m%d\")");
        accuScriptBufferedWriter.newLine();          
        accuScriptBufferedWriter.write("cd ${SMS_ORIG_DIR}");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("lastfile=$(ls -ltr *.std|tail -n 1|awk '{print $9}')");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("for file in $(ls *.std|grep -v ${lastfile})");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("do");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("cp ${file} ${NRT_BACKUP}/SMS_ORIG_BAK/$(date +\"%Y%m%d\")");
        accuScriptBufferedWriter.newLine();        
        accuScriptBufferedWriter.write("mv ${file} ${ACCU_ORIG_SMS}");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("done");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("cd ${VOICE_ORIG_DIR}");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("lastfile=$(ls -ltr *.std|tail -n 1|awk '{print $9}')");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("for file in $(ls *.std|grep -v ${lastfile})");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("do");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("cp ${file} ${NRT_BACKUP}/VOICE_ORIG_BAK/$(date +\"%Y%m%d\")");
        accuScriptBufferedWriter.newLine();        
        accuScriptBufferedWriter.write("mv ${file} ${ACCU_ORIG_VOICE}");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("done");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("cd ${ACCU_ORIG_VOICE}");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("for imsi in " + uniqImsis);
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("do");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("awk -v m=${imsi} '{if (substr($2,1,length(m)) == m) {print $0}}' *.std > ${ACCU_VOICE}/${imsi}_VOICE.std");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("done");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("rm -fr ${ACCU_ORIG_VOICE}/*");
        accuScriptBufferedWriter.newLine();        
        accuScriptBufferedWriter.write("cd ${ACCU_ORIG_SMS}");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("for imsi in " + uniqImsis);
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("do");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("awk -v m=${imsi} '{if (substr($3,1,length(m)) == m) {print $0}}' *.std > ${ACCU_SMS}/${imsi}_SMS.std");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("done");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("rm -fr ${ACCU_ORIG_SMS}/*");
        accuScriptBufferedWriter.newLine();        
        accuScriptBufferedWriter.write("mv ${ACCU_SMS}/*.std ${PRE_ACCU_ROAM_FINAL}");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("mv ${ACCU_VOICE}/*.std ${PRE_ACCU_ROAM_FINAL}");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("cd ${PRE_ACCU_ROAM_FINAL}");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("for imsi in " + uniqImsis);
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("do");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("cat ${imsi}* >> ${ACCU_ROAM_FINAL}/${imsi}_SMS_VOICE_ROAM.std");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("done");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("rm -fr ${PRE_ACCU_ROAM_FINAL}/*");
        accuScriptBufferedWriter.newLine();        
        accuScriptBufferedWriter.write("for file in ${ACCU_ROAM_FINAL}/*.std");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("do");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("cp ${file} ${NRT_BACKUP}/ACCU_FINAL_BAK/$(date +\"%Y%m%d\")/$(basename ${file}.$(date +\"%Y%m%d%H%M\"))");
        accuScriptBufferedWriter.newLine();
        accuScriptBufferedWriter.write("done");
        accuScriptBufferedWriter.newLine();
        //Delete the zero records files to prohibit generating a notification files        
        if (xmlReader.getParameter("IS_GENERATE_NOTIFICATION_FILES", "generic_config.xml").equals("no"))
            accuScriptBufferedWriter.write("find ${ACCU_ROAM_FINAL} -size 0|xargs rm -fr");
        accuScriptBufferedWriter.close();
        System.out.println("[" + new Date() + "][NrtrdeEngine.generateAccuScript][INFO]>> Writing the accumulation script completed succesfully ");
        accuScriptWriter.close();
        Process proc = Runtime.getRuntime().exec("/usr/bin/chmod +x " + scriptLocation);        
        proc.waitFor();
        System.out.println("[" + new Date() + "][NrtrdeEngine.generateAccuScript][INFO]>> Enabling the execute permission for the accumulation script ");
    }

    private static long getNoOfRecs(File myFile)   
        throws Exception
    {
        System.out.println("[" + new Date() + "][NrtrdeEngine.getNoOfRecs][INFO]>> Start getting number of records in " + myFile.getAbsolutePath());
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
        System.out.println("[" + new Date() + "][NrtrdeEngine.getNoOfRecs][INFO]>> Number of records in " + myFile.getAbsolutePath() + " is " + count);
        return count;
    }
    
    private static String genTimestamp()
    {
        // Get the timestamp elements as String Values
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(Calendar.getInstance().get(Calendar.HOUR));
        String minute = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        String second = String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
        // Add a leading zero to the single values elements
        if (month.length() == 1) month = "0" + month;
        if (day.length() == 1) day = "0" + day;
        if (hour.length() == 1) hour = "0" + hour;
        if (minute.length() == 1) minute = "0" + minute;
        if (second.length() == 1) second = "0" + second;
        // Concatnate the elements to form the timestamp
        String timestamp = year + month + day + hour + minute + second;
        return timestamp;       
    }
}