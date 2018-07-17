package com.hcl.kx.portal.usaa.pnc.ui.applet;

import java.awt.Component;
import java.awt.Font;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import com.hcl.kx.portal.usaa.pnc.app.PnCSharePointBaseApp;
import com.hcl.kx.portal.usaa.pnc.app.actualmiles.ActualMilesDefaulterApp;
import com.hcl.kx.portal.usaa.pnc.app.utility.PnCSharePointUtility;

/**
 * 
 * @author Ankush Gupta 
 * 
 * Email: no.1ankush@gmail.com
 * GitHub: https://github.com/no-1ankush
 * LinkedIn: www.linkedin.com/in/no1ankush
 *
 */
public class OneClickActualMilesDefaultersApp extends PnCSharePointBaseApplet
{

	private static final long serialVersionUID = -2111079756546788892L;
	
	public JLabel dateLB;
	public JTextField dateTF;
	
	/**
	 * PSVM
	 * @param args
	 */
	public static void main(String[] args) 
	{
	    SwingUtilities.invokeLater(new Runnable()
	    {
	        @Override
	        public void run() 
	        {
	            new OneClickActualMilesDefaultersApp();
	        }
	    });
	}
	
	@Override
	protected String getAppHeading() 
	{
		return "PnC - One Click Actual Miles Defaulters App";
	}

	@Override
	protected void _init() 
	{
		dateLB = new JLabel("Date to validate (YYYY-MM-DD)   :", 0);
		dateLB.setFont(new Font("Serif", Font.BOLD, 16));
		
		dateTF = new JTextField(15);
		dateTF.setText(PnCSharePointUtility.getDefaultDateAsString());
		
		contentPane.add(dateLB);
		layout.putConstraint(SpringLayout.WEST, dateLB, 5, SpringLayout.WEST, contentPane);
	    layout.putConstraint(SpringLayout.NORTH, dateLB, 25, SpringLayout.NORTH, passwordLB);
	    
		contentPane.add(dateTF);
		layout.putConstraint(SpringLayout.WEST, dateTF, 10, SpringLayout.EAST, dateLB);
	    layout.putConstraint(SpringLayout.NORTH, dateTF, 25, SpringLayout.NORTH, passwordTF);
		
	}

	@Override
	protected String getSubmitButtonLabel() 
	{
		return "E-Mail Defaulter List";
		
	}

	@Override
	protected void _validateUserInput() throws Exception 
	{
		validateDateField();
	}
	
	/**
	 * Validates the Date Field
	 * @throws Exception 
	 */
	private void validateDateField() throws Exception
	{
		Calendar UserCalendar = PnCSharePointUtility.getStringAsCalendar(dateTF.getText());
		Calendar todaysCalendar = Calendar.getInstance();
		Calendar beforeThirtyDays = Calendar.getInstance();
		beforeThirtyDays.add(Calendar.DATE, -30);
		
		if(UserCalendar.after(todaysCalendar)
				|| UserCalendar.before(beforeThirtyDays))
		{
			throw new Exception("Invalid Date");
		}
	}

	@Override
	protected void _submitButtonEventFired() throws Exception 
	{
		ActualMilesDefaulterApp actualMilesDefaulterApp = new ActualMilesDefaulterApp();
		
		PnCSharePointBaseApp.date = dateTF.getText();
		
		actualMilesDefaulterApp.execute();
		
		System.exit(0);
	}

	@Override
	protected Component getLastUIComponent() 
	{
		return dateTF;
	}
}
