/*
 * sTestSpellChecker.java	1.0 2017/06/28
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

import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;



public class sTestSpellChecker {
	
	private static String dictFile = "english.0";
	private static String phonetFile = "phonet.en";
	private sTestOSInformationFactory osInfo = null;
	  
	private SpellDictionary dictionary;

	private SpellChecker spellCheck = null;
	 /**
		 * This class demonstrates oTestHTTPFactory().
		 * <br>This class sets up the HTTPFactory for making HTTP calls.
		 * <br>
		 */
	    public sTestSpellChecker(){
	    	
	    	osInfo = new sTestOSInformationFactory();
	    	
	    	     
			try {
				dictionary = new SpellDictionaryHashMap(new File(osInfo.getDictionaryDirectory()+dictFile), new File(osInfo.getDictionaryDirectory()+phonetFile));
				 spellCheck = new SpellChecker(dictionary);
	    	       
			} catch (FileNotFoundException e) {
				System.out.println("sTestGoogleSpellChecker: failed to create SpellChecker");
				e.printStackTrace();
				System.exit(0);

				
			} catch (IOException e) {
				System.out.println("sTestGoogleSpellChecker: failed to create SpellChecker");
				e.printStackTrace();
				System.exit(0);
			}
	    }// sTestSpellChecker
		public void selfTest(){
	    	
	    	 if(spellCheck.isCorrect("hello")){
	    		 System.out.println("sTestGoogleSpellChecker: hello was spelled correctly");
	    	 }else{
	    		 System.out.println("sTestGoogleSpellChecker: hello was not spelled correctly");
	    	 }
	    	 if(!spellCheck.isCorrect("Helloooo")){
	    		 System.out.println("sTestGoogleSpellChecker: Helloooo was not spelled correctly");
	    	 }else{
	    		 System.out.println("sTestGoogleSpellChecker: Helloooo was spelled correctly");
	    	 }
	    	 if(spellCheck.isCorrect("turtle")){
	    		 System.out.println("sTestGoogleSpellChecker: turtle was not spelled correctly");
	    	 }else{
	    		 System.out.println("sTestGoogleSpellChecker: turtle was spelled correctly");
	    	 }
	    	 
	    }//selfTest
	
	    // Inner class for testing on the command line
		//
		public static class Test
		{
			public static void main(final String[] args){
				
				
				sTestSpellChecker spChecker = null;
				
				spChecker = new sTestSpellChecker();
				if(spChecker != null){
					
					spChecker.selfTest();
					
				}
				
			} // end Main
		 } // end Inner class Test
}//sTestSpellChecker
