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
public class CalcGUI {
    JFrame fr;
    JTextField tf1;
    JTextField tf2;
    JPanel  textPanel;   
    JPanel calGrid;
    CalcValid valid;
    public CalcGUI()
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
        valid=new CalcValid(this);
        for(String text : buttons)
        {
            JButton btn=new JButton(text);
            btn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
            calGrid.add(btn);
            btn.addActionListener(valid);
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
    
}