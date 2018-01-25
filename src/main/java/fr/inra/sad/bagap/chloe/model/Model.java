package fr.inra.sad.bagap.chloe.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileHeader;
import org.geotools.data.shapefile.shp.ShapefileReader;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisObserver;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.ChamferDistance;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringCsvOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringEuclidianDistanceAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringFunctionalDistanceAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringQueenAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringRookAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.Classification;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.Pixel2PixelMatrixCalculation;
import fr.inra.sad.bagap.apiland.analysis.matrix.util.AsciiGridManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.util.ExportAsciiGridFromShapefileAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.util.SpatialCsvManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShapeType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RasterManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;
import fr.inra.sad.bagap.apiland.domain.Domain;
import fr.inra.sad.bagap.apiland.treatment.Treatment;
import fr.inra.sad.bagap.apiland.treatment.TreatmentException;
import fr.inra.sad.bagap.apiland.treatment.TreatmentObserver;
import fr.inra.sad.bagap.apiland.treatment.TreatmentState;
import fr.inra.sad.bagap.apiland.treatment.window.GridWindowMatrixTreatment;
import fr.inra.sad.bagap.apiland.treatment.window.MapWindowMatrixTreatment;
import fr.inra.sad.bagap.apiland.treatment.window.SelectedWindowMatrixTreatment;
import fr.inra.sad.bagap.apiland.treatment.window.SlidingWindowMatrixTreatment;

public class Model implements TreatmentObserver, AnalysisObserver {
	
	public enum ACTION {PROGRESS, PUBLISH};
	private int progress, globalProgress;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private boolean batch = false;

	public void setProgress(int progress) {
		if(batch){
			System.out.println("## "+progress+" / 100");
		}else{
			pcs.firePropertyChange(new PropertyChangeEvent(this, ACTION.PROGRESS.toString(), null, progress));
		}
	}

