package fr.inra.sad.bagap.chloe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShapeType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RefPoint;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.domain.Domain;
import fr.inra.sad.bagap.chloe.controller.Controller;
import fr.inra.sad.bagap.chloe.controller.ChloeContext;
import fr.inra.sad.bagap.chloe.model.Model;
import fr.inra.sad.bagap.chloe.view.Ihm;
import fr.inra.sad.bagap.chloe.view.LogoFrame;
import fr.inra.sad.bagap.chloe.view.treatment.ClassificationPanel;

public class Main {

	//62012
	//C:/Hugues/formation/EcoPaysage/session_11-12-19/paysage_87/test/params_test1.properties
	
	public static void main(String[] args) {
		if(args[0].equalsIgnoreCase("62012")){
			launchGUI(args[0]);
		}else if(args[0].endsWith(".properties")){
			launchBatch(args[0]);
		}else{
			throw new IllegalArgumentException("argument "+args[0]+" is not recognize");
		}
	}
	
	private static void launchGUI(String socket){
		LogoFrame launch = null;
		try{
			launch = new LogoFrame();
			System.out.println("Chloe - INRAE - ACT - BAGAP");
			
			Locale.setDefault(Locale.US);
			ChloeContext.load(); // chargement du context local
			
			Ihm ihm = new Ihm(new ServerSocket(new Integer(socket)));
			Model model = new Model();
			new Controller(ihm, model);
			
			ihm.getFrame();
		}catch(Exception e){
			System.out.println("Chloe is already open !!!");
		}
		finally{
			launch.dispose();
		}
	}
	
