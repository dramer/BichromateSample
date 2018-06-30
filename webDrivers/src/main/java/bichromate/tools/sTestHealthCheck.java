/*
 * sTestHealthCheck.java	1.0 2013/01/23
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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
/**
 * This class Demonstrates oTestHealthCheck().
 * <br>This class factory is used to check the health of an application by reading the log file
 * <br>
 * @author davidwramer
 * @version 1.0
 */
public class sTestHealthCheck  extends Thread {
	
	private static ResourceBundle resources;
	private String tailFileName = null;
	private String tailFilePathName = null;
	private File tailFile = null;
	private LineNumberReader tailFileReader = null;
	private healthCheckCallBack callBack = null;
	
	private boolean running;
	static
	{
		try
		{
			resources = ResourceBundle.getBundle("tools.sTestHealthCheck",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestHealthCheck.properties not found: "+mre);
			System.exit(0);
		}
	}
	sTestHealthCheck(){
		tailFileName = new String(resources.getString("sTestHealthCheck.tailFileName"));
		tailFilePathName = new String(resources.getString("sTestHealthCheck.tailFilePathName"));
		
		tailFile = new File(tailFilePathName+tailFileName);	
		try {
			tailFileReader = new LineNumberReader(new FileReader(tailFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		running = true;
    }
	public void registerCallBack(healthCheckCallBack callBack){
		this.callBack = callBack;
	}
	public void run()
    {
		 String line;
		while (running) {
		    try {
				line = tailFileReader.readLine();
				if (line == null) {
					System.out.println("no more lines to read");
			        try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					
					}
			    } else {
			    	System.out.println("line of Data: "+line);
			    	callBack.callback_method(line);
			    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				running = false;
			}
		   
		}
    }
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			Thread myHealthCheck = new sTestHealthCheck();
			myHealthCheck.start();
		}
	}
}//oTestHealthCheck
