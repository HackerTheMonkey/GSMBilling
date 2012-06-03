package com.asiacell.billing.prm.idd;

public class TollFeeCalculator 
{
    public static double calTollFee(double occupied_time_long,double total_charge_rate) {
        double toll_fee = 0.00;
        toll_fee = (occupied_time_long/60) * total_charge_rate;
        double toll_fee_multiplied = toll_fee * 1000;
        long toll_fee_rounded_long = Math.round(toll_fee_multiplied);
        double toll_fee_rounded_double = toll_fee_rounded_long;
        double toll_fee_devided = toll_fee_rounded_double / 1000;
        toll_fee = toll_fee_devided;
        return toll_fee;
    }
}