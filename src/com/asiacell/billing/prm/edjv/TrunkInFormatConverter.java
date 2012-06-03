package com.asiacell.billing.prm.edjv;

import java.io.*;
public class TrunkInFormatConverter
{
	public static String generateSpace(int num)
	{
		String space = "";
		for (int i=0;i<num;i++) space += " ";
		return space;		
	}
	public static void main(String args[])
		throws Exception
	{
		String dirPath = "D:\\temp\\EDJV\\";
		String filesPath = "";
		//to get the directories list inside the dirPath
		File dir01 = new File(dirPath);
		String dirList01[] = dir01.list();
		for (int i=0;i<dirList01.length;i++)
		{
			File dir02 = new File(dirPath + dirList01[i]);
			String dirList02[] = dir02.list();
			FileWriter outputFile = new FileWriter("d:\\trunkin_" + i + ".txt");
			for (int j=0;j<dirList02.length;j++)
			{
				String filePath = dirPath + dirList01[i] + "\\" + dirList02[j];
				FileReader file01 = new FileReader(filePath);
				BufferedReader buffer01 = new BufferedReader(file01);
				while (buffer01.read() != -1)
				{
					String myLine = buffer01.readLine();
					if (myLine.substring(225,235).trim().equals("100"))
					{
						//outputFile.write(myLine + '\n');
						String cdrType = myLine.substring(1,5).trim();
						String caller = myLine.substring(8,31).trim();
						String called = myLine.substring(65,86).trim();
						String trunkIn = myLine.substring(225,235).trim();
						String trunkOut = myLine.substring(237,246).trim();
						String beginTime = myLine.substring(125,138).trim();
						String beginTimeAdd = myLine.substring(138,145).trim();
						String endTime = myLine.substring(145,157).trim();
						String endTimeAdd = myLine.substring(157,164).trim();
						String duration = myLine.substring(164,174).trim();
						String mscId = myLine.substring(174,189).trim();
						String causeForTerm = myLine.substring(268,271).trim();
						String crln = myLine.substring(480,481).trim();						
						outputFile.write("4" + generateSpace(5 - 1));
						outputFile.write(caller + generateSpace(22 - caller.length()));
						outputFile.write(called + generateSpace(22 - called.length()));
						outputFile.write(trunkIn + generateSpace(10 - trunkIn.length()));
						outputFile.write(trunkOut + generateSpace(10 - trunkOut.length()));
						outputFile.write(beginTime + generateSpace(12 - beginTime.length()));
						outputFile.write(beginTimeAdd + generateSpace(7 - beginTimeAdd.length()));
						outputFile.write(endTime + generateSpace(12 - endTime.length()));
						outputFile.write(endTimeAdd + generateSpace(7 - endTimeAdd.length()));
						outputFile.write(duration + generateSpace(10 - duration.length()));
						outputFile.write(mscId + generateSpace(15 - mscId.length()));
						outputFile.write(causeForTerm + generateSpace(5 - causeForTerm.length()));
						outputFile.write(crln + generateSpace(2 - crln.length()) + (char) 13 + (char) 10);						
					}					
				}
			}
			outputFile.close();
		}
	}
}