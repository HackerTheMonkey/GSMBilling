package com.asiacell.billing.undertest;

public class Stack
{
    //Declaration Part
    private int stackArray[] = new int[10];//the stack array
    private int tos;//Top of the Stack
    
    //Variables Initialization part (constructor)
    Stack()
    {
        tos = -1; //the stack pointer is now on the buttom of the stack and the stack is empty        
    }
    
    //the Push method    
    public void push(int i)
    {
        if (tos == 9)
        {
            System.out.println("The stack is full, can not insert more items");            
        }
        else        
            stackArray[++tos] = i;
    }
    
    //The pop method
    public int pop()
    {
        if (tos == -1)
        {
            System.out.println("The stack is empty");
            return 0;
        }
        else
        {
            return stackArray[tos--];
        }
    }
}