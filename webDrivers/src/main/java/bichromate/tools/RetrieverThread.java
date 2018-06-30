package bichromate.tools;

import java.net.*;

import javax.swing.JTextPane;


/**
 * This class is the thread which will contact the server and read the results.
 * There will be as many instances of this thread as the user indicates in the
 * UI of this application.
 */
@SuppressWarnings("unused")
public class RetrieverThread extends Thread
{
   
    private URL m_server;
    private boolean m_cond = true;
    private Exception m_exception;
    private boolean m_worked = false;
    private boolean m_shouldStop;
    private boolean m_isRunning = false;
    private static int g_threadCount;
    private int m_threadNumber;
    private long m_time;
   
	private boolean m_firstParam = true;
    private JTextPane  logOutput = null;

    public RetrieverThread(URL server,JTextPane  log)
    {
        m_server = server;
        logOutput = log;
        m_threadNumber = g_threadCount;
        g_threadCount++;
    }

    public String toString()
    {
        return "Client Thread " + (m_threadNumber + 1);
    }

    public Exception getException()
    {
        return m_exception;
    }

    public void doStop()
    {
        /*
         * Once we have set the flag to indicate this thread should stop we need
         * to notify the thread so it can stop waiting and let the run method
         * return.
         */
        m_shouldStop = true;
        stopWait();
    }

    public void run()
    {
        /*
         * This run method will work in a simple loop. It will execute the
         * getData method and then wait until it is notified again. This loop
         * will continue until the doStop method is called.
         */
        while (true) {
            if (m_shouldStop)
                return;

            m_isRunning = true;
            getData();
            m_isRunning = false;
            doWait();
        }
    }

    /*
     * This is the method which will actually get the data from the server.
     */
    private void getData()
    {
        /*
         * Step 1. Reset all the variables from the last time the thread ran.
         */
        m_time = -1;
        m_worked = false;
        m_exception = null;
        m_firstParam = true;

        /*
         * Step 2. Create the HTTP connection.
         */
        HttpURLConnection conn = null;
        long startTime = System.nanoTime();
        try {
            conn = (HttpURLConnection) m_server.openConnection();

            /*
             * Step 3. Set properties on the connection. This connection will
             * use the POST method, will not follow redirects, and will use the
             * form URL encoded content type for parameters.
             */
            conn.setRequestMethod("POST");
            HttpURLConnection.setFollowRedirects(false);

            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            /*
             * Step 6. Record the starting time and connect to the server.
             */
            startTime = System.nanoTime();
            conn.connect();

            //
            // 200 response success
            //
            if ((conn.getResponseCode() >= 200) &&(conn.getResponseCode() <= 299) ) {
            	logOutput.replaceSelection("We got a "+ conn.getResponseCode()+"\n");
            	m_worked = true;
            	m_time = System.nanoTime() - startTime;
                return;
            }
            /*
             * A response of 302 indicates that the URL has been temporarily
             * moved so we will get the new URL and try again.
             */
            if (conn.getResponseCode() == 302) {
            	logOutput.replaceSelection("We got a 302\n");
                m_server = new URL(conn.getHeaderField("Location"));
                getData();
                return;
            }
            //
            // 3XX errors are Redirection
            //
            if ((conn.getResponseCode() >= 300) &&(conn.getResponseCode() <= 399) ) {
            	logOutput.replaceSelection("We got a Redirection "+ conn.getResponseCode()+  "\n");
            }

            /*
             * A response of 4XX Client Error
             * URL.
             */
            if ((conn.getResponseCode() >= 400) &&(conn.getResponseCode() <= 499) ) {
            	logOutput.replaceSelection("We got a "+ conn.getResponseCode()+  " and were denied acces to the URL.\n");
            }

            /*
             * A response of 500 Server Error
             * URL.
             */
            if ((conn.getResponseCode() >= 500)) {
            	logOutput.replaceSelection("We got a "+ conn.getResponseCode()+  " and were denied acces to the URL.\n");
            }

            /*
             * Step 8. Parse the output from the server.
             */
           // InputStream in = conn.getInputStream();
           //  m_worked = parseSuccess(in);

            /*
             * Step 9. Record the amount of time it took to talk to the server.
             */
          
        } catch (Exception e) {
            e.printStackTrace();
            m_exception = e;
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        m_time = System.nanoTime() - startTime;
    }

    /*
     * This method will parse the output of the server and see if it returned
     * successfully. This sample is very simple and just searches for the text
     * "Everything worked fine". You can make this method as complicated as you
     * need it to be.
     
    private static boolean parseSuccess(InputStream in)
        throws IOException
    {
        LineNumberReader lnr = new LineNumberReader(new BufferedReader(new InputStreamReader(in)));

        String line = null;

        while ((line = lnr.readLine()) != null) {
            if (line.indexOf("<head>") != -1) {
                return true;
            }
        }

        return false;
    }
    */
    /**
     * This method indicates that the thread should stop waiting and run again.
     */
    protected synchronized void stopWait()
    {
        this.notify();
    }

    /**
     * This method will cause the thread to wait until the stopWait method is
     * called
     */
    protected synchronized void doWait()
    {
        m_cond = true;
        while (m_cond) {
            try {
                this.wait();
                m_cond = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Indicates if this thread is currently running or waiting.
     * 
     * @return true if the thread is running and false if it is waiting
     */
    protected boolean isRunning()
    {
        return m_isRunning;
    }

    /**
     * Indicates how long this thread ran for.
     * 
     * @return the amount of time it took the thread to rn in nano seconds
     */
    protected long getTime()
    {
        return m_time;
    }

    /**
     * Indicates if this thread was successful.
     * 
     * @return true if the thread completed successfully and false otherwise.
     */
    protected boolean worked()
    {
        return m_worked;
    }
}
