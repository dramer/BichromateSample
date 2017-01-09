# BichromateSample
Bichromate project to flesh out individual projects.

# Setting up TestNG with Eclipse
http://howtodoinjava.com/testng/testng-tutorial-with-eclipse/

# How to get started with Bichromate

Create a Mavenized Eclipse TestNG project
Download Bichromate Sample - This will give you the required folders Bichromate needs.
Add dependencies from the POM that is attached
Download Bichromate.jar

extends sTestWebDriverFactory - to gain access in creating all the necesarry webdrivers. Extending sTestWebDriverFactory also gives you access to Bichromate's Factories. You must pass in the Bichromate properties file when extending and creating sTestWebDriverFactory. For every new page object created (POM) add that into your webdriverFactory. Then during the execution of a test you simply access the page needed during the test (webdriver.getNewPage().isButtonEnabled())

extend sTestBaseTestNGDeclaration  for every test you write. This gives you access to Extend reports, and Bichromate reports. Also handles test when they fail by taking screen shots of the failed.

Creating Page Object Models you extend sTestBasePageDeclaration - This gives you all the methods for finding objects on the page.

#CI Environment Variables 

Running tests within a CI environment. Billfire Automation takes in the following system parameters

* -DBichromate.spreadsheet=stageTestSetup.xls
* -DBichromate.worksheet= X
* -DBichromate.table=x
* -DBichromate.logFileIP=35.162.206.134
* -DBichromate.dbServerName - server name to access the DataBase
* -DBichromate.dbName - DB schema name
* -Bichromate.sshServer - sshServer name


# Updates
8/28/2016 initial version. Sample project coming soon.  Email DavidWRamer@yahoo.com for more information.

12/6/2016  Added supporting pom files, Directories, and Bichromate.jar

1/9/2017  Added link to detail setting up Eclipse with TestNG
