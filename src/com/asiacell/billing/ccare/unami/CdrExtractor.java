package com.asiacell.billing.ccare.unami;

import java.sql.*;
import java.io.*;
import com.asiacell.billing.utils.*;
public class CdrExtractor implements Runnable
{       
    
    CdrExtractor(String ___getUniqueMsisdnQuery,String __outputDirPath,String __dbUserName,String __dbPassword,String __dbIpAddress,String __dbName,String _query,String headers,String fileSuffix)
    {
        _getUniqueMsisdnQuery = ___getUniqueMsisdnQuery;
        _outputDirPath = __outputDirPath;
        _dbUserName = __dbUserName;
        _dbPassword = __dbPassword;
        _dbIpAddress = __dbIpAddress;
        _dbName =__dbName;
        query = _query;
        _header = headers;
        _fileSuffix = fileSuffix;
    }
    @Override
    public void run()
    {                
        try
        {
            System.out.println("One Thread Started for processing " + _fileSuffix);
            OracleConnector oraConn1 = new OracleConnector();
            Connection conn1 = oraConn1.connectMe(_dbUserName,_dbPassword,_dbIpAddress,_dbName);
            Statement stmt01 = conn1.createStatement();
            System.out.println(_fileSuffix + " Processor >> connect to database success");
            ResultSet rs01 = stmt01.executeQuery(_getUniqueMsisdnQuery);
            while (rs01.next() != false)
            {                        
                FileWriter fw = new FileWriter(_outputDirPath + "\\0" + rs01.getString(1) + "\\0" + rs01.getString(1) + "_" + _fileSuffix + "_.csv");
                BufferedWriter br = new BufferedWriter(fw);
                br.write(_header);
                br.newLine();
                OracleConnector oraConn2 = new OracleConnector();
                Connection conn2 = oraConn2.connectMe(_dbUserName,_dbPassword,_dbIpAddress,_dbName);
                PreparedStatement stmt02 = conn2.prepareStatement(query);
                stmt02.setString(1,rs01.getString(1));                
                ResultSet rs02 = stmt02.executeQuery();                                      
                while (rs02.next() != false)
                {
                    System.out.println(rs01.getString(1) + "--> " + rs02.getString(1));
                    br.write(rs02.getString(1));
                    br.newLine();
                }
                rs02.close();
                stmt02.close();
                conn2.close();
                br.close();
                fw.close();
            }         
            rs01.close();
            stmt01.close();
            conn1.close();
        }
        catch (Exception e)
        {
            
        }
    }
    // Class Global Variables
    private String _getUniqueMsisdnQuery;
    private String _outputDirPath;
    private String _dbUserName;
    private String _dbPassword;
    private String _dbIpAddress;
    private String _dbName;
    private String query;
    private String _header;
    private String _fileSuffix;
}