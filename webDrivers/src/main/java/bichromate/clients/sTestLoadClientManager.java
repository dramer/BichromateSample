package bichromate.clients;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



/**
 * A simple Swing-based client for the chat server.  Graphically
 * it is a frame with a text field for entering messages and a
 * textarea to see the whole dialog.
 *
 * The client follows the Chat Protocol which is as follows.
 * When the server sends "SUBMITNAME" the client replies with the
 * desired screen name.  The server will keep sending "SUBMITNAME"
 * requests as long as the client submits screen names that are
 * already in use.  When the server sends a line beginning
 * with "NAMEACCEPTED" the client is now allowed to start
 * sending the server arbitrary strings to be broadcast to all
 * chatters connected to the server.  When the server sends a
 * line beginning with "MESSAGE " then all characters following
 * this string should be displayed in its message area.
 */
@SuppressWarnings("unused")
public class sTestLoadClientManager {


	private String serverIP = null;
    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("Bichromate client load connection");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);

    
 	private static ResourceBundle resources;
    
    static
	{
		try
		{
			resources = ResourceBundle.getBundle("clients.sTestLoadClientManager",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestLoadClientManager.properties not found: "+mre);
			System.exit(0);
		}
	}
    
    /**
     * Constructs the client by laying out the GUI and registering a
     * listener with the textfield so that pressing Return in the
     * listener sends the textfield contents to the server.  Note
     * however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED
     * message from the server.
     */
    public sTestLoadClientManager() {

       setup(resources);
       
    }//sTestLoadClientManager
    public sTestLoadClientManager(ResourceBundle myResources) {

        setup(myResources);
        
     }//sTestLoadClientManager
    private void setup(ResourceBundle myResources){
    	 // Layout GUI
    	
    	serverIP = new String(resources.getString("sTestLoadServerManager.serverIP"));
    	
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        // Add Listeners
        textField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server.    Then clear
             * the text area in preparation for the next message.
             */
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }
    /**
     * Prompt for and return the address of the server.
     */
   
	private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to the Chatter",
            JOptionPane.QUESTION_MESSAGE);
    }
    public void buildConnection(){
    	
    }

    /**
     * Prompt for and return the desired screen name.
     */
   
	private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }
    
	private static class Handler extends Thread {
    	private String serverIP = null;
    	private ActionListener clientListener = null;
        private ActionEvent myActions = null;
        private BufferedReader in = null;
        private PrintWriter out = null;
        private JTextField textField = null;
        private JTextArea messageArea = null;
    	 /**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         */
        public Handler(ActionListener setupNewClient,String serverIPAddress,JTextField theTextField,JTextArea theTextArea) {
            
            this.clientListener = setupNewClient;
            serverIP = new String(serverIPAddress);
            textField = theTextField;
            messageArea = theTextArea;
            System.out.println("Created Client Socket Handler");
        }
    	
    	
    	/**
         * Connects to the server then enters the processing loop.
         */
    	 public void run() {

            // Make connection and initialize streams
            String serverAddress = serverIP;
            Socket socket;
			try {
				socket = new Socket(serverAddress, 9001);
				 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			     out = new PrintWriter(socket.getOutputStream(), true);
			     // Process all messages from server, according to the protocol.
	            while (true) {
	                String line = in.readLine();
	                if (line.startsWith("SUBMITNAME")) {
	                    out.println(getName());
	                } else if (line.startsWith("NAMEACCEPTED")) {
	                    textField.setEditable(true);
	                } else if (line.startsWith("MESSAGE")) {
	                    messageArea.append(line.substring(8) + "\n");
	                }
	            }
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				
			}
        }   
    }
   
    //
   	// Inner class for testing on the command line
   	//
   	public static class Test
   	{
   		public static void main(final String[] args){
   			
   			sTestLoadClientManager client = new sTestLoadClientManager();
   			if(null != client){
   				client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   				client.frame.setVisible(true);
   				client.buildConnection();
   			}
   		} // end Main
   	 } // end Inner class Test  
}//sTestLoadClientManager