package com.hcl.kx.portal.usaa.pnc.app.utility;

/**
 * 
 * @author Ankush Gupta 
 * 
 * Email: no.1ankush@gmail.com
 * GitHub: https://github.com/no-1ankush
 * LinkedIn: www.linkedin.com/in/no1ankush
 *
 */
public class PnCSharePointConstants 
{
	/**
	 * Microsoft WebService Constants
	 */
	public static final String SOAP_ROW_TAG_NAME						= "z:row";
	public static final String Q_NAME_URI								= "http://schemas.microsoft.com/sharepoint/soap/";
	public static final String Q_NAME_LOCAL_LISTS						= "Lists";
	public static final String DATE_FORMAT								= "yyyy-MM-dd";
	
	/**
	 * PnC SharePoint Constants
	 */
	public static final String WSDL 									= "https://kx.hcl.com/engagement/usaa/portal/hubs/pnc/_vti_bin/Lists.asmx?wsdl";
	public static final String URI_END_POINT 							= "https://kx.hcl.com/engagement/usaa/portal/hubs/pnc/_vti_bin/Lists.asmx";
	public static final String EMPTY_STRING 							= "";
	public static final String HASH_SYMBOL								= "#";
	public static final String SEMI_COLON_SYMBOL						= ";";
	public static final String SPACE_STRING								= " ";
	public static final String LISTS_QUERY_OPTIONS_XML					= "<QueryOptions><IncludeMandatoryColumns>FALSE</IncludeMandatoryColumns></QueryOptions>";
	public static final String LISTS_ROW_LIMIT							= "1000";
	public static final String NEAR_SHORE_PRFIX_STRING					= "Near";
	public static final String OFF_SHORE_PREFIX_STRING					= "Off";
	public static final String ON_SHORE_PREFIX_STRING					= "On";
	public static final String PNC_SHAREPOINT_ROBOT_EMAIL_ID			= "pncsharepointrobot@gmail.com";
	public static final String PNC_SHAREPOINT_ROBOT_USER_NAME			= "pncsharepointrobot";
	public static final String PNC_SHAREPOINT_ROBOT_PASSWORD			= "PnCRocks";
	
	
	/**
	 * PnC All Application Constants
	 */
	//Perform the TODO in PnCSharePointBaseApp.java when changing isDebugingEnabled to true.
	public static final boolean isDebugingEnabled						= false;
	public static final boolean isOnHCLNetwork							= true;
	public static final boolean isEmailFeatureTurnedOn					= true;
	public static final boolean isOffshoreUsageApp						= true;
	public static final String PNC_OFFSHORE_SMTP_RELAY_IP				= "10.150.5.85";
	public static final String PNC_NEARSHORE_SMTP_RELAY_IP				= "10.3.0.88";
	public static final String PNC_SMTP_RELAY_IP_PORT					= "25";
	
	/**
	 * Actual Miles List Constants
	 */
	public static final String ACTUAL_MILES_AUTHOR_ID					= "ows_Author";
	public static final String ACTUAL_MILES_DATE_ID	 					= "ows_Date";
	public static final String ACTUAL_MILES_HOURS_ID					= "ows_Hours";
	public static final String ACTUAL_MILES_MINS_ID						= "ows_Min";
	public static final String ACTUAL_MILES_LIST_NAME					= "Actual Miles";
	public static final String ACTUAL_MILES_VIEW_NAME					= "D62AC9EC-2BB5-4544-8FB6-0A135D0E445A";
	
	/**
	 * Team Details List Constants
	 */
	public static final String TEAM_DETAILS_NAME_ID						= "ows_Emp_x0020_Name";
	public static final String TEAM_DETAILS_EMAIL_ID	 				= "ows_email";
	public static final String TEAM_DETAILS_GEO_ID	 					= "ows_Geo_x0020__x0028_Hidden_x0029_";
	public static final String TEAM_DETAILS_SPECIAL_PRIVILEDGES_FIELD_ID= "ows_Special_x0020_Privileges";
	public static final String TEAM_DETAILS_LIST_NAME					= "Team Details";
	public static final String TEAM_DETAILS_ACTUAL_MILES_LISTENER 		= "Actual Miles Listener";
	public static final String TEAM_DETAILS_ACTUAL_MILES_OFFICER 		= "Actual Miles Officer";
	public static final String TEAM_DETAILS_ACTUAL_MILES_WAIVER 		= "Actual Miles Waiver";
	public static final String TEAM_DETAILS_VIEW_NAME					= "C3BC3A4C-9293-4EBE-8B82-ADDCD84F8EA9";
	
	/**
	 * Leave Details List Constants
	 */
	public static final String LEAVE_DETAILS_NAME_ID					= "ows_Author";
	public static final String LEAVE_DETAILS_START_DATE 				= "ows_Start_x0020_Date";
	public static final String LEAVE_DETAILS_END_DATE 					= "ows_End_x0020_Date";
	public static final String LEAVE_DETAILS_LIST_NAME					= "Leave Details";
	public static final String LEAVE_DETAILS_VIEW_NAME					= "9B8D95E1-94FD-4CE2-A4FE-2BB9801D5F72";
	public static final String LEAVE_DETAILS_DATE						= "LEAVE_DETAILS_DATE";
	
	/**
	 * Actual Miles Defaulter Application Constants
	 */
	public static final String ACTUAL_MILES_DEFAULTER_DATE				= "ACTUAL_MILES_DEFAULTER_DATE";
	public static final String ACTUAL_MILES_DEFAULTER_DATE_1			= "ACTUAL_MILES_DEFAULTER_PLUS_ONE_DATE";
	public static final String ACTUAL_MILES_VIEW_FIELDS_XML				= "<ViewFields xmlns=''><FieldRef Name='Author' /><FieldRef Name='Date' /><FieldRef Name='Hours' /><FieldRef Name='Min' /></ViewFields>";
	public static final String ACTUAL_MILES_QUERY_XML					= "<Query xmlns=''><Where><And><Leq><FieldRef Name='Date' /><Value Type='DateTime' >"+ACTUAL_MILES_DEFAULTER_DATE_1+"</Value></Leq><Geq><FieldRef Name='Date' /><Value Type='DateTime' >"+ACTUAL_MILES_DEFAULTER_DATE+"</Value></Geq></And></Where></Query>";
	
	/**
	 * Active Team Details Application Constants
	 */
	public static final String TEAM_DETAILS_VIEW_FIELDS_XML 			= "<ViewFields xmlns=''><FieldRef Name='Emp_x0020_Name' /><FieldRef Name='email' /><FieldRef Name='Geo_x0020__x0028_Hidden_x0029_' /><FieldRef Name='Special_x0020_Privileges'/></ViewFields>";
	
	/**
	 * Today's Leave Details Application Constants
	 */
	public static final String LEAVE_DETAILS_VIEW_FIELDS_XML 			= "<ViewFields xmlns=''><FieldRef Name='Author' /><FieldRef Name='Start_x0020_Date' /><FieldRef Name='End_x0020_Date' /></ViewFields>";
	public static final String LEAVE_DETAILS_QUERY_XML					= "<Query xmlns=''><Where><And><Leq><FieldRef Name='Start_x0020_Date' /><Value Type='DateTime' >"+LEAVE_DETAILS_DATE+"</Value></Leq><Geq><FieldRef Name='End_x0020_Date' /><Value Type='DateTime' >"+LEAVE_DETAILS_DATE+"</Value></Geq></And></Where></Query>";
}
