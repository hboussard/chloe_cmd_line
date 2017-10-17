package fr.inra.sad_paysage.chloe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShapeType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.domain.Domain;
import fr.inra.sad_paysage.chloe.controller.Controller;
import fr.inra.sad_paysage.chloe.controller.LocalContext;
import fr.inra.sad_paysage.chloe.model.Model;
import fr.inra.sad_paysage.chloe.view.Ihm;
import fr.inra.sad_paysage.chloe.view.LogoFrame;
import fr.inra.sad_paysage.chloe.view.treatment.ClassificationPanel;
import fr.inra.sad_paysage.chloe.view.treatment.TreatmentPanel;

public class Main {

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
			System.out.println("Chloe2012 - INRA - SAD - BAGAP");
			
			Locale.setDefault(Locale.US);
			LocalContext.load(); // chargement du context local
			
			Ihm ihm = new Ihm(new ServerSocket(new Integer(socket)));
			Model model = new Model();
			new Controller(ihm, model);
			
			ihm.getFrame();
		}catch(Exception e){
			System.out.println("Chloe2012 is already open !!!");
		}
		finally{
			launch.dispose();
		}
	}
	
	private static void launchBatch(String file){
		Model model = new Model();
		try{
			Properties properties = new Properties();
			FileInputStream in = new FileInputStream(file);
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
				//case "combine" : launchCombine(model, properties); break; // a developper
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
		System.out.println("appel de la fonction launchFromCsv()");
		try {
			String inputCsv = importInputCsv(properties);
			Set<String> variables = importVariables(properties);
			int ncols = importNCols(properties);
			int nrows = importNRows(properties);
			double xllcorner = importXllCorner(properties);
			double yllcorner = importYllCorner(properties);
			double cellsize = importCellSize(properties);
			int nodatavalue = importNoDataValue(properties);
			String folder = importOutputFolder(properties);
			boolean viewAscii = importVisualizeAscii(properties);
			
			model.exportMapFromCsv(inputCsv, folder, variables, ncols, nrows, xllcorner, yllcorner, cellsize, nodatavalue, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchFromShapefile(Model model, Properties properties) {
		System.out.println("appel de la fonction launchFromShapefile()");
		try {
			Set<String> layers = importInputShapefile(properties);
			String attribute = importAttribute(properties);
			String lookupTable = importLookupTable(properties);
			Set<Double> cellsizes = importCellSizes(properties);
			String folder = importOutputFolder(properties);
			boolean viewAscii = importVisualizeAscii(properties);
			Double minx = importMinX(properties);
			Double maxx = importMaxX(properties);
			Double miny = importMinY(properties);
			Double maxy = importMaxY(properties);
			
			model.exportAsciiGridFromShapefile(layers, attribute, lookupTable, cellsizes, folder, viewAscii, minx, maxx, miny, maxy);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchSearchAndReplace(Model model, Properties properties) {
		System.out.println("appel de la fonction launchSearchAndReplace()");
		try {
			Set<String> asciis = importInputAscii(properties);
			int nodatavalue = importNoDataValue(properties);
			Map<Integer, Number> changes = importChanges(properties);
			String folder = importOutputFolder(properties);
			boolean viewAscii = importVisualizeAscii(properties);
			
			model.runSearchAndReplace(asciis, nodatavalue, changes, folder, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchOverlay(Model model, Properties properties) {
		System.out.println("appel de la fonction launchOverlay()");
		try {
			List<Matrix> matrix = importOverlayingMatrix(model, properties);
			String folder = importOutputFolder(properties);
			boolean viewAscii = importVisualizeAscii(properties);
			
			model.runOverlay(matrix, folder, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchDistance(Model model, Properties properties) {
		System.out.println("appel de la fonction launchDistance()");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			Set<Integer> distances = importDistances(properties);
			String folder = importOutputFolder(properties);
			boolean viewAscii = importVisualizeAscii(properties);
			
			model.runDistance(matrix, distances, folder, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchClassification(Model model, Properties properties) {
		System.out.println("appel de la fonction launchClassification()");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			Map<Domain<Double, Double>, Integer> domains = importDomains(properties);
			String folder = importOutputFolder(properties);
			boolean viewAscii = importVisualizeAscii(properties);
			
			model.runClassification(matrix, domains, folder, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchCluster(Model model, Properties properties) {
		System.out.println("appel de la fonction launchCluster()");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			Set<Integer> clusters = importCluster(properties);
			String typeCluster = importClusterType(properties);
			double distance = importClusterDistance(properties);
			Friction friction = importClusterFriction(properties);
			Matrix frictionMatrix =  importClusterFrictionMatrix(model, properties);
			String folder = importOutputFolder(properties);
			boolean viewAscii = importVisualizeAscii(properties);
			
			model.runCluster(matrix, clusters, typeCluster, distance, friction, frictionMatrix, folder, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchFilter(Model model, Properties properties) {
		System.out.println("appel de la fonction launchFilter()");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			Matrix filterMatrix = importFilterAscii(model, properties);
			Set<Integer> filterValues = importFilterValues(properties);
			String folder = importOutputFolder(properties);
			boolean viewAscii = importVisualizeAscii(properties);
			
			model.runFilter(matrix, filterMatrix, filterValues, folder, viewAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchMap(Model model, Properties properties) {
		System.out.println("appel de la fonction launchMap()");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			Set<String> metrics = importMetrics(properties);
			String csvOutput = importCsvOutput(properties);
			
			model.runMapWindow(matrix, metrics, csvOutput);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchGrid(Model model, Properties properties) {
		System.out.println("appel de la fonction launchGrid()");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			List<Integer> gridSizes = importGridSizes(properties);
			double minRate = importMaximumNoValueRate(properties);
			Set<String> metrics = importMetrics(properties);
			String folder = importOutputFolder(properties);
			boolean viewAscii = importVisualizeAscii(properties);
			boolean exportCsv = importExportCsv(properties);
			boolean exportAscii = importExportAscii(properties);
			
			model.runGridWindow(matrix, gridSizes, minRate, metrics, folder, viewAscii, exportCsv, exportAscii);
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchSliding(Model model, Properties properties) {
		System.out.println("appel de la fonction launchGrid()");
		try {
			Set<Matrix> matrix = importInputMatrix(model, properties);
			WindowShapeType shape = importShape(properties);
			String friction = importFriction(properties);
			Matrix frictionMatrix = importFrictionMatrix(model, properties);
			//List<Integer> windowSizes = importWindowSizes(properties);
			double minRate = importMaximumNoValueRate(properties);
			//int delta = importDeltaDisplacement(properties);
			Set<String> metrics = importMetrics(properties);
			String folder = importOutputFolder(properties);
			boolean viewAscii = importVisualizeAscii(properties);
			boolean exportCsv = importExportCsv(properties);
			boolean exportAscii = importExportAscii(properties);
			//Set<Integer> filters = importFilters(properties);
			//Set<Integer> unfilters = importUnfilters(properties);
			
			//model.runSlidingWindow(matrix, shape, friction, frictionMatrix, windowSizes, delta, interpolate, minRate, metrics, folder, viewAscii, exportCsv, exportAscii, filters, unfilters)
		} catch (NoParameterException e) {
			e.printStackTrace();
		}
	}

	private static void launchSelected(Model model, Properties properties) {
		// TODO Auto-generated method stub
		
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
		if(properties.containsKey("cell_sizes")){
			return Double.parseDouble(properties.getProperty("cell_sizes"));
		}
		throw new NoParameterException("cell_sizes");
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
	public static boolean importVisualizeAscii(Properties properties) throws NoParameterException {
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
	
	// required 
	public static Set<Double> importCellSizes(Properties properties) throws NoParameterException {
		if(properties.containsKey("cell_sizes")){
			String prop = properties.getProperty("cell_sizes");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ws = prop.split(";");
			Set<Double> cellsizes = new TreeSet<Double>();
			for(String w : ws){
				cellsizes.add(Double.parseDouble(w));
			}
			return cellsizes;
		}
		throw new NoParameterException("cell_sizes");
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
	public static Set<String> importInputAscii(Properties properties) throws NoParameterException {
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
						model.importAsciiGrid(matrix, f+"/"+c);
					}
				}
			}else{
				model.importAsciiGrid(matrix, prop);
			}
			return matrix;
		}
		throw new NoParameterException("input_ascii");
	}
	
	// required 
	public static Map<Integer, Number> importChanges(Properties properties) throws NoParameterException {
		if(properties.containsKey("changes")){
			String prop = properties.getProperty("changes").replace("{", "").replace("}", "");
			String[] cc = prop.split(";");
			String[] vv;
			Map<Integer, Number> changes = new HashMap<Integer, Number>();
			for(String c : cc){
				c = c.replace("(", "").replace(")", "");
				vv = c.split(",");
				changes.put(Integer.parseInt(vv[0]), Double.parseDouble(vv[1]));
			}
			return changes;
		}
		throw new NoParameterException("changes");
	}
	
	// required 
	public static List<Matrix> importOverlayingMatrix(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("overlaying_matrix")){
			String prop = properties.getProperty("overlaying_matrix");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			List<Matrix> inputMatrix = new ArrayList<Matrix>();
			for(String m : ms){
				model.importAsciiGrid(inputMatrix, m);
			}
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
	public static Set<Integer> importCluster(Properties properties) throws NoParameterException {
		if(properties.containsKey("cluster")){
			String prop = properties.getProperty("cluster").replace("{", "").replace("}", "");
			String[] df = prop.split(";");
			Set<Integer> clusters = new HashSet<Integer>();
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
	public static Friction importClusterFriction(Properties properties) throws NoParameterException {
		if(properties.containsKey("cluster_friction")){
			return new Friction(properties.getProperty("cluster_friction"));
		}
		return null;
	}
	
	// not required
	public static Matrix importClusterFrictionMatrix(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("cluster_friction_ascii")){
			String prop = properties.getProperty("cluster_friction_ascii");
			Matrix frictionMatrix = null;
			model.importAsciiGrid(frictionMatrix, prop);
			return frictionMatrix;
		}
		return null;
	}
	
	// required
	public static Matrix importFilterAscii(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("ascii_filter")){
			String prop = properties.getProperty("ascii_filter");
			Matrix filterMatrix = null;
			model.importAsciiGrid(filterMatrix, prop);
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
	public static String importCsvOutput(Properties properties) throws NoParameterException {
		if(properties.containsKey("output_csv")){
			return properties.getProperty("output_csv");
		}
		throw new NoParameterException("output_csv");
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
	public static String importFriction(Properties properties) throws NoParameterException {
		if(properties.containsKey("friction")){
			return  properties.getProperty("friction");
		}
		return null;
	}
	
	// not required
	public static Matrix importFrictionMatrix(Model model, Properties properties) throws NoParameterException {
		if(properties.containsKey("friction_ascii")){
			String prop = properties.getProperty("friction_ascii");
			Matrix frictionMatrix = null;
			model.importAsciiGrid(frictionMatrix, prop);
			return frictionMatrix;
		}
		return null;
	}
	/*
	public static void importWindowSize(Properties properties) throws NoParameterException {
		if(properties.containsKey("window_size")){
			spSize.setValue(Integer.parseInt(properties.getProperty("window_size")));
		}
		throw new NoParameterException("input_shapefile");
	}
	
	public static void importWindowSizes(Properties properties) throws NoParameterException {
		for(; 0<tSize.getRowCount();){
			((DefaultTableModel) tSize.getModel()).removeRow(0);
		}
		if(properties.containsKey("window_sizes")){
			String prop = properties.getProperty("window_sizes");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ws = prop.split(";");
			Vector<Integer> vi;
			for(String w : ws){
				vi = new Vector<Integer>();
				vi.add(new Integer(w));
				((DefaultTableModel) tSize.getModel()).addRow(vi);
			}
		}
		throw new NoParameterException("input_shapefile");
	}
	
	public static void importGridSize(Properties properties) throws NoParameterException {
		if(properties.containsKey("grid_size")){
			spGSize.setValue(Integer.parseInt(properties.getProperty("grid_size")));
		}
		throw new NoParameterException("input_shapefile");
	}
	
	

	

	public static void importDeltaDisplacement(Properties properties) throws NoParameterException {
		if(properties.containsKey("delta_displacement")){
			int d = Integer.parseInt(properties.getProperty("delta_displacement"));
			spDelta.setValue(d);
			if(d > 1){
				if(properties.containsKey("interpolate_values")){
					cbInterpolate.setSelected(Boolean.parseBoolean(properties.getProperty("interpolate_values")));
				}
			}
		}
		throw new NoParameterException("input_shapefile");
	}
	
	public static void importPixels(Properties properties) throws NoParameterException {
		rbPixel.setSelected(false);
		if(properties.containsKey("pixels")){
			rbPixel.setSelected(true);
			taPixel.setText(properties.getProperty("pixels"));
			pixels = CoordinateManager.initWithPixels(taPixel.getText());
		}
		throw new NoParameterException("input_shapefile");
	}
	
	public static void importPoints(Properties properties) throws NoParameterException {
		rbPoint.setSelected(false);
		if(properties.containsKey("points")){
			rbPoint.setSelected(true);
			taPoint.setText(properties.getProperty("points"));
			pixels = CoordinateManager.initWithPoints(inputMatrix.iterator().next(), taPoint.getText());
		}
		throw new NoParameterException("input_shapefile");
	}
	
	public static void importGeneratedPixels(Properties properties) throws NoParameterException {
		rbGPixel.setSelected(false);
		if(properties.containsKey("number_generated_pixels")){
			rbGPixel.setSelected(true);
			spNPixel.setValue(new Integer(properties.getProperty("number_generated_pixels")));
			
			importMinimumDistance(properties);
			importFilters(properties);
			importUnfilters(properties);
			
			int distance = -1;
			if(cbMinDistance.isSelected()){
				distance = 0;
				if(cbMinDistance.isSelected()){
					distance = (Integer) spMinDistance.getValue();
				}
			}
			
			Set<Integer> with = null;
			if(cbCI.isSelected()){
				with = new HashSet<Integer>();
				for(int r : tCI.getSelectedRows()){
					with.add((Integer) tCI.getModel().getValueAt(r, 0));
				}
			}
			
			Set<Integer> without = null;
			if(cbCNI.isSelected()){
				without = new HashSet<Integer>();
				for(int r : tCNI.getSelectedRows()){
					without.add((Integer) tCNI.getModel().getValueAt(r, 0));
				}
			}
			
			try{				
				getController().generatePixels(pixels, inputMatrix.iterator().next(), (Integer) spNPixel.getValue(), distance, with, without);
			}catch(IllegalArgumentException ex){
				ex.printStackTrace();
			}
		}
		throw new NoParameterException("input_shapefile");
	}
	
	public static void importMinimumDistance(Properties properties) throws NoParameterException {
		cbMinDistance.setSelected(false);
		if(properties.containsKey("minimum_distance")){
			cbMinDistance.setSelected(true);
			spMinDistance.setValue(new Integer(properties.getProperty("minimum_distance")));
		}
		throw new NoParameterException("input_shapefile");
	}

	public static void importFilters(Properties properties) throws NoParameterException {
		cbF.setSelected(false);
		for(int r : tF.getSelectedRows()){
			tF.getSelectionModel().removeSelectionInterval(r, r);
		}
		if(properties.containsKey("filters")){	
			String prop = properties.getProperty("filters");
			cbF.setSelected(true);
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] fs = prop.split(";");
			Integer fi;
			for(String f : fs){
				fi = new Integer(f);
				for(int i=0; i<tF.getModel().getRowCount(); i++){
					if(tF.getValueAt(i, 0).equals(fi)){
						tF.getSelectionModel().addSelectionInterval(i, i);
					}	
				}
			}
		}
		throw new NoParameterException("input_shapefile");
	}

	public static void importUnfilters(Properties properties) throws NoParameterException {
		cbNF.setSelected(false);
		for(int r : tNF.getSelectedRows()){
			tNF.getSelectionModel().removeSelectionInterval(r, r);
		}
		if(properties.containsKey("unfilters")){
			String prop = properties.getProperty("unfilters");
			cbNF.setSelected(true);
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] fs = prop.split(";");
			Integer fi;
			for(String f : fs){
				fi = new Integer(f);
				for(int i=0; i<tNF.getModel().getRowCount(); i++){
					if(tNF.getValueAt(i, 0).equals(fi)){
						tNF.getSelectionModel().addSelectionInterval(i, i);
					}	
				}
			}
		}
		throw new NoParameterException("input_shapefile");
	}
	
	
	
	
	
	public static void importMultiScalesMetrics(Properties properties) throws NoParameterException {
		if(properties.containsKey("multi_scales_metrics")){
			String prop = properties.getProperty("multi_scales_metrics");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			Vector<String> vs;
			for(String m : ms){
				vs = new Vector<String>();
				vs.add(m);
				((DefaultTableModel) tCMSMetrics.getModel()).addRow(vs);
				for(int r=0; r<tLMSMetrics.getModel().getRowCount(); r++){
					if(((String) tLMSMetrics.getModel().getValueAt(r, 0)).equalsIgnoreCase(m)){
						((DefaultTableModel) tLMSMetrics.getModel()).removeRow(r);
						break;
					}
				}
			}
		}
		throw new NoParameterException("input_shapefile");
	}

	

	
	
	
	
	public static void importFactors(Properties properties) throws NoParameterException {
		inputMatrix3.clear();
		for(; 0<tFuzion.getRowCount();){
			((DefaultTableModel) tFuzion.getModel()).removeRow(0);
		}
		if(properties.containsKey("factors")){
			String prop = properties.getProperty("factors").replace("{", "").replace("}", "");
			String[] ds = prop.split(";");
			int r = 0;
			for(String d : ds){
				d = d.replace("(", "").replace(")", "");
				String[] dd = d.split(",");
				getController().importAsciiGrid3(this, inputMatrix3, dd[0]);
				((DefaultTableModel) tFuzion.getModel()).setValueAt(dd[1], r, 1);
				r++;
			}
		}
		throw new NoParameterException("input_shapefile");
	}
	
	
	*/
	
	
	
	
}
