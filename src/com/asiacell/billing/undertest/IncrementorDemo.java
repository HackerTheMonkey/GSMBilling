package com.asiacell.billing.undertest;

/*
 * IncrementorDemo.java
 *
 * Created on October 11, 2007, 2:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author hasanein.ali
 */
public class IncrementorDemo {
    
    /** Creates a new instance of IncrementorDemo */
    public IncrementorDemo() {
    }
    public static void main(String args[])
    {
    Incrementor o1 = new Incrementor(10);
    Incrementor o2;
    System.out.println(o1.a);
    o2 = o1.incBy10(o1);
    System.out.println(o1.a);
    System.out.println(o2.a);
    }
    
}
