package com.asiacell.billing.prm.edjv;

import java.io.*;
public class TrunkOutSeparator
{
    public static void main(String args[])
        throws Exception
    {
        String dirArray[] = { "17052007" };
        String baseDir = "D:\\temp\\EDJV\\cdrs";
        String workingDir = "";
        for (int i=0;i<dirArray.length;i++)
        {
            workingDir = baseDir + "\\" + dirArray[i] + "\\";
            File myFile = new File(workingDir);
            String fileList[] = myFile.list();            
            for (int j=0;j<fileList.length;j++)
            {                                
                FileReader voiceFile = new FileReader(workingDir + fileList[j]);
                String outputFile = "d:\\output\\" + fileList[j];
                FileWriter fout = new FileWriter(outputFile);
                BufferedWriter bfwr = new BufferedWriter(fout);
                BufferedReader br = new BufferedReader(voiceFile);
                String myLine="";
                while (br.read() != -1)
                {
                    //System.out.println(fileList[j]);
                    myLine = br.readLine();
                    //System.out.println(myLine.substring(237,245).trim());
                    if (myLine.substring(223,232).trim().equals("100"))
                        bfwr.write(myLine + '\n');
                }
                bfwr.close();
            }                        
        }
    }
}