	public void publish(String text) {
		if(batch){
			System.out.println("## "+text);
		}else{
			pcs.firePropertyChange(new PropertyChangeEvent(this, ACTION.PUBLISH.toString(), "", text));
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	@Override
	public void notify(Analysis ma, AnalysisState state) {
		// do nothing
	}
	
	@Override
	public void notify(Treatment t, TreatmentState s) {
		// do nothing
	}

	@Override
	public void updateProgression(Analysis ma, int total) {
		updateProgression(total);
	}
	
	@Override
	public void updateProgression(Treatment t, int total) {
		updateProgression(total);
	}
	
	public void updateProgression(int total) {
		progress++;
		int gprogress = new Double((((double) progress) / total) * 100.0).intValue();
		if(globalProgress != gprogress){
			globalProgress = gprogress;
			setProgress(globalProgress);
		}
	}
	
	public boolean runSlidingWindow(boolean batch, Set<Matrix> inputMatrix, WindowShapeType shape, Friction friction, Matrix frictionMatrix,
			List<Integer> windowSizes, int delta, boolean interpolate, double minRate, Set<String> metrics,
			String outputFolder, String outputAsc, String outputCsv, boolean viewAsciiOutput, boolean exportCsv, boolean exportAscii,
			Set<Integer> filters, Set<Integer> unfilters) {
		
		this.batch = batch;
		
		Treatment treatment = new SlidingWindowMatrixTreatment();
		
		try{
			// settings
			treatment.setInput("shape", shape);
			treatment.setInput("delta", delta);
			treatment.setInput("interpolation", interpolate);
			treatment.setInput("min_rate", minRate);
			treatment.setInput("metrics", metrics);
			treatment.setInput("filters", filters);
			treatment.setInput("unfilters", unfilters);
			if(friction != null){
				treatment.setInput("friction_map", friction);
				//TranslateMatrixCalculation c = new TranslateMatrixCalculation(friction.getMap(), inputMatrix.iterator().next());
				//c.allRun();
				//MatrixManager.exportAsciiGridAndVisualize(c.getResult(), "/home/sad20/temp/chloe/friction/test.asc");
			}
			if(frictionMatrix != null){
				treatment.setInput("friction_matrix", frictionMatrix);
			}
			
			if(shape == WindowShapeType.FUNCTIONAL){
				for(Matrix matrix : inputMatrix){
					String name = new File(matrix.getFile()).getName().replace(".asc", "");
					
					treatment.setInput("matrix", matrix);
					
					for(int wsize : windowSizes){
			
						publish("treatment of the matrix "+name+" using window size = "+wsize);
						//System.out.println("treatment of the matrix "+name+" using window size = "+wsize);
						treatment.addObserver(this);
						progress = 0;
						
						List<Integer> ws = new ArrayList<Integer>();
						ws.add(wsize);
						treatment.setInput("window_sizes", ws);
						if(exportCsv){
							if(outputCsv != null){
								treatment.setInput("csv", outputCsv);
							}else{
								treatment.setInput("csv", outputFolder+"/"+name+"_"+WindowShapeType.getAbreviation(shape)+"_w"+wsize+".csv");
							}
						}
						if(exportAscii){
							if(outputAsc != null){
								treatment.setInput("ascii", outputAsc);
							}else{
								treatment.setInput("ascii", outputFolder+"/"+name+"_"+WindowShapeType.getAbreviation(shape)+"_w"+wsize+"_");
							}
						}
						
						// treatment
						try{
							treatment.allRun();
							
							treatment.clearObservers();
							treatment.clearOutputs();
						} catch(TreatmentException e){
							e.printStackTrace();
						}
					}
				}
			}
			else{
				for(Matrix matrix : inputMatrix){
					String name = new File(matrix.getFile()).getName().replace(".asc", "");
					
					treatment.setInput("matrix", matrix);
					
					publish("treatment of the matrix "+name+" using window size = "+windowSizes);
					treatment.addObserver(this);
					progress = 0;
					
					treatment.setInput("window_sizes", windowSizes);
					if(exportCsv){
						if(outputCsv != null){
							treatment.setInput("csv", outputCsv);
						}else{
							treatment.setInput("csv", outputFolder+"/"+name+"_"+WindowShapeType.getAbreviation(shape)+".csv");
						}
					}
					if(exportAscii){
						if(outputAsc != null){
							treatment.setInput("ascii", outputAsc);
						}else{
							treatment.setInput("ascii", outputFolder+"/"+name+"_"+WindowShapeType.getAbreviation(shape)+"_");
						}
					}
					
					// treatment
					try{
						treatment.allRun();
						
						treatment.clearObservers();
						treatment.clearOutputs();
					} catch(TreatmentException e){
						e.printStackTrace();
					}
				}
			}
			
			// visualization
			if(viewAsciiOutput){
				if(outputAsc != null){
					MatrixManager.visualize(outputAsc);
				}else{
					MatrixManager.visualize(outputFolder);	
				}
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

	public boolean runSelectedWindow(boolean batch, Set<Matrix> inputMatrix, double minRate, WindowShapeType shape, Friction friction, Matrix frictionMatrix,
			List<Integer> windowSizes, Set<Pixel> pixels, Set<String> metrics,
			String outputFolder, String outputAsc, String outputCsv, boolean viewAsciiOutput, boolean exportCsv, boolean exportAscii) {
		
		this.batch = batch;
		
		Treatment treatment = new SelectedWindowMatrixTreatment();
		
		try{
			
			if(outputFolder != null){
				treatment.setInput("path", outputFolder);	
			}else{
				treatment.setInput("path", new File(outputAsc).getParent());
			}
			
			// settings
			treatment.setInput("shape", shape);
			treatment.setInput("pixels", pixels);
			treatment.setInput("metrics", metrics);
			treatment.setInput("min_rate", minRate);
			if(friction != null){
				treatment.setInput("friction_map", friction);
			}
			if(frictionMatrix != null){
				treatment.setInput("friction_matrix", frictionMatrix);
			}
			
			if(shape == WindowShapeType.FUNCTIONAL){
				for(Matrix matrix : inputMatrix){
					String name = new File(matrix.getFile()).getName().replace(".asc", "");
					
					treatment.setInput("matrix", matrix);
					
					for(int wsize : windowSizes){
						
						publish("treatment of the matrix "+name+" using window size = "+wsize);
						treatment.addObserver(this);
						progress = 0;
						
						List<Integer> ws = new ArrayList<Integer>();
						ws.add(wsize);
						treatment.setInput("window_sizes", ws);
						if(exportCsv){
							if(outputCsv != null){
								treatment.setInput("csv", outputCsv);
							}else{
								treatment.setInput("csv", outputFolder+"/"+name+"_w"+wsize+".csv");
							}
						}
						if(exportAscii){
							if(outputAsc != null){
								treatment.setInput("ascii", outputAsc);
							}else{
								treatment.setInput("ascii", outputFolder+"/"+name+"_w"+wsize+"_");
							}
						}
							
						// treatment
						
						try{
							treatment.allRun();
							treatment.clearObservers();
							treatment.clearOutputs();
						} catch(TreatmentException e){
							e.printStackTrace();
						}
					}
				}
			}else{
				for(Matrix matrix : inputMatrix){
					String name = new File(matrix.getFile()).getName().replace(".asc", "");
					
					treatment.setInput("matrix", matrix);
					
					publish("treatment of the matrix "+name+" using window size = "+windowSizes);
					treatment.addObserver(this);
					progress = 0;
						
					treatment.setInput("window_sizes", windowSizes);
					if(exportCsv){
						if(outputCsv != null){
							treatment.setInput("csv", outputCsv);
						}else{
							treatment.setInput("csv", outputFolder+"/"+name+"_"+WindowShapeType.getAbreviation(shape)+".csv");
						}
					}
					if(exportAscii){
						if(outputAsc != null){
							treatment.setInput("ascii", outputAsc);
						}else{
							treatment.setInput("ascii", outputFolder+"/"+name+"_"+WindowShapeType.getAbreviation(shape)+"_");
						}
					}
							
					// treatment
						
					try{
						treatment.allRun();
						treatment.clearObservers();
						treatment.clearOutputs();
					} catch(TreatmentException e){
						e.printStackTrace();
					}
				}
			}
			
			
			// visualization
			if(viewAsciiOutput){
				if(outputAsc != null){
					MatrixManager.visualize(outputAsc);
				}else{
					MatrixManager.visualize(outputFolder);
				}
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean runMapWindow(boolean batch, Set<Matrix> inputMatrix, Set<String> metrics, String csvOutput) {
		
		this.batch = batch;
		
		Treatment treatment = new MapWindowMatrixTreatment();
		
		try{
			
			// settings
			treatment.setInput("metrics", metrics);
			
			try {
				CsvWriter cw = new CsvWriter(csvOutput);
				cw.setDelimiter(';');
				
				cw.write("name");
				for(String m : metrics){
					cw.write(m);
				}
				
				cw.endRecord();
				
				for(Matrix matrix : inputMatrix){
					publish("treatment of the matrix "+matrix);
					treatment.addObserver(this);
					progress = 0;
					
					treatment.setInput("matrix", matrix);
					
					treatment.setInput("csv", cw);
					
					// no view
					
					// treatment
					try{
						treatment.allRun();
						
						treatment.clearObservers();
						treatment.clearOutputs();
					} catch(TreatmentException e){
						e.printStackTrace();
					}
				}
				
				cw.close();
			} catch (FinalizedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 	
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean runGridWindow(boolean batch, Set<Matrix> inputMatrix, List<Integer> gridSizes, double minRate, Set<String> metrics, String outputFolder, String outputAsc, String outputCsv, 
			boolean viewAsciiOutput, boolean exportCsv, boolean exportAscii) {
		
		this.batch = batch;
		
		Treatment treatment = new GridWindowMatrixTreatment();
		
		try{
			
			// settings
			treatment.setInput("metrics", metrics);
			treatment.setInput("min_rate", minRate);
			
			for(Matrix matrix : inputMatrix){
				String name = new File(matrix.getFile()).getName().replace(".asc", "");
				
				treatment.setInput("matrix", matrix);
				
				for(int gsize : gridSizes){
					
					publish("treatment of the matrix "+name+" using grid size = "+gsize);
					treatment.addObserver(this);
					progress = 0;
					
					treatment.setInput("grid_size", gsize);
					if(exportCsv){
						if(outputCsv != null){
							treatment.setInput("csv", outputCsv);
						}else{
							treatment.setInput("csv", outputFolder+"/"+name+"_g"+gsize+".csv");
						}
					}
					if(exportAscii){
						if(outputAsc != null){
							treatment.setInput("ascii", outputAsc);
						}else{
							treatment.setInput("ascii", outputFolder+"/"+name+"_g"+gsize+"_");	
						}
					}
					
					// treatment
					try{
						treatment.allRun();
						
						treatment.clearObservers();
						treatment.clearOutputs();
					} catch(TreatmentException e){
						e.printStackTrace();
					}
				}
			}
			
			// visualization
			if(viewAsciiOutput){
				if(outputAsc != null){
					MatrixManager.visualize(outputAsc);
				}else{
					MatrixManager.visualize(outputFolder);	
				}
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean importAsciiGrid(Collection<Matrix> matrix, String ascii) {
		try{
			publish("import matrix "+ascii);
			
			//Matrix m = JaiMatrixFactory.get().createWithAsciiGrid(ascii);
			Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(ascii, true);
			
			matrix.add(m);
			
			return true;
		} catch(NumberFormatException ex){
			ex.printStackTrace();
			return false;
		} catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public Matrix importAsciiGrid(Matrix matrix, String ascii) {
		try{
			publish("import matrix "+ascii);
			
			//Matrix m = JaiMatrixFactory.get().createWithAsciiGrid(ascii);
			Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(ascii, true);
			matrix = m;
			
			return matrix;
		} catch(NumberFormatException ex){
			ex.printStackTrace();
			return null;
		} catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	public boolean getAttributesAndEnvelopeFromShapefile(String shape, Map<String, String> attributes, double[] envelope) {
		try {
			publish("import shapefile "+shape);
			ShpFiles sf = new ShpFiles(shape);
			
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new com.vividsolutions.jts.geom.GeometryFactory());
			ShapefileHeader sfh = sfr.getHeader();
			envelope[0] = sfh.minX();
			envelope[1] = sfh.maxX();
			envelope[2] = sfh.minY();
			envelope[3] = sfh.maxY();
			sfr.close();	
			
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			DbaseFileHeader dfh = dfr.getHeader();
			for (int i=0; i<dfh.getNumFields(); i++) {
				//attributes.add(dfh.getFieldName(i).toLowerCase());
				switch(dfh.getFieldType(i)){
				case 'C' : attributes.put(dfh.getFieldName(i).toLowerCase(), "String"); break;
				case 'N' : attributes.put(dfh.getFieldName(i).toLowerCase(), "Number"); break;
				case 'L' : attributes.put(dfh.getFieldName(i).toLowerCase(), "Boolean"); break;
				case 'D' : attributes.put(dfh.getFieldName(i).toLowerCase(), "Date"); break;
				}
			}
			dfr.close();
			return true;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (ShapefileException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean dispatch(Set<Pixel> pixels, Matrix m, Integer n,
			int distance, Set<Integer> with, Set<Integer> without) {
		try{
			publish("dispatch de "+n+" pixels");
			CoordinateManager.dispatch(pixels, m, n, distance, with, without);
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean dispatch(Set<Pixel> pixels, Set<Matrix> matrix, Integer n,
			int distance, Set<Integer> with, Set<Integer> without) {
		try{
			publish("dispatch de "+n+" pixels");
			CoordinateManager.dispatch(pixels, matrix, n, distance, with, without);
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

	public boolean exportAsciiGridFromCsv(boolean batch, String inputCsv, String folder, String outputAsc,
			Set<String> variables, int ncols, int nrows, double xllcorner,
			double yllcorner, double cellsize, int nodatavalue, boolean viewAscii) {
		this.batch = batch;
		try{
			publish("export ascii grid from csv file(s)");
			File f = new File(inputCsv);
			if(f.isDirectory()){
				for(String c : new File(inputCsv).list()){
					if(c.endsWith(".csv")){
						SpatialCsvManager.exportAsciiGrid(f+"/"+c, folder, outputAsc, variables, ncols, nrows, xllcorner, yllcorner, 1, cellsize, nodatavalue);
					}
				}
			}else{
				SpatialCsvManager.exportAsciiGrid(inputCsv, folder, outputAsc, variables, ncols, nrows, xllcorner, yllcorner, 1, cellsize, nodatavalue);
			}
			if(viewAscii){
				if(outputAsc != null){
					MatrixManager.visualize(outputAsc);
				}else{
					MatrixManager.visualize(folder);	
				}
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean sortCsv(String inputCsv) {
		try{
			publish("sort csv file(s)");
			
			File f = new File(inputCsv);
			if(f.isDirectory()){
				for(String c : new File(inputCsv).list()){
					if(c.endsWith(".csv")){
						SpatialCsvManager.sort(f+"/"+c);
					}
				}
			}else{
				SpatialCsvManager.sort(inputCsv);
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	private String formatDoubleToString(double d){
		int i = new Double(d).intValue();
		String s = new Integer(i).toString();
		while(s.length() < 4){
			s = "0"+s;
		}
		return s;	
	}
	
	public boolean runSearchAndReplace(boolean batch, Set<String> asciis, int noData, Map<Integer, Number> changes, String outputFolder, String outputAsc, boolean viewAscii) {
		this.batch = batch;
		try{
			String out;
			for(String ascii : asciis){
				publish("search and replace : "+ascii);
				if(outputAsc != null){
					out = outputAsc;
				}else{
					out = outputFolder+"/"+new File(ascii).getName();	
				}
				AsciiGridManager.searchAndReplace(ascii, out, noData, changes);
			}	
			
			if(viewAscii){
				if(outputAsc != null){
					MatrixManager.visualize(outputAsc);
				}else{
					MatrixManager.visualize(outputFolder);	
				}
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

	public boolean cleanAsciiGrid(String ascii) {
		try{
			publish("clean ascii grid file : "+ascii);
			
			AsciiGridManager.clean(ascii, ascii);
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

	public boolean exportAsciiGridFromShapefile(boolean batch, Set<String> layers, String attribute, String lookupTable,
			Set<Double> cellsizes, String outputFolder, String outputAsc, boolean viewAscii,
			Double minx, Double maxx, Double miny, Double maxy){
		this.batch = batch;
		try{
			publish("export ascii grid from shapefile");
			String name;
			String ascii;
			for(String layer : layers){
				
				name = new File(layer).getName().replace(".shp", ""); 
				for(double cellsize : cellsizes){
					if(outputAsc != null){
						publish("export "+outputAsc+" with cellsize "+cellsize);
						ascii = outputAsc;
					}else{
						publish("export "+name+" with cellsize "+cellsize);
						ascii = outputFolder+"/"+name+"_"+formatDoubleToString(cellsize)+".asc";
					}
					
					Analysis a;
					
					Map<String, String> map = null;
					if(!lookupTable.equalsIgnoreCase("")){
						map = new HashMap<String, String>();
						CsvReader cr = new CsvReader(lookupTable);
						cr.setDelimiter(';');
						cr.readHeaders();
						while(cr.readRecord()){
							map.put(cr.get(0).replace(" ", ""), cr.get(1).replaceAll(" ", ""));
						}
						cr.close();
					}
					
					if(minx == null){
						a = new ExportAsciiGridFromShapefileAnalysis(ascii, layer, attribute, cellsize, map);
					}else{
						a = new ExportAsciiGridFromShapefileAnalysis(ascii, layer, attribute, cellsize, minx, maxx, miny, maxy, map);
					}
					a.addObserver(this);
					progress = 0;
					a.allRun();
				}
			}
			
			if(viewAscii){
				if(outputAsc != null){
					MatrixManager.visualize(outputAsc);
				}else{
					MatrixManager.visualize(outputFolder);	
				}
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean runDistance(boolean batch, Set<Matrix> matrix, Set<Integer> values, String asciiOutput, String outputAsc, boolean viewAsciiOutput) {
		this.batch = batch;
		try{
			for(Matrix m : matrix){	
				String name = new File(m.getFile()).getName().replace(".asc", "");
				
				publish("distance calculation: "+m.getFile());
				progress = 0;
				
				ChamferDistance cd = new ChamferDistance(m, values);
				cd.addObserver(this);
				Matrix m2 = cd.allRun();
				
				publish("normalize the distance matrix");
				progress = 0;
				Pixel2PixelMatrixCalculation pp = new Pixel2PixelMatrixCalculation(m, m2){
					@Override
					protected double treatPixel(Pixel p) {
						if(m.get(p) != Raster.getNoDataValue()){
							return m2.get(p);
						}
						return Raster.getNoDataValue();
					}
				};
				pp.addObserver(this);
				Matrix m3 = pp.allRun();
				
				if(outputAsc != null){
					MatrixManager.exportAsciiGrid(m3, outputAsc);
				}else{
					MatrixManager.exportAsciiGrid(m3, asciiOutput+"/"+name+"_dist-"+values+".asc");
				}
				
				
			}	
			
			if(viewAsciiOutput){
				if(outputAsc != null){
					MatrixManager.visualize(outputAsc);
				}else{
					MatrixManager.visualize(asciiOutput);	
				}
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean runFilter(boolean batch, Set<Matrix> matrix, Matrix filterMatrix, Set<Integer> values, String asciiOutput, String outputAsc, boolean viewAsciiOutput) {
		this.batch = batch;
		try{
			for(Matrix m : matrix){	
				String name = new File(m.getFile()).getName().replace(".asc", "");
				
				publish("filter calculation: "+m.getFile());
				progress = 0;
				Pixel2PixelMatrixCalculation pp = new Pixel2PixelMatrixCalculation(m, filterMatrix){
					@Override
					protected double treatPixel(Pixel p) {
						double f = matrix(1).get(p);
						for(int v : values){
							if(v == f){
								return matrix(0).get(p);
							}
						}
						return 0;
					}
				};
				pp.addObserver(this);
				Matrix m2 = pp.allRun();
				
				if(outputAsc != null){
					MatrixManager.exportAsciiGrid(m2, outputAsc);
				}else{
					MatrixManager.exportAsciiGrid(m2, asciiOutput+"/"+name+"_filter-"+values+".asc");
				}
			}	
			
			if(viewAsciiOutput){
				if(outputAsc != null){
					MatrixManager.visualize(outputAsc);
				}else{
					MatrixManager.visualize(asciiOutput);	
				}
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

	public boolean runClassification(boolean batch, Set<Matrix> matrix, Map<Domain<Double, Double>, Integer> domains, String asciiOutput, String outputAsc, boolean viewAsciiOutput) {
		this.batch = batch;
		try{
			for(Matrix m : matrix){
				progress = 0;
				String name = new File(m.getFile()).getName().replace(".asc", "");
				publish("classification : "+m.getFile());
				Classification c = new Classification(m, domains);
				c.addObserver(this);
				Matrix m2 = c.allRun();
				
				if(outputAsc !=  null){
					MatrixManager.exportAsciiGrid(m2, outputAsc);
				}else{				
					MatrixManager.exportAsciiGrid(m2, asciiOutput+"/"+name+"_class.asc");
				}
			}	
			
			if(viewAsciiOutput){
				if(outputAsc != null){
					MatrixManager.visualize(outputAsc);
				}else{
					MatrixManager.visualize(asciiOutput);	
				}
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean runOverlay(boolean batch, List<Matrix> matrix, String asciiOutput, String outputAsc, boolean viewAsciiOutput){
		this.batch = batch;
		try {
			progress = 0;
			StringBuffer n = new StringBuffer();
			for(Matrix m : matrix){
				n.append(new File(m.getFile()).getName().replace(".asc", "")+"+");
			}
			//String name = "add_"+n.substring(0, n.length()-1);
			String name = "overlay";
			publish("overlay : "+n.toString());
			Matrix[] m = matrix.toArray(new Matrix[matrix.size()]);
			Pixel2PixelMatrixCalculation ppm = new Pixel2PixelMatrixCalculation(m){
				@Override
				protected double treatPixel(Pixel p) {
					//System.out.println(p);
					boolean zero = false;
					for(Matrix m : wholeMatrix()){
						double v = m.get(p);
						if(v != 0 && v != Raster.getNoDataValue()){
							return v;
						}else if(v == 0){
							zero = true;
						}
					}
					if(zero){
						return 0;
					}
					return Raster.getNoDataValue();
				}
			};
			ppm.addObserver(this);
			Matrix mo = ppm.allRun();
			
			if(outputAsc != null){
				MatrixManager.exportAsciiGrid(mo, outputAsc);
				if(viewAsciiOutput){
					MatrixManager.visualize(outputAsc);
				}
			}else{
				MatrixManager.exportAsciiGrid(mo, asciiOutput+"/"+name+".asc");
				if(viewAsciiOutput){
					MatrixManager.visualize(asciiOutput+"/"+name+".asc");
				}
			}
			
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean runCombine(List<Matrix> matrix, List<Double> factors, String asciiOutput, boolean viewAsciiOutput){
		try {
			progress = 0;
			String name = "combine";
			publish("combine");
			Matrix[] m = matrix.toArray(new Matrix[matrix.size()]);
			Pixel2PixelMatrixCalculation ppm = new Pixel2PixelMatrixCalculation(m){
				@Override
				protected double treatPixel(Pixel p) {
					double value = 0.0;
					int index = 0;
					for(Matrix m : wholeMatrix()){
						double v = m.get(p);
						if(v == Raster.getNoDataValue()){
							return Raster.getNoDataValue();
						}
						value += v * factors.get(index);
						index++;
					}
					return value;
				}
			};
			ppm.addObserver(this);
			Matrix mo = ppm.allRun();
			MatrixManager.exportAsciiGrid(mo, asciiOutput+"/"+name+".asc");
			
			if(viewAsciiOutput){
				MatrixManager.visualize(asciiOutput+"/"+name+".asc");
			}
			
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean runCluster(boolean batch, Set<Matrix> matrix, Set<Integer> values, String typeCluster, double distance, Friction friction, Matrix frictionMatrix, 
			/*boolean sameType, boolean sameMap,*/ String asciiOutput, String outputAsc, boolean viewAsciiOutput){
		this.batch = batch;
		try {
			String name = "cluster";
			
			for(Matrix m : matrix){
				progress = 0;
				String suf = new File(m.getFile()).getName().replace(".asc", "");
				publish("cluster : "+suf);
				ClusteringAnalysis ca = null;
				
				switch(typeCluster){
				case "rook" : ca = new ClusteringRookAnalysis(m, values); break;
				case "queen" : ca = new ClusteringQueenAnalysis(m, values); break;
				case "euclidian" : ca = new ClusteringEuclidianDistanceAnalysis(m, distance, values); break;
				case "functional" : 
					if(friction != null){
						ca = new ClusteringFunctionalDistanceAnalysis(m, distance, values, friction); 
					}else{
						ca = new ClusteringFunctionalDistanceAnalysis(m, distance, values, frictionMatrix);
					}
					break;
				}
				if(outputAsc != null){
					ca.addObserver(new ClusteringCsvOutput(outputAsc.replace(".asc", "")+".csv"));
				}else{
					ca.addObserver(new ClusteringCsvOutput(asciiOutput+"/"+name+"_"+suf+".csv"));
				}
				//ca.addObserver(new ClusteringWithDistanceCsvOutput(asciiOutput+"/"+name+"_"+suf+".csv", values));
				ca.addObserver(this);
				
				Raster r = (Raster) ca.allRun();
				Matrix m2 = RasterManager.exportMatrix(r, m);
				Pixel2PixelMatrixCalculation ppt = new Pixel2PixelMatrixCalculation(m, m2){
					@Override
					protected double treatPixel(Pixel p) {
						double v = m2.get(p);
						if(v != Raster.getNoDataValue()){
							return v;
						}
						if(m.get(p) != Raster.getNoDataValue()){
							return 0;
						}
						return Raster.getNoDataValue();
					}
				};
				if(outputAsc != null){
					MatrixManager.exportAsciiGrid(ppt.allRun(), outputAsc);
				}else{
					MatrixManager.exportAsciiGrid(ppt.allRun(), asciiOutput+"/"+name+"_"+suf+".asc");
				}
			}
			
			if(viewAsciiOutput){
				if(outputAsc != null){
					MatrixManager.visualize(outputAsc);
				}else{
					MatrixManager.visualize(asciiOutput);	
				}
			}
			
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		} 
	}
	
	/*
	
	private String getCommonString(String s1, String s2){
		String c = "";
		int index = 0;
		while(s1.length()>index && s2.length()>index && s1.charAt(index)==s2.charAt(index)){
			c += s1.charAt(index++);
		}
		if(!c.equals(null)){
			return c;
		}else{
			return "temporal";
		}
	}
	
	
	 private String getName(Set<Matrix> matrix){
		Set<String> set = new HashSet<String>();
		for(Matrix m : matrix){
			set.add(new File(m.getFile()).getName().replace(".asc", ""));
		}
		String c = null;
		for(String s : set){
			if(c == null){
				c = s;
			}else{
				c = getCommonString(c, s);
			}
		}
		return c;
	}
	
	public boolean runMultiSpatialSlidingWindow(Set<Matrix> inputMatrix,
			String processType, WindowShapeType shape,
			Friction friction, Matrix frictionMatrix,
			List<Integer> windowSizes, int delta, boolean interpolate, Set<String> metrics,
			String outputFolder, boolean viewAsciiOutput, boolean exportCsv, boolean exportAscii,
			Set<Integer> filters, Set<Integer> unfilters) {
		
		Treatment treatment = new MultiSpatialSlidingWindowTreatment();
		
		try{
			// settings
			if(processType.equalsIgnoreCase("qualitative")){
				treatment.setInput("qualitative", true);
			}else{
				treatment.setInput("qualitative", false);
			}
			treatment.setInput("shape", shape);
			treatment.setInput("delta", delta);
			treatment.setInput("interpolation", interpolate);
			treatment.setInput("metrics", metrics);
			treatment.setInput("window_sizes", windowSizes);
			treatment.setInput("filters", filters);
			treatment.setInput("unfilters", unfilters);
			if(friction != null){
				treatment.setInput("friction_map", friction);
			}
			if(frictionMatrix != null){
				treatment.setInput("friction_matrix", frictionMatrix);
			}
			
			for(Matrix matrix : inputMatrix){
				
				publish("treatment of the matrix "+matrix);
				treatment.addView(this);
				progress = 0;
				
				String name = new File(matrix.getFile()).getName().replace(".asc", "");
				
				treatment.setInput("matrix", matrix);
				
				if(exportCsv){
					treatment.setInput("csv", outputFolder+"/"+name+".csv");
				}
				if(exportAscii){
					treatment.setInput("ascii", outputFolder+"/"+name+"_");
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
			
			// visualization
			if(viewAsciiOutput){
				MatrixManager.visualizeAsciiGrid(outputFolder);
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean runMultiSpatialSelectedWindow(Set<Matrix> inputMatrix,
			String processType, WindowShapeType shape,
			Friction friction, Matrix frictionMatrix,
			List<Integer> windowSizes, Set<Pixel> pixels, Set<String> metrics,
			String outputFolder, boolean viewAsciiOutput, boolean exportCsv, boolean exportAscii) {
		
		Treatment treatment = new MultiSpatialSelectedWindowTreatment();
		
		try{
			// settings
			if(processType.equalsIgnoreCase("qualitative")){
				treatment.setInput("qualitative", true);
			}else{
				treatment.setInput("qualitative", false);
			}
			treatment.setInput("shape", shape);
			treatment.setInput("pixels", pixels);
			treatment.setInput("metrics", metrics);
			treatment.setInput("window_sizes", windowSizes);
			if(friction != null){
				treatment.setInput("friction_map", friction);
			}
			if(frictionMatrix != null){
				treatment.setInput("friction_matrix", frictionMatrix);
			}
			
			for(Matrix matrix : inputMatrix){
				
				publish("treatment of the matrix "+matrix);
				treatment.addView(this);
				progress = 0;
				
				String name = new File(matrix.getFile()).getName().replace(".asc", "");
				
				treatment.setInput("matrix", matrix);
				
				if(exportCsv){
					treatment.setInput("csv", outputFolder+"/"+name+".csv");
				}
				if(exportAscii){
					treatment.setInput("ascii", outputFolder+"/"+name+"_");
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
			
			// visualization
			if(viewAsciiOutput){
				MatrixManager.visualizeAsciiGrid(outputFolder);
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean runTemporalSlidingWindow(Set<Matrix> inputMatrix,
			String processType, WindowShapeType shape,
			Friction friction, Matrix frictionMatrix,
			int windowSize, int delta, boolean interpolate, Set<String> metrics,
			String asciiOutput, boolean viewAsciiOutput, boolean exportCsv, boolean exportAscii,
			Set<Integer> filters, Set<Integer> unfilters) {
		
		Treatment treatment = new TemporalSlidingWindowTreatment();
		
		try{
			String name = getName(inputMatrix);
			publish("temporal sliding treatment of "+name+" using window size = "+windowSize);
			treatment.addView(this);
			progress = 0;
			
			// settings
			if(processType.equalsIgnoreCase("qualitative")){
				treatment.setInput("qualitative", true);
			}else{
				treatment.setInput("qualitative", false);
			}
			treatment.setInput("shape", shape);
			treatment.setInput("delta", delta);
			treatment.setInput("interpolation", interpolate);
			treatment.setInput("metrics", metrics);
			treatment.setInput("matrix", inputMatrix);
			treatment.setInput("window_size", windowSize);
			treatment.setInput("filters", filters);
			treatment.setInput("unfilters", unfilters);
			if(friction != null){
				treatment.setInput("friction_map", friction);
			}
			if(frictionMatrix != null){
				treatment.setInput("friction_matrix", frictionMatrix);
			}
			
			if(exportCsv){
				treatment.setInput("csv", asciiOutput+"/"+name+"_w"+windowSize+".csv");
			}
			if(exportAscii){
				treatment.setInput("ascii", asciiOutput+"/"+name+"_w"+windowSize+"_");
			}
			
			// treatment
			try{
				treatment.allRun();
				
				treatment.clearViews();
				treatment.clearOutputs();
			} catch(TreatmentException e){
				e.printStackTrace();
			}
			
			// visualization
			if(viewAsciiOutput){
				MatrixManager.visualizeAsciiGrid(asciiOutput);
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean runTemporalSelectedWindow(Set<Matrix> inputMatrix,
			String processType, WindowShapeType shape,
			Friction friction, Matrix frictionMatrix,
			int windowSize, Set<Pixel> pixels, Set<String> metrics,
			String asciiOutput, boolean viewAsciiOutput, boolean exportCsv, boolean exportAscii) {
	
		Treatment treatment = new TemporalSelectedWindowTreatment();
		treatment.addView(this);
		progress = 0;
	
		try{
			String name = getName(inputMatrix);
			publish("temporal selected treatment of "+name+" using window size = "+windowSize);
			// settings
			if(processType.equalsIgnoreCase("qualitative")){
				treatment.setInput("qualitative", true);
			}else{
				treatment.setInput("qualitative", false);
			}
			treatment.setInput("shape", shape);
			treatment.setInput("pixels", pixels);
			treatment.setInput("metrics", metrics);
			treatment.setInput("window_size", windowSize);
			treatment.setInput("matrix", inputMatrix);
			if(friction != null){
				treatment.setInput("friction_map", friction);
			}
			if(frictionMatrix != null){
				treatment.setInput("friction_matrix", frictionMatrix);
			}
		
			if(exportCsv){
				treatment.setInput("csv", asciiOutput+"/"+name+".csv");
			}
			if(exportAscii){
				treatment.setInput("ascii", asciiOutput+"/"+name+"_w"+windowSize+"_");
			}
		
			// treatment
			try{
				treatment.allRun();
				
				treatment.clearViews();
				treatment.clearOutputs();
			} catch(TreatmentException e){
				e.printStackTrace();
			}
		
			// visualization
			if(viewAsciiOutput){
				MatrixManager.visualizeAsciiGrid(asciiOutput);
			}
		
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean runTemporalGridWindow(Set<Matrix> inputMatrix,
			String processType, int gridSize, Set<String> metrics,
			String asciiOutput, boolean viewAsciiOutput, boolean exportCsv, boolean exportAscii) {
		
		Treatment treatment = new TemporalGridWindowTreatment();
		
		try{
			String name = getName(inputMatrix);
			publish("temporal grid treatment of "+name+" using grid size = "+gridSize);
			treatment.addView(this);
			progress = 0;
			
			// settings
			if(processType.equalsIgnoreCase("qualitative")){
				treatment.setInput("qualitative", true);
			}else{
				treatment.setInput("qualitative", false);
			}
			treatment.setInput("grid_size", gridSize);
			treatment.setInput("metrics", metrics);
			treatment.setInput("matrix", inputMatrix);
			
			if(exportCsv){
				treatment.setInput("csv", asciiOutput+"/"+name+"_g"+gridSize+".csv");
			}
			if(exportAscii){
				treatment.setInput("ascii", asciiOutput+"/"+name+"_g"+gridSize+"_");
			}
			
			// treatment
			try{
				treatment.allRun();
				
				treatment.clearViews();
				treatment.clearOutputs();
			} catch(TreatmentException e){
				e.printStackTrace();
			}
			
			// visualization
			if(viewAsciiOutput){
				MatrixManager.visualizeAsciiGrid(asciiOutput);
			}
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	 */
}