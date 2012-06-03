package com.asiacell.billing.ccare.unami;

import java.io.File;
public class CDR_Packager
{
    public CDR_Packager()
    {
        //Empty constructor
    }
    
    public void packageCDRs(String cdrDirPath,String currentBillingCycle)
            throws Exception
    {       
        // Package all the collected CDRs into one RAR file
        Process proc = Runtime.getRuntime().exec("C:\\Program Files\\WinRAR\\WinRAR.exe a -r " + cdrDirPath + "\\UNAMI" + currentBillingCycle + ".rar " + cdrDirPath + "\\*");
        //Process proc = Runtime.getRuntime().exec("C:\\Program Files\\WinRAR\\WinRAR.exe a -r D:\\UNAMI_CDRS_200806\\UNAMI200806.rar D:\\UNAMI_CDRS_200806\\*");
        proc.waitFor();
        // Clean all the folders in the CDR path
        File dir = new File(cdrDirPath);
        File dirList[] = dir.listFiles();
        for (int i=0;i<dirList.length;i++)
        {
            if (dirList[i].isDirectory())
            {
                // Delete the contents of the directory
                File subDirList[] = dirList[i].listFiles();
                for (int j=0;j<subDirList.length;j++)
                {
                    subDirList[j].delete();
                }
                // Delete the directory
                dirList[i].delete();
            }
        }
    }
}