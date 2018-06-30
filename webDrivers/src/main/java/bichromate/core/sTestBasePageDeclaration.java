/*
 * sTestBasePageDeclaration.java	1.0 2013/01/23
 *
 * Copyright (c) 2001 by David Ramer, Inc. All Rights Reserved.
 *
 * David Ramer grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to David Ramer.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. David Ramer AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL David Ramer OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF DRamer HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 * 
 * 
 * 
 */

package bichromate.core;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.http.client.methods.HttpGet;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.apache.commons.lang.time.StopWatch;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
/**
 * @author davidwramer
 * @version 1.0
 *
 */
@SuppressWarnings("unused")
public class sTestBasePageDeclaration {

	private final String XPATH_SELECTOR = "xpath";
	private final String CSS_SELECTOR = "css";
	private final String CLASS_SELECTOR = "class";
	private final String ID_SELECTOR = "id";
	private final String LINK_TEXT_SELECTOR = "linkText";
	private final String TAG_NAME_SELECTOR = "tagName";
	private final String PARTIAL_LINKTEXT_SELECTOR = "partialLinkText";
	private final String NAME_SELECTOR = "name";
	//
	// For Page text validation
	//
	private final String numberTextItemsToValidate = "numberTextItemsToValidate";
	private final String textItem = "textItem";
	private int textValidationCount = 0;

	public WebDriver driver;
	private ResourceBundle resources;
	private boolean validateTextElements;
	public String pageTitle = null;
	public String pageURL = null;
	public String regExPageURL = null;
	private int invalidLinksCount;
	public static pageObjectModelLogFactory POMLOGGER = new pageObjectModelLogFactory();
	
	//
	// Stop Watch for page load times
	//
	private StopWatch pageLoad = null; //new StopWatch();
	private sTestOSInformationFactory path = null;

	public sTestBasePageDeclaration(WebDriver driver, ResourceBundle bundle) {
		this.driver = driver;
		resources = bundle;
		validateTextElements = false;
		pageLoad = new StopWatch(); //new StopWatch();
		path = new sTestOSInformationFactory();
	}

	/**
	 * This method demonstrates validateTextItems(). <br>
	 * validateTextItems, public function to validate page text items from the
	 * properties file. <br>
	 * @return failedCount - number of failed elements on the page
	 * @see WebDriver
	 * @see WebElement
	 * 
	 */
	public int validateTextItems() {

		String textToValidateCount;
		String textToValidate;
		int failedCount = 0;
		int numerOfTextItemsToValidate = 0;
		try {
			textToValidateCount = new String(resources.getString(numberTextItemsToValidate)).trim();
			WebElement bodyTag = driver.findElement(By.tagName("body"));
			numerOfTextItemsToValidate = Integer.parseInt(textToValidateCount);
			for (int x = 1; x <= numerOfTextItemsToValidate; x++) {
				textToValidate = new String(resources.getString(textItem + x)).trim();
				if (!(bodyTag.getText().contains(textToValidate))) {
					POMLOGGER.enterInfoLog(
							"oTestBasePageDeclaration: Warning, Text not found on page:" + textToValidate);
					failedCount++;
				}
			}
		} catch (MissingResourceException e) {
			POMLOGGER.enterSevereLog("oTestBasePageDeclaration: validateTextItems(), resouce not found");
		}
		return failedCount;

	}

	/**
	 * This method demonstrates setElementTextValidation(). <br>
	 * setElementTextValidation, public function to set element text validation
	 * to true. <br>
	 * @param set
	 *            - If true will validate all element text items when calling
	 *            FindElement
	 * @see WebDriver
	 * @see WebElement
	 * 
	 */
	public void setElementTextValidation(boolean set) {
		validateTextElements = set;
	}

