package fr.inra.sad.bagap.chloe.view.distance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination.CombinationExpressionFactory;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class DistanceFunctionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
    public DistanceFunctionDialog(Frame frame, String formula, double dmax) {
        super(frame, "Distance Function", true);
        init(frame, formula, dmax);
        setVisible(true);
    }
    
    private void init(Window window, String formula, double dmax) {
    	
    	setSize(500, 300);
    	
    	int x = window.getLocation().x + (window.getSize().width / 2) - (this.getSize().width / 2);        
    	int y = window.getLocation().y + (window.getSize().height / 2) - (this.getSize().height / 2);
    	setLocation(x, y);
    	
    	
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
      
        XYDataset dataset = createDataset(formula, dmax);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        c.add(chartPanel, null);
    }
	
    private XYDataset createDataset(String formula, double dmax) {
    	if(dmax == Raster.getNoDataValue()){
    		dmax = 1000;
    	}
    	DistanceFunction df = CombinationExpressionFactory.createDistanceFunction(formula, dmax);
    	
        final XYSeries series1 = new XYSeries("function");
        for(int i=0; i<dmax; i++){
        	series1.add(i, df.interprete(i));
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
             
        return dataset;
    }
    
    private JFreeChart createChart(final XYDataset dataset) {
    	
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "distance function",      // chart title
            "distance",                      // x axis label
            "weight",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            true                     // urls
        );

       // chart.
        
       // chart.setBackgroundPaint(Color.white);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
       
        return chart;
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
