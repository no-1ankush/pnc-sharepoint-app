# PnC Sharepoint Application

PnC Sharepoint Application is to help drive the process in PnC Team. PnC Team manages it's team data in Microsoft SharePoint. This Application interacts with the Microsoft SharePoint through Web Services, gather the data and performs process driven tasks..

This application is designed to host multiple processes driven actions or gather information such as 

* Active Team Details
* Team Members on Leave Today
* Team Members Defaulting on filling Actual Miles
* etc..

# One Click Actual Miles Defaulters App

Actual Miles is a in-house tool developed to track the effort put in by each team member daily. This tool helps the team managers derive various metrics such as the Estimated Hours Vs Actual Hours..

The entire process is based on the resposibility of each team member to update the efforts in the Sharepoint, failing to do so can result in incorrect data casaded up the chain. 

This applciation was built in order to valdiate that all the team members update their effort daily and send out a reminder email in case the team members fails to comply with the process..

### User Interface ###

The User needs to enter their Credentials and the Date (*Defaulted to Current Date - 1 since we validate if yesterday's efforts were logged properly in most cases*) they want to valdiate the actual miles

![picture alt](https://github.com/no-1ankush/pnc-sharepoint-app/blob/master/images/PnC_SharePoint_App_One_Click_Actual_Miles_Defauters_App_UI.JPG "One Click Actual Miles Defaulters App User Interface")

### Outcome ###

The outcome would be an email send from the company's network (*In this case from gmail since we are outside the network*) with the names of the team members who failed to update their efforts..

![picture alt](https://github.com/no-1ankush/pnc-sharepoint-app/blob/master/images/PnC_SharePoint_App_One_Click_Actual_Miles_Defauters_App_Output_1.JPG "One Click Actual Miles Defaulters App Outcome")

![picture alt](https://github.com/no-1ankush/pnc-sharepoint-app/blob/master/images/PnC_SharePoint_App_One_Click_Actual_Miles_Defauters_App_Output_2.JPG "One Click Actual Miles Defaulters App Outcome")
