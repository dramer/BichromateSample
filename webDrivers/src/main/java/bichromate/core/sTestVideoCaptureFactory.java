/*
 * sTestSpreadsheetFactory.java	1.0 2016/07/01
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

package bichromate.core;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.media.MediaLocator;

import bichromate.video.JpegImagesToMovie;
/**
 * @author davidwramer
 * @version 1.0
 *
 */
@SuppressWarnings("all")
public class sTestVideoCaptureFactory {
	String videoName = null;
	String movieName = null;
	/**
	 * Used with testNG and global enabling recording or not. all testNG tests start and stop recordings. Used for 
	 * debugging.
	 */
	private boolean enableRecording = false;
	/**
	 * Status of the recorder.
	 */
	public static boolean record = false;
	/**
	 * Screen Width.
	 */
	public static int screenWidth = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getWidth();

	/**
	 * Screen Height.
	 */
	public static int screenHeight = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getHeight();

	/**
	 * Interval between which the image needs to be captured.
	 */
	public static int captureInterval = 50;

	/**
	 * Temporary folder to store the screenshot.
	 */
	public static String store = "screenShots";
	
	private static sTestOSInformationFactory factoryOSInfo;
	
	public sTestVideoCaptureFactory(){
		
		factoryOSInfo = new sTestOSInformationFactory();
	}
	
	public sTestVideoCaptureFactory(boolean recordTest){
		enableRecording = recordTest;
		factoryOSInfo = new sTestOSInformationFactory();
	}
	
	
	/**
	 * @param movFile - name and path of the file to create
	 * @throws MalformedURLException -URL not formatted properly
	 * 
	 */
	
	public static void makeVideo(String movFile) throws MalformedURLException {
		System.out
				.println("#### Easy Capture making video, please wait!!! ####");
		JpegImagesToMovie imageToMovie = new JpegImagesToMovie();
		Vector<String> imgLst = new Vector<String>();
		File f = new File(store);
		File[] fileLst = f.listFiles();
		for (int i = 0; i < fileLst.length; i++) {
			imgLst.add(fileLst[i].getAbsolutePath());
		}
		//
		// grap the bichromate image and resize it
		//
		
		//
		// add the Bichromate image to the end of the video
		//
		for(int x = 0; x < 20; x++){
			imgLst.add(factoryOSInfo.workingDirectory()+factoryOSInfo.fileSeperator()+"images"+factoryOSInfo.fileSeperator()+"bichromate-1.JPG");
		}
		
		// Generate the output media locators.
		MediaLocator oml;
		if ((oml = imageToMovie.createMediaLocator(movFile)) == null) {
			System.err.println("Cannot build media locator from: " + movFile);
			System.exit(0);
		}
		imageToMovie.doIt(screenWidth, screenHeight, (1000 / captureInterval),
				imgLst, oml);
		//
		// Movie is made delete all the sceen captures for the next movie
		//
		//
		//delete all screen captures
		//
		File file = new File(store);
		File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File fa : contents) {
	            fa.delete();
	        }
	    }
	    file.delete();

	}
	public void cleanUpLastMovieRun(){
		//
		// Delete the movie
		//
		try {
			File f = new File(movieName);
		    Files.deleteIfExists(f.toPath());
		} catch (NoSuchFileException x) {
		    System.err.format("%s: no such" + " file or directory%n", movieName);
		} catch (DirectoryNotEmptyException x) {
		    System.err.format("%s not empty%n", movieName);
		} catch (IOException x) {
		    // File permission problems are caught here.
		    System.err.println(x);
		}
		//
		//delete all screen captures
		//
		File file = new File(store);
		File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            f.delete();
	        }
	    }
	    file.delete();
		
	}
	
	private void startRecordingTest(){
		System.out.println("######### Starting Easy Capture Recorder #######");
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println("Your Screen [Width,Height]:" + "["
				+ screen.getWidth() + "," + screen.getHeight() + "]");
		Scanner sc = new Scanner(System.in);
		System.out.println("Rate 20 Frames/Per Sec.");
		System.out
				.print("Do you wanna change the screen capture area (y/n) ? ");
		if (sc.next().equalsIgnoreCase("y")) {
			System.out.print("Enter the width:");
			screenWidth = sc.nextInt();
			System.out.print("Enter the Height:");
			screenHeight = sc.nextInt();
			System.out.println("Your Screen [Width,Height]:" + "["
					+ screen.getWidth() + "," + screen.getHeight() + "]");
		}
		System.out
				.print("Now move to the screen you want to record");
		for(int i=0;i<5;i++){
			System.out.print(".");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		File f = new File(store);
		if(!f.exists()){
			f.mkdir();
		}
		startRecord();
		System.out
		.println("\nEasy Capture is recording now!!!!!!!");

		System.out.println("Press e to exit:");
		String exit = sc.next();
		while (exit == null || "".equals(exit) || !"e".equalsIgnoreCase(exit)) {
			System.out.println("\nPress e to exit:");
			exit = sc.next();
		}
		record = false;
		System.out.println("Easy Capture has stopped.");
		try {
			makeVideo("sTestVideoCaptureFactory-UnitTest-"+System.currentTimeMillis()+".mov");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void startRecording(String testName){
		
		if(enableRecording){
			System.out.println("enableRecording Flag set to true");
			// used by testNG tests, by default this is always true
		}else{
			System.out.println("enableRecording Flag set to false, cancelled startRecording");
			return; 
		}
		videoName = new String(testName);	
		//
		// Create the Screen store folder 
		//
		File f = new File(store);
		if(!f.exists()){
			f.mkdir();
		}
		startRecord();
		
	}
	public void stopRecording(){
		//
		// recording never started
		//
		if(!record) 
			return;
		
		record = false;
		
		System.out.println("Easy Capture has stopped.");
		try {
			movieName = new String(videoName+" "+System.currentTimeMillis()+".mov");
			makeVideo(videoName+" "+System.currentTimeMillis()+".mov");
		} catch (MalformedURLException e) {
			System.err.println("Failed to create movie file: " + e.toString());
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	public static void startRecord() {
		Thread recordThread = new Thread() {
			@Override
			public void run() {
				Robot rt;
				int cnt = 0;
				try {
					rt = new Robot();
					while (cnt == 0 || record) {
						BufferedImage img = rt
								.createScreenCapture(new Rectangle(screenWidth,
										screenHeight));
						ImageIO.write(img, "jpeg", new File("./"+store+"/"
								+ System.currentTimeMillis() + ".jpeg"));
						if (cnt == 0) {
							record = true;
							cnt = 1;
						}
						// System.out.println(record);
						Thread.sleep(captureInterval);
					}
				} catch (Exception e) {
					System.err.println("Failed to start Recording " + e.toString());
				}
			}
		};
		recordThread.start();
	}
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args)
    	{

			sTestVideoCaptureFactory vid = new sTestVideoCaptureFactory(true);
			System.out.println(vid.screenHeight+ " height"+vid.screenWidth + " width");
			vid.stopRecording();
			
			vid.startRecording("sTestVideoCaptureFactory-UnitTest-");
			
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			vid.stopRecording();
			
			
			//vid.cleanUpLastMovieRun();
			
    	}
	}
}
