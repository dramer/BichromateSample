package bichromate.graphs;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import bichromate.core.sTestOSInformationFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;

@SuppressWarnings({ "restriction", "unused" })
public class sTestPieChartPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public sTestPieChartPanel(){
	
	       
	 
	        ObservableList<PieChart.Data> pieChartData =
	                FXCollections.observableArrayList(
	                new PieChart.Data("Grapefruit", 13),
	                new PieChart.Data("Oranges", 25),
	                new PieChart.Data("Plums", 10),
	                new PieChart.Data("Pears", 22),
	                new PieChart.Data("Apples", 30));
	        final PieChart chart = new PieChart(pieChartData);
	        chart.setTitle("Imported Fruits");

	       // this.add(chart);
	}//sTestPieChartPanel
	
	
	 //
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			sTestPieChartPanel pieChart = new sTestPieChartPanel();
			sTestOSInformationFactory path = new sTestOSInformationFactory();
			
			
			if(pieChart != null){
				
				JFrame frame = new JFrame();
				
				ImageIcon img = new ImageIcon(path.getImageDirectory()+"bichromateIcon.png");
				
				if(null != img)
					frame.setIconImage(img.getImage());
				
		       // frame.add(pieChart);
		        frame.setBounds(10, 10, 1200, 500);
		       frame.pack();
		       frame.setVisible(true);
			}
			
		} // end Main
	 } // end Inner class Test
	
	
}
