package fr.inra.sad.bagap.chloe.ecopaysage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.inra.sad.bagap.apiland.analysis.matrix.output.AsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.Pixel2PixelMatrixCalculation;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value.ShannonDiversityIndex;
import fr.inra.sad.bagap.apiland.analysis.matrix.util.AsciiGridManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysisBuilder;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.CircleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.CenteredWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class ScriptEcopaysage {

	private static String path = "F:/Ecopaysage/ecopaysages/robustesse_spatiale/";
	
	private static String method = "kmeans";
	//private static String method = "clara";
	
	//private static String shape = "threshold";
	private static String shape = "weighted";
	
	//private static String index = "shdi";
	private static String index = "boundary";
	
	
	public static void main(String[] args){
		
		List<String> metrics = new ArrayList<String>();
		metrics.add("compo");
		metrics.add("compo_couples");
		metrics.add("compo_het");
		metrics.add("couples");
		metrics.add("couples_het");
		metrics.add("all_metric");
		
		List<String> ks = new ArrayList<String>();
		ks.add("k2");
		ks.add("k3");
		ks.add("k4");
		ks.add("k5");
		ks.add("k6");
		ks.add("k7");
		ks.add("k8");
		ks.add("k9");
		
		List<String> scales = new ArrayList<String>();
		scales.add("500m");
		scales.add("1km");
		scales.add("2km");
		scales.add("3km");
		scales.add("4km");
		
		all();
		sortedByScales(scales);
		sortedByKs(ks);
		sortedByMetrics(metrics);
		
		fixKAndMetric(ks, metrics);
		fixScaleAndMetric(scales, metrics);
		fixKAndScale(ks, scales);
		
	}
	
	private static void all(){
		List<Matrix> lMatrix = new ArrayList<Matrix>();
		File folder = new File(path+method+"_"+shape+"/"+index+"/");
		new File(path+method+"_"+shape+"/analyse_"+index+"/all/").mkdirs();
		try {
			for(String file : folder.list()){
				if(file.endsWith(".asc")){
					Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+method+"_"+shape+"/"+index+"/"+file, false);
					lMatrix.add(m);
				}
			}
		
			Matrix[] matrix = lMatrix.toArray(new Matrix[lMatrix.size()]);
			
			Pixel2PixelMatrixCalculation ppmc = new Pixel2PixelMatrixCalculation(matrix){
				@Override
				protected double treatPixel(Pixel p) {
					double v = 0.0;
					for(Matrix m : wholeMatrix()){
						double lv = m.get(p);
						if(lv != Raster.getNoDataValue()){
							v += lv;
						}
					}
					return v;
				}
			};
			
			MatrixManager.exportAsciiGridAndVisualize(ppmc.allRun(), path+method+"_"+shape+"/analyse_"+index+"/all/"+method+"_"+shape+"_all.asc");
		
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void sortedByScales(List<String> scales){
		new File(path+method+"_"+shape+"/analyse_"+index+"/sorted_by_scale/").mkdirs();
		for(String scale : scales){
			sortedByScale(scale);
		}
	}
	
	private static void sortedByScale(String scale){
		List<Matrix> lMatrix = new ArrayList<Matrix>();
		File folder = new File(path+method+"_"+shape+"/"+index+"/");
		try {
			for(String file : folder.list()){
				if(file.endsWith(".asc") && file.contains(scale)){
					Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+method+"_"+shape+"/"+index+"/"+file, false);
					lMatrix.add(m);
				}
			}
		
			Matrix[] matrix = lMatrix.toArray(new Matrix[lMatrix.size()]);
			
			Pixel2PixelMatrixCalculation ppmc = new Pixel2PixelMatrixCalculation(matrix){
				@Override
				protected double treatPixel(Pixel p) {
					double v = 0.0;
					for(Matrix m : wholeMatrix()){
						double lv = m.get(p);
						if(lv != Raster.getNoDataValue()){
							v += lv;
						}
					}
					return v;
				}
			};
			
			MatrixManager.exportAsciiGridAndVisualize(ppmc.allRun(), path+method+"_"+shape+"/analyse_"+index+"/sorted_by_scale/"+method+"_"+shape+"_"+scale+".asc");
		
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void sortedByKs(List<String> ks){
		new File(path+method+"_"+shape+"/analyse_"+index+"/sorted_by_k/").mkdirs();
		for(String k : ks){
			sortedByK(k);
		}
	}
	
	private static void sortedByK(String k){
		List<Matrix> lMatrix = new ArrayList<Matrix>();
		File folder = new File(path+method+"_"+shape+"/"+index+"/");
		try {
			for(String file : folder.list()){
				if(file.endsWith(".asc") && file.contains(k)){
					Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+method+"_"+shape+"/"+index+"/"+file, false);
					lMatrix.add(m);
				}
			}
		
			Matrix[] matrix = lMatrix.toArray(new Matrix[lMatrix.size()]);
			
			Pixel2PixelMatrixCalculation ppmc = new Pixel2PixelMatrixCalculation(matrix){
				@Override
				protected double treatPixel(Pixel p) {
					double v = 0.0;
					for(Matrix m : wholeMatrix()){
						double lv = m.get(p);
						if(lv != Raster.getNoDataValue()){
							v += lv;
						}
					}
					return v;
				}
			};
			
			MatrixManager.exportAsciiGridAndVisualize(ppmc.allRun(), path+method+"_"+shape+"/analyse_"+index+"/sorted_by_k/"+method+"_"+shape+"_"+k+".asc");
		
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void sortedByMetrics(List<String> metrics){
		new File(path+method+"_"+shape+"/analyse_"+index+"/sorted_by_metric/").mkdirs();
		for(String metric : metrics){
			sortedByMetric(metric);
		}
	}
	
	private static void sortedByMetric(String metric){
		
		String bMetric = "";
		switch(metric){
		case "all_metric" : bMetric = "TRUE_TRUE_TRUE"; break;
		case "compo" : bMetric = "TRUE_FALSE_FALSE"; break;
		case "compo_het" : bMetric = "TRUE_FALSE_TRUE"; break;
		case "couples" : bMetric = "FALSE_TRUE_FALSE"; break;
		case "compo_couples" : bMetric = "TRUE_TRUE_FALSE"; break;
		case "couples_het" : bMetric = "FALSE_TRUE_TRUE"; break;
		}
		
		List<Matrix> lMatrix = new ArrayList<Matrix>();
		File folder = new File(path+method+"_"+shape+"/"+index+"/");
		try {
			for(String file : folder.list()){
				if(file.endsWith(".asc") && file.contains(bMetric)){
					Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+method+"_"+shape+"/"+index+"/"+file, false);
					lMatrix.add(m);
				}
			}
		
			Matrix[] matrix = lMatrix.toArray(new Matrix[lMatrix.size()]);
			
			Pixel2PixelMatrixCalculation ppmc = new Pixel2PixelMatrixCalculation(matrix){
				@Override
				protected double treatPixel(Pixel p) {
					double v = 0.0;
					for(Matrix m : wholeMatrix()){
						double lv = m.get(p);
						if(lv != Raster.getNoDataValue()){
							v += lv;
						}
					}
					return v;
				}
			};
			
			MatrixManager.exportAsciiGridAndVisualize(ppmc.allRun(), path+method+"_"+shape+"/analyse_"+index+"/sorted_by_metric/"+method+"_"+shape+"_"+metric+".asc");
		
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void fixKAndScale(List<String> ks, List<String> scales){
		new File(path+method+"_"+shape+"/analyse_"+index+"/fix_k_scale/").mkdirs();
		for(String k : ks){
			for(String scale : scales){
				fixSingleKAndScale(k, scale);
			}
		}
	}
	
	private static void fixSingleKAndScale(String k, String scale){
		
		List<Matrix> lMatrix = new ArrayList<Matrix>();
		File folder = new File(path+method+"_"+shape+"/"+index+"/");
		try {
			for(String file : folder.list()){
				if(file.endsWith(".asc") && file.contains(k) && file.contains(scale)){
					Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+method+"_"+shape+"/"+index+"/"+file, false);
					lMatrix.add(m);
				}
			}
		
			Matrix[] matrix = lMatrix.toArray(new Matrix[lMatrix.size()]);
			
			Pixel2PixelMatrixCalculation ppmc = new Pixel2PixelMatrixCalculation(matrix){
				@Override
				protected double treatPixel(Pixel p) {
					double v = 0.0;
					for(Matrix m : wholeMatrix()){
						double lv = m.get(p);
						if(lv != Raster.getNoDataValue()){
							v += lv;
						}
					}
					return v;
				}
			};
			
			MatrixManager.exportAsciiGridAndVisualize(ppmc.allRun(), path+method+"_"+shape+"/analyse_"+index+"/fix_k_scale/"+method+"_"+shape+"_fix_"+k+"_"+scale+".asc");
		
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void fixScaleAndMetric(List<String> scales, List<String> metrics){
		new File(path+method+"_"+shape+"/analyse_"+index+"/fix_scale_metric/").mkdirs();
		for(String scale : scales){
			for(String metric : metrics){
				fixSingleScaleAndMetric(scale, metric);
			}
		}
	}
	
	private static void fixSingleScaleAndMetric(String scale, String metric){
		
		String bMetric = "";
		switch(metric){
		case "all_metric" : bMetric = "TRUE_TRUE_TRUE"; break;
		case "compo" : bMetric = "TRUE_FALSE_FALSE"; break;
		case "compo_het" : bMetric = "TRUE_FALSE_TRUE"; break;
		case "couples" : bMetric = "FALSE_TRUE_FALSE"; break;
		case "compo_couples" : bMetric = "TRUE_TRUE_FALSE"; break;
		case "couples_het" : bMetric = "FALSE_TRUE_TRUE"; break;
		}
		
		List<Matrix> lMatrix = new ArrayList<Matrix>();
		File folder = new File(path+method+"_"+shape+"/"+index+"/");
		try {
			for(String file : folder.list()){
				if(file.endsWith(".asc") && file.contains(bMetric) && file.contains(scale)){
					Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+method+"_"+shape+"/"+index+"/"+file, false);
					lMatrix.add(m);
				}
			}
		
			Matrix[] matrix = lMatrix.toArray(new Matrix[lMatrix.size()]);
			
			Pixel2PixelMatrixCalculation ppmc = new Pixel2PixelMatrixCalculation(matrix){
				@Override
				protected double treatPixel(Pixel p) {
					double v = 0.0;
					for(Matrix m : wholeMatrix()){
						double lv = m.get(p);
						if(lv != Raster.getNoDataValue()){
							v += lv;
						}
					}
					return v;
				}
			};
			
			MatrixManager.exportAsciiGridAndVisualize(ppmc.allRun(), path+method+"_"+shape+"/analyse_"+index+"/fix_scale_metric/"+method+"_"+shape+"_fix_"+scale+"_"+metric+".asc");
		
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void fixKAndMetric(List<String> ks, List<String> metrics){
		new File(path+method+"_"+shape+"/analyse_"+index+"/fix_k_metric/").mkdirs();
		for(String k : ks){
			for(String metric : metrics){
				fixSingleKAndMetric(k, metric);
			}
		}
	}
	
	private static void fixSingleKAndMetric(String k, String metric){
		
		String bMetric = "";
		switch(metric){
		case "all_metric" : bMetric = "TRUE_TRUE_TRUE"; break;
		case "compo" : bMetric = "TRUE_FALSE_FALSE"; break;
		case "compo_het" : bMetric = "TRUE_FALSE_TRUE"; break;
		case "couples" : bMetric = "FALSE_TRUE_FALSE"; break;
		case "compo_couples" : bMetric = "TRUE_TRUE_FALSE"; break;
		case "couples_het" : bMetric = "FALSE_TRUE_TRUE"; break;
		}
		
		List<Matrix> lMatrix = new ArrayList<Matrix>();
		File folder = new File(path+method+"_"+shape+"/"+index+"/");
		try {
			for(String file : folder.list()){
				if(file.endsWith(".asc") && file.contains(bMetric) && file.contains(k)){
					Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+method+"_"+shape+"/"+index+"/"+file, false);
					lMatrix.add(m);
				}
			}
		
			Matrix[] matrix = lMatrix.toArray(new Matrix[lMatrix.size()]);
			
			Pixel2PixelMatrixCalculation ppmc = new Pixel2PixelMatrixCalculation(matrix){
				@Override
				protected double treatPixel(Pixel p) {
					double v = 0.0;
					for(Matrix m : wholeMatrix()){
						double lv = m.get(p);
						if(lv != Raster.getNoDataValue()){
							v += lv;
						}
					}
					return v;
				}
			};
			
			MatrixManager.exportAsciiGridAndVisualize(ppmc.allRun(), path+method+"_"+shape+"/analyse_"+index+"/fix_k_metric/"+method+"_"+shape+"_fix_"+k+"_"+metric+".asc");
		
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
}
