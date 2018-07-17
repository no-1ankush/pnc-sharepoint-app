package com.hcl.kx.portal.usaa.pnc.app.leavedetails;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hcl.kx.portal.usaa.pnc.app.PnCSharePointBaseApp;
import com.hcl.kx.portal.usaa.pnc.app.utility.PnCSharePointConstants;
import com.hcl.kx.portal.usaa.pnc.app.utility.PnCSharePointUtility;
import com.hcl.kx.portal.usaa.pnc.lists.session.EmployeeDetails;
import com.microsoft.sharepoint.webservices.GetListItems;
import com.microsoft.sharepoint.webservices.GetListItemsResponse;
import com.microsoft.sharepoint.webservices.GetListItemsResponse.GetListItemsResult;
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
public class TodayLeaveDetailsApp extends PnCSharePointBaseApp 
{

	public static void main(String[] args)
	{
		try 
		{
			if(PnCSharePointConstants.isDebugingEnabled)
			{
				date = PnCSharePointUtility.getDefaultDateAsString();
			}
			
			//Retrieve and Store the Team Member Who filled Actual Miles
			new TodayLeaveDetailsApp().execute();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
	}
	
	@Override
	public void execute() throws Exception 
	{
		System.out.println("\nRetrieving Today's Leave Details..");
		
		//Initialize the Application
		initialize();
		
		//Built the Soap Request and make the Web Service Call
		GetListItemsResponse.GetListItemsResult soapResponse = buildAndSendRequest();
		
		//Validate the Soap Response
		PnCSharePointUtility.validateSoapResponse(soapResponse);
		
		//Print the Soap Response in Console
		PnCSharePointUtility.writeSoapResultToConsole(soapResponse);
		
		//Process Response
		processSoapResponse(soapResponse);
		
		System.out.println("\nToday's Leave Details Loaded..");
	}
	
	@Override
	public GetListItemsResult buildAndSendRequest() throws Exception 
	{	
		ListsSoap listsSoap = getListsSoap();
		
		GetListItems.ViewFields viewFields = new GetListItems.ViewFields();
		viewFields.getContent().add(PnCSharePointUtility.createNodeFromXml(PnCSharePointConstants.LEAVE_DETAILS_VIEW_FIELDS_XML));
		
		GetListItems.QueryOptions msQueryOptions = new GetListItems.QueryOptions();
		msQueryOptions.getContent().add(PnCSharePointUtility.createNodeFromXml(PnCSharePointConstants.LISTS_QUERY_OPTIONS_XML));
		
		GetListItems.Query msQuery = new GetListItems.Query();
		msQuery.getContent().add(PnCSharePointUtility.createNodeFromXml(
														PnCSharePointConstants.LEAVE_DETAILS_QUERY_XML.replaceAll(
																PnCSharePointConstants.LEAVE_DETAILS_DATE, date)));
		
		return listsSoap.getListItems(PnCSharePointConstants.LEAVE_DETAILS_LIST_NAME, 
																			PnCSharePointConstants.LEAVE_DETAILS_VIEW_NAME, 
																			msQuery, 
																			viewFields, 
																			PnCSharePointConstants.LISTS_ROW_LIMIT, 
																			msQueryOptions, 
																			PnCSharePointConstants.EMPTY_STRING);
	}

	@Override
	public void processSoapResponse(GetListItemsResult soapResponse) throws Exception 
	{
		Element element = (Element) soapResponse.getContent().get(0);
		NodeList nodeList = element.getElementsByTagName(PnCSharePointConstants.SOAP_ROW_TAG_NAME);
		
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if(PnCSharePointUtility.isNamedItemValid(node, PnCSharePointConstants.LEAVE_DETAILS_NAME_ID))
			{
				if(PnCSharePointConstants.isDebugingEnabled)
				{
					System.out.println(PnCSharePointUtility.splitAfterHashString(node.getAttributes().getNamedItem(
																PnCSharePointConstants.LEAVE_DETAILS_NAME_ID).getNodeValue()));
				}
				String employeeName = PnCSharePointUtility.splitAfterHashString(node.getAttributes().getNamedItem(
																PnCSharePointConstants.LEAVE_DETAILS_NAME_ID).getNodeValue());
				for(EmployeeDetails employeeDetails : employeeDetailList)
				{
					if(employeeDetails.getName().equalsIgnoreCase(employeeName))
					{
						employeeDetails.setOnLeave(true);
					}
				}
			}
		}
	}
}
