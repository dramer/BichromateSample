
package bichromate.servers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A multithreaded chat room server.  When a client connects the
 * server requests a screen name by sending the client the
 * text "SUBMITNAME", and keeps requesting a name until
 * a unique one is received.  After a client submits a unique
 * name, the server acknowledges with "NAMEACCEPTED".  Then
 * all messages from that client will be broadcast to all other
 * clients that have submitted a unique screen name.  The
 * broadcast messages are prefixed with "MESSAGE ".
 *
 * Because this is just a teaching example to illustrate a simple
 * chat server, there are a few features that have been left out.
 * Two are very useful and belong in production code:
 *
 *     1. The protocol should be enhanced so that the client can
 *        send clean disconnect messages to the server.
 *
 *     2. The server should do some logging.
 */
@SuppressWarnings("unused")
public class sTestLoadServerManager extends Thread{
	 ServerSocket listener;
	 ActionListener setupNewClient = null;
    /**
     * The port that the server listens on.
     */
    private static final int PORT = 9001;
    private String portString = null;

	private int port = PORT;

    /**
     * The set of all names of clients in the chat room.  Maintained
     * so that we can check that new clients are not registering name
     * already in use.
     */
    private static HashSet<String> names = new HashSet<String>();

    /**
     * The set of all the print writers for all the clients.  This
     * set is kept so we can easily broadcast messages.
     */
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    
    private static ResourceBundle resources;
    
    static
	{
		try
		{
			resources = ResourceBundle.getBundle("servers.sTestLoadServerManager",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestLoadServerManager.properties not found: "+mre);
			System.exit(0);
		}
	}
    public sTestLoadServerManager(ActionListener myNewClient){
    	setupNewClient = myNewClient;
    	
    	portString = new String(resources.getString("sTestLoadServerManager.port"));
    	port = Integer.valueOf(portString);
    	
    }
    public sTestLoadServerManager(ActionListener myNewClient,ResourceBundle myResources){
    	setupNewClient = myNewClient;
    	
    	portString = new String(myResources.getString("sTestLoadServerManager.port"));
    	port = Integer.valueOf(portString);
    	
    }
    /**
     * StopServer, removes all registered names and closes the socket
     */
    public void stopServer(){
    	
    	for(int x = 0; x < names.size();x++){
    		names.remove(x);
    	}
    	
    	Iterator<PrintWriter> writerIterator = writers.iterator();
    	while(writerIterator.hasNext()){
    		writerIterator.next().println("SERVERMSG: Server going down...." );
    		writerIterator.next().close();
    	}
    	
    	for(int y = 0;y < writers.size();y++){
    		writers.remove(y);
    	}
    	try {
			listener.close();
		} catch (IOException e) {
			System.out.println("Error releasing socket");
			e.printStackTrace();
		}
    }
    public void run() {
    	 System.out.println("Server Socket created");
	        try {
	        	listener = new ServerSocket(PORT);
	        	// listener.setSoTimeout(10000);
	            while (true) {
	            	new Handler(listener.accept(),setupNewClient).start();
	                System.out.println("Connection made");
	            }
	        }catch (Exception e){
	        	 System.out.println("Some Exception"+e);
	        } finally{
	        	try {
					if(null != listener) listener.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	      }
    }
    
    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for a dealing with a single client
     * and broadcasting its messages.
     */
    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private ActionListener clientListener = null;
        private ActionEvent myActions = null;

        /**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         */
        public Handler(Socket socket,ActionListener setupNewClient) {
            this.socket = socket;
            this.clientListener = setupNewClient;
            System.out.println("Created Server Socket Handler");
        }
       
        /**
         * Services this thread's client by repeatedly requesting a
         * screen name until a unique one has been submitted, then
         * acknowledges the name and registers the output stream for
         * the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
        public void run() {
        	System.out.println("Connection Requested");
            try {

                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Request a name from this client.  Keep requesting until
                // a name is submitted that is not already used.  Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            myActions = new ActionEvent(name, 0,"Add Client");
                            clientListener.actionPerformed(myActions);
                            break;
                        }
                    }
                }

                // Now that a successful name has been chosen, add the
                // socket's print writer to the set of all writers so
                // this client can receive broadcast messages.
                out.println("NAMEACCEPTED");
                writers.add(out);

                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if (name != null) {
              
                	 myActions = new ActionEvent(name, 1, "Remove Client");
                     clientListener.actionPerformed(myActions);
                    names.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }// finally
        }//run
    }//Handler
    
    
    //
   	// Inner class for testing on the command line
   	//
   	public static class Test
   	{
   		public static void main(final String[] args){
   			ActionListener setupNewClient = new ActionListener() {
   				public void actionPerformed(ActionEvent e) {
   					System.out.println("Action Event (ActionEvent) Called " + e);
   				}
   	        };
   			sTestLoadServerManager myServer = new sTestLoadServerManager(setupNewClient);
   			
   			if(null != myServer){
   				myServer.start();
   			}
	       
   		}// main
    }// Test
}//sTestLoadServerManager