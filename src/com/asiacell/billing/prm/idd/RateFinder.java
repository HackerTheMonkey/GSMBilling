package com.asiacell.billing.prm.idd;

import java.sql.*;
public class RateFinder {
    // Instance Variables Declaration Start
    
    private static Connection dbcon=null;
    
    // Instance Variables Declaration End
    
    //RateFinder Constructor
    public RateFinder(Connection dbcon) {
        super();
        this.dbcon=dbcon;
    }
    
    /**
     * If the called number is null, then there is no charge rate and
     * no section code will be returned
     */
    private boolean isCalledEmpty(String calledNumber) {
        boolean status = false;
        if (calledNumber.length() == 0) {
            status = true;
        }
        return status;
    }
    
    /**
     * get the charge rate and the called section code basing on the passed called number
     */
    private String[] getRateAndAccessCode(String calledNumber,String iddPartner,String applyTime) {
        //local variables declaration start
        String rate_and_sc[] = new String[2];
        
        Statement rateFinderStatement1=null;
        Statement rateFinderStatement2 =null;
        ResultSet accessCodesList=null;
        ResultSet rate_sc=null;
        
        String firstCharacter = "";
        String getMatchedAccessCodeSQL="";
        String matchedAccessCode = "";
        String tempAccessCode = "";
        String finalAccessCode = "";
        String getRateAndSectionCodeSQL="";
        //local variables declaration end
        
        try{
            
            rateFinderStatement1=(Statement) dbcon.createStatement();
            rateFinderStatement2 = (Statement) dbcon.createStatement();
            
            firstCharacter = calledNumber.substring(0,1);
            getMatchedAccessCodeSQL = "select substr(mapping_sour,7,100) from mapping_list_sep_2007 a where a.mapping_id='181' and a.mapping_sour like '" + iddPartner + "," + firstCharacter + "%' order by length(substr(mapping_sour,7,100))";
            accessCodesList = rateFinderStatement1.executeQuery(getMatchedAccessCodeSQL);
            
            if (accessCodesList.next() == false) {
                String tempArray[] = { "0","0" };
                return tempArray;
            }
            
            do
            {
                matchedAccessCode = accessCodesList.getString(1);
                if (matchedAccessCode.length() > calledNumber.length()) continue;
                tempAccessCode = calledNumber.substring(0,matchedAccessCode.length());
                if (tempAccessCode.equals(matchedAccessCode)) {
                    finalAccessCode = matchedAccessCode;
                }
            } while(accessCodesList.next());
            
            getRateAndSectionCodeSQL = "select mapping_dest,substr(mapping_sour,13,100) from mapping_list_sep_2007 a where a.mapping_id='197' and a.applytime='" + applyTime + "' and mapping_sour in (select '" + iddPartner + ",SV301,'||mapping_dest from mapping_list_sep_2007 b where b.mapping_id='181' and b.mapping_sour='" + iddPartner + "," + finalAccessCode + "')";
            //System.out.println(accessCode03 + ":" + iddPartner);//for Debugging
            rate_sc = rateFinderStatement2.executeQuery(getRateAndSectionCodeSQL);
            
            if (rate_sc.next() == false) {
                String tempArray[] = { "0","0" };
                return tempArray;
            }
            
            rate_and_sc[0] = rate_sc.getString(1);
            rate_and_sc[1] = rate_sc.getString(2);
            
            //close all the statements and result sets (start)
            rate_sc.close();
            accessCodesList.close();
            rateFinderStatement2.close();
            rateFinderStatement1.close();
            
            rate_sc = null;
            accessCodesList = null;
            rateFinderStatement2 = null;
            rateFinderStatement1=null;
            //close all the statements and result sets (end)
            
        } catch(Exception ex) {
            System.out.println("getRate");
            System.out.println(ex);
        }
        return rate_and_sc;
        
    }
    
    /**
     * to get the appropriate rate to be used basing on the called number
     */
    public String[] getRate(String called,String iddPartner,String applyTime) {
        // returns zero rate and zero section code if the called number is empty
        if (isCalledEmpty(called)) {
            String tempArray[] = { "0","0" };
            return tempArray;
        }
        return getRateAndAccessCode(called,iddPartner,applyTime);
    }
}