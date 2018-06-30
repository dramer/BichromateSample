package bichromate.winium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.winium.WiniumDriver;

public class sTestReasonCode {
	
	private WiniumDriver myApp = null;
	
	public sTestReasonCode(WiniumDriver app){
		
		myApp = app;
		
	}//sTestReasonCode
	
  public boolean clickMenuItem(String menuName){
		  
		  WebElement e = myApp.findElementByName(String.valueOf(menuName));
		  if(null != e){
				  e.click();
				  return true;
		  }else{
			  return false;
		  }
		  
	  }//clickMenuItem
  public boolean clickButton(String menuName){
	  
	  WebElement e = myApp.findElementByName(String.valueOf(menuName));
	  if(null != e){
			  e.click();
			  return true;
	  }else{
		  return false;
	  }
	  
  }//clickMenuItem
  
 public boolean clickCalendar(){
	  
	  WebElement e = myApp.findElementByClassName("Calendar");
	  if(null != e){
			  e.click();
			  return true;
	  }else{
		  return false;
	  }
	  
  }//clickMenuItem
 
 public boolean clickCalendarNextMonthButton(){
	  
	 List<WebElement> buttonList = myApp.findElementsByClassName("Button");
	 for(int x = 0; x < buttonList.size();x++){
		  WebElement e = buttonList.get(x);
		  String nextButtonName = new String(e.getAttribute("Name"));
		  if(nextButtonName.equals("Next button")){
			  if(null != e){
				  e.click();
				  return true;
			  }
		  }
	  }
	  return false;
	  
 }//clickMenuItem
 public boolean clickCalendarPreviousMonthButton(){
	  
	 List<WebElement> buttonList = myApp.findElementsByClassName("Button");
	 for(int x = 0; x < buttonList.size();x++){
		  WebElement e = buttonList.get(x);
		  String previousButtonName = new String(e.getAttribute("Name"));
		  if(previousButtonName.equals("Previous button")){
			  if(null != e){
				  e.click();
				  return true;
			  }
		  }
	  }
	  return false;
	  
}//clickMenuItem
  
  public boolean clickdropDown(String menuName){
	  
	  WebElement e = myApp.findElementByName(String.valueOf(menuName));
	  if(null != e){
			  e.click();
			  return true;
	  }else{
		  return false;
	  }
	  
  }//clickMenuItem
  
  // DataGridCell, B41325
  
  
public boolean doubleClickTextInTable(String cData){
	  
	  List<WebElement> textBoxList = myApp.findElementsByClassName("DataGridCell");
	  for(int x = 0; x < textBoxList.size();x++){
		  WebElement e = textBoxList.get(x);
		  WebElement textBlock =  e.findElement(By.className("TextBlock"));
		  String cellData = new String(textBlock.getAttribute("Name"));
		  if(cellData.equals(cData)){
			  if(null != e){
				  performAction(textBlock, "doubleClick");
				  return true;
			  }
		  }
	  }
	  
	  return false;
  }//enterText
  
  
  public boolean enterText(String menuName,String text){
	  
	  List<WebElement> textBoxList = myApp.findElementsByClassName("TextBox");
	  for(int x = 0; x < textBoxList.size();x++){
		  WebElement e = textBoxList.get(x);
		  String autoID = new String(e.getAttribute("AutomationId"));
		  if(autoID.equals(menuName)){
			  if(null != e){
				  e.sendKeys(text);
				  return true;
			  }
		  }
	  }
	  
	  return false;
  }//enterText
  
  public void performAction(WebElement element, String whatAction){
	  Actions action = new Actions(myApp);
	 
	  if(whatAction.equals("doubleClick")){
		  //Double click
		  action.doubleClick(element).perform();
	  }else if(whatAction.equals("moveToElement")){
	  //Mouse over
	  action.moveToElement(element).perform();
	  }else if(whatAction.equals("rightClick")){
		  //Right Click
		  action.contextClick(element).perform();
	  }
  }
  
}//sTestReasonCode
