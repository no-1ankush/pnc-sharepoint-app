package com.hcl.kx.portal.usaa.pnc.lists.session;

/**
 * 
 * @author Ankush Gupta 
 * 
 * Email: no.1ankush@gmail.com
 * GitHub: https://github.com/no-1ankush
 * LinkedIn: www.linkedin.com/in/no1ankush
 *
 */
public class EmployeeDetails implements Comparable<EmployeeDetails>
{
	private String name;
	private String email;
	private String geo;
	private boolean isOnLeave;
	private boolean isActualMilesWaived;
	private boolean isActualMilesListnerOrOfficer;
	private float totalWorkedHours;
	
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public String getEmail() 
	{
		return email;
	}
	public void setEmail(String email) 
	{
		this.email = email;
	}
	public String getGeo() 
	{
		return geo;
	}
	public void setGeo(String geo) 
	{
		this.geo = geo;
	}
	public boolean isOnLeave() 
	{
		return isOnLeave;
	}
	public void setOnLeave(boolean isOnLeave) 
	{
		this.isOnLeave = isOnLeave;
	}
	public boolean isActualMilesWaived()
	{
		return isActualMilesWaived;
	}
	public void setActualMilesWaived(boolean isActualMilesWaived)
	{
		this.isActualMilesWaived = isActualMilesWaived;
	}
	public boolean isActualMilesListnerOrOfficer() 
	{
		return isActualMilesListnerOrOfficer;
	}
	public void setActualMilesListnerOrOfficer(boolean isActualMilesListnerOrOfficer) 
	{
		this.isActualMilesListnerOrOfficer = isActualMilesListnerOrOfficer;
	}
	public float getTotalWorkedHours() 
	{
		return totalWorkedHours;
	}
	public void setTotalWorkedHours(float totalWorkedHours) 
	{
		this.totalWorkedHours = totalWorkedHours;
	}
	
	@Override
	public int compareTo(EmployeeDetails o) 
	{
		return (int) (totalWorkedHours - o.getTotalWorkedHours());
	}
}