	/**
	 * This method demonstrates findElement(). <br>
	 * Find the first WebElement using the given method. See the note in
	 * findElements(By) <br>
	 * about finding via XPath. This method is affected by the 'implicit wait'
	 * times in <br>
	 * force at the time of execution. The findElement(..) invocation will
	 * return a matching row, <br>
	 * or try again repeatedly until the configured timeout is reached.
	 * findElement should not be <br>
	 * used to look for non-present elements, use findElements(By) and assert
	 * zero length response <br>
	 * instead. "Type" is appended to the elementName to find the type of
	 * element that is being requested. <br>
	 * EXAMPE: searchField=//input[@name='q'] <br>
	 * searchFieldType=xpath <br>
	 * <br>
	 * EXCEPTIONS: If no "Type" resource is defined for the element, xpath will
	 * be selected. <br>
	 * <br>
	 * 
	 * @param element
	 *            from the pages properties file.
	 * @return WebElement if found null otherwise
	 * @see WebDriver
	 * @see WebElement
	 * 
	 */
	protected WebElement findElement(String element) {

		String DOM_Element_Type = null; // EXAMPLE: xpath, css, partial link
		String DOM_Element = null; // oDesk element to find, EXAMPLE
									// passwordField
		String DOM_Element_Name = null; // Name of the specific element
		
		WebElement el = null; // Selenium web element that is found
		//
		// First find the element type from the resource file
		//
		try {
			DOM_Element_Type = new String(resources.getString(element + "Type")).trim();
		} catch (MissingResourceException e) {
			POMLOGGER
					.enterInfoLog("oTestBasePageDeclaration: Warning, no Type found in properties file for:" + element);
			DOM_Element_Type = new String(XPATH_SELECTOR);
		}
		//
		// Obtain the DOM element for the element passed in
		//
		try {
			DOM_Element = new String(resources.getString(element));
		} catch (MissingResourceException e) {
			POMLOGGER.enterSevereLog("oTestBasePageDeclaration: Error, element not found in properties file: " + e);
			return null;
		}
		//
		// Find the element on the page based on the findElement(By setting
		//
		try {
			if (DOM_Element_Type.equals(XPATH_SELECTOR)) {
				el = driver.findElement(By.xpath(DOM_Element));
			} else if (DOM_Element_Type.equals(CSS_SELECTOR)) {
				el = driver.findElement(By.cssSelector(DOM_Element));
			} else if (DOM_Element_Type.equals(CLASS_SELECTOR)) {
				el = driver.findElement(By.className(DOM_Element));
			} else if (DOM_Element_Type.equals(ID_SELECTOR)) {
				el = driver.findElement(By.id(DOM_Element));
			} else if (DOM_Element_Type.equals(LINK_TEXT_SELECTOR)) {
				el = driver.findElement(By.linkText(DOM_Element));
			} else if (DOM_Element_Type.equals(TAG_NAME_SELECTOR)) {
				el = driver.findElement(By.tagName(DOM_Element));
			} else if (DOM_Element_Type.equals(PARTIAL_LINKTEXT_SELECTOR)) {
				el = driver.findElement(By.partialLinkText(DOM_Element));
			} else if (DOM_Element_Type.equals(NAME_SELECTOR)) {
				el = driver.findElement(By.name(DOM_Element));
			} else {// default is xpath
				el = driver.findElement(By.xpath(DOM_Element));
				POMLOGGER.enterWarningLog(
						"Error: invalid element type " + DOM_Element + " for the following element " + DOM_Element);
			}
		} catch (Exception e) {
			POMLOGGER.enterWarningLog("Error: element \"Type\" not found on page: " + element);
		}

		if (validateTextElements)
			validateElementText(element);
		return el;
	}// findElement

