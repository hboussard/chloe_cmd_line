package fr.inra.sad.bagap.chloe.view.distance;

import java.awt.Color;
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
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.combination.CombinationExpressionFactory;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;

public class LineChartWeightedDistance extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	public LineChartWeightedDistance(String formula) {
        super("distance function");
        XYDataset dataset = createDataset(formula);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        
        pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
    }
    
	/*
	private double function(double x){
		// fonction droite absolue
		double bi = 0; // borne inférieure
		double vi = 0; // valeur inférieure
		double bs = 150; // borne supérieure 
		double vs = 1; // valeur supérieure
		
		double b = (bi*vs - bs*vi) / (bi - bs);
		double a = (vs - b) / bs;
		return a*x + b; 
	}
	*/
	
    private XYDataset createDataset(String formula) {
    	double dmax = 150;
    	DistanceFunction df = CombinationExpressionFactory.createDistanceFunction(formula, dmax);
    	
        final XYSeries series1 = new XYSeries("function");
        
        for(int i=0; i<dmax; i++){
        	series1.add(i, df.interprete(i));
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
                
        return dataset;
    }
    
    private JFreeChart createChart(XYDataset dataset) {
        
        // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            "distance function",      // chart title
            "distance",                      // x axis label
            "weight",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            true                     // urls
        );

        chart.setBackgroundPaint(Color.white);
        
        // get a reference to the plot for further customisation...
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
       
        return chart;
    }

    /*
    public static void main(final String[] args) {
        final LineChartWeightedDistance demo = new LineChartWeightedDistance("distance function");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
    */
    
}