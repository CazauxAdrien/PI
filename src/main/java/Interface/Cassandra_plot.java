package Interface;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author imssbora
 *
 */
public class Cassandra_plot extends JFrame {

  private static final long serialVersionUID = 1L;

  public Cassandra_plot(String title, XYSeriesCollection datasetTest) {
    super(title);
    // Create dataset
    // Create chart
    
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    JFreeChart chart = ChartFactory.createXYLineChart(
        "Cassandra", // Chart title
        "Nb", // X-Axis Label
        "Time", // Y-Axis Label
        datasetTest, PlotOrientation.VERTICAL, true, true, false);
    	XYPlot xyPlot = (XYPlot) chart.getPlot();
       
		
        LogarithmicAxis logAxis = new LogarithmicAxis("Time");
        logAxis.setTickUnit(new NumberTickUnit(2));
        logAxis.setAutoRangeIncludesZero(true);
        logAxis.setRange(0.0001, 10000.0);
        xyPlot.setRangeAxis(logAxis);
        
        
    ChartPanel panel = new ChartPanel(chart);
    setContentPane(panel);
  }

}