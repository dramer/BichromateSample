
/*
 * oTestTestNGAutomationFactory.java	1.0 2013/01/23
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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import bichromate.core.sTestOSInformationFactory;
import bichromate.core.sTestSpreadsheetFactory;



/**
 * This class Demonstrates cronJobPanel().
 * <br>This panel is used inside oTestTestNGAutomationFactory. This panel is used
 * <br> to show and run the testNG cron job
 * <br>
 * @author davidwramer
 * @version 1.0
 */
public class cronJobPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private sTestSplashScreen splash = null;
	private JPanel outPutPanel = new JPanel();
	private JPanel spreadSheetPanel = new JPanel();
	private JPanel actionPanel = new JPanel();
	private JPanel textPanel = new JPanel();
	private JPanel tableNamePanel = new JPanel();
	private JPanel workSheetPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel groupPanel = new JPanel();
	private sTestCronJob cronJob = null;
	private AbstractTableModel dataModel = null;
	private JButton start = new JButton("Start Cron Jobs");
	private JButton stop = new JButton("Stop Cron Jobs");
	private JButton loadCronData = new JButton("Load Cron Data...");
	private JButton saveCronData = new JButton("Save Cron Data...");
	private JTextPane  cronOutPut = new JTextPane();
	private JCheckBox sms = new JCheckBox("Enable SMS");
	private final int MAX_ROWS = 60;
	private final int MAX_COLS = 5;
	//private final int MAX_PANE_HEIGHT = 10;
	private String[][] data = null;
	JLabel  workSheetNameLabel = new JLabel("WorkSheet Name:");
	JTextField workSheetName = new JTextField("CRON", 20);
	JLabel  tableNameLabel = new JLabel("Table Name:");
	JTextField tableName = new JTextField("CRON", 20);
	/**
	 * This constructor Demonstrates cronJobPanel().
	 * <br>This panel sets up the spreadsheet, class text area and properties text area
	 * <br> 
	 * <br>
	 */
	public cronJobPanel(){
		
		splash = new sTestSplashScreen("Splash.png","Creating Cron Factory");
		if(null != splash)
			splash.showSplash();
		
		
		
		groupPanel.setLayout(new GridLayout(1/* rows */,0));
		
		
		outPutPanel.setLayout(new GridLayout(1/* rows */,0));
		outPutPanel.setBorder( new TitledBorder("Cron Status") );
		
		EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
		
		cronOutPut.setBorder(eb);
	       
		cronOutPut.setMargin(new Insets(5, 5, 5, 5));

		JScrollPane areaScrollPaneInput = new JScrollPane(cronOutPut);
		areaScrollPaneInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPaneInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		outPutPanel.add(areaScrollPaneInput);
		appendToPane(cronOutPut,"Click Start to run cron jobs...", Color.RED);
		
		
		
		actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		actionPanel.setBorder( new TitledBorder("Actions") );
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		textPanel.setLayout(new GridLayout(2/* rows */,0));
		tableNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		workSheetPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		start.addActionListener(this);
		stop.addActionListener(this);
		loadCronData.addActionListener(this);
		saveCronData.addActionListener(this);
		
		stop.setEnabled(false);
		start.setEnabled(false);
		
		buttonPanel.add(start);
		buttonPanel.add(stop);
		buttonPanel.add(loadCronData);
		buttonPanel.add( saveCronData );
		
		
		workSheetPanel.add(workSheetNameLabel);
		workSheetPanel.add(workSheetName);
		tableNamePanel.add(tableNameLabel);
		tableNamePanel.add(tableName );
		
		textPanel.add(workSheetPanel);
		textPanel.add(tableNamePanel);
		
		actionPanel.add(buttonPanel);
		actionPanel.add(textPanel);
		actionPanel.add(sms);
		
		spreadSheetPanel.setLayout(new GridLayout(1/* rows */,0));
		spreadSheetPanel.setBorder( new TitledBorder("SpreadSheet input") );
		
		
		
		dataModel = new AbstractTableModel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			//
			// Add the spreadsheet for the  auction table
			//
			final String[] colNames = {"Name","Description","Class File","Day","Time"};
			
		    final Object[][] data = {
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""},
					{"","","","",""}
		    	};
			
			public int getColumnCount() { return MAX_COLS; }
			public int getRowCount() { return MAX_ROWS;}
			public boolean isCellEditable(int row, int col) {return true; }
			public Object getValueAt(int row, int col) { return data[row][col]; }
			public String getColumnName(int column) {return colNames[column];}
			public void setValueAt(Object value, int row, int col)
			{
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
		};
		
		JTable table = new JTable(dataModel);
		
		JScrollPane scrollpane = new JScrollPane(table);
		
		spreadSheetPanel.add(scrollpane);
		GridLayout panelLayout = new GridLayout(2 /* rows */, 1/* cols */);
		// BoxLayout panelLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
	
		
		setLayout( panelLayout );
		groupPanel.add(outPutPanel);
		groupPanel.add(actionPanel);
		
		add(spreadSheetPanel);
		add(groupPanel);
		
		if(null != splash)
			splash.hideSplash();
		
	}
	
	public void actionPerformed(ActionEvent e) {
		 if(e.getSource() == start){
			 appendToPane(cronOutPut,"Start Cron thread...", Color.BLUE);
	        	stop.setEnabled(true);
	        	start.setEnabled(false);
	        	loadCronData.setEnabled(false);
	        	cronJob.setRunning(true);
	        	cronJob.start();
	        	
		 }
		 if(e.getSource() == stop){
			 appendToPane(cronOutPut,"Stop Cron thread...", Color.BLUE);
	        	stop.setEnabled(false);
	        	start.setEnabled(true);
	        	loadCronData.setEnabled(true);
	        	cronJob.stopCron();
		 }
		 if(e.getSource() == loadCronData){
			 appendToPane(cronOutPut,"Load Cron Data...", Color.BLUE);
	        	
	        	 File myFilename;
	             JFileChooser chooser = new JFileChooser();
	             chooser.addChoosableFileFilter(new OpenFileFilter("xls","Bichromate Data File") );
	             chooser.setDialogType(JFileChooser.OPEN_DIALOG );
	             chooser.setDialogTitle("Open Bichromate cron data");           
	             chooser.setCurrentDirectory(new File("."));
	             int returnVal = chooser.showSaveDialog(this);
	             
	             if (returnVal == JFileChooser.APPROVE_OPTION) {
	                  myFilename = chooser.getSelectedFile();
	                  loadSpreadsheet(myFilename.getAbsolutePath());
	                  //do something with the file
	             }
	        	
		 }
		 if(e.getSource() == saveCronData){
			 appendToPane(cronOutPut,"Save Cron Data...NOT IMPLEMENTED", Color.BLUE);
		 }
		
	}
	public class OpenFileFilter extends FileFilter {

	    String description = "";
	    String fileExt = "";

	    public OpenFileFilter(String extension) {
	        fileExt = extension;
	    }

	    public OpenFileFilter(String extension, String typeDescription) {
	        fileExt = extension;
	        this.description = typeDescription;
	    }

	    @Override
	    public boolean accept(File f) {
	        if (f.isDirectory())
	            return true;
	        return (f.getName().toLowerCase().endsWith(fileExt));
	    }

	    @Override
	    public String getDescription() {
	        return description;
	    }
	}
	/**
	 * This function appendToPane().
	 * <br>This function appends text, and text color to the JTextPane
	 * <br> 
	 * <br>
	 * @param JTextPane tp
	 * @param String msg
	 * @param COLOR c
	 * @exception None.
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
        tp.replaceSelection(msg+"\n");
    }
	private void  loadSpreadsheet(String file){
		int cols = 0;
		int row = 0;
		
		sTestSpreadsheetFactory xlFactory = new sTestSpreadsheetFactory();
		File spreadSheet = new File(file);
		if(null != spreadSheet){
			
			 
			
			
			data =  xlFactory.getTableArray(spreadSheet,spreadSheet.getName(),workSheetName.getText().trim() ,tableName.getText().trim());
		  
			for(int x = 0;x < data.length; x++)
			{
				//
				// Strip the quotes off the string
				//
				cols=0;
				
				for (int columns = 0; columns < MAX_COLS; columns++){
					if(row < dataModel.getRowCount() && cols < MAX_COLS){
						String cellData = new String(data[row][columns].trim());
						 appendToPane(cronOutPut,"loading Data: "+cellData, Color.BLUE);
						dataModel.setValueAt(cellData, row, cols++);
					}// cols
				}
				row++;
			}// for loop
		}//else
		
		// start cronJob
		
		cronJob = new sTestCronJob(cronOutPut,data);
		start.setEnabled(true);
		loadCronData.setEnabled(false);
		spreadSheetPanel.setBorder( new TitledBorder(file+" Loaded") );
	}// loadSpreadsheet
	
	
	 //
		// Inner class for testing on the command line
		//
		public static class Test
		{
			public static void main(final String[] args){
				
				sTestOSInformationFactory path = new sTestOSInformationFactory();
				cronJobPanel pom = null;
				
				pom = new cronJobPanel();
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
	
	
	
}//cronJobPanel
