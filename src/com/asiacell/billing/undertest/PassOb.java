package com.asiacell.billing.undertest;

public class PassOb
{
    public static void main(String args[])
    {
        Test ob1 = new Test(100,200);
        Test ob2 = new Test(100,200);
        Test ob3 = new Test(1000,2000);
        
        if (ob1.equals(ob2))
            System.out.println("ob1 is equal to ob2");
        else
            System.out.println("ob1 is not equal to ob2");
        
        if (ob1.equals(ob3))
            System.out.println("ob1 is equal to ob3");
        else
            System.out.println("ob1 is not equal to ob3");
    }
}