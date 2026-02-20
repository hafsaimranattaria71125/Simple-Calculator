/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.lang.Math;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;

/**
 *
 * @author DELL
 */
public class CalcGrid {
    JFrame fr;
    JTextField tf1;
    JTextField tf2;
    JPanel  textPanel;   
    JPanel calGrid;
    CaclLogic logic;
    public CalcGrid()
    {
        initGUI();
    }
    public void initGUI()
    {
        
        //Buttons to be added to calculator
        String buttons[]={
            "π","e","C","DEL",
            "7","8","9","/",
            "4","5","6","*",
            "1","2","3","-",
            ".","0","=","+"
        };
        
        //text field 
        tf1=new JTextField();
        tf2=new JTextField();
        tf1.setEditable(false);
        tf2.setEditable(false);
        //tf1.setHorizontalAlignment(JTextField.RIGHT);
       // tf2.setHorizontalAlignment(JTextField.RIGHT);
        tf1.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 28));
        tf2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 32));
       //text panel 
        textPanel=new JPanel();
        textPanel.setLayout(new GridLayout(2,1));
        textPanel.setSize(400,400);
        textPanel.add(tf1);
        textPanel.add(tf2);
        
        //Grid Layout for calculator
        calGrid=new JPanel();
        calGrid.setSize(200, 200);
        calGrid.setVisible(true);
        calGrid.setLayout(new GridLayout(5,4));
        for(String text : buttons)
        {
            JButton btn=new JButton(text);
            btn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
            calGrid.add(btn);
            btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 String command = e.getActionCommand();
                 handleCommand(command);
            } });
        }
        //Calculator frame
        fr=new JFrame();
        fr.setSize(450, 500);
        fr.setVisible(true);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setLayout(new BorderLayout());
        
       
        fr.add(textPanel, BorderLayout.NORTH);
        fr.add(calGrid,BorderLayout.CENTER);
        
    }
    private void handleCommand(String command)
    {
      
       String current = tf1.getText();//current complete input eq
       String prevRes=tf2.getText();
       char lastChar=' ';
       if(current.length()>0){
       lastChar=current.charAt(current.length()-1);}
      switch(command){
        case "C":
            tf1.setText("");
            tf2.setText("");
            break;
        
        case "DEL":
            if(current.length()==0)
            {
              return;
            }
            current = current.substring(0, current.length() - 1); 
            tf1.setText(current );
            break;
          
        case "=":           
            if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
                    current = current.substring(0, current.length() - 1);
                }
             if (current.isEmpty()) return;
            if (current.matches(".*[+\\-*/].*")){
            logic=new CaclLogic(current);
            tf1.setText(current + command);
            String ans=logic.solve();
            tf2.setText(ans);
            }
            else// 6=
            {   
                tf1.setText(current + command);
                tf2.setText(current);
            }
            break;
            
        case "π":
        case "e":
            if(lastChar=='=')// e.g scenerio 9=π
            {
             tf1.setText(command);
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
            tf1.setText(current+Math.E);
             }else{
            tf1.setText(current+Math.PI);}
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
                    tf1.setText(prevRes+command);
                }
                else if (current.matches(".*[+\\-*/].*")) {
                    logic = new CaclLogic(current);
                    String ans = logic.solve();
                    tf2.setText(ans);
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
             tf1.setText(current + command);
             break; 
        default:
        //default case for numbers
         if(lastChar=='=')// e.g scenerio 6+3=55
         {
             tf1.setText(command);
         }
         else{
         tf1.setText(current + command);   
         }
      }
}
}