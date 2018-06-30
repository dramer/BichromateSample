package bichromate.tools;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import bichromate.core.sTestHTTPFactory;
/*
 * sTestSplashScreen.java	1.0 2016/11/25
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


public class sTestZipCodeAPI {
	
	String key = null;//"XMDdOvpCCL8YZymguyJO8IVw2oJSFCWDoAQc6gOI2XG1w3i1C1PKJLhM91FPDCs1";
	String url = null;
	String mileURL = null;
	String kilometerURL =  null;
	String radiusResultsInKM =  null;
	String radiusResultsInMiles =  null;
	String findZipCodesForGivenCityAndState =  null;
	String zipCodeLocationInformation =  null;


	sTestHTTPFactory httpFactory = null;
	protected static ResourceBundle resources;
	static {
		try {
			resources = ResourceBundle.getBundle("tools.sTestZipCodeAPI", Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestZipCodeAPI.properties not found: "+ mre);
			System.exit(0);
		}
		
	}
	/**
	 * method sTestZipCodeAPI() constructor
	 * constructor for the sTestZipCodeAPI
	 */
	public sTestZipCodeAPI(){
		setup(resources);
		
	}//sTestZipCodeAPI
	/**
	 * This method sTestZipCodeAPI() constructor
	 * Constructor for the sTestZipCodeAPI 
	 * @param myResources to initialize all Factories, you can pass in Bichromate.properties
	 */
	public sTestZipCodeAPI(ResourceBundle myResources){
		setup(myResources);
		
	}//sTestZipCodeAPI
	private void setup(ResourceBundle res){
		httpFactory = new sTestHTTPFactory();
		key = new String(res.getString("sTestZipCodeAPI.key"));
		url = new String("https://www.zipcodeapi.com/rest/\"+%s+\"/info.json/\" + %s +\"/radians");
		mileURL = new String( "https://www.zipcodeapi.com/rest/"+key+"/distance.json/%s/%s/mile");
		kilometerURL = new String( "https://www.zipcodeapi.com/rest/"+key+"/distance.json/%s/%s/km");
		radiusResultsInKM = new String( "https://www.zipcodeapi.com/rest/"+key+"/radius.json/%s/%s/km");
		radiusResultsInMiles = new String( "https://www.zipcodeapi.com/rest/"+key+"/radius.json/%s/%s/mile");
		findZipCodesForGivenCityAndState = new String( "https://www.zipcodeapi.com/rest/"+key+"/city-zips.json/%s/%s");
		zipCodeLocationInformation = new String( "https://www.zipcodeapi.com/rest/"+key+"/multi-info.json/%s/degrees");
	}//setup
	/**
	 * This method findZipCodeLocation(String zipCode) returns the lat,lng city,state,timezone 
	 * @param zipCode - zipcode to calcuate
	 * @return String - Location of the zipcode
	 */
	public String findZipCodeLocation(String zipCode){
		String theResults = null;
		String myURL = String.format(zipCodeLocationInformation,zipCode);
		System.out.println("ZipCode location URL: "+myURL);
		try{
			Object result = httpFactory.processGetRequest(myURL);
			theResults = new String(result.toString());
		}catch(Exception e){
			System.out.println("sTestZipCodeAPI:findZipCodeLocation some exception from processGetRequest: "+e);
		}
		return theResults;
		
	}//findZipCodeLocation
	/**
	 * This method zipCodesFromCityState(String city, String state) returns the zipcodes for a city state 
	 * @param city - City name
	 * @param state - state name
	 * @return String zip code from the city and state input
	 */
	public String zipCodesFromCityState(String city, String state){
		String theResults = null;
		String myURL = String.format(findZipCodesForGivenCityAndState,city,state);
		System.out.println("ZipCode from city State URL: "+myURL);
		try{
			Object result = httpFactory.processGetRequest(myURL);
			theResults = new String(result.toString());
		}catch(Exception e){
			System.out.println("sTestZipCodeAPI:zipCodesFromCityState some exception from processGetRequest: "+e);
		}
		return theResults;
	}//zipCodesFromCityState
	/**
	 * This method zipCodeByRadius(String zipCode, String radius) returns all city and zipcodes from a given zipcode and radius. Radius is in miles 
	 * @param zipCode Zip code to calculate distance
	 * @param radius - radius to calcuate distance
	 * @return String -distance in terms of a radius
	 */
	public String zipCodeByRadius(String zipCode, String radius){
		String theResults = null;
		String myURL = String.format(radiusResultsInMiles,zipCode,radius);
		System.out.println("ZipCode from radius URL: "+myURL);
		try{
			Object result = httpFactory.processGetRequest(myURL);
			theResults = new String(result.toString());
		}catch(Exception e){
			System.out.println("sTestZipCodeAPI:getDistanceInMiles some exception from processGetRequest: "+e);
		}
		return theResults;
	}
	/**
	 * This method  getDistanceInMiles(String zipCodeFrom, String ZipCodeTo) returns distance in miles between two zip codes
	 * @param zipCodeFrom - from zip code
	 * @param ZipCodeTo - to zip code
	 * @return String - distance as a String
	 */
	public String getDistanceInMiles(String zipCodeFrom, String ZipCodeTo){
		String theResults = null;
		String myURL = String.format(mileURL,zipCodeFrom,ZipCodeTo);
		System.out.println("ZipCode Distance URL: "+myURL);
		try{
			Object result = httpFactory.processGetRequest(myURL);
			theResults = new String(result.toString());
		}catch(Exception e){
			System.out.println("sTestZipCodeAPI:getDistanceInMiles some exception from processGetRequest: "+e);
		}
		return theResults;
		
	}//getDistanceInMiles
	/**
	 * This method getDistanceInKilometers(String zipCodeFrom, String ZipCodeTo) returns distance in Kilometers between two zip codes
	 * @param zipCodeFrom - from zip code
	 * @param ZipCodeTo - to zip code
	 * @return String - distance as a string
	 */
	public String getDistanceInKilometers(String zipCodeFrom, String ZipCodeTo){
		String theResults = null;
		String myURL = String.format(kilometerURL,zipCodeFrom,ZipCodeTo);
		System.out.println("ZipCode Distance URL: "+myURL);
		try{
			Object result = httpFactory.processGetRequest(myURL);
			theResults = new String(result.toString());
		}catch(Exception e){
			System.out.println("sTestZipCodeAPI:getDistanceInMiles some exception from processGetRequest: "+e);
		}
		return theResults;
		
	}//getDistanceInKilometers
	/**
	 * This method test() tests all the public APIs. run this test to ensure sTestZipCodeAPI is working as expected
	 * @param zipCodeFrom - from zip code
	 * @param ZipCodeTo - to zip code
	 */
	public void test(String zipCodeFrom, String ZipCodeTo){
		String results = getDistanceInMiles("30066","34748");
		if(null != results){
			System.out.println("Distance between 30066 and 34748: "+ results);
		}else{
			System.out.println("Failed to find distance between 30066 and 34748");
		}
		results = zipCodeByRadius("30066","20");
		if(null != results){
			System.out.println("ZipCodes in a 20 mile Radius of 30066: "+ results);
		}else{
			System.out.println("Failed to find find any zipcodes in radius of 20 miles from 30066");
		}
		results = zipCodesFromCityState("Goshen","NY");
		if(null != results){
			System.out.println("ZipCodes from Goshen, NY: "+ results);
		}else{
			System.out.println("Failed to find zipcodes from City and State");
		}
		results = findZipCodeLocation("34748");
		if(null != results){
			System.out.println("ZipCode location"+ results);
		}else{
			System.out.println("Failed to find zipcode location");
		}
	}//test
	
	 //
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			sTestZipCodeAPI zipAPI = new sTestZipCodeAPI();
			if(null != zipAPI){
				zipAPI.test("30066","34748");
			}
			
			
		} // end Main
	 } // end Inner class Test

	
}// sTestZipCodeAPI
