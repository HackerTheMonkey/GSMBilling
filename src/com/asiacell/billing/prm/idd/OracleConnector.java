package com.asiacell.billing.prm.idd;

import java.sql.*;
import javax.sql.*;
import oracle.jdbc.pool.*;
public class OracleConnector
{
    public Connection connectMe(String username,String password,String server,String dbName)
        throws Exception
    {
        //Create the pooled connection instance (physical connection)
        OracleConnectionPoolDataSource connPoll = new OracleConnectionPoolDataSource();
        //Set the connection parameters
        String URL = "jdbc:oracle:thin:@//" + server + ":1521/" + dbName;
        connPoll.setURL(URL);
        connPoll.setUser(username);
        connPoll.setPassword(password);
        //Create the pooled connection 
        PooledConnection pc = connPoll.getPooledConnection();
        Connection myConn = pc.getConnection();
        return myConn;
    }
}