package bichromate.mobile;

import java.io.IOException;

import bichromate.core.sTestOSInformationFactory;

public class bichromateAppiumServerFactory {
	private static Process process; 
	sTestOSInformationFactory os = null;
	// private static String APPIUMSERVERSTART = "node /home/adminuser/Java_Projects/Appium/appium";
	private  String APPIUMSERVERSTART = "node appium --address 127.0.0.1 --port 4723";
	
	
	public bichromateAppiumServerFactory(){
		
		os = new sTestOSInformationFactory();
	}
	
	
	public void startAppiumServer(){
		 
		APPIUMSERVERSTART = new String(APPIUMSERVERSTART +" --log "+os.getLogFileDirectory()+"appium.log");
		
		try{
			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec(APPIUMSERVERSTART);
			Thread.sleep(5000);
			if (process != null) {
				System.out.println("bichromateAppiumServerFactory:startAppiumServer - Appium server started");
			}
		}catch (IOException ioe){
			System.out.println("bichromateAppiumServerFactory:startAppiumServer - Appium server IOException");
		} catch (InterruptedException ie){
			System.out.println("bichromateAppiumServerFactory:startAppiumServer - Appium server InterruptedException");
		}
	}//startAppiumServer
	
	public void stopAppiumServer() throws IOException{
		if (process != null) {
		    process.destroy();
		}
		System.out.println("Appium server stop");
		process = null;
		
	}// stopAppiumServer
	
	private void test() throws InterruptedException{
		startAppiumServer();
		
		try {
			try {
				Thread.sleep(5000);
				stopAppiumServer();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stopAppiumServer();
		} catch (IOException e) {
			System.out.println("bichromateAppiumServerFactory:test - Appium server IOException");
			e.printStackTrace();
		}
		
	}
	
	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public  static void main(final String[] args){
	 		bichromateAppiumServerFactory as = new bichromateAppiumServerFactory();
	 		
	 		if(null != as){
	 			try {
					as.test();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 		}
	 		
	 		
	 	}//main
	 	
	 }//Test
}