	/**
	 * This methodemonstrates findElements(). <br>
	 * Find the list of WebElements using the given method. See the note in
	 * findElements(By) <br>
	 * about finding via XPath. This method is affected by the 'implicit wait'
	 * times in <br>
	 * force at the time of execution. The findElement(..) invocation will
	 * return a matching row, <br>
	 * or try again repeatedly until the configured timeout is reached.
	 * findElement should not be <br>
	 * used to look for non-present elements, use findElements(By) and assert
	 * zero length response <br>
	 * instead. "Type" is appended to the elementName to find the type of
	 * element that is being requested. <br>
	 * EXAMPE: searchField=//input[@name='q'] <br>
	 * searchFieldType=xpath <br>
	 * <br>
	 * EXCEPTIONS: If no "Type" resource is defined for the element, xpath will
	 * be selected. <br>
	 * <br>
	 * 
	 * @param element
	 *            from the pages properties file.
	 * @return theList if found null otherwise
	 * @see WebDriver
	 * @see WebElement
	 * 
	 */
	protected List<WebElement> findElements(String element) {

		String DOM_Element_Type = null; // EXAMPLE: xpath, css, partial link
		String DOM_Element = null; // oDesk element to find, EXAMPLE
									// passwordField
		String DOM_Element_Name = null; // Name of the specific element
		List<WebElement> theList = null;
		//
		// First find the element type from the resource file
		//
		try {
			DOM_Element_Type = new String(resources.getString(element + "Type")).trim();
		} catch (MissingResourceException e) {
			POMLOGGER.enterSevereLog("oTestBasePageDeclaration: Warning, no Type found for:" + element);
			DOM_Element_Type = new String(XPATH_SELECTOR);
		}
		//
		// Obtain the DOM element for the element passed in
		//
		try {
			DOM_Element = new String(resources.getString(element));
		} catch (MissingResourceException e) {
			POMLOGGER.enterSevereLog("oTestBasePageDeclaration: Error, element not found: " + e);
			return theList;
		}
		//
		// Find the element on the page based on the findElement(By setting
		//
		try {
			if (DOM_Element_Type.equals(XPATH_SELECTOR)) {
				theList = driver.findElements(By.xpath(DOM_Element));
			} else if (DOM_Element_Type.equals(CSS_SELECTOR)) {
				theList = driver.findElements(By.cssSelector(DOM_Element));
			} else if (DOM_Element_Type.equals(CLASS_SELECTOR)) {
				theList = driver.findElements(By.className(DOM_Element));
			} else if (DOM_Element_Type.equals(ID_SELECTOR)) {
				theList = driver.findElements(By.id(DOM_Element));
			} else if (DOM_Element_Type.equals(LINK_TEXT_SELECTOR)) {
				theList = driver.findElements(By.linkText(DOM_Element));
			} else if (DOM_Element_Type.equals(TAG_NAME_SELECTOR)) {
				theList = driver.findElements(By.tagName(DOM_Element));
			} else if (DOM_Element_Type.equals(PARTIAL_LINKTEXT_SELECTOR)) {
				theList = driver.findElements(By.partialLinkText(DOM_Element));
			} else if (DOM_Element_Type.equals(NAME_SELECTOR)) {
				theList = driver.findElements(By.name(DOM_Element));
			} else {// default is xpath
				theList = driver.findElements(By.xpath(DOM_Element));
				POMLOGGER.enterWarningLog(
						"Error: invalid element type " + DOM_Element + " for the following element " + DOM_Element);
			}
		} catch (Exception e) {
			POMLOGGER.enterSevereLog("Error: element list \"Type\" not found on page: " + element);
		}

		return theList;
	}// findElements

