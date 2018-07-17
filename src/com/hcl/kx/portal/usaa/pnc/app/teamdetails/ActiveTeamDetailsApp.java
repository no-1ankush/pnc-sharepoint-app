package com.hcl.kx.portal.usaa.pnc.app.teamdetails;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hcl.kx.portal.usaa.pnc.app.PnCSharePointBaseApp;
import com.hcl.kx.portal.usaa.pnc.app.utility.PnCSharePointConstants;
import com.hcl.kx.portal.usaa.pnc.app.utility.PnCSharePointUtility;
import com.hcl.kx.portal.usaa.pnc.lists.session.EmployeeDetails;
import com.microsoft.sharepoint.webservices.GetListItems;
import com.microsoft.sharepoint.webservices.GetListItemsResponse;
import com.microsoft.sharepoint.webservices.ListsSoap;
import com.microsoft.sharepoint.webservices.GetListItemsResponse.GetListItemsResult;

/**
 * 
 * @author Ankush Gupta 
 * 
 * Email: no.1ankush@gmail.com
 * GitHub: https://github.com/no-1ankush
 * LinkedIn: www.linkedin.com/in/no1ankush
 *
 */
public class ActiveTeamDetailsApp extends PnCSharePointBaseApp
{
	/**
	 * PSVM
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			new ActiveTeamDetailsApp().execute();
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
		try 
		{
			System.out.println("\nRetrieving Active Team Members..");
			
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
			
			System.out.println("\nActive Team Members Loaded..");
		} 
		catch (Exception ex)
		{
			throw new Exception("Error caught while retrieving Team details: " + ex.getMessage());
		}
		
	}
	
	/**
	 * Builds and Make the WebService Call
	 * 
	 * @return GetListItemsResponse.GetListItemsResult
	 * @throws Exception
	 */
	@Override
	public GetListItemsResult buildAndSendRequest() throws Exception 
	{	
		ListsSoap listsSoap = getListsSoap();
		
		GetListItems.ViewFields viewFields = new GetListItems.ViewFields();
		viewFields.getContent().add(PnCSharePointUtility.createNodeFromXml(PnCSharePointConstants.TEAM_DETAILS_VIEW_FIELDS_XML));
		
		GetListItems.QueryOptions msQueryOptions = new GetListItems.QueryOptions();
		msQueryOptions.getContent().add(PnCSharePointUtility.createNodeFromXml(PnCSharePointConstants.LISTS_QUERY_OPTIONS_XML));
		
		return listsSoap.getListItems(PnCSharePointConstants.TEAM_DETAILS_LIST_NAME, 
																			PnCSharePointConstants.TEAM_DETAILS_VIEW_NAME, 
																			null, 
																			viewFields, 
																			PnCSharePointConstants.LISTS_ROW_LIMIT, 
																			msQueryOptions, 
																			PnCSharePointConstants.EMPTY_STRING);
	}
	
	/**
	 * processes the SOAP Response Received..
	 * 
	 * @param soapResponse
	 * @throws Exception
	 */
	@Override
	public void processSoapResponse(GetListItemsResult soapResponse) throws Exception 
	{
		Element element = (Element) soapResponse.getContent().get(0);
		NodeList nodeList = element.getElementsByTagName(PnCSharePointConstants.SOAP_ROW_TAG_NAME);
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if(PnCSharePointUtility.isNamedItemValid(node, PnCSharePointConstants.TEAM_DETAILS_NAME_ID)
					&& PnCSharePointUtility.isNamedItemValid(node, PnCSharePointConstants.TEAM_DETAILS_EMAIL_ID)
					&& PnCSharePointUtility.isNamedItemValid(node, PnCSharePointConstants.TEAM_DETAILS_GEO_ID))
			{
				if(employeeDetailList == null)
				{
					employeeDetailList = new ArrayList<>();
				}
				
				EmployeeDetails employeeDetails = new EmployeeDetails();
				
				if(PnCSharePointConstants.isDebugingEnabled)
				{
					System.out.println("Name: " + node.getAttributes().getNamedItem(
											PnCSharePointConstants.TEAM_DETAILS_NAME_ID).getNodeValue());
					System.out.println("Email ID: " + PnCSharePointUtility.splitAfterHashString(node.getAttributes().getNamedItem(
											PnCSharePointConstants.TEAM_DETAILS_EMAIL_ID).getNodeValue()));
					System.out.println("Geo: " + PnCSharePointUtility.splitAfterHashString(node.getAttributes().getNamedItem(
											PnCSharePointConstants.TEAM_DETAILS_GEO_ID).getNodeValue()));
					if(node.getAttributes().getNamedItem(PnCSharePointConstants.TEAM_DETAILS_SPECIAL_PRIVILEDGES_FIELD_ID) != null)
					{
						System.out.println("Special Privildges: " + node.getAttributes().getNamedItem(
								 				PnCSharePointConstants.TEAM_DETAILS_SPECIAL_PRIVILEDGES_FIELD_ID).getNodeValue());	
					}
				}
				
				employeeDetails.setName(node.getAttributes().getNamedItem(
											PnCSharePointConstants.TEAM_DETAILS_NAME_ID).getNodeValue());
				employeeDetails.setEmail(PnCSharePointUtility.splitAfterHashString(node.getAttributes().getNamedItem(
											PnCSharePointConstants.TEAM_DETAILS_EMAIL_ID).getNodeValue()));
				employeeDetails.setGeo(PnCSharePointUtility.splitAfterHashString(node.getAttributes().getNamedItem(
											PnCSharePointConstants.TEAM_DETAILS_GEO_ID).getNodeValue()));
				if(node.getAttributes().getNamedItem(PnCSharePointConstants.TEAM_DETAILS_SPECIAL_PRIVILEDGES_FIELD_ID) != null)
				{
					String[] specialPriviledges = PnCSharePointUtility.splitAfterHashSemiColonString(node.getAttributes().getNamedItem(
			 				PnCSharePointConstants.TEAM_DETAILS_SPECIAL_PRIVILEDGES_FIELD_ID).getNodeValue());
					if(specialPriviledges != null)
					{
						employeeDetails.setActualMilesWaived(PnCSharePointUtility.isActualMilesWaived(specialPriviledges));
						employeeDetails.setActualMilesListnerOrOfficer(PnCSharePointUtility.isActualMilesListnerOrOfficer(specialPriviledges));
					}
				}
				employeeDetailList.add(employeeDetails);
			}
		}
	}
}
