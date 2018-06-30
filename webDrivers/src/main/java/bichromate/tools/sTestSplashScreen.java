/*
 * sTestSplashScreen.java	1.0 2016/09/03
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
 */

package bichromate.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import javax.swing.JLabel;
import javax.swing.JWindow;

import bichromate.core.sTestOSInformationFactory;

public class sTestSplashScreen {

	private JLabel splashLabel  = null;
	private JWindow splashScreen = null;
	private sTestOSInformationFactory path = null;
	private String imageFileName = null;
	private String imageDescription = null;
	
	
	public sTestSplashScreen(String image, String text){
		path = new sTestOSInformationFactory();
		imageFileName = new String(image);
		imageDescription = new String(text);
		createSplashScreen();
		
	}//sTestSplashScreen
	
	private void createSplashScreen()
	{
		splashLabel = new JLabel(createImageIcon(imageFileName, "Splash.accessible_description"));
		splashScreen = new JWindow();
		splashScreen.getContentPane().add(splashLabel);
		splashLabel.setText(imageDescription);
		splashLabel.setHorizontalTextPosition(JLabel.CENTER);
		splashLabel.setVerticalTextPosition(JLabel.CENTER);
		splashLabel.setForeground (Color.red);
		
		splashScreen.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		splashScreen.setLocation(screenSize.width/2 - splashScreen.getSize().width/2,
					     screenSize.height/2 - splashScreen.getSize().height/2);
		
			
	 }//createSplashScreen
	private ImageIcon createImageIcon(String filename, String description)
	{
			String imgPath = path.getImageDirectory()+filename;
			try{
				ImageIcon icon = new ImageIcon(imgPath);
				return icon;
			}catch(Exception e){
				System.out.println("Error creating splash screen");
			}
			return null;
	}//createImageIcon
	
	public void hideSplash()
	{
		if(splashScreen != null)splashScreen.setVisible(false);
		splashScreen = null;
		splashLabel = null;
	}
	public void showSplash()
	{
		if(splashScreen != null)splashScreen.setVisible(true);
		
		try {
			Thread.sleep(5000); // 1 minute wait
		} catch (InterruptedException e) {
			System.out.println("Error during Driver sleep");
			e.printStackTrace();
		}
	}
	
	 //
		// Inner class for testing on the command line
		//
		public static class Test
		{
			public static void main(final String[] args){
				
				sTestSplashScreen splash = null;
				
				splash = new sTestSplashScreen("Splash.png", "Testing Splash Screens");
				if(splash != null){
					
					splash.showSplash();
					
					try {
						Thread.sleep(5000); // 5 minutes wait
					} catch (InterruptedException e) {
						System.out.println("Error during Driver sleep");
						e.printStackTrace();
					}
					splash.hideSplash();
				}
				
			} // end Main
		 } // end Inner class Test
	
	
	

}//sTestSplashScreen


