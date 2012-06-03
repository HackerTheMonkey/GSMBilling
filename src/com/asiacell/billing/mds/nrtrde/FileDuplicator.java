/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.asiacell.billing.mds.nrtrde;

/**
 *
 * @author Hasanein
 */
import java.io.*;
public class FileDuplicator
{
    public static void main(String args[])
            throws Exception
    {
        String srcFile = "d:\\test01.txt";
        String destDir = "d:\\dest";
        for (int i = 12312 ; i < 13312 ; i++)
        {
            File src = new File(srcFile);
            FileReader fr = new FileReader(srcFile);
            BufferedReader br = new BufferedReader(fr);
            FileWriter fw = new FileWriter(destDir + "\\" + "BAG_G9_" + i + ".MID");
            BufferedWriter bf = new BufferedWriter(fw);
            String line = "";
            while ((line = br.readLine()) != null)
            {
                bf.write(line);
                bf.newLine();
            }
            bf.close();
            fw.close();
            br.close();
            fr.close();
        }
    }
}
