package Interface;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;

public class Influxdb_plot extends JFrame {

	  private static final long serialVersionUID = 1L;

	  public Influxdb_plot(String title, XYSeriesCollection datasetTest) {
	    super(title);
	    // Create dataset
	    // Create chart
	    
	    
	    JFreeChart chart = ChartFactory.createXYLineChart(
	        "Influxdb", // Chart title
	        "Nb", // X-Axis Label
	        "Time", // Y-Axis Label
	        datasetTest, PlotOrientation.VERTICAL, true, true, false);
	    	XYPlot xyPlot = (XYPlot) chart.getPlot();
	       
			
	        LogarithmicAxis logAxis = new LogarithmicAxis("Price($)");
	        logAxis.setTickUnit(new NumberTickUnit(2));
	        logAxis.setAutoRangeIncludesZero(true);
	        logAxis.setRange(0.9, 4000.0);
	        xyPlot.setRangeAxis(logAxis);
	        
	        
	    ChartPanel panel = new ChartPanel(chart);
	    setContentPane(panel);
	  }
	}
