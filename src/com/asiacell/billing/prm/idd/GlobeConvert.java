package com.asiacell.billing.prm.idd;

import java.io.*;
public class GlobeConvert
{
    public static void main(String args[])
        throws Exception
    {
        String operatorName = "";
        String basicAccessCode = "";
        String extAccessCode = "";
        String rate = "";
        String lineToBePrinted = "";
        FileReader rateSheetFile = new FileReader("d:\\GLOBE_TEMP.csv");
        BufferedReader rateSheetBuffer = new BufferedReader(rateSheetFile);
        FileWriter outputFile = new FileWriter("d:\\globecom_rates_converted_nov.csv");
        BufferedWriter bf = new BufferedWriter(outputFile);
        String s = "";
        while ((s = rateSheetBuffer.readLine()) != null)
        {
            String splitLine[] = s.split(",");
            operatorName = splitLine[0].trim();
            basicAccessCode = splitLine[1].trim();
            if (splitLine.length == 3)
            {
                rate = splitLine[2].trim();
                lineToBePrinted = basicAccessCode + "," + operatorName + "," + rate;
                bf.write(lineToBePrinted);
                bf.newLine();
            }
            else if (splitLine.length == 4)
            {
                extAccessCode = splitLine[2].trim();
                rate = splitLine[3].trim();
                lineToBePrinted = basicAccessCode + extAccessCode + "," + operatorName + "," + rate;
                bf.write(lineToBePrinted);
                bf.newLine();
            }
            else
            {
                String commaSplitted[] = s.split("\"");
                String allExtAccessCodes[] = commaSplitted[1].split(","); 
                rate = commaSplitted[2].substring(1).trim();
//                for (String extac : allExtAccessCodes)
//                {
//                    lineToBePrinted = basicAccessCode + extac.trim() + "," + operatorName + "," + rate;
//                    bf.write(lineToBePrinted);
//                    bf.newLine();
//                }
            }
        }
        bf.close();
        outputFile.close();
        rateSheetBuffer.close();
        rateSheetBuffer.close();
    }
}