	/**
	 * This method Demonstrates verifyTableContent(). This method searches the
	 * columns in a given list of data.
	 * 
	 * @param searchFor
	 *            - the string to find in the table
	 * @param tr_collection
	 *            - the rows of data
	 * @param colElements
	 *            - column element
	 * @param matchType
	 *            - EXACT/CONTAINS
	 * @return true of searchFor was found, false otherwise
	 * @see WebDriver
	 * @see WebElement
	 * 
	 */
	public boolean verifyTableContent(String searchFor, List<WebElement> tr_collection, String colElements,
			String matchType) {

		POMLOGGER.enterInfoLog("NUMBER OF ROWS IN THIS TABLE = " + tr_collection.size());
		int row_num, col_num;
		row_num = 1;
		for (WebElement trElement : tr_collection) {
			List<WebElement> td_collection = trElement.findElements(By.xpath(colElements));
			POMLOGGER.enterInfoLog("NUMBER OF COLUMNS=" + td_collection.size());
			col_num = 1;
			for (WebElement tdElement : td_collection) {
				POMLOGGER.enterInfoLog("row # " + row_num + ", col # " + col_num + "text=" + tdElement.getText());
				if (matchType.equals("CONTAINS")) {
					if (tdElement.getText().contains(searchFor)) {
						POMLOGGER.enterInfoLog("====FOUND CONTAINING MATCHING TEXT======");
						return true;
					}
				} else {
					if (tdElement.getText().equals(searchFor)) {
						POMLOGGER.enterSevereLog("====FOUND EXACT MATCHING TEXT======");
						return true;
					}
				}

				col_num++;
			}
			row_num++;
		}

		return false;

	}// verifyTableContent

	/**
	 * This method demonstrates validateElementText(). ValidateElementText will
	 * look in the properties file for the text related to a specific element.
	 * Read the element text from the properties file and then try to validate
	 * it on the page <br>
	 * 
	 * @param element
	 *            web element to verify in the properties file
	 * @see WebDriver
	 * @see WebElement
	 * 
	 */
	private void validateElementText(String element) {
		String textToValidate;
		try {
			textToValidate = new String(resources.getString(element + "Text")).trim();
			WebElement bodyTag = driver.findElement(By.tagName("body"));
			if (null != bodyTag) {
				if (!(bodyTag.getText().contains(textToValidate))) {
					POMLOGGER.enterWarningLog(
							"oTestBasePageDeclaration: Warning, element Text not found on page:" + element);
				}
			} else {
				POMLOGGER.enterWarningLog(
						"oTestBasePageDeclaration: Warning, element \"body\" tag not found on page :" + element);
			}
		} catch (MissingResourceException e) {
			POMLOGGER.enterSevereLog(
					"oTestBasePageDeclaration: Severe, no Text type found in properties file for:" + element);
		}

	}// validateElementText

	/**
	 * This method demonstrates clickElement Use this base function if no
	 * specific element function has been created in the elements page
	 * 
	 * @param element
	 *            web elment to click
	 * 
	 */
	public void clickElement(String element) {
		try{
			WebElement elem = findElement(element);
			if (null != elem) {
				stopWatchStart();
				elem.click();
				stopWatchStop(element);
			} else {
				POMLOGGER.enterInfoLog("sTestWebDriverFactory: isElementDisplayed: element not found");
			}
		} catch (Exception e) {
			POMLOGGER.enterInfoLog("sTestWebDriverFactory: isElementDisplayed: element not found");
		}
	}

	/**
	 * This method demonstrates isElementDisplayed(). <br>
	 * Method to check if the element is displayed <br>
	 * 
	 * @param element
	 *            web element if displayed
	 * @return boolean if elment is displayed false otherwise
	 * @throws Exception
	 *             if element is not found
	 * 
	 */
	public boolean isElementDisplayed(String element) throws Exception {
		try {
			WebElement elem = findElement(element);
			if (null != elem) {
				return (elem.isDisplayed());
			} else {
				POMLOGGER.enterInfoLog("sTestWebDriverFactory: isElementDisplayed: element not found");
			}
		} catch (Exception e) {
			POMLOGGER.enterInfoLog("sTestWebDriverFactory: isElementDisplayed: element not found");
		}
		return false;
	}

	/**
	 * This method demonstrates isElementDisplayed(). <br>
	 * Function to check if the element is enabled <br>
	 * 
	 * @param theElement
	 *            if displayed
	 * @return boolean if element is enabled
	 * 
	 */
	public boolean isElementEnabled(String theElement) {
		try {
			WebElement element = findElement(theElement);
			if (null != element) {
				return (element.isEnabled());
			} else {
				POMLOGGER.enterInfoLog("sTestWebDriverFactory: isElementEnabled: element not found");
			}
		} catch (Exception e) {
			POMLOGGER.enterInfoLog("sTestWebDriverFactory: isElementEnabled: element not found");
		}
		return false;
	}// isElementEnabled

