# BichromateSample
Bichromate project to flesh out individual projects.

# How to get started with Bichromate

Create a Mavenized Eclipse TestNG project
Download Bichromate Sample - This will give you the required folders Bichromate needs.
Add dependencies from the POM that is attached
Download Bichromate.jar

extends sTestWebDriverFactory - to gain access in creating all the necesarry webdrivers. Extending sTestWebDriverFactory also gives you access to Bichromate's Factories. You must pass in the Bichromate properties file when extending and creating sTestWebDriverFactory.

extend billFireBaseTestNGDeclaration  for every test you write. This gives you access to Extend reports, and Bichromate reports. Also handles test when they fail by taking screen shots of the failed. page

# Updates
8/28/2016 initial version. Sample project coming soon.  Email DavidWRamer@yahoo.com for more information.

12/6/2016  Added supporting pom files, Directories, and Bichromate.jar
