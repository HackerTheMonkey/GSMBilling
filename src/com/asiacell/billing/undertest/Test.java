package com.asiacell.billing.undertest;

public class Test
{
    int x;
    int y;
    
    Test(int x,int y)
    {
        this.x = x;
        this.y = y;
    }
    
    boolean equals(Test o)
    {
        if ((o.x == this.x) && (o.y == this.y))
            return true;
        else
            return false;
    }
}