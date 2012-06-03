package com.asiacell.billing.undertest;

class TestStack
{
    public static void main(String args[])
    {
        Stack stack01 = new Stack();
        Stack stack02 = new Stack();
        for (int i=0;i<10;i++)
            stack01.push(i);
        for (int i=10;i<20;i++)
            stack02.push(i);
        for (int i=0;i<10;i++)
            System.out.println(stack01.pop());
        for (int i=10;i<20;i++)
            System.out.println(stack02.pop());
    }
}