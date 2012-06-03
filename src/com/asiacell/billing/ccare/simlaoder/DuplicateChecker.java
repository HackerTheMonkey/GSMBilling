package com.asiacell.billing.ccare.simlaoder;

import com.asiacell.billing.utils.*;
import java.sql.*;

/**
 *
 * @author hasanein
 */
public class DuplicateChecker 
{
    public boolean checkDuplicate(String duplicateEntity,String configFilePath,String dupType)
            throws Exception
    {
        XmlReader xmlReader = new XmlReader();
        String username = xmlReader.getParameter("DbUserName", "D:\\temp\\java\\source_code\\StartPackLoader\\config\\SIM_CONFIG.XML.xml");
        String password = xmlReader.getParameter("DbPassword", "D:\\temp\\java\\source_code\\StartPackLoader\\config\\SIM_CONFIG.XML.xml");
        String ip_address = xmlReader.getParameter("IpAddress", "D:\\temp\\java\\source_code\\StartPackLoader\\config\\SIM_CONFIG.XML.xml");
        String db_name = xmlReader.getParameter("DbName", "D:\\temp\\java\\source_code\\StartPackLoader\\config\\SIM_CONFIG.XML.xml");
        
        return false;
    }
}