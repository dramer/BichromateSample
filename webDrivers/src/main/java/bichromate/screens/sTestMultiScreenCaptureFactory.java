package bichromate.screens;



import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import bichromate.core.sTestOSInformationFactory;


public class sTestMultiScreenCaptureFactory {
	
	private 	GraphicsEnvironment ge = null;
	private GraphicsDevice[] screens = null;
	private sTestOSInformationFactory osInfo = null;
	
	public sTestMultiScreenCaptureFactory(){
		
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		if(null != ge){
			screens = ge.getScreenDevices();
		}
		osInfo = new sTestOSInformationFactory();
		
		
	}//sTestMultiScreenCaptureFactory
	public void takeScreenShot() throws IOException, AWTException{
		
		  
		  Rectangle allScreenBounds = new Rectangle();
		  for (GraphicsDevice screen : screens) {
		   Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();
		   
		   allScreenBounds.width += screenBounds.width;
		   allScreenBounds.height = Math.max(allScreenBounds.height, screenBounds.height);      
		  }
		  
		  Robot robot;
		
			robot = new Robot();
		
		  BufferedImage screenShot = robot.createScreenCapture(allScreenBounds);
		  
		  String ext = "jpg";
		  String outputFile = osInfo.getScreenCaptureDirectory() + osInfo.getCurrentDateAndUTCTime()+"-allScreens"
		   + "."
		   + ext;   
		  ImageIO.write(screenShot, ext, new File(outputFile));
		  System.out.println("Saved " + outputFile 
		   + " of all monitors " 
		   + " xy=(" + allScreenBounds.x + "," + allScreenBounds.y + ")"
		   + " bounds=("+ allScreenBounds.width + "," + allScreenBounds.height + ")");  
	}
	public void takeScreenShot(int screenID,String format){
		//
		// if screens are detected proceed
		//
		if (screens.length == 0) {
			System.out.println("sTestMultiScreenCaptureFactory:takeScreenShot No Screens found");
			return;
		}
		if (screens.length <= screenID) {
			System.out.println("sTestMultiScreenCaptureFactory:takeScreenShot Invalid screenID");
			return;
		}
		try {
			//
			// get the first screen
			//
			GraphicsDevice a = screens[screenID];

			Robot robotForScreen = new Robot(a);
			Rectangle screenBounds = a.getDefaultConfiguration().getBounds();
			   
			BufferedImage screenShot = robotForScreen.createScreenCapture(screenBounds);
			String ext = "."+format;
			String outputFile = osInfo.getScreenCaptureDirectory() + osInfo.getCurrentDateAndUTCTime()+"-id-"+screenID+ext;   
			  
			ImageIO.write(screenShot, format, new File(outputFile));
			
			System.out.println("Saved " + outputFile + " of all monitors " + " xy=(" + screenBounds.x + "," + screenBounds.y + ")"+ " bounds=("+ screenBounds.width + "," + screenBounds.height + ")");  
			
			} catch (AWTException ex) {
				System.out.println("sTestMultiScreenCaptureFactory:takeScreenShot Some AWTException");
			} catch (IOException e) {
				System.out.println("sTestMultiScreenCaptureFactory:takeScreenShot Some IOException");
				
			}
			
	}//takeScreenShot
	
	
	
	private void selfTest(){
		
		takeScreenShot(0,"jpg");
		takeScreenShot(1,"png");
		takeScreenShot(2,"gif");
		try{
			takeScreenShot();
		} catch(IOException ioe){
			
		} catch(AWTException awt){
			
		}
		
	}
	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
		 public static void main(final String[] args){
		 		
			 sTestMultiScreenCaptureFactory smsc = new sTestMultiScreenCaptureFactory();
		 		
			 if(null != smsc){
				 smsc.selfTest();
			 }
			 
		 }//main
	 }//Test

}//sTestMultiScreenCaptureFactory
