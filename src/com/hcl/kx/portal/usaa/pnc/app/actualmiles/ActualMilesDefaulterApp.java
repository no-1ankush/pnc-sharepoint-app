package com.hcl.kx.portal.usaa.pnc.app.actualmiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hcl.kx.portal.usaa.pnc.app.PnCSharePointBaseApp;
import com.hcl.kx.portal.usaa.pnc.app.leavedetails.TodayLeaveDetailsApp;
import com.hcl.kx.portal.usaa.pnc.app.teamdetails.ActiveTeamDetailsApp;
import com.hcl.kx.portal.usaa.pnc.app.utility.PnCEmailingUtility;
import com.hcl.kx.portal.usaa.pnc.app.utility.PnCSharePointConstants;
import com.hcl.kx.portal.usaa.pnc.app.utility.PnCSharePointUtility;
import com.hcl.kx.portal.usaa.pnc.lists.session.EmployeeDetails;
import com.microsoft.sharepoint.webservices.GetListItems;
import com.microsoft.sharepoint.webservices.GetListItemsResponse;
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
public class ActualMilesDefaulterApp extends PnCSharePointBaseApp 
{
	/**
	 * PSVM
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			//Retrieve and Store the Team Member Who filled Actual Miles
			new ActualMilesDefaulterApp().execute();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	@Override
	public void execute() throws Exception 
	{
		try 
		{
			//Retrieve and Store the Active Team Member Details
			new ActiveTeamDetailsApp().execute();
			
			//Retrieve Leave Details
			new TodayLeaveDetailsApp().execute();
			
			System.out.println("\nRetrieving Actual Miles Authors..");
			
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
			
			System.out.println("\nActual Miles Authors Loaded..");
			System.out.println("\nDetermining Actual Miles Defaulters..");
			
			//Process the Actual Miles Defaulter List
			processActualMilesDefaulters();
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new Exception("Error caught while determining Actual Miles Defaulters: " + ex.getMessage());
		}
	}

	/**
	 * Builds and Make the WebService Call
	 * 
	 * @return GetListItemsResponse.GetListItemsResult
	 * @throws Exception
	 */
	@Override
	public GetListItemsResponse.GetListItemsResult buildAndSendRequest() throws Exception 
	{	
		ListsSoap listsSoap = getListsSoap();
		
		GetListItems.ViewFields viewFields = new GetListItems.ViewFields();
		viewFields.getContent().add(PnCSharePointUtility.createNodeFromXml(PnCSharePointConstants.ACTUAL_MILES_VIEW_FIELDS_XML));
		
		GetListItems.QueryOptions msQueryOptions = new GetListItems.QueryOptions();
		msQueryOptions.getContent().add(PnCSharePointUtility.createNodeFromXml(PnCSharePointConstants.LISTS_QUERY_OPTIONS_XML));
		
		GetListItems.Query msQuery = new GetListItems.Query();
		msQuery.getContent().add(PnCSharePointUtility.createNodeFromXml(
														PnCSharePointConstants.ACTUAL_MILES_QUERY_XML.replace(
																PnCSharePointConstants.ACTUAL_MILES_DEFAULTER_DATE, date).replace(
																		PnCSharePointConstants.ACTUAL_MILES_DEFAULTER_DATE_1, 
																						PnCSharePointUtility.getPlusOneDateAsString(date))));
		
		return listsSoap.getListItems(PnCSharePointConstants.ACTUAL_MILES_LIST_NAME, 
																			PnCSharePointConstants.ACTUAL_MILES_VIEW_NAME, 
																			msQuery, 
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
	public void processSoapResponse(GetListItemsResponse.GetListItemsResult soapResponse) throws Exception 
	{
		Element element = (Element) soapResponse.getContent().get(0);
		NodeList nodeList = element.getElementsByTagName(PnCSharePointConstants.SOAP_ROW_TAG_NAME);
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if(PnCSharePointUtility.isNamedItemValid(node, PnCSharePointConstants.ACTUAL_MILES_AUTHOR_ID)
					&& PnCSharePointUtility.isNamedItemValid(node, PnCSharePointConstants.ACTUAL_MILES_DATE_ID))
			{
				if(PnCSharePointConstants.isDebugingEnabled)
				{
					System.out.println(PnCSharePointUtility.splitAfterHashString(node.getAttributes().getNamedItem(
																			PnCSharePointConstants.ACTUAL_MILES_AUTHOR_ID).getNodeValue()));
				}
				
				String emailID = PnCSharePointUtility.splitAfterHashString(node.getAttributes().getNamedItem(
																				PnCSharePointConstants.ACTUAL_MILES_AUTHOR_ID).getNodeValue());
				String inputHours = node.getAttributes().getNamedItem(PnCSharePointConstants.ACTUAL_MILES_HOURS_ID).getNodeValue();
				String inputMins = node.getAttributes().getNamedItem(PnCSharePointConstants.ACTUAL_MILES_MINS_ID).getNodeValue();
				
				for(EmployeeDetails employeeDetails: employeeDetailList)
				{
					if(employeeDetails.getEmail().equalsIgnoreCase(emailID))
					{
						employeeDetails.setTotalWorkedHours(PnCSharePointUtility.getTotalHours(employeeDetails.getTotalWorkedHours(), inputHours, inputMins));
					}
				}
			}
		}
	}
	
	/**
	 * process Actual Miles Defaulter List
	 * @throws Exception 
	 */
	private void processActualMilesDefaulters() throws Exception
	{
		List<EmployeeDetails> onShoreDefaulters = new ArrayList<>();
		List<EmployeeDetails> offShoreDefaulters = new ArrayList<>();
		List<EmployeeDetails> nearShoreDefaulters = new ArrayList<>();
		List<EmployeeDetails> offshoreIdleEmployees = new ArrayList<>();
		List<EmployeeDetails> nearshoreIdleEmployees = new ArrayList<>();
		List<EmployeeDetails> actualMilesListenerEmployees = new ArrayList<>();
		List<EmployeeDetails> actualMilesDefaulterEmployees = new ArrayList<>();

		for(EmployeeDetails employeeDetails : employeeDetailList)
		{
			if(employeeDetails.getTotalWorkedHours() < 8
					&& !employeeDetails.isActualMilesWaived()
					&& !employeeDetails.isOnLeave())
			{
				if(employeeDetails.getTotalWorkedHours() == 0)
				{
					actualMilesDefaulterEmployees.add(employeeDetails);
					if(employeeDetails.getGeo().startsWith(PnCSharePointConstants.ON_SHORE_PREFIX_STRING))
					{
						onShoreDefaulters.add(employeeDetails);
					}
					else if(employeeDetails.getGeo().startsWith(PnCSharePointConstants.NEAR_SHORE_PRFIX_STRING))
					{
						nearShoreDefaulters.add(employeeDetails);
					}
					else
					{
						offShoreDefaulters.add(employeeDetails);
					}
				}
				
				if(employeeDetails.getGeo().startsWith(PnCSharePointConstants.OFF_SHORE_PREFIX_STRING))
				{
					offshoreIdleEmployees.add(employeeDetails);
				}
				else if(employeeDetails.getGeo().startsWith(PnCSharePointConstants.NEAR_SHORE_PRFIX_STRING))
				{
					nearshoreIdleEmployees.add(employeeDetails);
				}
			}
			
			if(employeeDetails.isActualMilesListnerOrOfficer())
			{
				actualMilesListenerEmployees.add(employeeDetails);
			}
		}
		
		Collections.sort(offshoreIdleEmployees);
		Collections.sort(nearshoreIdleEmployees);
		
		PnCEmailingUtility.sendEmail(actualMilesDefaulterEmployees, actualMilesListenerEmployees,
											buildDefaultersMessageContent(onShoreDefaulters, nearShoreDefaulters, offShoreDefaulters), 
											"Actual Miles Defaulters - " + date);
		
		PnCEmailingUtility.sendEmail(actualMilesListenerEmployees, null,
											buildIdleEmployeeMessageContent(offshoreIdleEmployees, nearshoreIdleEmployees), 
											"Team Members with Bandwidth - " + date);
	}
	
	/**
	 * Builds the Email Body for the Actual Miles Defaulter
	 * 
	 * @param onShoreDefaulters
	 * @param nearShoreDefaulters
	 * @param offShoreDefaulters
	 * @param date
	 * @return String
	 */
	private static String buildDefaultersMessageContent(List<EmployeeDetails> onShoreDefaulters, 
													List<EmployeeDetails> nearShoreDefaulters, 
													List<EmployeeDetails> offShoreDefaulters)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<FONT COLOR=\"#1F497D\" />");
		sb.append("Team,");
		sb.append("<br/><br/>");
		sb.append("You have not filled the Actual Miles for "+date);
		sb.append("<br/><br/>");
		sb.append("Please fill the Actual Miles on Daily Basis.");
		sb.append("<br/>");
		sb.append("<a href='http://kx.hcl.com/engagement/usaa/portal/hubs/pnc/Lists/Team%20Productivity/Today.aspx'>http://kx.hcl.com/engagement/usaa/portal/hubs/pnc/Lists/Team%20Productivity/Today.aspx</a>");
		sb.append("<br/>");
		
