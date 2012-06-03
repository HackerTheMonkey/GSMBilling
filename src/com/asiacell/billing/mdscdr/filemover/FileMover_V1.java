package com.asiacell.billing.mdscdr.filemover;
import java.io.*;
public class FileMover_V1
{
    //Variables Declaration Start
    //static String configFile = "D:\\temp\\mover_test\\src_dest_config.cfg";
    static String configFile = "";
    static String srcDir = "";
    static String destDir1 = "";
    static String destDir2 = "";
    static String date_position = "";
    static String ext = "";
    static String badNameDir = "";
    static String logFileName = "";
    //Variables Declaration End

    // Main Method Start
    public static void main(String args[])
        throws Exception
    {
        configFile = args[0];
        //configFile = "D:\\temp\\mover_test\\src_dest_config.cfg";
        //Read the config file and iterate on the directores
        FileReader configFileReader = new FileReader(configFile);
        BufferedReader configBufferedReader = new BufferedReader(configFileReader);
        String configLine = null;
        while ((configLine = configBufferedReader.readLine()) != null)
        {
            //Extract elements from the config line
            if (configLine.startsWith("#")) continue;//ignore comments
            String configElements[] = configLine.split(",");
            srcDir = configElements[0];
            destDir1 = configElements[1];
            destDir2 = configElements[2];
            date_position = configElements[3];
            ext = configElements[4];
            badNameDir = configElements[5];
            logFileName = configElements[6];
            File sourceDir = new File(srcDir);
            String subDirList[] = sourceDir.list();
            //End extracting elements

            //start create the log file basing on the current date
            String day_of_month = String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH));
            if (day_of_month.length() == 1) day_of_month = "0" + day_of_month;
            String year = String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
            //get a proper value of the current month since java sustitute 0 for JAN
            int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
            String new_month = null;
            if(month != 11 & month != 10 & month != 9)
            {
                new_month = "0" + String.valueOf(month + 1);
                //System.out.println(new_month);
            }
            else
                new_month = String.valueOf(month + 1);
            String currentDate = year + new_month + day_of_month;
            //end create the log file basing on the current date
            FileWriter logFile = new FileWriter(logFileName + "_" + currentDate + ".log",true);
            BufferedWriter logBuffer = new BufferedWriter(logFile);
            //end create the log file basing on the current date

            //Iterate on the directory sub directories
            for (int i=0;i<subDirList.length;i++)
            {
                //work only on directories and ignore files
                if (new File(srcDir + "\\" + subDirList[i]).isDirectory())
                {
                    //get the subdirectory file list
                    File sourceSubDir = new File(srcDir + "\\" + subDirList[i]);
                    String subDirFileList[] = sourceSubDir.list();

                    // Start Iteration on the subdirectory files
                    for (int j=0;j<subDirFileList.length;j++)
                    {
                        if (subDirFileList[j].substring(subDirFileList[j].length() - 3,subDirFileList[j].length()).equals(ext))//to validate the extension
                       {
                            /* Extract the date from the filename and create the respective
                             * directory in the first destination using the date as the directory name
                             */
                            Runtime.getRuntime().exec("C:\\WINDOWS\\mkdir.bat " + destDir1 + "\\" + subDirFileList[j].substring(Integer.parseInt(date_position),Integer.parseInt(date_position) + 8)).waitFor();
                            //Copy the file to the first destination
                            Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\xcopy.exe /Y " + sourceSubDir.toString() + "\\" + subDirFileList[j] + " " + destDir1 + "\\" + subDirFileList[j].substring(Integer.parseInt(date_position),Integer.parseInt(date_position) + 8)).waitFor();
                            logBuffer.write(new java.util.Date() + " >> " + "Copying file " + sourceSubDir + "\\" + subDirFileList[j] + " to " + destDir1);
                            logBuffer.newLine();
                            //check if there is a second destination to copy the file
                            if (!destDir2.equals("no"))
                            {
                                //Copy the file to the second destination
                                Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\xcopy.exe /Y " + sourceSubDir.toString() + "\\" + subDirFileList[j] + " " + destDir2).waitFor();
                                logBuffer.write(new java.util.Date() + " >> " + "Copying file " + sourceSubDir + "\\" + subDirFileList[j] + " to " + destDir2);
                                logBuffer.newLine();
                            }
                            // delete the transfered file from their respective source
                            new File(sourceSubDir.toString() + "\\" + subDirFileList[j]).delete();
                        }
                        else
                        {
                            //move bad name files to their bad name directory
                            Runtime.getRuntime().exec("C:\\WINDOWS\\mv.bat " + srcDir + "\\" + sourceSubDir + "\\" + subDirFileList[j] + " " + badNameDir);
                        }
                    }

                    // End Iteration on the subdirectory files
                }
            }
            //End Iterate on the directory sub directories
        //closing the log buffer
        logBuffer.close();
        logFile.close();
        }
        //End reading the config file
    }
    // Main Method End
}