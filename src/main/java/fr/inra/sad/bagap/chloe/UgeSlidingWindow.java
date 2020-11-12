package fr.inra.sad.bagap.chloe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.analysis.matrix.util.SpatialCsvManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShapeType;
import fr.inra.sad.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.ArrayMatrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.ArrayMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.ProxyMatrix;
import fr.inra.sad.bagap.apiland.treatment.Treatment;
import fr.inra.sad.bagap.apiland.treatment.TreatmentException;
import fr.inra.sad.bagap.apiland.treatment.window.SlidingWindowMatrixTreatment;

public class UgeSlidingWindow {
	/*
	private static String path = "C:/Hugues/data/ascii/decoupe/";
	
	public static void main(String[] args){
		List<Integer> windowSizes = new ArrayList<Integer>();
		windowSizes.add(51);
		Set<String> metrics = new TreeSet<String>();
		metrics.add("SHDI");
		Set<Integer> filters = new HashSet<Integer>();
		Set<Integer> unfilters = new HashSet<Integer>();
		
		
		runSlidingWindow(path+"pf2000n.asc", WindowShapeType.CIRCLE, null, null, windowSizes, 1, false, 0, metrics, path+"/test/", null, null, true, true, true, filters, unfilters);
	}
	
	public static boolean runSlidingWindow(String inputAscii, WindowShapeType shape, Friction friction, Matrix frictionMatrix,
			List<Integer> windowSizes, int delta, boolean interpolate, double minRate, Set<String> metrics,
			String outputFolder, String outputAsc, String outputCsv, boolean viewAsciiOutput, boolean exportCsv, boolean exportAscii,
			Set<Integer> filters, Set<Integer> unfilters) {
		
		// 1. quelle taille de carte ?
		
		ProxyMatrix mp = MatrixFactory.readHeader(inputAscii);
		System.out.println(mp.width()+" "+mp.height()+" "+mp.size());
		if(mp.size() <= 1000000){
			System.out.println("carte gérable en ArrayMatrix");
		}else if(mp.size() <= 100000000){
			System.out.println("carte gérable en JaiMatrix");
		}else{
			System.out.println("carte à découper");
		}
		
		// 2. quelle taille d'analyse ?
		
		int sf = 0; // somme des carrés des tailles de fenêtre
		int gf = 0; // plus grande taille de fenêtre
		for(Integer ws : windowSizes){
			sf += ws * ws;
			gf = Math.max(gf, ws);
		}
		int ta = sf * mp.width() / delta;
		System.out.println("la taille de l'analyse est "+ta);
		
		// 3. quelle taille de découpage ?
		
		int tuile = 500;
		
		// 4. découpage
		
		Set<Tuile> tuiles = decoupeAscii(inputAscii, tuile, gf-1);
		File file = new File(inputAscii);
		File folder = new File(file.getParent()+"/"+file.getName().replaceAll(".asc", "")); 
		
		// 5. paramétrisation et lancement des analyses sur chaque tuile		

		String outputs = folder+"/analyse/";
		new File(outputs).mkdirs();
		
		Set<Thread> threads = new HashSet<Thread>();
		for(String f : folder.list()){
			if(f.endsWith(".asc")){
				try {
				//Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(folder+"/"+f, false);
				Matrix m = ArrayMatrixFactory.get().createWithAsciiGrid(folder+"/"+f, true);
				Thread thread = new Thread(){
					@Override
					public void run() {
						runSingleSlidingWindow(m, shape, friction, frictionMatrix, windowSizes, delta, interpolate, minRate, metrics, outputs, 
									outputAsc, outputCsv, false, exportCsv, false, filters, unfilters);
					}
				};
				threads.add(thread);
				
				thread.start();
				} catch (NumberFormatException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		while(!allTerminated(threads)){
			// do nohting
		}
		
		// 6. reconstitution des cartes résultantes
		
		for(Tuile t : tuiles){
			System.out.println(t.getIndex()+" "+t.getMinX()+" "+t.getMaxX()+" "+t.getMinY()+" "+t.getMaxY());
		}
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(path+"test.csv"));
			StringBuffer sb = new StringBuffer("X;Y");
			for(String m : metrics){
				sb.append(";"+m);
			}
			bw.write(sb.toString());
			bw.newLine();
			sb.setLength(0);
			
			Set<Tuile> readers = new HashSet<Tuile>();
			Tuile currentT = null;
			String values, xy;
			double pX, pY;
			for(int j=0; j<mp.height(); j++){
				pY = CoordinateManager.getProjectedY(mp, j);
				readers = getTuiles(readers, tuiles, j);
				for(int i=0; i<mp.width(); i++){
					for(Tuile t : readers){
						if(t.isXActive(i)){
							currentT = t;
							break;
						}
					}
					pX = CoordinateManager.getProjectedX(mp, i);
					//System.out.println(pX);
					xy = pX+";"+pY;
					
					values = currentT.getValues(xy);
					
					sb.append(values);
					bw.write(sb.toString());
					bw.newLine();
					sb.setLength(0);
				}
				//sb.deleteCharAt(sb.length()+1);
			}
		
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		exportAsciiFromCsv(path+"test.csv", outputFolder, outputAsc, mp.width(), mp.height(), mp.minX(), mp.minY(), mp.cellsize(), mp.noDataValue(), viewAsciiOutput);
		
		return true;
	}
	
	private static void exportAsciiFromCsv(String inputCsv, String folder, String outputAsc, int ncols, int nrows, double xllcorner,
				double yllcorner, double cellsize, int nodatavalue, boolean viewAscii) {
			
		Set<String> variables = new TreeSet<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputCsv));
			String[] values = br.readLine().split(";");
			for(String v : values){
				if(!v.equalsIgnoreCase("id") 
						&& !v.equalsIgnoreCase("x")
						&& !v.equalsIgnoreCase("y")){
					variables.add(v);
				}
			}
			
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		try{
			SpatialCsvManager.exportAsciiGrid(inputCsv, folder, outputAsc, variables, ncols, nrows, xllcorner, yllcorner, 1, cellsize, nodatavalue);
				
			if(viewAscii){
				if(outputAsc != null){
					MatrixManager.visualize(outputAsc);
				}else{
					MatrixManager.visualize(folder);	
				}
			}
				
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private static boolean allTerminated(Set<Thread> threads) {
		for(Thread thread : threads){
			if(thread.getState() != State.TERMINATED){
				return false;
			}
		}
		for(Thread thread : threads){
			thread = null;
		}
		return true;
	}

	private static Set<Tuile> getTuiles(Set<Tuile> readers, Set<Tuile> tuiles, int j) {
		if(readers.size() != 0  && readers.iterator().next().isYActive(j)){
			return readers;
		}else{
			for(Tuile t : readers){
				t.closeBufferedReaders();
			}
			
			Set<Tuile> newReaders = new HashSet<Tuile>();
			for(Tuile t : tuiles){
				if(t.isYActive(j)){
					newReaders.add(t);
					t.openBufferedReaders();
				}
			}
			return newReaders;
		}
	}

	private static Set<Tuile> decoupeAscii(String inputAscii, int tuile, int chevauchement) {
		
		System.out.println("decoupage de "+inputAscii+" avec des tuiles de taille "+tuile+" et un chevauchement de "+chevauchement);
		
		File f = new File(inputAscii);
		File folder = new File(f.getParent()+"/"+f.getName().replaceAll(".asc", "")); 
		folder.mkdir();
		
		ProxyMatrix mp = MatrixFactory.readHeader(inputAscii);
		int nbTuile = 0;
		Set<Decoupeur> decoupeurs = new HashSet<Decoupeur>();
		int tabX = 0, tabY = 0;
		int minX, maxX, minY, maxY;
		Set<Tuile> tuiles = new HashSet<Tuile>();
		for(int y=0; y<mp.height()-chevauchement; y+=tuile-chevauchement){
			tabY++;
			tabX = 0;
			
			if(tabY == 1){
				minY = 0;
			}else{
				minY = y + chevauchement/2;
			}
			if((y + tuile - chevauchement/2 - 1) < mp.height()){
				maxY = y + tuile - chevauchement/2 - 1;
			}else{
				maxY = mp.height() - 1;
			}
			
			for(int x=0; x<mp.width()-chevauchement; x+=tuile-chevauchement){
				tabX++;
				//System.out.println(x+" "+y);
				nbTuile++;
				decoupeurs.add(new Decoupeur(inputAscii, folder.getAbsolutePath()+"/", tuile, nbTuile, x ,y));
				
				if(tabX == 1){
					minX = 0;
				}else{
					minX = x + chevauchement/2;
				}
				if((x + tuile - chevauchement/2 - 1) < mp.width()){
					maxX = x + tuile - chevauchement/2 - 1;
				}else{
					maxX = mp.width() - 1;
				}
				tuiles.add(new Tuile(nbTuile, tabX, tabY, minX, maxX, minY, maxY, folder.getAbsolutePath()+"/"));
			}
		}
		System.out.println("nombre de tuiles au total "+nbTuile);
		
		for(Decoupeur d : decoupeurs){
			d.init();
		}
		
		exportTuiles(inputAscii, decoupeurs);
		
		for(Decoupeur d : decoupeurs){
			d.close();
		}
		
		return tuiles;
	}

	private static void exportTuiles(String inputAscii, Set<Decoupeur> decoupeurs) {
		
		ProxyMatrix mp = MatrixFactory.readHeader(inputAscii);
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inputAscii));
			String line;
			
			br.readLine();
			br.readLine();
			br.readLine();
			br.readLine();
			br.readLine();
			br.readLine();
			
			for(int y=0; y<mp.height(); y++){
				line = br.readLine();
				if(line.startsWith(" ")){
					line=line.substring(1, line.length());
				}
				//System.out.println(line.length());
				String[] values = line.split(" ");
				//System.out.println(values.length+" "+values[0]);
				
				for(int x=0; x<mp.width(); x++){
					double v = Double.parseDouble(values[x]);
					//System.out.println(v);
					for(Decoupeur d : decoupeurs){
						d.setValue(x, y, v);
					}
				}
			}
			
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static boolean runSingleSlidingWindow(Matrix inputMatrix, WindowShapeType shape, Friction friction, Matrix frictionMatrix,
			List<Integer> windowSizes, int delta, boolean interpolate, double minRate, Set<String> metrics,
			String outputFolder, String outputAsc, String outputCsv, boolean viewAsciiOutput, boolean exportCsv, boolean exportAscii,
			Set<Integer> filters, Set<Integer> unfilters) {
		
		
		Treatment treatment = new SlidingWindowMatrixTreatment();
		try{
			
			String name = new File(inputMatrix.getFile()).getName().replace(".asc", "");
			
			// settings
			treatment.setInput("shape", shape);
			treatment.setInput("delta", delta);
			treatment.setInput("interpolation", interpolate);
			treatment.setInput("min_rate", minRate);
			treatment.setInput("metrics", metrics);
			treatment.setInput("filters", filters);
			treatment.setInput("unfilters", unfilters);
			if(friction != null){treatment.setInput("friction_map", friction);}
			if(frictionMatrix != null){treatment.setInput("friction_matrix", frictionMatrix);}		
			treatment.setInput("matrix", inputMatrix);	
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
	*/
}
