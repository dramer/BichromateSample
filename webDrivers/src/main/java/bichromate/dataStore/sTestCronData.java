package bichromate.dataStore;

import java.awt.Color;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.testng.IResultMap;
import org.testng.ITestContext;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import bichromate.core.sTestOSInformationFactory;

@SuppressWarnings("unused")
public class sTestCronData extends TestListenerAdapter{

	public String title;
	public String description;
	public String test;
	public String day;  // Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday, All, Weekend, MWF,
	public String timeToRun; // hh:mm:ss
	public boolean testRunning = false;
	public boolean runToday = false;
	public boolean hasFailures = false;
	public IResultMap results = null;
	private JTextPane  testOutput = null;
	private sTestOSInformationFactory path = null;
	
	private TestNG testng = null;
	
	public sTestCronData() {
		
		title = new String("");
		description  = new String("");
		test  = new String("");
		day  = new String("");
		timeToRun  = new String("");
		testng = new TestNG();
		
		path = new sTestOSInformationFactory();
	}
	public sTestCronData(String title, String description, String test) {
		
		this.title = new String(title);
		this.description  = new String(description);
		this.test  = new String(test);
		testng = new TestNG();
	}
	 /**
	 * This method sTestCronData().
	 * <br>This method creates cron data from the input data
	 * <br> 
	 * <br>
	 * @param outputPanel  to show status on the U/I
	 * @param title of the test
	 * @param description of the test
	 * @param test test to execute something like X.Y.test
	 * @author davidwramer
	 * @version 1.0
	 */
	public sTestCronData(JTextPane outputPanel,String title, String description, String test) {
		this.testOutput = outputPanel;
		this.title = new String(title);
		this.description  = new String(description);
		this.test  = new String(test);
		testng = new TestNG();
	}
	 /**
	 * This method executeTest().
	 * <br>This method executes the test class
	 * <br> 
	 * <br>
	 * @author davidwramer
	 * @version 1.0
	 */
	public void executeTest(){
		
		@SuppressWarnings("rawtypes")
		Class sTestTest;
		//try{
			// sTestTest = Class.forName(test);
			sTestTest = buildTheTestClass(test);
			testng.setTestClasses(new Class[] {/* com.test.visitorSite.releaseNightTest */sTestTest});
			testng.addListener(this);
			System.out.println("testNG running test: "+ test);
			testng.run();
	
	//	} catch (ClassNotFoundException e) {
		//	e.printStackTrace();
			//if(testOutput != null) appendToPane(testOutput,"sTestCronData : executeTest Failed: "+ test, Color.RED);
		//}
		
	}// executeTest
	 /**
	 * This method executeFromXLS().
	 * <br>This method executes the test from a XML file
	 * <br> 
	 * <br>
	 * <br> suite name="TmpSuite"
  	 * <br>	test name="TmpTest"
     * <br>		classes
     * <br>			class name="test.failures.Child"  
     * <br>		classes
     * <br>	test
	 * <br>	suite
	 * <br>
	 * @param suiteName - name of the test
	 * @param testName - class to execute
	 * @param testClass - test class to execute 
	 * @author davidwramer
	 * @version 1.0
	 */
	public void executeFromXLS(String suiteName, String testName, String testClass){
		
		XmlSuite suite = new XmlSuite();
		suite.setName(suiteName);
		 
		XmlTest test = new XmlTest(suite);
		test.setName(testName);
		List<XmlClass> classes = new ArrayList<XmlClass>();
		classes.add(new XmlClass(testClass));
		test.setXmlClasses(classes) ;

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);
		TestNG tng = new TestNG();
		tng.addListener(this);
		tng.setXmlSuites(suites);
		tng.run();
	}// executeFromXLS
	 /**
		 * This method onStart().
		 * <br>Listner for the testNG test
		 * <br> 
		 * <br>
		 * @param testContext - testing results
		 * @author davidwramer
		 * @version 1.0
		 */
	public void onStart(ITestContext testContext){
		System.out.println("TestStarting: "+ test);
		if(testOutput != null) appendToPane(testOutput,"TestStarting: "+ test, Color.GREEN);
		testRunning = true;
		results = null;
		hasFailures = false;
		runToday = true;
	}
	 /**
	 * This method onFinish().
	 * <br>Listner for the testNG test
	 * <br> 
	 * <br>
	 * @param testContext - testing results
	 * @author davidwramer
	 * @version 1.0
	 */
	public void onFinish(ITestContext testContext){
		System.out.println("TestCompleted: "+ test);
		if(testOutput != null) appendToPane(testOutput,"TestCompleted: "+ test, Color.GREEN);
		runToday = true;
		testRunning = false;
		results = testContext.getFailedTests();
		if(testOutput != null) appendToPane(testOutput,results.toString(), Color.GREEN);
		hasFailures = testng.hasFailure();
		if(testOutput != null) {
			if(hasFailures){
				appendToPane(testOutput,test +": FAILED", Color.RED);
			}else{
					appendToPane(testOutput,test +": PASSED", Color.GREEN);
			}
		}
	}
	
	private Class<?> buildTheTestClass(String test){
		File file = new File(path.getTestClassDirectory());
		 Class<?> testClass = null;
		 ClassLoader cl = null;
		try {
		    // Convert File to a URL
		    URL url = file.toURI().toURL();// file:/c:/myclasses/
		    URL[] urls = new URL[]{url};

		    // Create a new class loader with the directory
		    cl = new URLClassLoader(urls);

		    // Load in the class; MyClass.class should be located in
		    // the directory file:/c:/myclasses/com/mycompany
		    testClass = cl.loadClass(test);
		  
		  
		} catch (MalformedURLException e) {
			e.printStackTrace();
			if(testOutput != null) appendToPane(testOutput,"buildTheTestClass : failed to find test class: "+ test, Color.RED);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			if(testOutput != null) appendToPane(testOutput,"buildTheTestClass : Failed to find test class: "+ test, Color.RED);
		}
		return testClass;
		
	}// buildTheTestClass
	/**
	 * This function appendToPane().
	 * <br>This function appends text, and text color to the JTextPane
	 * <br> 
	 * <br>
	 * @param tp
	 * @param msg
	 * @param c
	 * @return None.
	 * @exception None.
	 * @author davidwramer
	 * @version 1.0
	 */
	private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection("\n"+msg);
    }
}// sTestCronData
