package com.hcl.kx.portal.usaa.pnc.app.utility;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.hcl.kx.portal.usaa.pnc.app.PnCSharePointBaseApp;
import com.hcl.kx.portal.usaa.pnc.lists.session.EmployeeDetails;

/**
 * 
 * @author Ankush Gupta 
 * 
 * Email: no.1ankush@gmail.com
 * GitHub: https://github.com/no-1ankush
 * LinkedIn: www.linkedin.com/in/no1ankush
 *
 */
public class PnCEmailingUtility 
{
	public static void sendEmail(List<EmployeeDetails> toEmployeeList,
									List<EmployeeDetails> ccEmployeeList,
									String emailBody,
									String emailSubject) throws Exception
	{
	      // Get the default Session object.
	      Session session = getMailSession();

	      try
	      {
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(getFromEmailID(), "P&C Robo"));

	         // Set To: header field of the header.
	         addRecipients(message, toEmployeeList, ccEmployeeList);
	         
	         // Set Subject: header field
	         message.setSubject(emailSubject);

	         // Send the actual HTML message
	         message.setContent(emailBody, "text/html" );

	         // Send message
	         Transport.send(message);
	         
	         //Email Send Notification..
	         System.out.println("Sent message successfully....");
	      }
	      catch (MessagingException mex) 
	      {
	         throw new Exception("Unable to send email. Error Message: " + mex.getMessage());
	      }
	}
	
	/**
	 * Set To: header field of the header.
	 * 
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	private static void addRecipients(MimeMessage message, List<EmployeeDetails> toEmployeeList, List<EmployeeDetails> ccEmployeeList) throws AddressException, MessagingException
	{
		if(PnCSharePointConstants.isEmailFeatureTurnedOn)
		{
			if(toEmployeeList != null)
			{
				for(EmployeeDetails employeeDetails : toEmployeeList)
				{
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(employeeDetails.getEmail()));
				}
			}
			if(ccEmployeeList != null)
			{
				for(EmployeeDetails employeeDetails : ccEmployeeList)
				{
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(employeeDetails.getEmail()));
				}
			}
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress("ankush-g@hcl.com"));
		}
		else
		{
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(getUserEmailID()));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("ankush-g@hcl.com"));
		}
	}
	
	/**
	 * Returns the From email ID
	 * @return String
	 */
	private static String getFromEmailID()
	{
		if(PnCSharePointConstants.isOnHCLNetwork)
		{
			return getUserEmailID();
		}
		else
		{
			return PnCSharePointConstants.PNC_SHAREPOINT_ROBOT_EMAIL_ID;
		}
	}
	
	/**
	 * Returns the email ID of the User
	 * @return String
	 */
	private static String getUserEmailID()
	{
		String username = new String(PnCSharePointBaseApp.userName);
		username = username.replaceAll("hcltech", PnCSharePointConstants.EMPTY_STRING);
		username = username.substring(1);
		return username+"@hcl.com";
		
	}
	
	/**
	 * Returns the SMTP Email Properties
	 * @return Properties
	 */
	private static Properties getMailProperties()
	{
		Properties properties = new Properties();
		// Setup mail properties
		if(PnCSharePointConstants.isOnHCLNetwork)
		{
			 if(PnCSharePointConstants.isOffshoreUsageApp)
			 {
				 properties.put("mail.smtp.host", PnCSharePointConstants.PNC_OFFSHORE_SMTP_RELAY_IP);
			 }
			 else
			 {
				 properties.put("mail.smtp.host", PnCSharePointConstants.PNC_NEARSHORE_SMTP_RELAY_IP);
			 }
			 properties.put("mail.smtp.port", PnCSharePointConstants.PNC_SMTP_RELAY_IP_PORT);
			 properties.put("mail.smtp.auth", "true");
			 properties.put("mail.smtp.starttls.enable", "true");
		}
		else
		{
			 properties.put("mail.smtp.host", "smtp.gmail.com");
			 properties.put("mail.smtp.port", "587");
			 properties.put("mail.smtp.auth", "true");
			 properties.put("mail.smtp.socketFactory.port", "465");
		     properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}
	    return properties;
	}
	
	/**
	 * Returns Mail Session Object
	 * @return Session
	 */
	private static Session getMailSession()
	{
		  // Get system properties
	      Properties properties = getMailProperties();
	      
	      // Get the default Session object.
	      Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() 
	      {
	    	  protected PasswordAuthentication getPasswordAuthentication() 
	    	  {
	    		  if(PnCSharePointConstants.isOnHCLNetwork)
	    		  {
	    			  return new PasswordAuthentication(PnCSharePointBaseApp.userName, PnCSharePointBaseApp.password.toString());
	    		  }
	    		  else
	    		  {
	    			  return new PasswordAuthentication(PnCSharePointConstants.PNC_SHAREPOINT_ROBOT_USER_NAME, PnCSharePointConstants.PNC_SHAREPOINT_ROBOT_PASSWORD);
	    		  }
	    	  }
	      });
	      
	      return session;
	}
}
