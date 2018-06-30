/*
 * sDeskLogFilePanel.java	1.0 2013/01/23
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

import java.awt.Cursor;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//import java.util.Map;

import java.util.Scanner;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
//import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import bichromate.core.sTestOSInformationFactory;
import bichromate.tools.sTestLogFileFactory;


/**
 * This class Demonstrates oDeskLogFilePanel().
 * <br>This panel is used inside oTestTestNGAutomationFactory. This panel is used
 * <br> to create the oDesk log file panel
 * <br>
 * @author davidwramer
 * @version 1.0
 */
public class sTestLogFilePanel extends JPanel implements ActionListener,KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private sTestLogFileFactory log = null;
	private sTestSplashScreen splash = null;
	private JTextPane  logOutput = new JTextPane();
	private JPanel logOutputPanel = new JPanel();
	private JPanel actionPanelTop = new JPanel();
	private JPanel actionPanelBottom = new JPanel();
	private JPanel actionPanelBottom2 = new JPanel();
	private JPanel actionPanelBottom3 = new JPanel();
	private JPanel actionPanel = new JPanel();
	private JButton actionButton = new JButton("Access logs...");
	private JLabel stageLabel = new JLabel("Select Stage:");
	private JLabel logFileLabel = new JLabel("Select Log File:");
	private JPasswordField password = null;
	private JTextField login = null;
	private JComboBox <String>fileSelectionBox =null;
	private JComboBox <String>serverSelectionBox = null;
	
	private JLabel errorsLabel = new JLabel("Total Errors:");
	private JTextField errors = new JTextField(10);
	private JLabel fatalLabel = new JLabel("Total Fatal:");
	private JTextField fatal = new JTextField(10);
	private JLabel customLabel = new JLabel("Custom Field:");
	private JTextField custom = new JTextField(10);
	
	private JLabel searchLabel = new JLabel("Search For:");
	private JTextField search = new JTextField(10);
	
	private JButton parseLogFileButton = new JButton("Parse logs");
	
	 Vector <String>serverSelection = null;
	    Vector <String>fileSelection = null;
	
	
	public sTestLogFilePanel(){
		
		
		splash = new sTestSplashScreen("Splash.png","Creating Log File Factory");
		if(null != splash)
			splash.showSplash();
	
		log = new sTestLogFileFactory();
		
		fileSelection = log.getLogFileComboBox();
		serverSelection = log.getServerComboBox();
		
		
		serverSelectionBox = new JComboBox<String>(serverSelection);
		fileSelectionBox = new JComboBox<String>(fileSelection);
		
		//
		// Log output panel
		//
		EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
		
		logOutput.setBorder(eb);
	       
		logOutput.setMargin(new Insets(5, 5, 5, 5));
		
		logOutputPanel.setLayout( new GridLayout(1,0));
		logOutputPanel.setBorder( new TitledBorder("Log Results") );
	
	       
		logOutput.setMargin(new Insets(5, 5, 5, 5));

		JScrollPane areaScrollPaneInput = new JScrollPane(logOutput);
		areaScrollPaneInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPaneInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		logOutputPanel.add(areaScrollPaneInput);
		//
		// Setup action panel
		//
		actionPanel.setLayout(new GridLayout(2 /* rows */, 2/* cols */));
		actionPanel.setBorder( new TitledBorder("Access Log Files") );
		
		actionPanelTop.setLayout(new GridLayout(1 /* rows */, 2/* cols */));
		actionPanelTop.setBorder( new TitledBorder("Log File Selection") );
		
		JPanel stagePanel = new JPanel();
		stagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		stagePanel.add(stageLabel);
		stagePanel.add(serverSelectionBox);
		
		JPanel logPanel = new JPanel();
		logPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		logPanel.add(logFileLabel);
		logPanel.add(fileSelectionBox);
		
		actionButton.addActionListener(this);
		
		actionPanelTop.add(stagePanel);
		actionPanelTop.add(logPanel);
		
		
		
		actionPanelBottom.setLayout(new FlowLayout(FlowLayout.LEFT));
		actionPanelBottom.setBorder( new TitledBorder("Login Information") );
		
		
		
		actionPanelBottom.add(new JLabel("Login:"));
		login = new JTextField(20);
		actionPanelBottom.add(login);
		password = new JPasswordField(20);
		password.addActionListener(this);
		actionPanelBottom.add(new JLabel("Password:"));
		actionPanelBottom.add(password);
		
		actionPanelBottom2.setLayout(new FlowLayout(FlowLayout.LEFT));
		actionPanelBottom2.setBorder( new TitledBorder("Log Parsing Information") );
		
		actionPanelBottom3.setLayout(new FlowLayout(FlowLayout.LEFT));
		actionPanelBottom3.setBorder( new TitledBorder("Actions") );
		
		JPanel errorPanel = new JPanel();
		errorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		errorPanel.add(errorsLabel);
		errorPanel.add(errors);
		errors.setEnabled(false);
		
		JPanel fatalPanel = new JPanel();
		fatalPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		fatalPanel.add(fatalLabel);
		fatalPanel.add(fatal);
		fatal.setEnabled(false);
		 
		
		JPanel CustomPanel = new JPanel();
		CustomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		CustomPanel.add(customLabel);
		CustomPanel.add(custom);
		custom.setEnabled(false);
		
		
		JPanel parsePanel = new JPanel();
		parsePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		parsePanel.add(actionButton);
		parsePanel.add(parseLogFileButton);
		// parsePanel.add(loadTestData);
		parseLogFileButton.addActionListener(this);
		  
		  
		
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchPanel.add(searchLabel);
		searchPanel.add(search);
		search.addKeyListener(this);
		
		 actionPanelBottom2.add(errorPanel);
		 actionPanelBottom2.add(fatalPanel);
		 actionPanelBottom2.add(CustomPanel);
		 
		 
		 actionPanelBottom3.add(parsePanel);
		 actionPanelBottom3.add(searchPanel);
		 
		 
		actionPanel.add(actionPanelTop);
		actionPanel.add(actionPanelBottom);
		actionPanel.add(actionPanelBottom2);
		actionPanel.add(actionPanelBottom3);
		
		setLayout( new GridLayout(2 /* rows */, 1/* cols */) );
		add(logOutputPanel);
		add(actionPanel);
		
		if(null != splash)
			splash.hideSplash();
	
	}//sTestLogFilePanel
	
	
	public void actionPerformed(ActionEvent e) {
		 
		
		if(e.getSource() == parseLogFileButton){
			 System.out.println("process log files");
			 if(!search.equals("")){
				 customLabel.setText(search.getText());
				 customLabel.invalidate();
			 }
			 String data = logOutput.getText();
			 int errorCnt = 0;
			 int fatalCnt = 0;
			 int customCnt = 0;
			 Scanner scan = new Scanner(data);
			 String word;

			 while(scan.hasNext()){ //scanner automatically uses " " as a delimiter
				 word = scan.next();
				 if(word !=null){
		    	   if(word.contains("ERROR"))
		    		   errorCnt++;
		    	   if(word.contains("FATAL"))
		    		   fatalCnt++;
		    	   if(!search.equals("")){
		    		   if(word.contains(search.getText()))
		    			   customCnt++;
		    	   }
		    	   
				 }
			 }
			 scan.close();
			 errors.setText(Integer.toString(errorCnt));
			 fatal.setText(Integer.toString(fatalCnt));
			 custom.setText(Integer.toString(customCnt));
			
		}
		if(e.getSource() == actionButton){
			 System.out.println("process action button");
			 String serverName = new String(serverSelectionBox.getItemAt(serverSelectionBox.getSelectedIndex()));
			 String logFileName = new String(fileSelectionBox.getItemAt(fileSelectionBox.getSelectedIndex()));
			 String loginName = new String(login.getText());
			// String passwordName = new String(password.getPassword());
			 Cursor originalCursor = this.getCursor();
			 this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
			 log.downloadLogFile(serverName, log.getStartingDirectory()+logFileName, loginName);
			 logOutput.setText(log.obtainLogFileData(log.getLastLogFile()));
			 this.setCursor(originalCursor);
		 }
	
	}//actionPerformed
	public void keyPressed(KeyEvent e) {
		
	}
	public void keyReleased(KeyEvent arg0) {
		
		
	}
	public void keyTyped(KeyEvent k) {
		
	}
	 //
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			sTestOSInformationFactory path = new sTestOSInformationFactory();
			sTestLogFilePanel pom = null;
			
			pom = new sTestLogFilePanel();
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
	
	
}// oDeskLogFilePanel
