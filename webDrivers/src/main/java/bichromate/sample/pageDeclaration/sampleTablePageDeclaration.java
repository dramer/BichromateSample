package bichromate.sample.pageDeclaration;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;

import bichromate.baseObjectTypes.sTestTableObject;
import bichromate.baseObjectTypes.sTestTextBoxObject;
import bichromate.core.sTestBasePageDeclaration;

/**
 * This class Demonstrates sampleTablePageDeclaration().
 * <br>sampleTablePageDeclaration encapsulates all the Selenium elements on the . 
 * <br> When building new testNG tests, the tester will create a new sTestWebDriverFactory
 * <br> and create this page using the newly created  a new sTestWebDriverFactory
 * <br> EXAMPLE:  sTestWebDriverFactory driver = new sTestWebDriverFactory(remote,version,platform,browser,setupString)
 * <br> EXAMPLE: driver.getLoginPage().pageURL
 * @see sTestBasePageDeclaration
 * @author davidwramer
 * @version 1.0
 */
@SuppressWarnings("unused")
public class sampleTablePageDeclaration extends sTestBasePageDeclaration{
	
	static ResourceBundle resources;
	String tableName = "tableName";
	//
	// Page Elements
	//
	private sTestTableObject tableElement = null;
	
	static
	{
		try
		{
			resources = ResourceBundle.getBundle("pageDeclarations.sampleTablePageDeclaration",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sampleTablePageDeclaration.properties not found: "+mre);
			System.exit(0);
		}
	}
	/**
	 * method This class Demonstrates sampleTablePageDeclaration().
	 * The sampleTablePageDeclaration uses https://www.w3schools.com/html/html_tables.asp landing page for a table Example 
	 * @param driver webDriver
	 * @author davidwramer
	 * @version 1.0
	 */
	public sampleTablePageDeclaration(WebDriver driver){
		super(driver,resources);
		
		pageTitle = new String(resources.getString("pageTitle"));
		pageURL = new String(resources.getString("pageURL"));

	
	}
	
	/**
	 * Method This class Demonstrates geTable()
	 * @return sTestTableObject - table DOM element 
	 * @author davidwramer
	 * @version 3.0
	 */
	public sTestTableObject geTable(){
		
		if(null == tableElement)
			tableElement = new sTestTableObject(driver,findElement(tableName),"sampleTablePageDeclaration",POMLOGGER);
		return tableElement;
		
	}

}//sampleTablePageDeclaration
