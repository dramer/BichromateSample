package bichromate.tools;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;

import javax.swing.border.TitledBorder;

import bichromate.core.sTestOSInformationFactory;

public class sTestLoadClientPanel extends JPanel implements ActionListener,KeyListener {
	

	private static final long serialVersionUID = 1L;
	private JPanel actionPanel = new JPanel();
	private JSpinner m_count,m_threadCount;
	private JButton removeClient;
	private JCheckBox enabled;
	
	private JPanel iterationPanel = new JPanel();
	private JPanel loadCountPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel messagePanel = new JPanel();
	private JPanel controlsPanel = new JPanel();
	
	 JTextArea messageArea = new JTextArea(4, 60);
	
	public sTestLoadClientPanel(){
	
		 //
		// Setup action panel
		//
		actionPanel.setLayout( new GridLayout(2 /* rows */, 1/* cols */) );
		actionPanel.setBorder( new TitledBorder("Client Connection - Not Connected") );
        
		 loadCountPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		 loadCountPanel.setBorder( new TitledBorder("Thread Count") );
		 iterationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		 iterationPanel.setBorder( new TitledBorder("Execution Iterations") );
		 buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		 buttonPanel.setBorder( new TitledBorder("Settings") );
		 messagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		 messagePanel.setBorder( new TitledBorder("Client Messages") );
		 controlsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		 controlsPanel.setBorder( new TitledBorder("Client Input") );
		 messagePanel.add(messageArea);
		 
        /*
         * The thread count spinner
         */
        JLabel threadLabel = new JLabel("Number of Threads:");
        loadCountPanel.add(threadLabel);
       
        m_threadCount = new JSpinner();
        m_threadCount.setValue(Integer.valueOf(5));
        Dimension a = m_threadCount.getPreferredSize();
        a.width = 50;
        m_threadCount.setPreferredSize(a);
    
        loadCountPanel.add(m_threadCount);
        
        
        
        /*
         * The operation count
         */
        JLabel iterationLabel = new JLabel("Number of Iterations:");
        iterationPanel.add(iterationLabel);

        m_count = new JSpinner();
        m_count.setValue(Integer.valueOf(5));
        Dimension ab = m_count.getPreferredSize();
        ab.width = 50;
        m_count.setPreferredSize(ab);
        iterationPanel.add(m_count);
        
        //
        // Buttons
        //
        removeClient = new JButton("Remove Client");
        
        enabled = new JCheckBox("Enabled");
        removeClient.addActionListener(this);
        enabled.addActionListener(this);
        buttonPanel.add(removeClient);
        buttonPanel.add(enabled);
        
        setLayout( new GridLayout(2 /* rows */, 1/* cols */) );
		
        controlsPanel.add(loadCountPanel);
        controlsPanel.add(iterationPanel);
        controlsPanel.add(buttonPanel);
        actionPanel.add(controlsPanel);
        actionPanel.add(messagePanel);
       
		add(actionPanel);
		
		
	}//sTestLoadClientPanel
	
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
	public void setElementsEnabled(boolean enableSetting,String panelName) {
		m_count.setEnabled(enableSetting);
		m_threadCount.setEnabled(enableSetting);
		enabled.setEnabled(enableSetting);
		removeClient.setEnabled(enableSetting);
		if(enableSetting){
			actionPanel.setBorder( new TitledBorder("Client Connection - "+panelName) );
		}else{
			actionPanel.setBorder( new TitledBorder("Client Connection - Not Connected") );
		}
	}
	 //
		// Inner class for testing on the command line
		//
		public static class Test
		{
			public static void main(final String[] args){
				
				sTestOSInformationFactory path = new sTestOSInformationFactory();
				sTestLoadClientPanel clientPanel = null;
				
				clientPanel = new sTestLoadClientPanel();
				if(clientPanel != null){
					
					JFrame frame = new JFrame();
					
					ImageIcon img = new ImageIcon(path.getImageDirectory()+"bichromateIcon.png");
					
					if(null != img)
						frame.setIconImage(img.getImage());
					clientPanel.setElementsEnabled(false,"");
			        frame.add(clientPanel);
			        frame.setBounds(10, 10, 1200, 500);
			       
			        frame.setVisible(true);
			    
				}
				
			} // end Main
		 } // end Inner class Test
}//sTestLoadClientPanel
