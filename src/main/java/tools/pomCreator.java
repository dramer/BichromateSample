package tools;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import bichromate.tools.sTestPOMFactory;



public class pomCreator  extends sTestPOMFactory{
	
	
	

	public pomCreator(ResourceBundle resources){
		super(resources);
		
	}//pomCreator
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		
		private static ResourceBundle resources;

		static
		{
				try
				{
					resources = ResourceBundle.getBundle("common.Bichromate",Locale.getDefault());
				} catch (MissingResourceException mre) {
					System.out.println("Bichromate.properties not found: "+mre);
					System.exit(0);
				}
		}
		public static void main(final String[] args){
			pomCreator pc = null;
			
			
			pc = new pomCreator(resources);
			if(null != pc){
				pc.selfTest();
			}
			
			
		} // end Main
	 } // end Inner class Test
}//pomCreator
