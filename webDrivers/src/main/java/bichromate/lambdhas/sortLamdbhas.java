package bichromate.lambdhas;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// http://winterbe.com/posts/2014/03/16/java-8-tutorial/


public class sortLamdbhas {

	List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
	
	
	
	private void selfTest(){
		
		for(int x = 0; x < names.size(); x++){
			
			System.out.println(names.get(x).toString());
		}
		
		
		//
		// Old way
		//
		/*
		Collections.sort(names, new Comparator<String>() {
			
		    @Override
		    public int compare(String a, String b) {
		        return b.compareTo(a);
		    }
		});
		*/
		//
		// Using Lambdahs
		//
		//Collections.sort(names, (String a, String b) -> {
		//    return b.compareTo(a);
		//});
		//
		// Refine Lambdahs
		//
		Collections.sort(names, (a, b) -> b.compareTo(a));
		
		System.out.println("============== AFTER SORT==================");
		for(int x = 0; x < names.size(); x++){
			
			System.out.println(names.get(x).toString());
		}
	}
	
	
	
	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public static void main(final String[] args){
	 		
	 		sortLamdbhas testLamdhas = new sortLamdbhas();
	 		if(null != testLamdhas){
	 			testLamdhas.selfTest();
	 		}
	 		
	 	}
	 }
	
}