	private static void launchBatch(String file){
		Model model = new Model();
		try{
			Properties properties = new Properties();
	        Reader in = new InputStreamReader(new FileInputStream(file), "UTF8");
			//FileInputStream in = new FileInputStream(file);
			properties.load(in);
			in.close();
			if(properties.containsKey("treatment")){
				String treatment = properties.getProperty("treatment");
				switch(treatment){
				case "from csv" : launchFromCsv(model, properties); break;
				case "from shapefile" : launchFromShapefile(model, properties); break;
				case "search and replace" : launchSearchAndReplace(model, properties); break;
				case "overlay" : launchOverlay(model, properties); break;
				case "distance" : launchDistance(model, properties); break;
				case "classification" : launchClassification(model, properties); break;
				case "cluster" : launchCluster(model, properties); break;
				case "combine" : launchCombine(model, properties); break; 
				case "filter" : launchFilter(model, properties); break;
				case "map" : launchMap(model, properties); break;
				case "grid" : launchGrid(model, properties); break;
				case "sliding" : launchSliding(model, properties); break;
				case "selected" : launchSelected(model, properties); break;
				default :
					throw new IllegalArgumentException("treatment "+treatment+" is not implemented yet");
				}
			}
		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	private static void launchFromCsv(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'exportAsciiGridFromCsv()'");
		try {
			String inputCsv = importInputCsv(properties);
			Set<String> variables = importVariables(properties);
			int ncols = importNCols(properties);
			int nrows = importNRows(properties);
			double xllcorner = importXllCorner(properties);
			double yllcorner = importYllCorner(properties);
			double cellsize = importCellSize(properties);
			int nodatavalue = importNoDataValue(properties);
			String folder = null;
			String outputAsc = null;
			if(properties.containsKey("output_folder")){
				folder = importOutputFolder(properties);
			}else{
				outputAsc = importOutputAscii(properties);
			}
			boolean viewAscii = importVisualizeAscii(properties);
			
			model.exportAsciiGridFromCsv(true, inputCsv, folder, outputAsc, variables, ncols, nrows, xllcorner, yllcorner, cellsize, nodatavalue, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchFromShapefile(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'exportAsciiGridFromShapefile()'");
		try {
			Set<String> layers = importInputShapefile(properties);
			String attribute = importAttribute(properties);
			String lookupTable = importLookupTable(properties);
			Set<Double> cellsizes = importCellSizes(properties);
			String folder = null;
			String outputAsc = null;
			if(properties.containsKey("output_folder")){
				folder = importOutputFolder(properties);
			}else{
				outputAsc = importOutputAscii(properties);
			}
			boolean viewAscii = importVisualizeAscii(properties);
			Double minx = importMinX(properties);
			Double maxx = importMaxX(properties);
			Double miny = importMinY(properties);
			Double maxy = importMaxY(properties);
			
			model.exportAsciiGridFromShapefile(true, layers, attribute, lookupTable, cellsizes, folder, outputAsc, viewAscii, minx, maxx, miny, maxy);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchSearchAndReplace(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'runSearchAndReplace()'");
		try {
			Set<String> asciis = importInputAsciiGrid(properties);
			int nodatavalue = importNoDataValue(properties);
			Map<String, String> changes = importChanges(properties);
			String folder = null;
			String outputAsc = null;
			if(properties.containsKey("output_folder")){
				folder = importOutputFolder(properties);
			}else{
				outputAsc = importOutputAscii(properties);
			}
			boolean viewAscii = importVisualizeAscii(properties);
			
			model.runSearchAndReplace(true, asciis, nodatavalue, changes, folder, outputAsc, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchOverlay(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'runOverlay()'");
		try {
			//List<Matrix> matrix = importOverlayingMatrix(model, properties);
			List<String> asciis = importOverlayingAsciis(model, properties);
			String folder = null;
			String outputAsc = null;
			if(properties.containsKey("output_folder")){
				folder = importOutputFolder2(properties);
			}else{
				outputAsc = importOutputAscii2(properties);
			}
			boolean viewAscii = importVisualizeAscii2(properties);
			
			model.runOverlay(true, asciis, folder, outputAsc, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchDistance(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'runDistance()'");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			Set<Integer> distances = importDistances(properties);
			String distanceType = importDistanceType(properties);
			double maxDistance = importMaxDistance(properties);
			Friction friction = importDistanceFriction(properties);
			Matrix frictionMatrix =  importDistanceFrictionMatrix(model, properties);
			String folder = null;
			String outputAsc = null;
			if(properties.containsKey("output_folder")){
				folder = importOutputFolder(properties);
			}else{
				outputAsc = importOutputAscii(properties);
			}
			boolean viewAscii = importVisualizeAscii(properties);
			
			model.runDistance(true, matrix, distances, distanceType, maxDistance, friction, frictionMatrix, folder, outputAsc, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchClassification(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'runClassification()'");
		try {
			Set<String> asciis = importInputAsciiGrid(properties);
			//Set<Matrix> matrix = importInputMatrix(model, properties);
			Map<Domain<Double, Double>, Integer> domains = importDomains(properties);
			String folder = null;
			String outputAsc = null;
			if(properties.containsKey("output_folder")){
				folder = importOutputFolder(properties);
			}else{
				outputAsc = importOutputAscii(properties);
			}
			boolean viewAscii = importVisualizeAscii(properties);
			
			model.runClassification(true, asciis, domains, folder, outputAsc, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchCluster(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'runCluster()'");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			List<Integer> clusters = importCluster(properties);
			String typeCluster = importClusterType(properties);
			double distance = importClusterDistance(properties);
			Friction friction = importClusterFriction(properties);
			Matrix frictionMatrix =  importClusterFrictionMatrix(model, properties);
			
			String folder = null;
			String outputAsc = null;
			String outputCsv = null;
			if(properties.containsKey("output_folder")){
				folder = importOutputFolder(properties);
			}else{
				outputAsc = importOutputAscii(properties);
				outputCsv = importOutputCsv(properties);
			}
			boolean viewAscii = importVisualizeAscii(properties);
			double minimumTotalArea = importMinimumTotalArea(properties);
			
			model.runCluster(true, matrix, clusters, typeCluster, distance, minimumTotalArea, friction, frictionMatrix, folder, outputAsc, outputCsv, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	private static void launchCombine(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'runCombine()'");
		try {
			//List<Matrix> matrix = importFactorsMatrix(model, properties);
			List<String> asciis = importFactorsAsciis(model, properties);
			List<String> names = importFactorsNames(model, properties);
			String combination = importCombination(model, properties);
			
			String folder = null;
			String outputAsc = null;
			if(properties.containsKey("output_folder")){
				folder = importOutputFolder3(properties);
			}else{
				outputAsc = importOutputAscii3(properties);
			}
			boolean viewAscii = importVisualizeAscii3(properties);
			
			model.runCombine(true, asciis, names, combination, folder, outputAsc, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchFilter(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'runFilter()'");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			Matrix filterMatrix = importFilterAscii(model, properties);
			Set<Integer> filterValues = importFilterValues(properties);
			String folder = null;
			String outputAsc = null;
			if(properties.containsKey("output_folder")){
				folder = importOutputFolder(properties);
			}else{
				outputAsc = importOutputAscii(properties);
			}
			boolean viewAscii = importVisualizeAscii(properties);
			
			model.runFilter(true, matrix, filterMatrix, filterValues, folder, outputAsc, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchMap(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'runMapWindow()'");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			Set<String> metrics = importMetrics(properties);
			String csvOutput = importOutputCsv(properties);
			
			model.runMapWindow(true, matrix, metrics, csvOutput);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchGrid(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'runGridWindow()'");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			List<Integer> gridSizes = importGridSizes(properties);
			double minRate = importMaximumNoValueRate(properties);
			Set<String> metrics = importMetrics(properties);
			
			String folder = null;
			String outputAsc = null;
			String outputCsv = null;
			if(properties.containsKey("output_folder")){
				folder = importOutputFolder(properties);
			}else{
				outputAsc = importOutputAscii(properties);
				outputCsv = importOutputCsv(properties);
			}
			
			boolean viewAscii = importVisualizeAscii(properties);
			boolean exportCsv = importExportCsv(properties);
			boolean exportAscii = importExportAscii(properties);
			
			model.runGridWindow(true, matrix, gridSizes, minRate, metrics, folder, outputAsc, outputCsv, viewAscii, exportCsv, exportAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchSliding(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'runSlidingWindow()'");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			WindowShapeType shape = importShape(properties);
			Friction friction = importFriction(properties);
			Matrix frictionMatrix = importFrictionMatrix(model, properties);
			List<Integer> windowSizes = importWindowSizes(properties);
			double minRate = importMaximumNoValueRate(properties);
			int delta = importDeltaDisplacement(properties);
			int xOrigin = importXOrigin(properties);
			int yOrigin = importYOrigin(properties);
			boolean interpolate = importInterpolation(properties);
			Set<String> metrics = importMetrics(properties);
			String distanceFunction = importDistanceFunction(properties);
			
			String folder = null;
			String outputAsc = null;
			String outputCsv = null;
			if(properties.containsKey("output_folder")){
				folder = importOutputFolder(properties);
			}else{
				outputAsc = importOutputAscii(properties);
				outputCsv = importOutputCsv(properties);
			}
			
			boolean viewAscii = importVisualizeAscii(properties);
			boolean exportCsv = importExportCsv(properties);
			boolean exportAscii = importExportAscii(properties);
			Set<Integer> filters = importFilters(properties);
			Set<Integer> unfilters = importUnfilters(properties);
			
			model.runSlidingWindow(true, matrix, shape, friction, frictionMatrix, windowSizes, distanceFunction, delta, xOrigin, yOrigin, interpolate, minRate, metrics, folder, outputAsc, outputCsv, viewAscii, exportCsv, exportAscii, filters, unfilters);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchSelected(Model model, Properties properties) {
		//System.out.println("##chloe - appel de la fonction 'runSelectedWindow()'");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			WindowShapeType shape = importShape(properties);
			Friction friction = importFriction(properties);
			Matrix frictionMatrix = importFrictionMatrix(model, properties);
			double minRate = importMaximumNoValueRate(properties);
			List<Integer> windowSizes = importWindowSizes(properties);
			Set<Pixel> pixels = importPixels(matrix, properties);
			Set<RefPoint> points = importPoints(properties);
			Set<String> metrics = importMetrics(properties);
			String distanceFunction = importDistanceFunction(properties);
			
			String folder = null;
			String outputAsc = null;
			String outputCsv = null;
			if(properties.containsKey("output_folder")){
				folder = importOutputFolder(properties);
			}else{
				outputAsc = importOutputAscii(properties);
				outputCsv = importOutputCsv(properties);
			}
			
			boolean viewAscii = importVisualizeAscii(properties);
			boolean exportCsv = importExportCsv(properties);
			boolean exportAscii = importExportAscii(properties);
			
			model.runSelectedWindow(true, matrix, minRate, shape, friction, frictionMatrix, windowSizes, distanceFunction, pixels, points, metrics, folder, outputAsc, outputCsv, viewAscii, exportCsv, exportAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}
	
	// required
	public static String importInputCsv(Properties properties) throws NoParameterException {
		if(properties.containsKey("input_csv")){
			return properties.getProperty("input_csv");
		}
		throw new NoParameterException("input_csv");
	}
	
	// required	
	public static Set<String> importVariables(Properties properties) throws NoParameterException {
		if(properties.containsKey("variables")){
			String prop = properties.getProperty("variables");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] vs = prop.split(";");
			Set<String> vars = new TreeSet<String>();
			for(String v : vs){
				vars.add(v);

			}
			return vars;
		}
		throw new NoParameterException("variables");
	}
	
	// required 
	public static int importNCols(Properties properties) throws NoParameterException {
		if(properties.containsKey("ncols")){
			return Integer.parseInt(properties.getProperty("ncols"));
		}
		throw new NoParameterException("ncols");
	}
	
	// required
	public static int importNRows(Properties properties) throws NoParameterException {
		if(properties.containsKey("nrows")){
			return Integer.parseInt(properties.getProperty("nrows"));
		}
		throw new NoParameterException("nrows");
	}
	
	// required
	public static double importXllCorner(Properties properties) throws NoParameterException {
		if(properties.containsKey("xllcorner")){
			return Double.parseDouble(properties.getProperty("xllcorner"));
		}
		throw new NoParameterException("xllcorner");
	}
	
	// required 
	public static double importYllCorner(Properties properties) throws NoParameterException {
		if(properties.containsKey("yllcorner")){
			return Double.parseDouble(properties.getProperty("yllcorner"));
		}
		throw new NoParameterException("yllcorner");
	}
	
	// required 
	public static double importCellSize(Properties properties) throws NoParameterException {
		if(properties.containsKey("cellsize")){
			return Double.parseDouble(properties.getProperty("cellsize"));
		}
		throw new NoParameterException("cellsize");
	}
	
	// required 
	public static int importNoDataValue(Properties properties) throws NoParameterException {
		if(properties.containsKey("nodata_value")){
			return Integer.parseInt(properties.getProperty("nodata_value"));
		}
		throw new NoParameterException("nodata_value");
	}
	
	// required 
	public static String importOutputFolder(Properties properties) throws NoParameterException {
		if(properties.containsKey("output_folder")){
			return properties.getProperty("output_folder")+"/";
		}
		throw new NoParameterException("output_folder");
	}
	
	// required 
	public static String importOutputFolder2(Properties properties) throws NoParameterException {
		if(properties.containsKey("output_folder")){
			return properties.getProperty("output_folder")+"/";
		}
		throw new NoParameterException("output_folder");
	}
	
	// required 
	public static String importOutputFolder3(Properties properties) throws NoParameterException {
		if(properties.containsKey("output_folder")){
			return properties.getProperty("output_folder")+"/";
		}
		throw new NoParameterException("output_folder");
	}
	
	// required 
	/*
	public static String importOutputName(Properties properties) throws NoParameterException {
		if(properties.containsKey("output_name")){
			return properties.getProperty("output_name");
		}
		throw new NoParameterException("output_name");
	}
	*/
	
	// required 
	public static String importOutputAscii(Properties properties) throws NoParameterException {
		if(properties.containsKey("output_asc")){
			return properties.getProperty("output_asc");
		}
		throw new NoParameterException("output_asc");
	}
	
	// required 
	public static String importOutputAscii2(Properties properties) throws NoParameterException {
		if(properties.containsKey("output_asc")){
			return properties.getProperty("output_asc");
		}
		throw new NoParameterException("output_asc");
	}
	
	// required 
	public static String importOutputAscii3(Properties properties) throws NoParameterException {
		if(properties.containsKey("output_asc")){
			return properties.getProperty("output_asc");
		}
		throw new NoParameterException("output_asc");
	}
		
	// required 
	public static String importOutputCsv(Properties properties) throws NoParameterException {
		if(properties.containsKey("output_csv")){
			return properties.getProperty("output_csv");
		}
		throw new NoParameterException("output_csv");
	}
	
	// required
	public static boolean importVisualizeAscii(Properties properties) throws NoParameterException {
		if(properties.containsKey("visualize_ascii")){
			return Boolean.parseBoolean(properties.getProperty("visualize_ascii"));
		}
		throw new NoParameterException("visualize_ascii");
	}
	
	// required
	public static boolean importVisualizeAscii2(Properties properties) throws NoParameterException {
		if(properties.containsKey("visualize_ascii")){
			return Boolean.parseBoolean(properties.getProperty("visualize_ascii"));
		}
		throw new NoParameterException("visualize_ascii");
	}
	
	// required
	public static boolean importVisualizeAscii3(Properties properties) throws NoParameterException {
		if(properties.containsKey("visualize_ascii")){
			return Boolean.parseBoolean(properties.getProperty("visualize_ascii"));
		}
		throw new NoParameterException("visualize_ascii");
	}
	
	// required
	public static Set<String> importInputShapefile(Properties properties) throws NoParameterException {
		if(properties.containsKey("input_shapefile")){
			String prop = properties.getProperty("input_shapefile");
			File f = new File(prop);
			Set<String> shapes = new HashSet<String>();
			if(f.isDirectory()){
				for(String c : f.list()){
					if(c.endsWith(".shp")){
						shapes.add(f+"/"+c);
					}
				}
			}else{
				shapes.add(prop);
			}
			return shapes;
		}
		throw new NoParameterException("input_shapefile");
	}
	
	// required
	public static String importAttribute(Properties properties) throws NoParameterException {
		if(properties.containsKey("attribute")){
			return properties.getProperty("attribute");
		}
		throw new NoParameterException("attribute");
	}
	
	// not required 
	public static String importLookupTable(Properties properties) throws NoParameterException {
		if(properties.containsKey("lookup_table")){
			return properties.getProperty("lookup_table");
		}
		return "";
	}
	
	public static String importDistanceFunction(Properties properties) throws NoParameterException {
		if(properties.containsKey("distance_function")){
			return properties.getProperty("distance_function");
		}
		return "";
	}
	
	// required 
	public static Set<Double> importCellSizes(Properties properties) throws NoParameterException {
		if(properties.containsKey("cellsizes")){
			String prop = properties.getProperty("cellsizes");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ws = prop.split(";");
			Set<Double> cellsizes = new TreeSet<Double>();
			for(String w : ws){
				cellsizes.add(Double.parseDouble(w));
			}
			return cellsizes;
		}
		throw new NoParameterException("cellsizes");
	}
	
	// not required 
	public static Double importMinX(Properties properties) throws NoParameterException {
		if(properties.containsKey("minx")){
			return new Double(Double.parseDouble(properties.getProperty("minx")));
		}
		return null;
	}
	
	// not required 
	public static Double importMaxX(Properties properties) throws NoParameterException {
		if(properties.containsKey("maxx")){
			return new Double(Double.parseDouble(properties.getProperty("maxx")));
		}
		return null;	
	}
	
	//not required 
	public static Double importMinY(Properties properties) throws NoParameterException {
		if(properties.containsKey("miny")){
			return new Double(Double.parseDouble(properties.getProperty("miny")));
		}
		return null;	
	}
	
	// not required 
	public static Double importMaxY(Properties properties) throws NoParameterException {
		if(properties.containsKey("maxy")){
			return new Double(Double.parseDouble(properties.getProperty("maxy")));
		}else{
			return null;
		}	
	}
	
	// required
	public static Set<String> importInputAsciiGrid(Properties properties) throws NoParameterException {
		if(properties.containsKey("input_ascii")){
			String prop = properties.getProperty("input_ascii");
			File f = new File(prop);
			Set<String> asciis = new HashSet<String>();
			if(f.isDirectory()){
				for(String c : f.list()){
					if(c.endsWith(".asc")){
						asciis.add(f+"/"+c);
					}
				}
			}else{
				asciis.add(prop);
			}
			return asciis;
		}
		throw new NoParameterException("input_ascii");
	}
	
	// required 
	public static Set<Matrix> importInputMatrix(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("input_ascii")){
			String prop = properties.getProperty("input_ascii");
			File f = new File(prop);
			Set<Matrix> matrix = new HashSet<Matrix>();
			if(f.isDirectory()){
				for(String c : f.list()){
					if(c.endsWith(".asc")){
						model.importAsciiGrid(matrix, f+"/"+c, false);
					}
				}
			}else{
				model.importAsciiGrid(matrix, prop, false);
			}
			return matrix;
		}
		throw new NoParameterException("input_ascii");
	}
	
	/*
	// required 
	public static List<Matrix> importFactorsMatrix(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("factors")){
			String prop = properties.getProperty("factors").replace("{", "").replace("}", "");
			String[] ds = prop.split(";");
			List<Matrix> matrix = new ArrayList<Matrix>();
			for(String d : ds){
				d = d.replace("(", "").replace(")", "");
				String[] dd = d.split(",");
				model.importAsciiGrid(matrix, dd[0], false);
			}
			return matrix;
		}
		throw new NoParameterException("factors");
	}
	*/
	
	// required 
	public static List<String> importFactorsAsciis(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("factors")){
			String prop = properties.getProperty("factors").replace("{", "").replace("}", "");
			String[] ds = prop.split(";");
			List<String> asciis = new ArrayList<String>();
			for(String d : ds){
				d = d.replace("(", "").replace(")", "");
				String[] dd = d.split(",");
				//model.importAsciiGrid(matrix, dd[0], false);
				asciis.add(dd[0]);
			}
			return asciis;
		}
		throw new NoParameterException("factors");
	}
	
	// required 
	public static List<String> importFactorsNames(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("factors")){
			String prop = properties.getProperty("factors").replace("{", "").replace("}", "");
			String[] ds = prop.split(";");
			List<String> names = new ArrayList<String>();
			for(String d : ds){
				d = d.replace("(", "").replace(")", "");
				String[] dd = d.split(",");
				names.add(dd[1]);
			}
			return names;
		}
		throw new NoParameterException("factors");
	}
	
	// required 
	private static String importCombination(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("combination")){
			String combination = properties.getProperty("combination");
			return combination;
		}
		throw new NoParameterException("combination");
	}
	
	// required 
	public static Map<String, String> importChanges(Properties properties) throws NoParameterException {
		if(properties.containsKey("changes")){
			String prop = properties.getProperty("changes").replace("{", "").replace("}", "");
			String[] cc = prop.split(";");
			String[] vv;
			Map<String, String> changes = new HashMap<String, String>();
			for(String c : cc){
				c = c.replace("(", "").replace(")", "");
				vv = c.split(",");
				changes.put(vv[0]+"", vv[1]+"");
			}
			return changes;
		}
		throw new NoParameterException("changes");
	}
	
	/*
	// required 
	public static List<Matrix> importOverlayingMatrix(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("overlaying_matrix")){
			String prop = properties.getProperty("overlaying_matrix");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			List<Matrix> inputMatrix = new ArrayList<Matrix>();
			for(String m : ms){
				model.importAsciiGrid(inputMatrix, m, false);
			}
			return inputMatrix;
		}
		throw new NoParameterException("overlaying_matrix");
	}
	*/
	
	// required 
	public static List<String> importOverlayingAsciis(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("overlaying_matrix")){
			String prop = properties.getProperty("overlaying_matrix");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			List<String> asciis = new ArrayList<String>();
			for(String m : ms){
				//model.importAsciiGrid(inputMatrix, m, false);
				asciis.add(m);
			}
			return asciis;
		}
		throw new NoParameterException("overlaying_matrix");
	}
	
	// required
	public static Set<Integer> importDistances(Properties properties) throws NoParameterException {
		if(properties.containsKey("distance_from")){
			String prop = properties.getProperty("distance_from").replace("{", "").replace("}", "");
			String[] df = prop.split(";");
			Set<Integer> distances = new HashSet<Integer>();
			for(String d : df){
				distances.add(Integer.parseInt(d));
			}
			return distances;
		}
		throw new NoParameterException("distance_from");
	}

	
	// not required
	public static String importDistanceType(Properties properties) throws NoParameterException {
		if(properties.containsKey("distance_type")){
			return properties.getProperty("distance_type");
		}
		return "euclidian";
	}
	
	// not required
	public static double importMaxDistance(Properties properties) throws NoParameterException {
		if(properties.containsKey("max_distance")){
			return Double.parseDouble(properties.getProperty("max_distance"));
		}
		return Raster.getNoDataValue();
	}
	
	// not required
	public static Friction importDistanceFriction(Properties properties) throws NoParameterException {
		if(properties.containsKey("distance_friction")){
			String prop = properties.getProperty("distance_friction");
			if(!prop.endsWith(".asc")){
				return new Friction(prop);
			}
		}
		return null;
	}
	
	// not required
	public static Matrix importDistanceFrictionMatrix(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("distance_friction_ascii")){
			String prop = properties.getProperty("distance_friction_ascii");
			Matrix frictionMatrix = null;
			frictionMatrix = model.importAsciiGrid(frictionMatrix, prop, false);
			return frictionMatrix;
		}
		if(properties.containsKey("distance_friction")){
			String prop = properties.getProperty("distance_friction");
			if(prop.endsWith(".asc")){
				Matrix frictionMatrix = null;
				frictionMatrix = model.importAsciiGrid(frictionMatrix, prop, false);
				return frictionMatrix;
			}
		}
		return null;
	}	
	
	// required
	public static Map<Domain<Double, Double>, Integer> importDomains(Properties properties) throws NoParameterException {
		if(properties.containsKey("domains")){
			String prop = properties.getProperty("domains").replace("{", "").replace("}", "");
			String[] ds = prop.split(";");
			Map<Domain<Double, Double>, Integer> domains = new HashMap<Domain<Double, Double>, Integer>();
			for(String d : ds){
				d = d.replace("(", "").replace(")", "");
				String[] dd = d.split("-");
				domains.put(ClassificationPanel.getDomain(dd[0]), Integer.parseInt(dd[1]));
			}
			return domains;
		}
		throw new NoParameterException("domains");
	}
	
	// required
	public static List<Integer> importCluster(Properties properties) throws NoParameterException {
		if(properties.containsKey("cluster")){
			String prop = properties.getProperty("cluster").replace("{", "").replace("}", "");
			String[] df = prop.split(";");
			List<Integer> clusters = new ArrayList<Integer>();
			for(String d : df){
				clusters.add(Integer.parseInt(d));
			}
			return clusters;
		}
		throw new NoParameterException("cluster");
	}
	
	// required
	public static String importClusterType(Properties properties) throws NoParameterException {
		if(properties.containsKey("cluster_type")){
			return properties.getProperty("cluster_type");
		}
		throw new NoParameterException("cluster_type");
	}
	
	// not required
	public static double importClusterDistance(Properties properties) throws NoParameterException {
		if(properties.containsKey("cluster_distance")){
			return Double.parseDouble(properties.getProperty("cluster_distance"));
		}
		return 0;
	}
	
	// not required
	private static double importMinimumTotalArea(Properties properties) {
		if(properties.containsKey("minimum_total_area")){
			return Double.parseDouble(properties.getProperty("minimum_total_area"));
		}
		return 0;
	}

	// not required
	public static Friction importClusterFriction(Properties properties) throws NoParameterException {
		if(properties.containsKey("cluster_friction")){
			String prop = properties.getProperty("cluster_friction");
			if(!prop.endsWith(".asc")){
				return new Friction(prop);
			}
		}
		return null;
	}
	
	// not required
	public static Matrix importClusterFrictionMatrix(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("cluster_friction_ascii")){
			String prop = properties.getProperty("cluster_friction_ascii");
			Matrix frictionMatrix = null;
			frictionMatrix = model.importAsciiGrid(frictionMatrix, prop, false);
			return frictionMatrix;
		}
		if(properties.containsKey("cluster_friction")){
			String prop = properties.getProperty("cluster_friction");
			if(prop.endsWith(".asc")){
				Matrix frictionMatrix = null;
				frictionMatrix = model.importAsciiGrid(frictionMatrix, prop, false);
				return frictionMatrix;
			}
		}
		return null;
	}
	
	// required
	public static Matrix importFilterAscii(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("ascii_filter")){
			String prop = properties.getProperty("ascii_filter");
			Matrix filterMatrix = null;
			filterMatrix = model.importAsciiGrid(filterMatrix, prop, false);
			return filterMatrix;
		}
		throw new NoParameterException("ascii_filter");
	}
	
	// required
	public static Set<Integer> importFilterValues(Properties properties) throws NoParameterException {
		if(properties.containsKey("filter_values")){
			String prop = properties.getProperty("filter_values").replace("{", "").replace("}", "");
			String[] df = prop.split(";");
			Set<Integer> filterValues = new HashSet<Integer>();
			for(String d : df){
				filterValues.add(Integer.parseInt(d));
			}
			return filterValues;
		}
		throw new NoParameterException("filter_values");
	}
	
	// required
	public static Set<String> importMetrics(Properties properties) throws NoParameterException {
		if(properties.containsKey("metrics")){
			String prop = properties.getProperty("metrics");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			Set<String> metrics = new TreeSet<String>();
			for(String m : ms){
				metrics.add(m);
			}
			return metrics;
		}
		throw new NoParameterException("metrics");
	}
	
	// required
	public static List<Integer> importGridSizes(Properties properties) throws NoParameterException {
		if(properties.containsKey("grid_sizes")){
			String prop = properties.getProperty("grid_sizes");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ws = prop.split(";");
			List<Integer> gridSizes = new ArrayList<Integer>();
			for(String w : ws){
				gridSizes.add(Integer.parseInt(w));
			}
			return gridSizes;
		}
		throw new NoParameterException("grid_sizes");
	}
	
	// required
	public static double importMaximumNoValueRate(Properties properties) throws NoParameterException {
		if(properties.containsKey("maximum_nodata_value_rate")){
			return 1.0-((Integer.parseInt(properties.getProperty("maximum_nodata_value_rate")))/100.0);
		}
		throw new NoParameterException("maximum_nodata_value_rate");
	}
	
	// required
	public static boolean importExportCsv(Properties properties) throws NoParameterException {
		if(properties.containsKey("export_csv")){
			return Boolean.parseBoolean(properties.getProperty("export_csv"));
		}
		throw new NoParameterException("export_csv");
	}

	// required
	public static boolean importExportAscii(Properties properties) throws NoParameterException {
		if(properties.containsKey("export_ascii")){
			return Boolean.parseBoolean(properties.getProperty("export_ascii"));
		}
		throw new NoParameterException("export_ascii");
	}
	
	// required
	public static WindowShapeType importShape(Properties properties) throws NoParameterException {
		if(properties.containsKey("shape")){
			return WindowShapeType.get(properties.getProperty("shape"));
		}
		throw new NoParameterException("shape");
	}
	
	// not required
	public static Friction importFriction(Properties properties) throws NoParameterException {
		if(properties.containsKey("friction")){
			String prop = properties.getProperty("friction");
			if(!prop.endsWith(".asc")){
				return  new Friction(prop);
			}
		}
		return null;
	}
	
	// not required
	public static Matrix importFrictionMatrix(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("friction_ascii")){
			String prop = properties.getProperty("friction_ascii");
			Matrix frictionMatrix = null;
			frictionMatrix = model.importAsciiGrid(frictionMatrix, prop, false);
			return frictionMatrix;
		}
		if(properties.containsKey("friction")){
			String prop = properties.getProperty("friction");
			if(prop.endsWith(".asc")){
				Matrix frictionMatrix = null;
				frictionMatrix = model.importAsciiGrid(frictionMatrix, prop, false);
				return frictionMatrix;
			}
		}
		return null;
	}
	
	// required
	public static List<Integer> importWindowSizes(Properties properties) throws NoParameterException {
		if(properties.containsKey("window_sizes")){
			String prop = properties.getProperty("window_sizes");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ws = prop.split(";");
			List<Integer> windowSizes = new ArrayList<Integer>();
			for(String w : ws){
				windowSizes.add(Integer.parseInt(w));
			}
			return windowSizes;
		}
		throw new NoParameterException("window_sizes");
	}
	
	// required
	public static int importDeltaDisplacement(Properties properties) throws NoParameterException {
		if(properties.containsKey("delta_displacement")){
			return Integer.parseInt(properties.getProperty("delta_displacement"));
		}
		throw new NoParameterException("delta_displacement");
	}
	
	// not required
	public static int importXOrigin(Properties properties) throws NoParameterException {
		if(properties.containsKey("x_origin")){
			return Integer.parseInt(properties.getProperty("x_origin"));
		}
		return 0;
	}
	
	// not required
	public static int importYOrigin(Properties properties) throws NoParameterException {
		if(properties.containsKey("y_origin")){
			return Integer.parseInt(properties.getProperty("y_origin"));
		}
		return 0;
	}
	
	// not required
	public static boolean importInterpolation(Properties properties) throws NoParameterException {
		if(properties.containsKey("interpolation")){
			return Boolean.parseBoolean(properties.getProperty("interpolation"));
		}
		return false;
	}
	
	// not required
	public static Set<Integer> importFilters(Properties properties) throws NoParameterException {
		Set<Integer> filters = new HashSet<Integer>();
		if(properties.containsKey("filters")){	
			String prop = properties.getProperty("filters");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] fs = prop.split(";");
			for(String f : fs){
				filters.add(Integer.parseInt(f));
			}
		}
		return filters;
	}

	// not required
	public static Set<Integer> importUnfilters(Properties properties) throws NoParameterException {
		Set<Integer> unfilters = new HashSet<Integer>();
		if(properties.containsKey("unfilters")){
			String prop = properties.getProperty("unfilters");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] fs = prop.split(";");
			for(String f : fs){
				unfilters.add(Integer.parseInt(f));
			}
		}
		return unfilters;
	}
	
	// complex
	public static Set<Pixel> importPixels(Set<Matrix> matrix, Properties properties) throws NoParameterException {
		Set<Pixel> pixels = new HashSet<Pixel>();
		if(properties.containsKey("pixels")){
			String prop = properties.getProperty("pixels");
			pixels = CoordinateManager.initWithPixels(prop);
		}else if(properties.containsKey("number_generated_pixels")){
			String prop = properties.getProperty("pixels");
			int n = Integer.parseInt(prop);
			int distance = -1;
			if(properties.containsKey("minimum_distance")){
				prop = properties.getProperty("minimum_distance");
				distance = Integer.parseInt(prop);
			}
			
			Set<Integer> with = null;
			if(properties.containsKey("filters")){	
				with = new HashSet<Integer>();
				prop = properties.getProperty("filters");
				prop = prop.replace("{", "").replace("}", "").replace(" ", "");
				String[] fs = prop.split(";");
				for(String f : fs){
					with.add(Integer.parseInt(f));
				}
			}
			
			Set<Integer> without = null;
			if(properties.containsKey("unfilters")){
				without = new HashSet<Integer>();
				prop = properties.getProperty("unfilters");
				prop = prop.replace("{", "").replace("}", "").replace(" ", "");
				String[] fs = prop.split(";");
				for(String f : fs){
					without.add(Integer.parseInt(f));
				}
			}
			
			try{
				CoordinateManager.dispatch(pixels, matrix.iterator().next(), n, distance, with, without);
			}catch(IllegalArgumentException ex){
				ex.printStackTrace();
			}
		}
		
		if(pixels.size() == 0){
			pixels = null;
		}
		
		return pixels;
	}
	
	public static Set<RefPoint> importPoints(Properties properties) throws NoParameterException {
		if(properties.containsKey("points")){
			String prop = properties.getProperty("points");
			return CoordinateManager.initWithPoints(prop);
		}
		return null;
	}
	
}
