package com.asiacell.billing.undertest;
import ftp.*;
import java.util.Date;
public class FtpTest01
{
    public static void main(String args[])
            throws Exception
    {
        try
        {
            FtpBean ftp = new FtpBean();
            ftp.ftpConnect("10.205.1.202","hasanein","hashas");
            ftp.setDirectory("/source/");
            ftp.setPassiveModeTransfer(false);
            FtpListResult ftpList = ftp.getDirectoryContent();
            String destDir = "D:\\temp\\dest\\";
            while (ftpList.next())
            {                
                //Download the file
                if (!(ftpList.getName().equals(".") || ftpList.getName().equals("..")))
                {
                    System.out.println(new Date() + " >> Downloading " + ftpList.getName() + " from " + ftp.getServerName() + " : " + ftpList.getSize() / 1024 + " KB");
                    ftp.getAsciiFile(ftpList.getName(),destDir + ftpList.getName(),"\r\n");
                    if (ftpList.getSize() == new java.io.File(destDir + ftpList.getName()).length())
                    {
                        ftp.fileDelete(ftpList.getName());
                    }
                }
            }
            ftp.close();
        }
        catch (Exception e)
        {
            System.out.println(e);            
        }
    }
}