	/**
	 * method demonstrates enterElementText Used to send key strokes to a web
	 * element
	 * 
	 * @param text
	 *            description
	 * @param element
	 *            page element to send text to.
	 * 
	 */
	public void enterElementText(String text, String element) {
		try {
			WebElement elem = findElement(element);
			if (null != elem) {
				elem.clear();
				elem.sendKeys(text);
			} else {
				POMLOGGER.enterInfoLog("sTestWebDriverFactory: enterElementText: element not found");
			}
		} catch (Exception e) {
			POMLOGGER.enterInfoLog("sTestWebDriverFactory: enterElementText: element not found");
		}
	}

	/**
	 * This method demonstrates clearElementText(). Clears the given elements
	 * text field.
	 * 
	 * @param theElement
	 *            page element
	 */
	public void clearElementText(String theElement) {
		try {
			WebElement element = findElement(theElement);
			if (element != null) {
				element.clear();
			} else {
				POMLOGGER.enterInfoLog("sTestWebDriverFactory: clearElementText: element not found");
			}
		} catch (Exception e) {
			POMLOGGER.enterInfoLog("sTestWebDriverFactory: clearElementText: element not found");
		}
	}

	/**
	 * verifyFoundPageByURL()
	 * 
	 * @param waitTime
	 *            How long to wait for page to respond
	 * @param URL
	 *            - URL of the page loading
	 * @param testName
	 *            - Name of the test running
	 * @see WebDriverWait
	 * @author DRamer
	 * @version 1.0
	 */
	public void verifyFoundPageByURL(int waitTime, String URL, String testName) {

		final String thisURL = new String(URL);
		try {
			(new WebDriverWait(driver, waitTime)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					boolean result = d.getCurrentUrl().contains(thisURL);
					if (true == result) {
						POMLOGGER.enterInfoLog("verifyFoundPageByURL : MATCHED: Actual URL: " + d.getCurrentUrl()
								+ "   vs Expected URL: " + thisURL);
					} else {
						POMLOGGER.enterInfoLog("verifyFoundPageByURL : MISMATCH: Actual URL: " + d.getCurrentUrl()
								+ "   vs Expected URL: " + thisURL);
					}
					return result;

				}// apply
			});
		} catch (TimeoutException e) {
			POMLOGGER.enterInfoLog(testName + ": Driver.getCurrentUrl did not match: " + thisURL);
			throw new TimeoutException();
		}
	}

	/**
	 * validateInvalidLinks(). Find out broken links on website using selenium
	 * webdriver and HTTP Client
	 * @return int - number of invalid links
	 * @author DRamer
	 * @version 1.0
	 */

	public int validateInvalidLinks() {

		try {
			invalidLinksCount = 0;
			List<WebElement> anchorTagsList = driver.findElements(By.tagName("a"));
			POMLOGGER.enterInfoLog("Total no. of links are " + anchorTagsList.size());
			for (WebElement anchorTagElement : anchorTagsList) {
				if (anchorTagElement != null) {
					String url = anchorTagElement.getAttribute("href");
					if (url != null && !url.contains("javascript")) {
						verifyURLStatus(url);
					} else {
						invalidLinksCount++;
					}
				}
			}
			Reporter.log("Total no. of invalid links are " + invalidLinksCount, true);
			return invalidLinksCount;
		} catch (Exception e) {
			e.printStackTrace();
			POMLOGGER.enterInfoLog(e.getMessage());
			return invalidLinksCount;
		}
	}// validateInvalidLinks

	/**
	 * verifyURLStatus(). check URL status
	 * 
	 * @exception None.
	 * 
	 */
	private void verifyURLStatus(String URL) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(request);
			// verifying response code and The HttpStatus should be 200 if not,
			// increment invalid link count
			//// We can also check for 404 status code like
			// response.getStatusLine().getStatusCode() == 404
			if (response.getStatusLine().getStatusCode() != 200)
				invalidLinksCount++;
		} catch (Exception e) {
			POMLOGGER.enterInfoLog("STestWebDriverFactory:verifyURLStatus: Exception caught" + e);
		}
	}

	/**
	 * method demonstrates selectDropDownElement Used to perform operations on a
	 * select dropdown
	 * 
	 * @param text
	 *            option you wish select
	 * @param element
	 *            page element of dropdown.
	 */
	public void selectDropDownElement(String text, String element) {
		try {
			WebElement dropdownElement = findElement(element);
			if (null != dropdownElement) {
				Select selector = new Select(dropdownElement);
				if (null != selector) {
					selector.selectByVisibleText(text);
				} else {
					POMLOGGER.enterInfoLog("STestWebDriverFactory: selectDropDownElement: new Selector not created");
				}
			} else {
				POMLOGGER.enterInfoLog("STestWebDriverFactory: selectDropDownElement: dropDownElement not found");
			}

		} catch (Exception e) {
			POMLOGGER.enterInfoLog("STestWebDriverFactory: selectDropDownElement: Exception caught" + e);
		}
	}

	/**
	 * method demonstrates scrollGrid Used to perform scroll operations.
	 * 
	 * @param row
	 *            element to search for while scrolling in grid
	 * @return boolean - true if scroll happened
	 */
	public boolean scrollGrid(WebElement row) {

		// Clicking on an element inside grid to get it into focus
		row.click();

		WebElement ele = null;
		int flag = 0;
		int count = 0;

		do {
			try {
				// element to search for while scrolling in grid
				row.isDisplayed();
				flag = 1;
			} catch (Throwable e) {
				// scrolling the grid using the grid's xpath
				row.sendKeys(Keys.PAGE_DOWN);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} while ((flag == 0) || ((++count) == 250));

		if (flag == 1) {
			POMLOGGER.enterInfoLog("sTestWebDriverFactory: scrollGrid: Element has been found.!!");
			return true;
		} else {
			POMLOGGER.enterInfoLog("sTestWebDriverFactory: scrollGrid: Element has not been found.!!");
		}
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			POMLOGGER.enterSevereLog("sTestWebDriverFactory: scrollGrid: Exception caught");
		} // to check if the element scrolled to is highlighted.
		return false;
	}//// ScrollGrid
	/**
	 * openURLInNewTab()
	 * Used to open a new tab for testing
	 * @author DRamer
	 * @version 1.0
	 */
	public void openNewTab(){
		if(path.getOSName().contains(Platform.MAC.toString())){
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.COMMAND +"t");
		    ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		    Reporter.log("number of Tabs" + tabs.size(),true);
		    driver.switchTo().window(tabs.get(0));
		}else{
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");
		    ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		    Reporter.log("number of Tabs" + tabs.size(),true);
		    driver.switchTo().window(tabs.get(0));
		}
		
	}//openURLInNewTab
	
	/**
	 * startStopWatch()
	 * Used to calculate page load times
	 * @return osInfo
	 */
	private void stopWatchStart(){
		if(null != pageLoad){
			pageLoad.start();
		}else{
			 POMLOGGER.enterInfoLog("sTestPageDeclaration : stopWatchStart - pageLoad = null");
		}
	}//stopWatchStart
	/**
	 * stopWatchStop()
	 * Used to calculate page load times
	 * @return osInfo
	 */
	private long stopWatchStop(String url){
		if(null != pageLoad){ 
			pageLoad.stop();
			long pageLoadTime_ms = pageLoad.getTime();
			long pageLoadTime_Seconds = pageLoadTime_ms / 1000;
			POMLOGGER.enterInfoLog("Load Time for: "+url+" is: " + pageLoadTime_ms + " milliseconds");
			pageLoad.reset();
			return pageLoadTime_ms;
		}else{
			return 0;
		}
	}//stopWatchStop

}// sTestBasePageDeclaration
