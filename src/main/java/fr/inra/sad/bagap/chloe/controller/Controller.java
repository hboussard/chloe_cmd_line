package fr.inra.sad.bagap.chloe.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import com.csvreader.CsvReader;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShapeType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RefPoint;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.domain.Domain;
import fr.inra.sad.bagap.chloe.model.Model;
import fr.inra.sad.bagap.chloe.view.Ihm;
import fr.inra.sad.bagap.chloe.view.TreatmentWorker;
import fr.inra.sad.bagap.chloe.view.treatment.TreatmentPanel;

public class Controller {

	private final Ihm ihm;
	
	private final Model model;
	
	public Controller(Ihm i, Model m) {
		this.ihm = i;
		this.ihm.setController(this);
		
		this.model = m;
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				if (Model.ACTION.PROGRESS.toString().equals(pce.getPropertyName())) {
					ihm.setProgress((Integer)pce.getNewValue());
				}else if (Model.ACTION.PUBLISH.toString().equals(pce.getPropertyName())) {
					ihm.publish((String)pce.getNewValue());
				}
			}
		});
	}
	
	public void close(){
		try{
			LocalContext.save();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public Ihm getIhm(){
		return ihm;
	}
	
	public void runSlidingWindow(final Set<Matrix> inputMatrix, final WindowShapeType shape, final Friction friction, final Matrix frictionMatrix,
			final List<Integer> windowSizes, final String distanceFunction, final int delta, final int xOrigin, final int yOrigin, final boolean interpolate, final double minRate,	
			final Set<String> metrics, final String outputFolder, final boolean viewAsciiOutput, final boolean exportCsv, 
			final boolean exportAscii, final Set<Integer> filters, final Set<Integer> unfilters) {
	
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				
				return model.runSlidingWindow(false, inputMatrix, shape, friction, frictionMatrix,
						windowSizes, distanceFunction, delta, xOrigin, yOrigin, interpolate, minRate, metrics, 
						outputFolder, null, null, viewAsciiOutput, exportCsv, exportAscii,
						filters, unfilters);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}

	public void runSelectedWindow(final Set<Matrix> inputMatrix, final double minRate,
			final WindowShapeType shape, final Friction friction, final Matrix frictionMatrix, final List<Integer> windowSizes, final String distanceFunction,
			final Set<Pixel> pixels, final Set<RefPoint> points, final Set<String> metrics, final String asciiOutput,
			final boolean viewAsciiOutput, final boolean exportCsv, final boolean exportAscii) {

		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runSelectedWindow(false, inputMatrix, minRate, shape, friction, frictionMatrix, 
						windowSizes, distanceFunction, pixels, points, metrics, asciiOutput, null, null, viewAsciiOutput, exportCsv, exportAscii);	
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runMapWindow(final Set<Matrix> inputMatrix, final Set<String> metrics, final String csvOutput) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runMapWindow(false, inputMatrix, metrics, csvOutput);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runGridWindow(final Set<Matrix> inputMatrix, final List<Integer> gridSizes, final double minRate, final Set<String> metrics, 
			final String asciiOutput, final boolean viewAsciiOutput, final boolean exportCsv, final boolean exportAscii) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runGridWindow(false, inputMatrix, gridSizes, minRate, metrics, asciiOutput, null, null, viewAsciiOutput, exportCsv, exportAscii);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void importAsciiGridFilter(final TreatmentPanel panel, final Set<Matrix> matrix, final String ascii) {
		if(model.importAsciiGrid(matrix, ascii, true)){
			panel.displayIhm4(ascii);
		}
	}
	
	public void importAsciiGridFriction(final TreatmentPanel panel, final Set<Matrix> matrix, final String ascii) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {	
				ihm.start();
				try{
					model.importAsciiGrid(matrix, ascii, true);
					//System.out.println(matrix);
					if(matrix != null){
						return true;
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return false;
			}
			@Override
			protected void done() {
				super.done();
				ihm.resetProgressBar();
			}
			
		};
		swingworker.execute();
	}
	
	public void importAsciiGrid(final TreatmentPanel panel, final Collection<Matrix> matrix, final String ascii) {
		
		File file = new File(ascii);
		//matrix.clear();
		if(file.isFile()){
			if(model.importAsciiGrid(matrix, ascii, true)){
				panel.displayIhm();
			}else{
				
				System.err.println("not possible to import this file : "+ascii+", try to clean it");
				ihm.publish("not possible to import this file : "+ascii+", try to clean it");
				model.cleanAsciiGrid(ascii);
				if(model.importAsciiGrid(matrix, ascii, true)){
					panel.displayIhm();
				}else{
					System.err.println("not possible to import this file : "+ascii);
					ihm.publish("not possible to import this file : "+ascii);
				}
			}
		}else{
			for(File f : file.listFiles()){
				if(f.isFile() && f.getName().endsWith(".asc")){
					if(!model.importAsciiGrid(matrix, f.toString(), true)){
						ihm.publish("not possible to import this file : "+f.toString()+", try to clean it");
						model.cleanAsciiGrid(f.toString());
					}
				}
			}
			panel.displayIhm();
		}
	}
	
	public void importAsciiGrid2(final TreatmentPanel panel, final Collection<Matrix> matrix, final String ascii) {
		File file = new File(ascii);
		if(file.isFile()){
			if(model.importAsciiGrid(matrix, ascii, true)){
				panel.displayIhm2(ascii);
			}else{
				
				System.err.println("not possible to import this file : "+ascii+", try to clean it");
				ihm.publish("not possible to import this file : "+ascii+", try to clean it");
				model.cleanAsciiGrid(ascii);
				if(model.importAsciiGrid(matrix, ascii, true)){
					panel.displayIhm2(ascii);
				}else{
					System.err.println("not possible to import this file : "+ascii);
					ihm.publish("not possible to import this file : "+ascii);
				}
			}
		}else{
			throw new IllegalArgumentException();
		}
	}
	
	public void importAsciiGrid3(final TreatmentPanel panel, final Collection<Matrix> matrix, final String ascii) {
		File file = new File(ascii);
		if(file.isFile()){
			if(model.importAsciiGrid(matrix, ascii, true)){
				panel.displayIhm3(ascii);
			}else{
				
				System.err.println("not possible to import this file : "+ascii+", try to clean it");
				ihm.publish("not possible to import this file : "+ascii+", try to clean it");
				model.cleanAsciiGrid(ascii);
				if(model.importAsciiGrid(matrix, ascii, true)){
					panel.displayIhm3(ascii);
				}else{
					System.err.println("not possible to import this file : "+ascii);
					ihm.publish("not possible to import this file : "+ascii);
				}
			}
		}else{
			throw new IllegalArgumentException();
		}
	}
	
	public void importShapefile(final TreatmentPanel panel, final String inputShape, final Set<String> shapes) {
		Map<String, String> attributes = new TreeMap<String, String>();
		File f = new File(inputShape);
		if(f.isDirectory()){
			double[] totalenvelope = new double[4];
			totalenvelope[0] = Double.MAX_VALUE;
			totalenvelope[1] = Double.MIN_VALUE;
			totalenvelope[2] = Double.MAX_VALUE;
			totalenvelope[3] = Double.MIN_VALUE;
			for(String file : f.list()){
				if(file.endsWith(".shp") || inputShape.endsWith(".SHP")){
					double[] envelope = new double[4];
					model.getAttributesAndEnvelopeFromShapefile(inputShape+"/"+file, attributes, envelope);
					shapes.add(inputShape+"/"+file);
					totalenvelope[0] = Math.min(totalenvelope[0], envelope[0]);
					totalenvelope[1] = Math.max(totalenvelope[1], envelope[1]);
					totalenvelope[2] = Math.min(totalenvelope[2], envelope[2]);
					totalenvelope[3] = Math.max(totalenvelope[3], envelope[3]);
				}
			}
			panel.displayAttributes(inputShape, false, attributes, totalenvelope);
		}else{
			if(inputShape.endsWith(".shp") || inputShape.endsWith(".SHP")){
				double[] envelope = new double[4];
				model.getAttributesAndEnvelopeFromShapefile(inputShape, attributes, envelope);
				shapes.add(inputShape);
				panel.displayAttributes(inputShape, true, attributes, envelope);
			}
		}
	}
	
	public void importXYCsv(final TreatmentPanel panel, final String inputCsv, final Set<String> variables){
		try{
			File fcsv = new File(inputCsv);
			if(fcsv.isDirectory()){
				for(String csv : fcsv.list()){
					if(csv.endsWith(".csv")){
						CsvReader cr = new CsvReader(inputCsv+"/"+csv);
						cr.setDelimiter(';');
						cr.readHeaders();
						
						for(String h : cr.getHeaders()){
							if(!h.equalsIgnoreCase("X") && !h.equalsIgnoreCase("Y")){
								variables.add(h);
							}
						}
						cr.close();
					}
				}
				panel.displayVariables();
				panel.enabledIhm();
			}else{
				CsvReader cr = new CsvReader(inputCsv);
				cr.setDelimiter(';');
				cr.readHeaders();
				for(String h : cr.getHeaders()){
					if(!h.equalsIgnoreCase("X") && !h.equalsIgnoreCase("Y")){
						variables.add(h);
					}
				}
				cr.close();
				panel.displayVariables();
				//panel.enabledIhm();
				panel.enabledIhmforCsv();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (FinalizedException e1) {
			e1.printStackTrace();
		} catch (CatastrophicException e1) {
			e1.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void importMapCsv(final TreatmentPanel panel, final String inputCsv, final Set<String> variables){
		try{
			CsvReader cr = new CsvReader(inputCsv);
			cr.setDelimiter(';');
			cr.readHeaders();
			for(String h : cr.getHeaders()){
				if(!h.equalsIgnoreCase("id")){
					variables.add(h);
				}
			}
			cr.close();
			panel.displayVMap();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (FinalizedException e1) {
			e1.printStackTrace();
		} catch (CatastrophicException e1) {
			e1.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void runSearchAndReplace(final Set<String> asciis, final int noData, final Map<Integer, Number> changes, 
			final String asciiOutput, final boolean viewAsciiOutput) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runSearchAndReplace(false, asciis, noData, changes, asciiOutput, null, viewAsciiOutput);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}

	public void generatePixels(final Set<Pixel> pixels, final Matrix m, 
			final Integer value, final int distance, final Set<Integer> with, final Set<Integer> without) {
		
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {	
				ihm.start();
				
				if(model.dispatch(pixels, m, value, distance, with, without)){
					TreatmentPanel.enabledAfterDispatch();
					return true;
				}
				publish("pixel generation : wrong parameters");
				return false;
			}
			@Override
			protected void done() {
				super.done();
				try {
					if(!get()){
						Thread.sleep(2000);
					}
					ihm.resetProgressBar();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			
		};
		swingworker.execute();
	}
	
	public void generatePixels(final Set<Pixel> pixels, final Set<Matrix> matrix, 
			final Integer value, final int distance, final Set<Integer> with, final Set<Integer> without) {
		
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {	
				ihm.start();
				
				if(model.dispatch(pixels, matrix, value, distance, with, without)){
					TreatmentPanel.enabledAfterDispatch();
					return true;
				}
				publish("pixel generation : wrong parameters");
				return false;
			}
			@Override
			protected void done() {
				super.done();
				try {
					if(!get()){
						Thread.sleep(2000);
					}
					ihm.resetProgressBar();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			
		};
		swingworker.execute();
	}

	public void exportAsciiGridFromCsv(final String inputCsv, final String folder, final Set<String> variables, 
			final int ncols, final int nrows, final double xllcorner, final double yllcorner, 
			final double cellsize, final int nodatavalue, final boolean viewAscii) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.exportAsciiGridFromCsv(false, inputCsv, folder, null, variables, ncols, nrows, 
						xllcorner, yllcorner, cellsize, nodatavalue, viewAscii);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}

	public void sortCsv(final String inputCsv) {
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.sortCsv(inputCsv);
			}
			@Override
			protected void done() {
				super.done();
				ihm.resetProgressBar();
			}
		};
		swingworker.execute();
	}
	
	public void exportAsciiGridFromShapefile(final Set<String> shapes, final String attribute, final String lookupTable,
			final Set<Double> cellsizes, final String outputFolder, final boolean viewAscii,
			final Double minx, final Double maxx, final Double miny, final Double maxy) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				
				return model.exportAsciiGridFromShapefile(false, shapes, attribute, lookupTable, cellsizes, outputFolder, null, viewAscii, minx, maxx, miny, maxy);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runDistance(final Set<Matrix> matrix, final Set<Integer> values, final String typeDistance, final double distance, 
			final Friction friction, final Matrix frictionMatrix, final String asciiOutput, final boolean viewAsciiOutput) {
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runDistance(false, matrix, values, typeDistance, distance, friction, frictionMatrix, asciiOutput, null, viewAsciiOutput);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runFilter(final Set<Matrix> matrix, final Matrix filterMatrix, final Set<Integer> values, final String asciiOutput, final boolean viewAsciiOutput) {
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runFilter(false, matrix, filterMatrix, values, asciiOutput, null, viewAsciiOutput);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runOverlay(final List<Matrix> matrix, final String asciiOutput, final boolean viewAsciiOutput) {
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runOverlay(false, matrix, asciiOutput, null, viewAsciiOutput);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runCombine(final List<Matrix> matrix, final List<String> names, String formula, final String asciiOutput, final boolean viewAsciiOutput) {
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runCombine(matrix, names, formula, asciiOutput, null, viewAsciiOutput);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}

	public void runClassification(final TreatmentPanel panel, final Set<Matrix> inputMatrix, final Map<Domain<Double, Double>, Integer> domains, final String asciiOutput, final boolean viewAsciiOutput) {
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				//panel.cleanDomains();
				return model.runClassification(false, inputMatrix, domains, asciiOutput, null, viewAsciiOutput);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runCluster(final Set<Matrix> matrix, final List<Integer> values, final String typeCluster, final double distance, final double minimumTotalArea,
			final Friction friction, final Matrix frictionMatrix, 
			final String outputFolder, final boolean viewAsciiOutput) {
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runCluster(false, matrix, values, typeCluster, distance, minimumTotalArea, friction, frictionMatrix, outputFolder, null, viewAsciiOutput);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runGroup(final Set<Matrix> matrix, final List<Integer> values, final List<Double> minimumAreas, 
			final double minimumTotal, final String typeCluster, final double distance, 
			final Friction friction, final Matrix frictionMatrix, 
			final String outputFolder, final boolean viewAsciiOutput) {
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runGroup(false, matrix, values, minimumAreas, minimumTotal, typeCluster, distance, friction, frictionMatrix, outputFolder, null, viewAsciiOutput);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	/*
	public void exportAsciiGridFromShapefile(final Set<DynamicLayer<DynamicFeature>> layers, final Instant t, 
			final String attribute, final Set<Double> cellsizes, final String outputFolder, final boolean viewAscii) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.exportAsciiGridFromShapefile(layers, t, attribute, cellsizes, outputFolder, viewAscii);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runMultiSpatialSlidingWindow(final Set<Matrix> inputMatrix,
			final String processType, final WindowShapeType shape, final Friction friction, final Matrix frictionMatrix,
			final List<Integer> windowSizes, final int delta, final boolean interpolate, final Set<String> metrics, 
			final String asciiOutput, final boolean viewAsciiOutput, final boolean exportCsv, final boolean exportAscii,
			final Set<Integer> filters, final Set<Integer> unfilters) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runMultiSpatialSlidingWindow(inputMatrix, processType, shape, friction, frictionMatrix,
						windowSizes, delta, interpolate, metrics, asciiOutput, viewAsciiOutput, exportCsv, exportAscii,
						filters, unfilters);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runMultiSpatialSelectedWindow(final Set<Matrix> inputMatrix,
			final String processType, final WindowShapeType shape, final Friction friction, final Matrix frictionMatrix,
			final List<Integer> windowSizes, final Set<Pixel> pixels, final Set<String> metrics, 
			final String asciiOutput, final boolean viewAsciiOutput, final boolean exportCsv, final boolean exportAscii) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runMultiSpatialSelectedWindow(inputMatrix, processType, shape, friction, frictionMatrix, 
						windowSizes, pixels, metrics, asciiOutput, viewAsciiOutput, exportCsv, exportAscii);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runTemporalSlidingWindow(final Set<Matrix> inputMatrix,
			final String processType, final WindowShapeType shape, final Friction friction, final Matrix frictionMatrix,
			final int windowSize, final int delta, final boolean interpolate, final Set<String> metrics,
			final String asciiOutput, final boolean viewAsciiOutput, final boolean exportCsv, final boolean exportAscii,
			final Set<Integer> filters, final Set<Integer> unfilters) {

		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runTemporalSlidingWindow(inputMatrix, processType, shape, friction, frictionMatrix, 
						windowSize, delta, interpolate, metrics, asciiOutput, viewAsciiOutput, exportCsv, exportAscii,
						filters, unfilters);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runTemporalSelectedWindow(final Set<Matrix> inputMatrix,
			final String processType, final WindowShapeType shape, final Friction friction, final Matrix frictionMatrix,
			final int windowSize, final Set<Pixel> pixels, final Set<String> metrics,
			final String asciiOutput, final boolean viewAsciiOutput, final boolean exportCsv, final boolean exportAscii) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runTemporalSelectedWindow(inputMatrix, processType, shape, friction, frictionMatrix,
						windowSize, pixels, metrics, asciiOutput, viewAsciiOutput, exportCsv, exportAscii);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	public void runTemporalGridWindow(final Set<Matrix> inputMatrix,
			final String processType, final int gridSize, final Set<String> metrics,
			final String asciiOutput, final boolean viewAsciiOutput, final boolean exportCsv, final boolean exportAscii) {

		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {
				ihm.start();
				return model.runTemporalGridWindow(inputMatrix, processType, gridSize, metrics, 
						asciiOutput, viewAsciiOutput, exportCsv, exportAscii);
			}
			@Override
			protected void done() {
				super.done();
				ihm.reset();
			}
		};
		swingworker.execute();
	}
	
	*/
	
	/*
	public void importAsciiGridFilter(final TreatmentPanel panel, final Set<Matrix> matrix, final String ascii) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {	
				ihm.start();
				try{
					if(model.importAsciiGrid(matrix, ascii)){
						panel.displayIhm4(ascii);
						return true;
					}
				}catch(Exception ex){
					ex.printStackTrace();
					return false;
				}
				return false;
			}
			@Override
			protected void done() {
				super.done();
				ihm.resetProgressBar();
			}
			
		};
		swingworker.execute();
	}*/
	
	/*
	public void importAsciiGrid(final TreatmentPanel panel, final Collection<Matrix> matrix, final String ascii) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {	
				ihm.start();
				try{
					File file = new File(ascii);
					if(file.isFile()){
						if(model.importAsciiGrid(matrix, ascii)){
							panel.displayIhm(true);
							return true;
						}else{
							
							System.err.println("not possible to import this file : "+ascii+", try to clean it");
							ihm.publish("not possible to import this file : "+ascii+", try to clean it");
							model.cleanAsciiGrid(ascii);
							if(model.importAsciiGrid(matrix, ascii)){
								panel.displayIhm(true);
								return true;
							}else{
								System.err.println("not possible to import this file : "+ascii);
								ihm.publish("not possible to import this file : "+ascii);
								return false;
							}
						}
					}else{
						for(File f : file.listFiles()){
							if(f.isFile() && f.getName().endsWith(".asc")){
								if(!model.importAsciiGrid(matrix, f.toString())){
									ihm.publish("not possible to import this file : "+f.toString()+", try to clean it");
									model.cleanAsciiGrid(f.toString());
									if(!model.importAsciiGrid(matrix, f.toString())){
										return false;
									}
								}
							}
						}
						panel.displayIhm(false);
						return true;
					}
				}catch(Exception ex){
					ex.printStackTrace();
					return false;
				}
			}
			
			@Override
			protected void done() {
				super.done();
				ihm.resetProgressBar();
			}
			
		};
		swingworker.execute();
	}*/
	
	/*
	public void importAsciiGrid2(final TreatmentPanel panel, final Collection<Matrix> matrix, final String ascii) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {	
				ihm.start();
				try{
					File file = new File(ascii);
					if(file.isFile()){
						if(model.importAsciiGrid(matrix, ascii)){
							panel.displayIhm2(ascii);
							return true;
						}else{
							
							System.err.println("not possible to import this file : "+ascii+", try to clean it");
							ihm.publish("not possible to import this file : "+ascii+", try to clean it");
							model.cleanAsciiGrid(ascii);
							if(model.importAsciiGrid(matrix, ascii)){
								panel.displayIhm2(ascii);
								return true;
							}else{
								System.err.println("not possible to import this file : "+ascii);
								ihm.publish("not possible to import this file : "+ascii);
								return false;
							}
						}
					}else{
						throw new IllegalArgumentException();
					}
				}catch(Exception ex){
					ex.printStackTrace();
					return false;
				}
			}
			
			@Override
			protected void done() {
				super.done();
				ihm.resetProgressBar();
			}
			
		};
		swingworker.execute();
	}
	*/
	
	/*
	public void importAsciiGrid3(final TreatmentPanel panel, final Collection<Matrix> matrix, final String ascii) {
		
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {	
				ihm.start();
				try{
					File file = new File(ascii);
					if(file.isFile()){
						if(model.importAsciiGrid(matrix, ascii)){
							panel.displayIhm3(ascii);
							return true;
						}else{
							
							System.err.println("not possible to import this file : "+ascii+", try to clean it");
							ihm.publish("not possible to import this file : "+ascii+", try to clean it");
							model.cleanAsciiGrid(ascii);
							if(model.importAsciiGrid(matrix, ascii)){
								panel.displayIhm3(ascii);
								return true;
							}else{
								System.err.println("not possible to import this file : "+ascii);
								ihm.publish("not possible to import this file : "+ascii);
								return false;
							}
						}
					}else{
						throw new IllegalArgumentException();
					}
				}catch(Exception ex){
					ex.printStackTrace();
					return false;
				}
			}
			
			@Override
			protected void done() {
				super.done();
				ihm.resetProgressBar();
			}
			
		};
		swingworker.execute();
	}*/
	
	/*
	public void importShapefile(final TreatmentPanel panel, final String inputShape, final Set<String> shapes) {
		System.out.println("lancement de l'importation");
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {	
				ihm.start();
				try{
					Map<String, String> attributes = new TreeMap<String, String>();
					File f = new File(inputShape);
					if(f.isDirectory()){
						double[] totalenvelope = new double[4];
						totalenvelope[0] = Double.MAX_VALUE;
						totalenvelope[1] = Double.MIN_VALUE;
						totalenvelope[2] = Double.MAX_VALUE;
						totalenvelope[3] = Double.MIN_VALUE;
						for(String file : f.list()){
							if(file.endsWith(".shp")){
								double[] envelope = new double[4];
								model.getAttributesAndEnvelopeFromShapefile(inputShape+"/"+file, attributes, envelope);
								shapes.add(inputShape+"/"+file);
								totalenvelope[0] = Math.min(totalenvelope[0], envelope[0]);
								totalenvelope[1] = Math.max(totalenvelope[1], envelope[1]);
								totalenvelope[2] = Math.min(totalenvelope[2], envelope[2]);
								totalenvelope[3] = Math.max(totalenvelope[3], envelope[3]);
							}
						}
						panel.displayAttributes(inputShape, false, attributes, totalenvelope);
						//panel.enabledImportation();
						return true;
					}else{
						if(inputShape.endsWith(".shp")){
							double[] envelope = new double[4];
							model.getAttributesAndEnvelopeFromShapefile(inputShape, attributes, envelope);
							shapes.add(inputShape);
							panel.displayAttributes(inputShape, true, attributes, envelope);
							//panel.enabledImportation();
							return true;
						}
						return false;
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return false;
			}
			
			@Override
			protected void done() {
				super.done();
				ihm.resetProgressBar();
			}
		};
		swingworker.execute();
	}*/
	
	/*
	public void importXYCsv2(final TreatmentPanel panel, final String inputCsv, final Set<String> variables){
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {	
				ihm.start();
				try{
					File fcsv = new File(inputCsv);
					if(fcsv.isDirectory()){
						for(String csv : fcsv.list()){
							if(csv.endsWith(".csv")){
								CsvReader cr = new CsvReader(inputCsv+"/"+csv);
								cr.setDelimiter(';');
								cr.readHeaders();
								
								for(String h : cr.getHeaders()){
									if(!h.equalsIgnoreCase("X") && !h.equalsIgnoreCase("Y")){
										variables.add(h);
									}
								}
								cr.close();
							}
						}
						panel.displayVariables(false);
						panel.enabledIhm(false);
						//panel.enabledImportation();
					}else{
						CsvReader cr = new CsvReader(inputCsv);
						cr.setDelimiter(';');
						cr.readHeaders();
						for(String h : cr.getHeaders()){
							if(!h.equalsIgnoreCase("X") && !h.equalsIgnoreCase("Y")){
								variables.add(h);
							}
						}
						cr.close();
						panel.displayVariables(true);
						panel.enabledIhm(true);
						//panel.enabledImportation();
					}
					return true;
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (FinalizedException e1) {
					e1.printStackTrace();
				} catch (CatastrophicException e1) {
					e1.printStackTrace();
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return false;
			}
			
			@Override
			protected void done() {
				super.done();
				ihm.resetProgressBar();
			}
			
		};
		swingworker.execute();
		
	}
	*/
	
	/*
	public void importMapCsv(final TreatmentPanel panel, final String inputCsv, final Set<String> variables){
		TreatmentWorker swingworker = new TreatmentWorker(ihm) {
			@Override
			protected Boolean doInBackground() throws Exception {	
				ihm.start();
				try{
					CsvReader cr = new CsvReader(inputCsv);
					cr.setDelimiter(';');
					cr.readHeaders();
					for(String h : cr.getHeaders()){
						if(!h.equalsIgnoreCase("id")){
							variables.add(h);
						}
					}
					cr.close();
					panel.displayVMap();
					return true;
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (FinalizedException e1) {
					e1.printStackTrace();
				} catch (CatastrophicException e1) {
					e1.printStackTrace();
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return false;
			}
			
			@Override
			protected void done() {
				super.done();
				ihm.resetProgressBar();
			}
			
		};
		swingworker.execute();
		
	}*/
}
