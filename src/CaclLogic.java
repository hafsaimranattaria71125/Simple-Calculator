/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

/**
 *
 * @author DELL
 */
class CaclLogic {
    double num1;
    double num2;
    char oper;
    public  CaclLogic(String equation)
    {
       parseStr(equation);
    }
    private void parseStr(String eq)
    {
        int opInd=0;
        for(int i=0;i<eq.length();i++)
        {
            char c=eq.charAt(i);
            if(c=='+'||c=='-'||c=='*'||c=='/')
            {
                opInd=i;
                oper=c;
            }
        }
        num1=Double.parseDouble(eq.substring(0, opInd));
        num2=Double.parseDouble(eq.substring(opInd+1));       
    }
    public String solve()
    {
        
        double ans=0;
        switch(oper)
           {
            case '+':
                ans=num1+num2;
                break;
            case '-':
                ans=num1-num2;
                break;
            case '*':
                ans=num1*num2;
                break;
            default:
                if(num2==0)
                {
                    return  ("Can't Divide by zero");
                }
                ans=num1/num2;
            
        }
        
        return ans+"";
        
    }
    
}
