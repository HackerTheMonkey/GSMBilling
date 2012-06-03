package com.asiacell.billing.mds.nrtrde;

import java.util.*;

public class test1
{
    public static void main(String args[])
            throws Exception
    {      
        // Get the timestamp elements as String Values
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(Calendar.getInstance().get(Calendar.HOUR));
        String minute = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        String second = String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
        // Add a leading zero to the single values elements
        if (month.length() == 1) month = "0" + month;
        if (day.length() == 1) day = "0" + day;
        if (hour.length() == 1) hour = "0" + hour;
        if (minute.length() == 1) minute = "0" + minute;
        if (second.length() == 1) second = "0" + second;
        // Concatnate the elements to form the timestamp
        String timestamp = year + month + day + hour + minute + second;
        System.out.println(timestamp);
    }
}