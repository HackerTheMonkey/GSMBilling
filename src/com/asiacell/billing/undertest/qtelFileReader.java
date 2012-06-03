package com.asiacell.billing.undertest;

import java.io.*;
public class qtelFileReader 
{
    public static void main(String args[])
            throws Exception
    {
        FileReader fw = new FileReader("D:\\documents\\Billing System & IN\\Current Work\\QTel International Rates\\Edit1");
        BufferedReader br = new BufferedReader(fw);
        String line = null;
        String opr = null;
        String netType = null;
        String accessCodes = null;
        String elementsArray[] = null;        
        while ((line = br.readLine()) != null)
        {
            elementsArray = line.split(":");
            opr = elementsArray[0].trim();
            netType = elementsArray[1].trim();
            accessCodes = elementsArray[2].trim();
            String accessCodeList[] = null;
            accessCodeList = accessCodes.split(";");
            for (int i=0;i<accessCodeList.length;i++)
            {
                String accessCode = accessCodeList[i].replaceAll("\"", "").trim();
                if (accessCode.contains("-"))//check if the current access code is a range access code
                {
                    //System.out.println(opr + "|" + netType + "|" + accessCode);
                    String minAccessCode = null,maxAccessCode = null;
                    String accArray[] = accessCode.split("-");
                    minAccessCode = accArray[0].trim();
                    maxAccessCode = accArray[1].trim();
                    System.out.println(opr + "|" + netType + "|" + minAccessCode);
                    int newAccessCode = Integer.parseInt(minAccessCode);
                    for (int j=0;j<((Integer.parseInt(maxAccessCode) - Integer.parseInt(minAccessCode)));j++)
                    {                                                
                        System.out.println(opr + "|" + netType + "|" + ++newAccessCode);
                    }
                }
                else
                    System.out.println(opr + "|" + netType + "|" + accessCode);
            }
        }
    }
}
