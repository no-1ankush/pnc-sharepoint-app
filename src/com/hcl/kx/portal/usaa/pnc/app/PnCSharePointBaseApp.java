package com.hcl.kx.portal.usaa.pnc.app;

import java.net.Authenticator;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.hcl.kx.portal.usaa.pnc.app.utility.PnCSharePointConstants;
import com.hcl.kx.portal.usaa.pnc.lists.session.EmployeeDetails;
import com.microsoft.sharepoint.webservices.GetListItemsResponse;
import com.microsoft.sharepoint.webservices.Lists;
import com.microsoft.sharepoint.webservices.ListsSoap;

/**
 * 
 * @author Ankush Gupta 
 * 
 * Email: no.1ankush@gmail.com
 * GitHub: https://github.com/no-1ankush
 * LinkedIn: www.linkedin.com/in/no1ankush
 *
 */
public abstract class PnCSharePointBaseApp 
{
	//COMMON APPLICATION DATA
	public static List<EmployeeDetails> 			employeeDetailList;
	public static String							defaultersString;
	public static String							userName;
	public static char[]							password;
	public static String							date;
	
	//ABSTRACT METHODS
	public abstract GetListItemsResponse.GetListItemsResult buildAndSendRequest() throws Exception;
	public abstract void processSoapResponse(GetListItemsResponse.GetListItemsResult soapResponse) throws Exception;
	public abstract void execute() throws Exception;

	//COMMON METHODS
	/**
	 * 
	 * @return ListsSoap
	 * @throws Exception
	 */
	protected ListsSoap getListsSoap() throws Exception 
	{
		Lists listsService = new Lists(new URL(PnCSharePointConstants.WSDL), new QName(PnCSharePointConstants.Q_NAME_URI, PnCSharePointConstants.Q_NAME_LOCAL_LISTS));
		ListsSoap port = listsService.getListsSoap();
		
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, getUserName());
		bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, new String(getPassword()));
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, PnCSharePointConstants.URI_END_POINT);
		
		return port;
	}

	/**
	 * Returns the userName
	 * @return String
	 */
	private String getUserName()
	{
		if(PnCSharePointConstants.isDebugingEnabled)
		{
			//TODO Enter your userName
			return "hcltech\\username";
		}
		return userName;
	}
	
	/**
	 * Returns the password
	 * @return String
	 */
	private char[] getPassword()
	{
		if(PnCSharePointConstants.isDebugingEnabled)
		{
			//TODO Enter your password
			return "password".toCharArray();
		}
		return password;
	}
	
	/**
	 * Initializes Application
	 * @throws Exception
	 */
	protected void initialize()
	{
		java.net.CookieManager cookieManager = new java.net.CookieManager();
		java.net.CookieHandler.setDefault(cookieManager);
		Authenticator.setDefault(new PnCSharePointAuthenticator(getUserName(), getPassword()));
	}
}
