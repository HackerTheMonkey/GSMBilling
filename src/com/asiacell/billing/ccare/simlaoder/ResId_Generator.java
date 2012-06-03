package com.asiacell.billing.ccare.simlaoder;

import com.asiacell.billing.utils.*;
import java.sql.*;

/**
 *
 * @author hasanein
 */
public class ResId_Generator
{

    /**
     * This method will be used to access the Customer Schema to get
     * the Resource ID of the given Resource Type
     * available resource types are:
     * MSISDN(20), SIM(30), Startpack(60)
     * @param String resType: The type of the required resource as mentioned above
     * @return The resource current sequence of 
     * the given resource type
     * @throws java.lang.Exception
     */
    public String getId(String resType)
            throws Exception 
    {
        // Get the resource sequence as per the given resource type
        OracleConnector oraConn01 = new OracleConnector();
        Connection conn01 = oraConn01.connectMe("ccare", "acccare", "192.168.117.12", "BOSS1");
        Statement stmt01 = conn01.createStatement();
        ResultSet rs01 = stmt01.executeQuery("Select a.cur_sequence From tmp_res_id_gen a Where a.res_type_id='" + resType + "'");
        String result = null;
        if (rs01.next() != false)
        {
            result = String.valueOf(rs01.getInt(1));
            rs01.close();
            stmt01.close();
            conn01.close();
            return String.valueOf(result);
        } 
        else 
        {
            rs01.close();
            stmt01.close();
            conn01.close();
            return result;
        }
    }

    /**
     * This method will be used to increment the sequence of the
     * given resource type after the current sequence has been used 
     * for inserting a specific record in the database
     * @param resType
     * @return
     * @throws java.lang.Exception
     */
    public boolean incId(String resType)
            throws Exception {
        OracleConnector oraConn02 = new OracleConnector();
        Connection conn02 = oraConn02.connectMe("ccare", "acccare", "192.168.117.12", "BOSS1");
        conn02.setAutoCommit(true);
        Statement stmt02 = conn02.createStatement();
        boolean result = stmt02.execute("update tmp_res_id_gen set cur_sequence = cur_sequence + 1 where res_type_id=" + resType);
        return result;
    }
}