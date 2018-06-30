
package bichromate.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


import bichromate.core.sTestOSInformationFactory;
import bichromate.graphs.sTestCPUGraph;
import bichromate.servers.sTestLoadServerManager;
import bichromate.servers.sTestServerIndicator;
import bichromate.system.systemInformation;

import javax.swing.border.*;

/**
 * This class is the main UI for our load testing client. This class will handle
 * the UI and manage all of the thread calling the server.
 */
public class sTestLoadTester extends JPanel implements ActionListener
{
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean serverIsRunning = false;
	//private boolean serverSetup = false;
	private boolean clientConnected = false;
	private sTestSplashScreen splash = null;
    private JPanel logOutputPanel = new JPanel();
    private JTextPane  logOutput = new JTextPane();
    private JPanel actionPanel = new JPanel();
    private JPanel serverInfo = new JPanel(); //.setLayout(new FlowLayout(FlowLayout.LEFT));
    private JPanel loadButtonPanel = new JPanel();
    private JPanel iterationPanel = new JPanel();
    private JPanel loadCountPanel = new JPanel();
    private JPanel serverIP = new JPanel();
    private JPanel iterationLoadSetupPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JPanel clientPanel = new JPanel();
    private JPanel systemPanel = new JPanel();
    private systemInformation mySysInfo = null;
    private ActionListener setupNewClient = null;
    private sTestLoadServerManager serverManagerThread = null;
    private sTestServerIndicator serverIndi = null;
   
    
    private static HashSet<sTestLoadClientPanel> clients = new HashSet<sTestLoadClientPanel>();
    
