package com.asiacell.billing.mds.nrtrde;
import java.io.*;
import java.text.*;
public class nrtrdeInTest01
{
    public static void main(String args[])
            throws Exception
    {
        File inFile = new File("D:\\Projects\\NRTRDE-IN\\NR_HUR_ALLBELTBIRQAC512008.csv");
        FileReader fr = new FileReader(inFile);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String lineArray[] = null;
        String cdrType = null;
        String IMSI = null;
        String eventStartDate = null;
        String eventEndDate = null;
        String duration = null;
        String volume = null;
        String sdrAmount = null;
        String no_of_events = null;
        String formattedImsi = null;
        System.out.println();
        System.out.println("NRTRDE-In System Started");        
        System.out.println("#####################################################");
        while ((line = br.readLine()) != null)
        {
           // Filter the un-wanted lines
           if (line.startsWith("C") || line.startsWith("S") || line.startsWith("P") || line.startsWith("M"))
           {
               lineArray = line.split(",");
               cdrType = lineArray[0];
               IMSI = lineArray[1];
               eventStartDate = lineArray[2] + lineArray[3];
               eventEndDate = lineArray[4] + lineArray[5];
               duration = lineArray[6];
               no_of_events = lineArray[7];
               volume = lineArray[8];
               sdrAmount = lineArray[9];
               formattedImsi = formatImsi(IMSI);
               System.out.println();
               System.out.println(cdrType + "|" + formattedImsi + "|" + eventStartDate + "|" + eventEndDate + "|" + duration + "|" + no_of_events + "|" + volume + "|" + sdrAmount);
           }
        }
        System.out.println();
        System.out.println("#####################################################");
        System.out.println();
        System.out.println("NRTRDE-IN System completed...");
        System.out.println();
        br.close();
        fr.close();
    }

    private static String formatImsi(String imsi)
    {
        String imsiSplitArray[] = imsi.split("E");
        String imsiFloatPart = imsiSplitArray[0];
        String imsiExpoPart = imsiSplitArray[1];
        String expoArray[] = imsiExpoPart.split("\\+");
        String expo = expoArray[1];
        // to format the IMSI number to 15 decimal digits
        NumberFormat nf = new DecimalFormat("000000000000000");
        String retImsi = nf.format(Double.parseDouble(imsiFloatPart) * Math.pow(10d, Double.parseDouble(expo)));
        return retImsi;
    }
}