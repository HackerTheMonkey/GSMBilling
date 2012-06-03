package com.asiacell.billing.undertest;

import java.io.*;
public class BoxDemo
{
    public static void main(String args[])
    {
        Box b1 = new Box();
        Box b2 = new Box(5);
        Box b3 = new Box(10,20,30);
        Box b4 = new Box(b2);
        Double vol;
        
        vol = b1.volume();
        System.out.println(vol);
        vol = b2.volume();
        System.out.println(vol);
        vol = b3.volume();
        System.out.println(vol);
        vol = b4.volume();
        System.out.println(vol);
        
        for (long h=0;h<1000000000;h++);
        for (long h=0;h<1000000000;h++);
        for (long h=0;h<1000000000;h++);
        for (long h=0;h<1000000000;h++);
        for (long h=0;h<1000000000;h++);
    }
    

}