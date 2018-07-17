package com.hcl.kx.portal.usaa.pnc.ui.applet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.hcl.kx.portal.usaa.pnc.app.PnCSharePointBaseApp;

/**
 * 
 * @author Ankush Gupta 
 * 
 * Email: no.1ankush@gmail.com
 * GitHub: https://github.com/no-1ankush
 * LinkedIn: www.linkedin.com/in/no1ankush
 *
 */
public abstract class PnCSharePointBaseApplet extends JFrame
{
	private static final long serialVersionUID = 5568018440511209385L;
	
	public JLabel userNameLB;
	public JLabel passwordLB;
	
	public JLabel errorLB;
	public JTextField userNameTF;
	public JPasswordField passwordTF;
	public Container contentPane;
	public SpringLayout layout;
	public JButton submitButton;
	
	public PnCSharePointBaseApplet()
	{
		init();
	}
	
	/**
	 * 
	 */
	public void init() 
	{
		JFrame frame = new JFrame(getAppHeading());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		contentPane = frame.getContentPane();
		layout = new SpringLayout();
	    contentPane.setLayout(layout);
	    contentPane.setBackground(Color.lightGray);
	    
	    errorLB = new JLabel();
	    errorLB.setForeground(Color.RED);
	    
		// Construct the TextFields & Buttons
		userNameLB = new JLabel("HCL Username (domain\\username) :", 0);
		userNameLB.setFont(new Font("Serif", Font.BOLD, 16));
		userNameTF = new JTextField(15);
		userNameTF.setText("hcltech\\");
		
		passwordLB = new JLabel("HCL Password                                   :", 0);
		passwordLB.setFont(new Font("Serif", Font.BOLD, 16));
		passwordTF = new JPasswordField(15);
		
		submitButton = new JButton(getSubmitButtonLabel());
		
	    //Add the Text Fields and Buttons on Applet
		contentPane.add(errorLB);
	    layout.putConstraint(SpringLayout.WEST, errorLB, 5, SpringLayout.WEST, contentPane);
	    layout.putConstraint(SpringLayout.NORTH, errorLB, 10, SpringLayout.NORTH, contentPane);
	    
	    contentPane.add(userNameLB);
	    layout.putConstraint(SpringLayout.WEST, userNameLB, 5, SpringLayout.WEST, contentPane);
	    layout.putConstraint(SpringLayout.NORTH, userNameLB, 25, SpringLayout.NORTH, errorLB);
	    
	    contentPane.add(userNameTF);
	    layout.putConstraint(SpringLayout.WEST, userNameTF, 10, SpringLayout.EAST, userNameLB);
	    layout.putConstraint(SpringLayout.NORTH, userNameTF, 25, SpringLayout.NORTH, errorLB);
	    
	    contentPane.add(passwordLB);
	    layout.putConstraint(SpringLayout.WEST, passwordLB, 5, SpringLayout.WEST, contentPane);
	    layout.putConstraint(SpringLayout.NORTH, passwordLB, 25, SpringLayout.NORTH, userNameLB);
	    
	    contentPane.add(passwordTF);
	    layout.putConstraint(SpringLayout.WEST, passwordTF, 10, SpringLayout.EAST, passwordLB);
	    layout.putConstraint(SpringLayout.NORTH, passwordTF, 25, SpringLayout.NORTH, userNameTF);
	    
	    _init();
	    
	    contentPane.add(submitButton);
	    layout.putConstraint(SpringLayout.WEST, submitButton, 145, SpringLayout.WEST, contentPane);
	    layout.putConstraint(SpringLayout.NORTH, submitButton, 45, SpringLayout.NORTH, getLastUIComponent());
	    
	    layout.putConstraint(SpringLayout.EAST, contentPane, 150, SpringLayout.EAST, submitButton);
	    layout.putConstraint(SpringLayout.SOUTH, contentPane, 10, SpringLayout.SOUTH, submitButton);
	    
	    //Create Action Listener
	    SubmitButtonListner actionListener = new SubmitButtonListner();
	    submitButton.addActionListener(actionListener);
	    
	    //Display the window.
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
	}

	/**
	 * Initializes the Applet Heading
	 * @return String
	 */
	protected abstract String getAppHeading();
	
	/**
	 * Initializes the Applet specific details
	 */
	protected abstract void _init();
	
	/**
	 * Return the Submit Button Label
	 * @return String
	 */
	protected abstract String getSubmitButtonLabel();
	
	/**
	 * Performs the Applet Specific Validation
	 */
	protected abstract void _validateUserInput() throws Exception;
	
	/**
	 * Performs the Applet Specific Actions
	 */
	protected abstract void _submitButtonEventFired() throws Exception;
	
	/**
	 * Returns the last component added by the Specific applet
	 * @return Component
	 * @throws Exception
	 */
	protected abstract Component getLastUIComponent();
	
	/**
	 * 
	 * @author Ankush Gupta (51380677)
	 *
	 */
	public class SubmitButtonListner implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent actionEvent) 
		{
			try 
			{
				//Validate User Input
				validateUserInput();
				PnCSharePointBaseApp.userName = userNameTF.getText();
				PnCSharePointBaseApp.password = passwordTF.getPassword();
				_submitButtonEventFired();
			} 
			catch (Exception e) 
			{
				errorLB.setText(e.getMessage());
			}
		}
		
		/**
		 * Validates the user input data
		 * @throws Exception 
		 */
		private void validateUserInput() throws Exception
		{
			try
			{
				validatePasswordField();
				validateUserNameField();
				_validateUserInput();
			}
			catch(Exception e)
			{
				throw new Exception("Details provided are in-correct. Please check and try again..");
			}
		}
		
		
		
		/**
		 * Validate the user name against the company standard
		 * @throws Exception 
		 */
		private void validateUserNameField() throws Exception
		{
			if(userNameTF.getText() == null 
					|| userNameTF.getText().isEmpty()
					|| !userNameTF.getText().contains("hcltech"))
			{
				throw new Exception("Invalid UserName..");
			}
		}
		
		/**
		 * Validated the user Password against the Basic lenght check
		 * @throws Exception 
		 */
		private void validatePasswordField() throws Exception
		{
			if(passwordTF.getPassword() == null 
					|| passwordTF.getPassword().length < 8)
			{
				throw new Exception("Invalid password..");
			}
		}
	}
}
