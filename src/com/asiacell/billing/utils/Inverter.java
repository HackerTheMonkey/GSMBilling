package com.asiacell.billing.utils;

import java.io.*;
public class Inverter
{
	public static void main(String args[])
		throws IOException
	{
            try
            {
                    FileInputStream inFile = new FileInputStream(args[0]);
                    FileOutputStream outFile = new FileOutputStream(args[1]);
                    int i;
                    int j;
                    do
                    {
                        i = inFile.read();
                        j = inFile.read();
                        if (i != 13 & i != 10 & j != 13 & j != 10)
                        {
                            if (j != -1){
                                System.out.print((char) j);
                                outFile.write(j);
                            }
                            if (i != -1){
                                System.out.print((char) i);
                                outFile.write(i);
                            }
                        }
                        else if (j == 13)
                        {
                            if (i != -1){
                                System.out.println((char) i);
                                outFile.write(i);
                                outFile.write(13);
                                outFile.write(10);
                                i = inFile.read();							
                            }
                        }
                        else if (i == 13 && j == 10)
                        {
                            System.out.println();
                            outFile.write(10);
                            //outFile.write(13);
                        }
                }while (i != -1);
                inFile.close();
                outFile.close();
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                System.out.println();
                System.out.println("         ################################################################");
                System.out.println("         #                                                              #");
                System.out.println("         # Usage: java Inverter <Input File> <Output File>              #");
                System.out.println("         #                                                              #");
                System.out.println("         # ASIACELL TELECOM /CORE NETWORK / Hasanein Ali 5/7/2007       #");
                System.out.println("         #                                                              #");
                System.out.println("         #  if your path contain spaces,surround it with double quotes  #");
                System.out.println("         ################################################################");
            }
	}
}