		sb.append("<h4><u>Off-Shore</u></h4>");
		if(!offShoreDefaulters.isEmpty())
		{
			sb.append("<ol>");
			for(EmployeeDetails employeeDetails : offShoreDefaulters)
			{
				sb.append("<li>" + employeeDetails.getName() + "</li>");
			}
			sb.append("</ol>");
		}
		else
		{
			sb.append("<b>None. Kudos to Off-Shore Team!</b><br/>");
		}
		sb.append("<h4><u>Near-Shore</u></h4>");
		if(!nearShoreDefaulters.isEmpty())
		{
			sb.append("<ol>");
			for(EmployeeDetails employeeDetails : nearShoreDefaulters)
			{
				sb.append("<li>" + employeeDetails.getName() + "</li>");
			}
			sb.append("</ol>");
		}
		else
		{
			sb.append("<b>None. Kudos to Near-Shore Team!</b><br/>");
		}
		sb.append("<h4><u>On-Shore</u></h4>");
		if(!onShoreDefaulters.isEmpty())
		{
			sb.append("<ol>");
			for(EmployeeDetails employeeDetails : onShoreDefaulters)
			{
				sb.append("<li>" + employeeDetails.getName() + "</li>");
			}
			sb.append("</ol>");
		}
		else
		{
			sb.append("<b>None. Kudos to On-Shore Team!</b><br/><br/>");
		}
		