    public sTestLoadTester() {
    	splash = new sTestSplashScreen("Splash.png","Creating Load Testing Factory");
		if(null != splash)
			splash.showSplash();
		
		
        setNativeLookAndFeel();
    
        mySysInfo = new systemInformation();
        
        setOpaque(true);
        //
		// Build the tabs for the application
		//
        JTabbedPane tabs = new JTabbedPane();
        
        
        serverIP.setLayout(new GridLayout(2 /* rows */, 1/* cols */));
       // serverIP.setBorder( new TitledBorder("Server IP") );
        
        loadButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        loadButtonPanel.setBorder( new TitledBorder("Start Load Test") );
        //
		// Setup action panel
		//
		actionPanel.setLayout(new GridLayout(1 /* rows */, 2/* cols */));
		actionPanel.setBorder( new TitledBorder("Action Panel") );
		
		
		clientPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		clientPanel.setBorder( new TitledBorder("Client Connection") );
		
		systemPanel.setLayout(new GridLayout(1 /* rows */, 1/* cols */));
		systemPanel.setBorder( new TitledBorder("System Information") );
		
		sTestCPUGraph graph = new sTestCPUGraph();
		systemPanel.add(graph);
		//
		// Flow layout for the input
		//
		serverInfo.setLayout(new GridLayout(2 /* rows */, 1/* cols */));
		//serverInfo.setBorder( new TitledBorder("Server Info") );
		
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setBorder( new TitledBorder("Server Connection") );
       
		
      
		iterationLoadSetupPanel.setLayout(new GridLayout(2 /* rows */, 1/* cols */));
        //
        // ouput panel
        //
        logOutputPanel.setLayout( new GridLayout(1,0));
		logOutputPanel.setBorder( new TitledBorder("Log Results") );
		
		EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
		 
		logOutput.setBorder(eb);
	       
		logOutput.setMargin(new Insets(5, 5, 5, 5));

		JScrollPane areaScrollPaneInput = new JScrollPane(logOutput);
		areaScrollPaneInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPaneInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		logOutputPanel.add(areaScrollPaneInput);
		JLabel ipLabel = new JLabel("Server IP");
        m_ip = new JTextField();
        m_ip.setText("10.0.0.122");
        
        serverIP.add(ipLabel);
        serverIP.add(m_ip);
        
        /*
         * The server URL
         */
		JLabel label = new JLabel("Server URL:");
        m_url = new JTextField();
        m_url.setText("http://www.bichromate.org");
        serverInfo.add(label);
        serverInfo.add(m_url);
        Dimension b = m_url.getPreferredSize();
        b.width = 200;
        m_url.setPreferredSize(b);
     
        @SuppressWarnings("unused")
		ActionListener setupNewClient = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Action Event (ActionEvent) Called " + e);
				//
				// Figure out if the call is add or delete
				//
				
			}
        };

        //
        // Number of Concurrent threads
        //
        loadCountPanel.setLayout(new GridLayout(2 /* rows */, 1/* cols */));
		//loadCountPanel.setBorder( new TitledBorder("Number Concurrent Connections") );
        label = new JLabel("Number of Threads:");
        m_threadCount = new JSpinner();
        m_threadCount.setValue(Integer.valueOf(5));
        Dimension a = m_threadCount.getPreferredSize();
        a.width = 50;
        m_threadCount.setPreferredSize(a);
        loadCountPanel.add(label);
        loadCountPanel.add(m_threadCount);
       
        //
        // Number of iterations
        //
        iterationPanel.setLayout(new GridLayout(2 /* rows */, 1/* cols */));
		//iterationPanel.setBorder( new TitledBorder("Number of Iterations") );
        label = new JLabel("Number of Iterations:");
        m_count = new JSpinner();
        m_count.setValue(Integer.valueOf(5));
        Dimension ab = m_count.getPreferredSize();
        ab.width = 50;
        m_count.setPreferredSize(ab);
        iterationPanel.add(label);
        iterationPanel.add(m_count);
        
        
        iterationLoadSetupPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        iterationLoadSetupPanel.add(loadCountPanel);
        iterationLoadSetupPanel.add(iterationPanel);
        iterationLoadSetupPanel.add(serverInfo);
        iterationLoadSetupPanel.add(loadButtonPanel);
        iterationLoadSetupPanel.add(buttonPanel);
        iterationLoadSetupPanel.add(clientPanel);
        
        actionPanel.add(iterationLoadSetupPanel);
        
       // actionPanel.add(inputPanel);
        actionPanel.add(systemPanel);
        serverIndi = new sTestServerIndicator();

        startServer = new JButton("Start Server");
        serverIsRunning = false;
        client = new JButton("Connect Client");
        m_start = new JButton("Start Load Test");
        m_start.addActionListener(this);
        startServer.addActionListener(this);
        client.addActionListener(this);
        loadButtonPanel.add(m_start);
        buttonPanel.add(startServer);
        buttonPanel.add(serverIndi);
        clientPanel.add(client);
        clientPanel.add(serverIP);
       
        setLayout( new BorderLayout() );
    	setSize(640, 520);
		
		
        JPanel serverConfiguration = new JPanel();
        serverConfiguration.setLayout(new GridLayout(2 /* rows */, 1/* cols */));
        
        JPanel clientConnectionPanel = new JPanel();
       
        clientConnectionPanel.setLayout(new GridLayout(1 /* rows */, 1/* cols */));
        
        serverConfiguration.add(logOutputPanel);
        serverConfiguration.add(actionPanel);
        //
        // Add the server settings
        //
        sTestLoadClientPanel clientPanel = new sTestLoadClientPanel();
        clients.add(clientPanel);
       
      
        clientConnectionPanel.add(clientPanel);
     
		
		tabs.addTab( "Server Settings", null, serverConfiguration );
		tabs.addTab( "Client Connection", null, clientConnectionPanel );
		
		add(tabs);
		
		try {
			logOutput.replaceSelection(mySysInfo.getLocalIPAddress()+"\n");
		} catch (UnknownHostException e) {
			
		} catch (SocketException e) {
			
		}
		
		if(null != splash)
			splash.hideSplash();
        
    }//sTestLoadTester

    public static void setNativeLookAndFeel()
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting native LAF: " + e);
        }
    }

    /**
     * A spinner to let users choose the number of threads they want to use
     */
    private JSpinner m_threadCount;
    /**
     * A spinner to let the users choose the number of times they want the test
     * to run.
     */
    private JSpinner m_count;
    /**
     * This variable will hold the number of times have executed the test.
     */
    private int m_opCount;
    /**
     * The button allowing the user to start the test
     */
    private JButton m_start,startServer,client;
    /**
     * A text field where the user can specify the URL to their server.
     */
    private JTextField m_url,m_ip;

    /**
     * We will use this timer to know when to look at the threads and see which
     * ones need to be restarted.
     */
    private Timer m_shortTimer;

    /**
     * This HashMap will hold onto the threads which are running and the number
     * of times each of those threads has run.
     */
    private HashMap<RetrieverThread, Integer> m_threads = new HashMap<RetrieverThread, Integer>();


    /**
     * This method will reset our thread pool and restart all of the threads.
     */
    private void doStart()
    {
        try {
            m_threads.clear();
            int threadCount = ((Integer) m_threadCount.getValue()).intValue();
            m_opCount = ((Integer) m_count.getValue()).intValue() - 1;
            for (int i = 0; i < threadCount; i++) {
                RetrieverThread thread = new RetrieverThread(new URL(m_url.getText()),logOutput);
                m_threads.put(thread, Integer.valueOf(0));
                thread.start();
            }

            m_shortTimer = new Timer(100, this);
            m_shortTimer.start();
        } catch (MalformedURLException e) {
        	logOutput.replaceSelection("Invalid URL: " + m_url.getText()+"\n");
        }
    }
   
    private static String formatTime(long time)
    {
        return (time / 1000000) + " milliseconds";
    }
    private void setupServer(){
    	//serverSetup = true;
			SwingUtilities.invokeLater(new Runnable() {
				
				public void run() {
					serverManagerThread = new sTestLoadServerManager(setupNewClient);
					serverManagerThread.start();
				}
			});
		
    }//setupServer  

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == m_shortTimer) {
            boolean foundOne = false;
            Object keys[] = m_threads.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                RetrieverThread thread = (RetrieverThread) keys[i];
                int currentCount = ((Integer) m_threads.get(thread)).intValue();
                if (!thread.isRunning()) {
                    /*
                     * If the thread is not running then we want to print the
                     * result and restart it.
                     */
                    if (currentCount <= m_opCount) {
                        if (thread.getException() != null) {
                        	logOutput.replaceSelection(thread + " (Iteration: " + currentCount + ") - " + 
                                               formatTime(thread.getTime()) + " result: " + 
                                               thread.getException().getMessage()+"\n");
                        } else if (!thread.worked()) {
                        	logOutput.replaceSelection(thread + " (Iteration: " + currentCount + ") - " + 
                                               formatTime(thread.getTime()) + " result: FAILED\n");
                        } else {
                        	logOutput.replaceSelection(thread + " (Iteration: " + currentCount + ") - " + 
                                               formatTime(thread.getTime()) + " result: SUCCESS\n");
                        }

                        // Indicate that the test is not done yet.
                        foundOne = true;

                        // Restart the thread.
                        thread.stopWait();

                        // Add the thread back to our thread pool with a new
                        // number.
                        m_threads.remove(thread);
                        m_threads.put(thread, Integer.valueOf(currentCount + 1));

                    } else {
                        /*
                         * If the cound for this thread is greater than the
                         * number of iterations we want then we can just stop
                         * the thread. It is important that we call this stop
                         * method so the run method of the thread can return
                         * properly. If we didn't do that then the thread would
                         * just stay active and waiting and we would have a
                         * memory leak.
                         */
                        thread.doStop();
                    }
                } else {
                    foundOne = true;
                }
            }

            if (!foundOne) {
                /*
                 * If there are no more running threads then we can tell the
                 * user that the test is done.
                 */
            	logOutput.replaceSelection( "All Threads Completed\n");
                m_shortTimer.stop();
            }
        } else if (e.getSource() == m_start) {
            doStart();
        }else if (e.getSource() == client) {
        	startServer.setEnabled(false);
        	if(clientConnected){
        		clientConnected = false;
        		startServer.setEnabled(true);
        		client.setText("Connect Client");
        	}else{
        		client.setText("Disconnet Client");
        		clientConnected = true;
        	}
        	
        } else if (e.getSource() == startServer) {
        	client.setEnabled(false);
        	if(serverIsRunning){
        		if(null != serverManagerThread)
        			serverManagerThread.stopServer();
				serverManagerThread = null;
        		startServer.setText("Start Server");
        		serverIndi.stopAnimation();
        		serverIsRunning = false;
        		client.setEnabled(true);
        	}else{
        		
        		setupServer();
        		 startServer.setText("Stop Server");
        		 serverIsRunning = true;
        		 serverIndi.startAnimation();
        	}
        }
    }
   

 	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			sTestOSInformationFactory path = new sTestOSInformationFactory();
			sTestLoadTester pom = null;
			
			pom = new sTestLoadTester ();
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

}//sTestLoadTester 
