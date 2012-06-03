package com.asiacell.billing.prm.idd;

/*
 * dbConnection.java
 *
 * Created on October 20, 2007, 12:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author hasanein.ali
 */

import java.sql.*;
import javax.sql.*;
import oracle.jdbc.pool.*;

public class dbConnection {
    
    private static Connection oraDBcon=null;
    
    private String url="";
    private String user="";
    private String pwd="";
    
    /** Creates a new instance of dbConnection */
    public dbConnection(String url,String user,String pwd) {
        super();
        this.url=url;
        this.user=user;
        this.pwd=pwd;
        initailConn(url,user,pwd);
    }
    
    
    public Connection getConnection() {
        return oraDBcon;
    }
    
    
    /**
     *Initailizing database connection
     *Paramater : url, user, pwd
     */
    private static boolean initailConn(String url,String user,String pwd) {
        boolean connStat=false;
        try{
            OracleConnectionPoolDataSource connPoll = new OracleConnectionPoolDataSource();
            
            connPoll.setURL(url);
            connPoll.setUser(user);
            connPoll.setPassword(pwd);
            
            PooledConnection myPoolledConnection = connPoll.getPooledConnection();
            oraDBcon = myPoolledConnection.getConnection();
            oraDBcon.setAutoCommit(false);
            connStat=true;
        }catch(Exception ex) {
            //System.out.println(ex.);
            ex.printStackTrace();
        }
        return connStat;
    }
    
}
