package bichromate.graphs;

import java.awt.GridLayout;
import java.lang.management.ManagementFactory;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.knowm.xchart.QuickChart;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import bichromate.core.sTestOSInformationFactory;


public class sTestCPUGraph extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private XChartPanel<XYChart> sw = null;
	private XYChart chart = null;
	private double[][] initdata = null;
	
	private graphDataThread dataThread = null;
	
	public sTestCPUGraph(){
		
		
		initdata = getUsedMemory();//getCPUData(); //getSineData(phase);

        // Create Chart
        chart = QuickChart.getChart("MEM USAGE", "Time", "Memory", "Memory", initdata[0], initdata[1]);

        // Show it
        // sw = new SwingWrapper<XYChart>(chart);
        setLayout( new GridLayout(1 /* rows */, 1/* cols */) );
        add(sw = new XChartPanel<XYChart>(chart)); 
        dataThread = new graphDataThread(sw,chart);
        dataThread.start();
        System.out.println(dataThread.getName());
       
	}//sTestCPUGraph
	private static double[][] getUsedMemory(){
		Runtime runtime = Runtime.getRuntime();
		int mb = 1024*1024;
		double time = 0;
		 double[] xData = new double[100];
        double[] yData = new double[100];
        for (int i = 0; i < xData.length; i++) {
          xData[i] = time++;
          yData[i] = (double)((runtime.totalMemory() - runtime.freeMemory()) / mb);
        }
        return new double[][] { xData, yData };
	}
	@SuppressWarnings("unused")
	private static double[][] getCPUData() {
		double cnt = 0;
        double[] xData = new double[100];
        double[] yData = new double[100];
        for (int i = 0; i < xData.length; i++) {
          xData[i] = cnt++;
          yData[i] = getProcessCpuLoad();
        }
        return new double[][] { xData, yData };
	}
	public static double getProcessCpuLoad(){
		try{
			MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
			ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
			AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });
		
		    if (list.isEmpty())     return Double.NaN;
	
		    Attribute att = (Attribute)list.get(0);
		    Double value  = (Double)att.getValue();
	
		    // usually takes a couple of seconds before we get real values
		    if (value == -1.0)      return Double.NaN;
		    // returns a percentage value with 1 decimal point precision
		    return ((int)(value * 1000) / 10.0);
		}catch(Exception e){
			return 0;
		}
	}//getProcessCpuLoad
	@SuppressWarnings("unused")
	private static double[][] getSineData(double phase) {

        double[] xData = new double[100];
        double[] yData = new double[100];
        for (int i = 0; i < xData.length; i++) {
          double radians = phase + (2 * Math.PI / xData.length * i);
          xData[i] = radians;
          yData[i] = Math.sin(radians);
        }
        return new double[][] { xData, yData };
	}
	private class graphDataThread extends Thread {
		private XChartPanel<XYChart> sw = null;
		private XYChart chart = null;
		public graphDataThread(XChartPanel<XYChart> sw,XYChart chart){
			
			this.sw = sw;
			this.chart = chart;
		}
	    public void run(){
	  
	    	while(true){
	    	 try{
	        	 final double[][] data = getUsedMemory();//getCPUData(); //getSineData(phase);
	 	         chart.updateXYSeries("Memory", data[0], data[1], null);
	 	         sw.repaint();
	 	        Thread.sleep(300);
	          }catch (Exception e){
	        	  System.out.println("Exception"+e);
	          }
	    	}
	    }
	    @SuppressWarnings("unused")
		private  double[][] getSineData(double phase) {
	
	          double[] xData = new double[100];
	          double[] yData = new double[100];
	          for (int i = 0; i < xData.length; i++) {
	            double radians = phase + (2 * Math.PI / xData.length * i);
	            xData[i] = radians;
	            yData[i] = Math.sin(radians);
	          }
	          return new double[][] { xData, yData };
	        }
	  }//graphDataThread
	
	 //
		// Inner class for testing on the command line
		//
		public static class Test
		{
			public static void main(final String[] args){
				
				sTestCPUGraph graph = new sTestCPUGraph();
				sTestOSInformationFactory path = new sTestOSInformationFactory();
				
				
				if(graph != null){
					
					JFrame frame = new JFrame();
					
					ImageIcon img = new ImageIcon(path.getImageDirectory()+"bichromateIcon.png");
					
					if(null != img)
						frame.setIconImage(img.getImage());
					
			       frame.add(graph);
			        frame.setBounds(10, 10, 1200, 500);
			       frame.pack();
			       frame.setVisible(true);
				}
				
			} // end Main
		 } // end Inner class Test

}
