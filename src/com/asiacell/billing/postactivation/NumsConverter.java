package com.asiacell.billing.postactivation;
public class NumsConverter
{
    public static String convertStrToBin(String msg)
    {
        byte byteMessage[] = msg.getBytes();
        String msgArray[][] = new String[byteMessage.length][2];
        StringBuilder binaryNum = new StringBuilder();
        for (int i = 0 ; i < byteMessage.length ; i++)
        {            
            int val = byteMessage[i];
            for (int j = 0 ; j < 8 ; j++)
            {
                binaryNum.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binaryNum.toString();
    }

    public static String convertBinToHex()
    {
        String hex = "11111111111111111111111111111111";
        long num = (long) Long.parseLong(hex);
        System.out.println(num);
        return "";
    }

    public static int convertBinToDec(String binNumber)
    {
        byte[] array = binNumber.getBytes();
        double decValue = 0;
        int j = 0;
        for (int i = (array.length - 1) ; i >= 0 ; i--)
        {            
            int val = (array[i] == 49) ? 1 : 0;
            decValue += (Math.pow(2,j) * val);
            j++;
        }        
        System.out.println(decValue);
        return 0;
    }
}