		sb.append("<b><u>Note:</u></b> ");
		sb.append("Team Members who was on Leave for the day, please update the same in the Leave Tracker.");
		sb.append("<br/>");
		sb.append("<a href='http://kx.hcl.com/engagement/usaa/portal/hubs/pnc/Lists/Leave%20Details/AllItems.aspx'>http://kx.hcl.com/engagement/usaa/portal/hubs/pnc/Lists/Leave%20Details/AllItems.aspx</a>");
		sb.append("<br/><br/>");
		
		sb.append("Thanks & Regards,<br/> P&C Robo.</FONT>");
		
		sb.append("<br/><br/><hr>");
		sb.append("<small><u>Disclaimer:</u> This is an auto-generated email based on data updated in P&C SharePoint.");
		sb.append("<br/>");
		sb.append("If you have any questions please reach out to <a href='mailto:ankush-g@hcl.com'>Ankush Gupta</a>.</small>");
		sb.append("<br/><hr><br/>");
		return sb.toString();
	}

	/**
	 * Builds the Email Body for the Actual Miles Defaulter
	 * 
	 * @param onShoreDefaulters
	 * @param nearShoreDefaulters
	 * @param offShoreDefaulters
	 * @param date
	 * @return String
	 */
	private static String buildIdleEmployeeMessageContent(List<EmployeeDetails> offShoreIdleEmployees, 
													List<EmployeeDetails> nearShoreIdleEmployees)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<FONT COLOR=\"#1F497D\" />");
		sb.append("Dear Listners,");
		sb.append("<br/><br/>");
		sb.append("Following employees had bandwidth on " + date + ".");
		sb.append("<br/>");
		sb.append("Please check with their availability today and assign work appropriately..");
		sb.append("<br/>");
		
		sb.append("<h4><u>Off-Shore</u></h4>");
		if(!offShoreIdleEmployees.isEmpty())
		{
			sb.append("<table style='font-family: arial, sans-serif; border-collapse: collapse;'>");
			sb.append("<tr>");
			sb.append("<th style='text-align: left;'>Employee Name</th>");
			sb.append("<th>Bandwidth (in Hours)</th>");
			sb.append("</tr>");
			for(EmployeeDetails employeeDetails : offShoreIdleEmployees)
			{
				sb.append("<tr><td style='border: 1px solid #dddddd; text-align: left;padding: 8px;'>");
				sb.append(employeeDetails.getName() + "</td>");
				sb.append("<td style='border: 1px solid #dddddd; text-align: left;padding: 8px;'>");
				sb.append((8 - employeeDetails.getTotalWorkedHours()) + " Hours </td>");
				sb.append("</tr>");
			}
			sb.append("</table>");
		}
		sb.append("<h4><u>Near-Shore</u></h4>");
		if(!nearShoreIdleEmployees.isEmpty())
		{
			sb.append("<table style='font-family: arial, sans-serif; border-collapse: collapse;'>");
			sb.append("<tr>");
			sb.append("<th style='text-align: left;'>Employee Name</th>");
			sb.append("<th>Bandwidth (in Hours)</th>");
			sb.append("</tr>");
			for(EmployeeDetails employeeDetails : nearShoreIdleEmployees)
			{
				sb.append("<tr><td style='border: 1px solid #dddddd; text-align: left;padding: 8px;'>");
				sb.append(employeeDetails.getName() + "</td>");
				sb.append("<td style='border: 1px solid #dddddd; text-align: left;padding: 8px;'>");
				sb.append((8 - employeeDetails.getTotalWorkedHours()) + " Hours </td>");
				sb.append("</tr>");
			}
			sb.append("</table>");
		}
		
		sb.append("<br/><br/>");
		
		sb.append("Thanks & Regards,<br/> P&C Robo.</FONT>");
		
		sb.append("<br/><br/><hr>");
		sb.append("<small><u>Disclaimer:</u> This is an auto-generated email based on data updated in P&C SharePoint.");
		sb.append("<br/>");
		sb.append("If you have any questions please reach out to <a href='mailto:ankush-g@hcl.com'>Ankush Gupta</a>.</small>");
		sb.append("<br/><hr><br/>");
		return sb.toString();
	}

}
