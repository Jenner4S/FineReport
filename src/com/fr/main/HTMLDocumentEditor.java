package com.fr.main;

	import java.awt.BorderLayout; 
import java.awt.Container; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 

	import javax.swing.Box; 
import javax.swing.BoxLayout; 
import javax.swing.JButton; 
import javax.swing.JFrame; 
import javax.swing.JLabel; 
import javax.swing.JTextField; 

import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
	/**
	 * SWing测试的主界面
	 * @author longgangbai
	 *
	 */
	public class HTMLDocumentEditor  extends JFrame { 
	  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public HTMLDocumentEditor () { 
		    setTitle("My Frame"); 
		    setSize(400, 300); 
		    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		     
		    Container content = getContentPane(); 
		    Box vbox = new Box(BoxLayout.Y_AXIS); 
		    content.add(vbox, BorderLayout.CENTER); 
		     
		    final JLabel showTextLabel = new JLabel(" "); 
		    showTextLabel.setName("show"); 
		    vbox.add(showTextLabel); 
		    final JTextField input = new JTextField(); 
		    input.setName("input"); 
		    vbox.add(input); 
		    JButton button = new JButton("copy");
		    button.setName("copy"); 
		    button.addActionListener(new ActionListener() { 
		
		      @Override 
		      public void actionPerformed(ActionEvent e) { 
		        showTextLabel.setText(input.getText()); 
		      } 
		        
		    }); 
		    vbox.add(button); 
		  } 
	}