package bichromate.sample.pageDeclaration;

import org.openqa.selenium.winium.WiniumDriver;

import bichromate.core.sTestBasePageDeclaration;

public class sTestCalculatorStandardView {
	
	 private WiniumDriver app = null;
	
	public sTestCalculatorStandardView(WiniumDriver appDriver){
			
		app = appDriver;
		
	}
	
	 public int add(int firstNumber, int secondNumber) {
	        return compute("Add", firstNumber, secondNumber);
    }

    public int subtract(int firstNumber, int secondNumber) {
        return compute("Subtract", firstNumber, secondNumber);
    }

    public int multiply(int firstNumber, int secondNumber) {
        return compute("Multiply", firstNumber, secondNumber);
    }

    public int divide(int firstNumber, int secondNumber) {
        return compute("Divide", firstNumber, secondNumber);
    }

    private int compute(String type, int firstNumber, int secondNumber) {
        app.findElementByName(String.valueOf(firstNumber)).click();
        app.findElementByName(type).click();
        app.findElementByName(String.valueOf(secondNumber)).click();
        app.findElementByName("Equals").click();
        Double result = Double.parseDouble(app.findElementById("150").getAttribute("Name"));
        return result.intValue();
    }
	
	

}
