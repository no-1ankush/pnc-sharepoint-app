package com.hcl.kx.portal.usaa.pnc.app.utility;

import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.microsoft.sharepoint.webservices.GetListItemsResponse;

/**
 * 
 * @author Ankush Gupta 
 * 
 * Email: no.1ankush@gmail.com
 * GitHub: https://github.com/no-1ankush
 * LinkedIn: www.linkedin.com/in/no1ankush
 *
 */
public class PnCSharePointUtility 
{

	/**
	 * 
	 * @param xmlString
	 * @return Node
	 * @throws Exception
	 */
	public static Node createNodeFromXml(String xmlString) throws Exception 
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setValidating(false);
		
		Document document = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(new StringReader(xmlString)));
		Node node = document.getDocumentElement();
		
		return node;
	}

	/**
	 * Write the SOAP Response to Console
	 * 
	 * @param soapResult
	 * @throws Exception
	 */
	public static void writeSoapResultToConsole(GetListItemsResponse.GetListItemsResult soapResponse) throws Exception 
	{
		if(PnCSharePointConstants.isDebugingEnabled)
		{
			Element element = (Element) soapResponse.getContent().get(0);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(new DOMSource(element.getOwnerDocument()), new StreamResult(new OutputStreamWriter(System.out, "UTF-8")));
		}
	}
	
	/**
	 * Validates the SOAP Response Received..
	 * 
	 * @param soapResponse
	 * @throws Exception
	 */
	public static void validateSoapResponse(GetListItemsResponse.GetListItemsResult soapResponse) throws Exception 
	{
		if (soapResponse == null) 
		{
			throw new Exception("Recevied Response is NULL");
		}
		if(soapResponse.getContent() != null && soapResponse.getContent().get(0) != null)
		{
			if (!(soapResponse.getContent().get(0) instanceof Element)) 
			{
				throw new Exception("Recevied Response Content DOES NOT have type Element. It is " + soapResponse.getClass().getName());
			}
		}
		else
		{
			throw new Exception("Recevied Response Content is NULL");
		}
	}
	
	/**
	 * Split the email from the Author String
	 * 
	 * @param inputString
	 * @return String
	 */
	public static String splitAfterHashString(String inputString)
	{
		if(inputString != null)
		{
			String[] splits = inputString.split(PnCSharePointConstants.HASH_SYMBOL);
			return splits[1];
		}
		
		return PnCSharePointConstants.EMPTY_STRING;
	}
	
	/**
	 * Split the #; from the String
	 * 
	 * @param inputString
	 * @return String[]
	 */
	public static String[] splitAfterHashSemiColonString(String inputString)
	{
		if(inputString != null && !inputString.isEmpty())
		{
			return inputString.split(PnCSharePointConstants.SEMI_COLON_SYMBOL + PnCSharePointConstants.HASH_SYMBOL);
		}
		return null;
	}
	
	/**
	 * Split the email from the Author String
	 * 
	 * @param inputString
	 * @return String
	 */
	public static boolean convertStringToBoolean(String inputString)
	{
		if(inputString != null
				&& inputString.trim().equalsIgnoreCase("yes"))
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if Actual Miles Waiver is enabled or not
	 * 
	 * @param inputString
	 * @return boolean
	 */
	public static boolean isActualMilesWaived(String [] inputStringArray)
	{
		if(inputStringArray != null
				&& inputStringArray.length > 0)
		{
			for(String str : inputStringArray)
			{
				if(PnCSharePointConstants.TEAM_DETAILS_ACTUAL_MILES_WAIVER.equalsIgnoreCase(str))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if the user is an Actual Miles Officer or Listner
	 * 
	 * @param inputString
	 * @return boolean
	 */
	public static boolean isActualMilesListnerOrOfficer(String [] inputStringArray)
	{
		if(inputStringArray != null
				&& inputStringArray.length > 0)
		{
			for(String str : inputStringArray)
			{
				if(PnCSharePointConstants.TEAM_DETAILS_ACTUAL_MILES_LISTENER.equalsIgnoreCase(str)
						|| PnCSharePointConstants.TEAM_DETAILS_ACTUAL_MILES_OFFICER.equalsIgnoreCase(str))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Split the date from the Date Time String
	 * 
	 * @param dateTimeString
	 * @return String
	 */
	public static String retrieveDateFromDateTime(String dateTimeString)
	{
		if(dateTimeString != null)
		{
			String[] splits = dateTimeString.split(PnCSharePointConstants.SPACE_STRING);
			return splits[0];
		}
		
		return PnCSharePointConstants.EMPTY_STRING;
	}
	
	/**
	 * Validates the Named Item
	 * @param node
	 * @param namedItemID
	 * @return
	 */
	public static boolean isNamedItemValid(Node node, String namedItemID)
	{
		return node != null
				&& node.getAttributes() != null
				&& node.getAttributes().getNamedItem(namedItemID) != null
				&& node.getAttributes().getNamedItem(namedItemID).getNodeValue() != null
				&& !node.getAttributes().getNamedItem(namedItemID).getNodeValue().isEmpty();
	}
	
	/**
	 * Returns the default date for the applet Window
	 * One day less than the system date
	 * 
	 * @return String
	 */
	public static String getDefaultDateAsString()
	{
		Calendar calendar = Calendar.getInstance();
		
		if(calendar.get(Calendar.DAY_OF_WEEK) == 2)
		{
			calendar.add(Calendar.DATE, -3);
		}
		else
		{
			calendar.add(Calendar.DATE, -1);
		}
		return new SimpleDateFormat(PnCSharePointConstants.DATE_FORMAT).format(calendar.getTime());
	}
	
	/**
	 * Returns a Date one day greater than the input Date.
	 * 
	 * @param inputDate
	 * @return
	 * @throws ParseException
	 */
	public static String getPlusOneDateAsString(String inputDate) throws ParseException
	{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(PnCSharePointConstants.DATE_FORMAT);
		calendar.setTime(sdf.parse(inputDate));
	
		if(calendar.get(Calendar.DAY_OF_WEEK) == 6)
		{
			calendar.add(Calendar.DATE, 3);
		}
		else
		{
			calendar.add(Calendar.DATE, 1);
		}
		
		return new SimpleDateFormat(PnCSharePointConstants.DATE_FORMAT).format(calendar.getTime());
	}
	
	/**
	 * 
	 * @param inputDate
	 * @return
	 * @throws ParseException 
	 */
	public static Calendar getStringAsCalendar(String inputDate) throws ParseException
	{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(PnCSharePointConstants.DATE_FORMAT);
		calendar.setTime(sdf.parse(inputDate));
		return calendar;
	}
	
	/**
	 * adds the new Hours & minutes to the input hours..
	 * 
	 * @param initialHours
	 * @param inputHours
	 * @param inputMins
	 * @return float
	 */
	public static float getTotalHours(float initialHours, String inputHours, String inputMins)
	{
		int hours = Integer.parseInt(inputHours);
		int minutes = Integer.parseInt(inputMins);
		
		return initialHours + hours + (minutes/60);
	}
}
