package com.asiacell.billing.ccare.mnfi;

import com.asiacell.billing.utils.OracleConnector;
import java.sql.*;
import java.io.*;

public class FileGenerator
{
    public static void main(String args[])
            throws Exception
    {
        OracleConnector oraConnector = new OracleConnector();
        Connection conn = oraConnector.connectMe("ccare", "acccare", "192.168.117.12", "BOSS1");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from tmp_iccid_ranges");        
        File outputFile = new File("D:\\roaming_batch_04_mnfi.txt");
        FileWriter fw = new FileWriter(outputFile,true);
        BufferedWriter br = new BufferedWriter(fw);
        int counter = 1;
        while ((rs.next() != false))
        {
            br.write("1|MNFI_N|MNFI_RA" + counter + "|x|xx|" + rs.getString(1) + "|" + rs.getString(2) + "|1000|01|R1167|S1292,S1291,S1009,S1221,S1012,S1001,S1004,S1013,S1008|2|Baghdad|Baghdad|Baghdad|Baghdad|2");
            br.newLine();
            counter ++;
        }
        rs.close();
        stmt.close();
        conn.close();        
        br.close();
        fw.close();
    }
}