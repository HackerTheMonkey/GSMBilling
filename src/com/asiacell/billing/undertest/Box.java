package com.asiacell.billing.undertest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
public class Box
{
    double width;
    double height;
    double depth;
    
    //constructor that dont have any parameters
    Box()
    {
        width = -1;
        height = -1;
        depth = -1;
    }
    
    //constructor when all the dimensions need to be provided
    Box(double width,double height,double depth)
    {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }
    
    //constructor when a cube need to be created
    Box(double len)
    {
        width = height = depth = len;
    }
    
    Box(Box cloneBox)
    {
        this.width = cloneBox.width;
        this.height = cloneBox.height;
        this.depth = cloneBox.depth;
    }
    
    double volume()
    {
        return width * height * depth;
    }
    
    protected void finalize()
    {
        try {
            FileWriter testfile = new FileWriter("d:\\has.txt");
            testfile.write("test");
            testfile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}