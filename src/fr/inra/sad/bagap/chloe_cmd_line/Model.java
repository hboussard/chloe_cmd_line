package fr.inra.sad.bagap.chloe_cmd_line;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.csvreader.CsvWriter;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.treatment.Treatment;
import fr.inra.sad_paysage.apiland.treatment.TreatmentException;
import fr.inra.sad_paysage.apiland.treatment.window.MapWindowMatrixTreatment;
import fr.inra.sad_paysage.apiland.treatment.window.SlidingWindowMatrixTreatment;
import fr.inra.sad_paysage.apiland.window.shape.WindowShapeType;

public class Model {


	public static boolean runMapWindow(String[] inputMatrix, String processType, String[] metrics, String csvOutput) {
		
		Treatment treatment = new MapWindowMatrixTreatment();
		
		Set<String> myMetrics = Util.getSetString(metrics);
		
		try{
			
			// settings
			if(processType.equalsIgnoreCase("qualitative")){
				treatment.setInput("qualitative", true);
			}else{
				treatment.setInput("qualitative", false);
			}
			treatment.setInput("metrics", myMetrics);
			
			try {
				CsvWriter cw = new CsvWriter(csvOutput);
				cw.setDelimiter(';');
				
				cw.write("name");
				for(String m : myMetrics){
					cw.write(m);
				}
				
				cw.endRecord();
				
				for(String matrix : inputMatrix){
					//publish("treatment of the matrix "+matrix);
					//treatment.addView(this);
					//progress = 0;
					
					treatment.setInput("matrix", Util.getMatrix(matrix));
					
					treatment.setInput("csv", cw);
					
					// treatment
					try{
						treatment.allRun();
						
						treatment.clearViews();
						treatment.clearOutputs();
					} catch(TreatmentException e){
						e.printStackTrace();
					}
				}
				
				cw.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public static boolean runSlidingWindow(String[] inputMatrix, String processType, String shape, String friction, String frictionMatrix,
			String[] windowSizes, String delta, String interpolate, String minRate, String[] metrics,
			String outputFolder, String exportCsv, String exportAscii,
			String[] filters, String[] unfilters) {
		
		/*
		 * Set<Matrix> inputMatrix,
			String processType, WindowShapeType shape, Friction friction, Matrix frictionMatrix,
			List<Integer> windowSizes, int delta, boolean interpolate, double minRate, Set<String> metrics,
			String outputFolder, boolean viewAsciiOutput, boolean exportCsv, boolean exportAscii,
			Set<Integer> filters, Set<Integer> unfilters
		 */
		
		Treatment treatment = new SlidingWindowMatrixTreatment();
		
		Set<String> myMetrics = Util.getSetString(metrics);
		WindowShapeType myShape = Util.getShape(shape);
		int myDelta = Util.getInteger(delta);
		boolean myInterpolate = Util.getBoolean(interpolate);
		double myMinRate = Util.getDouble(minRate);
		Set<Integer> myFilters = Util.getSetInteger(filters);
		Set<Integer> myUnfilters = Util.getSetInteger(filters);
		Friction myFriction = Util.getFriction(friction);
		Matrix myFrictionMatrix = Util.getMatrix(frictionMatrix);
		List<Integer> myWindowSizes = Util.getListInteger(windowSizes);
		boolean myExportCsv = Util.getBoolean(exportCsv);
		boolean myExportAscii = Util.getBoolean(exportAscii);
		
		try{
			// settings
			if(processType.equalsIgnoreCase("qualitative")){
				treatment.setInput("qualitative", true);
			}else{
				treatment.setInput("qualitative", false);
			}
			treatment.setInput("shape", myShape);
			treatment.setInput("delta", myDelta);
			treatment.setInput("interpolation", myInterpolate);
			treatment.setInput("min_rate", myMinRate);
			treatment.setInput("metrics", myMetrics);
			treatment.setInput("filters", myFilters);
			treatment.setInput("unfilters", myUnfilters);
			if(myFriction != null){
				treatment.setInput("friction_map", myFriction);
			}
			if(myFrictionMatrix != null){
				treatment.setInput("friction_matrix", myFrictionMatrix);
			}
			
			if(myShape == WindowShapeType.FUNCTIONAL){
				for(String matrix : inputMatrix){
					String name = new File(matrix).getName().replace(".asc", "");
					
					treatment.setInput("matrix", Util.getMatrix(matrix));
					
					for(int wsize : myWindowSizes){
			
						//publish("treatment of the matrix "+name+" using window size = "+wsize);
						//System.out.println("treatment of the matrix "+name+" using window size = "+wsize);
						//treatment.addView(this);
						//progress = 0;
						
						List<Integer> ws = new ArrayList<Integer>();
						ws.add(wsize);
						treatment.setInput("window_sizes", ws);
						if(myExportCsv){
							treatment.setInput("csv", outputFolder+"/"+name+"_"+WindowShapeType.getAbreviation(myShape)+"_w"+wsize+".csv");
						}
						if(myExportAscii){
							treatment.setInput("ascii", outputFolder+"/"+name+"_"+WindowShapeType.getAbreviation(myShape)+"_w"+wsize+"_");
						}
						
						// treatment
						try{
							treatment.allRun();
							
							treatment.clearViews();
							treatment.clearOutputs();
						} catch(TreatmentException e){
							e.printStackTrace();
						}
					}
				}
			}
			else{
				for(String matrix : inputMatrix){
					String name = new File(matrix).getName().replace(".asc", "");
					
					treatment.setInput("matrix", Util.getMatrix(matrix));
					
					//publish("treatment of the matrix "+name+" using window size = "+windowSizes);
					//treatment.addView(this);
					//progress = 0;
					
					treatment.setInput("window_sizes", myWindowSizes);
					if(myExportCsv){
						treatment.setInput("csv", outputFolder+"/"+name+"_"+WindowShapeType.getAbreviation(myShape)+".csv");
					}
					if(myExportAscii){
						treatment.setInput("ascii", outputFolder+"/"+name+"_"+WindowShapeType.getAbreviation(myShape)+"_");
					}
					
					// treatment
					try{
						treatment.allRun();
						
						treatment.clearViews();
						treatment.clearOutputs();
					} catch(TreatmentException e){
						e.printStackTrace();
					}
				}
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
}
