package com.asiacell.billing.undertest;

/*
 * Incrementor.java
 *
 * Created on October 11, 2007, 2:13 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author hasanein.ali
 */
public class Incrementor {
    int a;
    /** Creates a new instance of Incrementor */
    public Incrementor() {
    }
    public Incrementor(int a)
    {
        this.a = a;
    }
    
    Incrementor incBy10(Incrementor ob)
    {
        ob.a *= 10;
        return ob;
    }
    
}
