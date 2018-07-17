package com.hcl.kx.portal.usaa.pnc.app;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * 
 * @author Ankush Gupta 
 * 
 * Email: no.1ankush@gmail.com
 * GitHub: https://github.com/no-1ankush
 * LinkedIn: www.linkedin.com/in/no1ankush
 *
 */
public class PnCSharePointAuthenticator extends Authenticator 
{
	private String userName;
	private char[] password;
	
	public PnCSharePointAuthenticator(String userName, char[] password) 
	{
		this.userName = userName;
		this.password = password;
	}
    public PasswordAuthentication getPasswordAuthentication () 
    {
	    return new PasswordAuthentication(userName, password);
	}
}
