/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author DELL
 */
public class CalcValid implements ActionListener {
    CalcGUI ref;
    CaclLogic logic;
    public CalcValid(CalcGUI g)
    {
        ref=g;
    }
    @Override
    public void actionPerformed(ActionEvent e){
       String command=e.getActionCommand();
       String current = ref.tf1.getText();//current complete input eq
       String prevRes=ref.tf2.getText();
       char lastChar=' ';
       if(current.length()>0){
       lastChar=current.charAt(current.length()-1);}
      switch(command){
        case "C":
            ref.tf1.setText("");
            ref.tf2.setText("");
            break;
        
        case "DEL":
            if(current.length()==0)
            {
              return;
            }
            current = current.substring(0, current.length() - 1); 
            ref.tf1.setText(current );
            break;
          
        case "=":           
            if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
                    current = current.substring(0, current.length() - 1);
                }
             if (current.isEmpty()) return;
            if (current.matches(".*[+\\-*/].*")){
            logic=new CaclLogic(current);
            ref.tf1.setText(current + command);
            String ans=logic.solve();
            ref.tf2.setText(ans);
            }
            else// 6=
            {   
                ref.tf1.setText(current + command);
                ref.tf2.setText(current);
            }
            break;
        case ".":
            if(!current.matches(".*[.].*"))
            {
                ref.tf1.setText(current+command);
            }
            break;
            
        case "π":
        case "e":
            if(lastChar=='=')// e.g scenerio 9=π
            {
             ref.tf1.setText(command);
             break;
            }
            // if user enters number and then select e
             if (!current.isEmpty() && (Character.isDigit(lastChar) || lastChar == '.')) {
             int i = current.length() - 1;
             while (i >= 0 && (Character.isDigit(current.charAt(i)) || current.charAt(i) == '.')) {
                 i--;
             }
             current = current.substring(0, i + 1);
            }
             if(command=="e"){
            ref.tf1.setText(current+Math.E);
             }else{
            ref.tf1.setText(current+Math.PI);}
            break;
      
      // If command is operator
        case "+":
        case "-":
        case "*":
        case "/":
            if (!current.isEmpty()){
                         // If last character is already operator → replace it
                if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
                    current = current.substring(0, current.length() - 1);
                }
                //case 5+2=+
                if(lastChar=='=')
                {
                    current=prevRes;
                    ref.tf1.setText(prevRes+command);
                }
                else if (current.matches(".*[+\\-*/].*")) {
                    logic = new CaclLogic(current);
                    String ans = logic.solve();
                    ref.tf2.setText(ans);
                    current = ans;  // use result for next operation
        }
            }
            else{
                if(prevRes.isEmpty())
                {
                current="0";
                }
                else
                {
                    current=prevRes;
                }
                
            }
             ref.tf1.setText(current + command);
             break; 
        default:
        //default case for numbers
         if(lastChar=='=')// e.g scenerio 6+3=55
         {
             ref.tf1.setText(command);
         }
         else{
         ref.tf1.setText(current + command);   
         }
      }
}
}
