package bichromate.tools;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;


import bichromate.core.sTestOSInformationFactory;

@SuppressWarnings("unused")
public class sTestLicenseFactoryPanel  extends JPanel implements ActionListener,KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private sTestSplashScreen splash = null;
	
	private KeyVerifier keyVerification = null;
	private KeyGenerator keyGen = null;
	
	public sTestLicenseFactoryPanel(){
		
		
		splash = new sTestSplashScreen("Splash.png","Creating License Factory");
		if(null != splash)
			splash.showSplash();
		
		
		keyVerification = new KeyVerifier();
		keyGen = new KeyGenerator();
	
		setLayout( new GridLayout(2 /* rows */, 1/* cols */) );
		
		
		
		if(null != splash)
			splash.hideSplash();
		
	}//sTestLicenseFactoryPanel
	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	 //
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			sTestOSInformationFactory path = new sTestOSInformationFactory();
			sTestLicenseFactoryPanel pom = null;
			
			pom = new sTestLicenseFactoryPanel();
			if(pom != null){
				
				JFrame frame = new JFrame();
				
				ImageIcon img = new ImageIcon(path.getImageDirectory()+"bichromateIcon.png");
				
				if(null != img)
					frame.setIconImage(img.getImage());
				
		        frame.add(pom);
		        frame.setBounds(10, 10, 1200, 500);
		       
		        frame.setVisible(true);
				
			}
			
		} // end Main
	 } // end Inner class Test

		
}//sTestLicenseFactoryPanel
