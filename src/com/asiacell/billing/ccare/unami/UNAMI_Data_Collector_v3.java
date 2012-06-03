package com.asiacell.billing.ccare.unami;

import com.asiacell.billing.utils.*;
import java.sql.*;
import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class UNAMI_Data_Collector_v3
{    
     public static void main(String args[])
        throws Exception
    {         
    	  XmlUnamiConfigReader xmlRead = new XmlUnamiConfigReader();
        String outputDirPath = xmlRead.getParameter("DirOutputPath").trim();
        String getUniqueMsisdnQuery = "Select t.msisdn From BS_UNAMI_MSISDNs t where rownum <2";
        String dbUserName = xmlRead.getParameter("DatabaseUserName").trim();
        String dbPassword = xmlRead.getParameter("DatabaseUserPassword").trim();
        String dbIpAddress = xmlRead.getParameter("DatabaseIPAddress").trim();
        String dbName = xmlRead.getParameter("DatabaseName").trim();
        String currentBillingCycle = xmlRead.getParameter("CurrentBillingCycle").trim();
        String localCallsQuery = "Select t.msisdn||','||t.othertelnum||','||t.localfee From cdr_voice" + currentBillingCycle + " t Where t.roamtype=0 And t.calltype <> 2 And t.cdrtype='CSMOC' and msisdn = ?";
        String iddCallsQuery = "Select t.msisdn||','||t.othertelnum||','||t.Tollfee From cdr_voice" + currentBillingCycle + " t Where t.roamtype=0 And t.calltype=2 And t.cdrtype='CSMOC' and msisdn=?";
        String roamingCallsQuery = "Select t.cdrtype||','||t.msisdn||','||t.othertelnum||','||(t.localfee + t.roamfee) as Fee From cdr_voice" + currentBillingCycle + " t Where t.roamtype=2 and msisdn=?";
        String smsRecordsQuery = "Select t.cdrtype||','||t.msisdn||','||t.fee_s From cdr_smsp2p" + currentBillingCycle + " t Where t.cdrtype='SMSMO' and msisdn=?";
        String gprsRecordsQuery = "Select t.msisdn||','||t.upfee||','||t.starttime||','||(t.upfee_s + t.downfee_s) as fee From cdr_gprs" + currentBillingCycle + " t Where msisdn=?";
        String spcpRecordsQuery = "Select t.oa||','||substr(g.service_name,length(g.service_name)-3)||','||t.fee From cdr_dsmp" + currentBillingCycle + " t,dsmp_product_view g Where t.servicetype=2 And t.serviceid=g.serv_id And t.spid=g.spcp_id and msisdn=?";
        String mmsRecordsQuery = "Select t.oa||','||t.da||','||t.fee From cdr_dsmp" + currentBillingCycle + " t Where t.servicetype=3 and msisdn=?";    

        
        File outputDir = new File(outputDirPath);
        outputDir.mkdir();
        OracleConnector myConnector = new OracleConnector();
        Connection conn = myConnector.connectMe(dbUserName,dbPassword,dbIpAddress,dbName);
        Statement stmt = conn.createStatement();
        ResultSet rs01 = stmt.executeQuery(getUniqueMsisdnQuery);        
        // Create the directories
        while (rs01.next() != false)
        {          
            new File(outputDir + "\\0" + rs01.getString(1)).mkdir();
        }
        rs01.close();
        stmt.close();
        conn.close();
        
        CdrExtractor cdrExt1 = new CdrExtractor(getUniqueMsisdnQuery,outputDirPath,dbUserName,dbPassword,dbIpAddress,dbName,localCallsQuery,"msisdn,othernum,fee","local_calls");
        CdrExtractor cdrExt2 = new CdrExtractor(getUniqueMsisdnQuery,outputDirPath,dbUserName,dbPassword,dbIpAddress,dbName,iddCallsQuery,"msisdn,othernum,fee","idd_calls");
        CdrExtractor cdrExt3 = new CdrExtractor(getUniqueMsisdnQuery,outputDirPath,dbUserName,dbPassword,dbIpAddress,dbName,roamingCallsQuery,"msisdn,othernum,fee","roaming_calls");
        CdrExtractor cdrExt4 = new CdrExtractor(getUniqueMsisdnQuery,outputDirPath,dbUserName,dbPassword,dbIpAddress,dbName,smsRecordsQuery,"msisdn,othernum,fee","sms_calls");
        CdrExtractor cdrExt5 = new CdrExtractor(getUniqueMsisdnQuery,outputDirPath,dbUserName,dbPassword,dbIpAddress,dbName,gprsRecordsQuery,"msisdn,othernum,fee","gprs_calls");
        CdrExtractor cdrExt6 = new CdrExtractor(getUniqueMsisdnQuery,outputDirPath,dbUserName,dbPassword,dbIpAddress,dbName,spcpRecordsQuery,"msisdn,othernum,fee","sms2tv_calls");
        CdrExtractor cdrExt7 = new CdrExtractor(getUniqueMsisdnQuery,outputDirPath,dbUserName,dbPassword,dbIpAddress,dbName,mmsRecordsQuery,"msisdn,othernum,fee","mms_calls");         
        
        ExecutorService threadExecutor = Executors.newFixedThreadPool( 7 );
        threadExecutor.execute(cdrExt1);
        threadExecutor.execute(cdrExt2);
        threadExecutor.execute(cdrExt3);
        threadExecutor.execute(cdrExt4);
        threadExecutor.execute(cdrExt5);
        threadExecutor.execute(cdrExt6);
        threadExecutor.execute(cdrExt7);
        threadExecutor.shutdown();        
        // package the files and do the cleanup
        for(;;)
        {
            if (threadExecutor.isTerminated())
            {
                System.out.println("Compressing and Deleting CDRs");
                CDR_Packager cdrPack = new CDR_Packager();
                cdrPack.packageCDRs(outputDirPath, currentBillingCycle);
                break;
            }
        }
    }
}