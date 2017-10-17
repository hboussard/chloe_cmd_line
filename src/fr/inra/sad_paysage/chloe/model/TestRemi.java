package fr.inra.sad_paysage.chloe.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.FuzionMatrixCalculation;
import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.element.DynamicLayer;
import fr.inra.sad.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.ArrayMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Time;

public class TestRemi {

	// Instant neutre
	private static Instant t = Time.get(1,1,2000);
	private static String folder = "/home/sad20/agents/rémi/data/";
	
	public static void main(String[] args) {
		decoupe();
	}
	
	public static void decoupe(){
		
		try {
			
			Matrix m1 = JaiMatrixFactory.get().createWithAsciiGrid("/home/sad20/agents/yannick/output5/BATIMENTS_&_MAJIC_024_0005.asc");
			Matrix m2 = JaiMatrixFactory.get().createWithAsciiGrid("/home/sad20/agents/yannick/output5/commune_24_L93_0005.asc");
			String out = "/home/sad20/agents/yannick/output5/majic_5.asc";
			BufferedWriter bw = new BufferedWriter(new FileWriter(out));

			bw.write("ncols " + m1.width());
			bw.newLine();
			bw.write("nrows " + m1.height());
			bw.newLine();
			bw.write("xllcorner " + m1.minX());
			bw.newLine();
			bw.write("yllcorner " + m1.minY());
			bw.newLine();
			bw.write("cellsize " + m1.cellsize());
			bw.newLine();
			bw.write("NODATA_value "+Raster.getNoDataValue());
			
			int y = -1;
			for(Pixel p : m1){
				if(p.y() != y){
					bw.newLine();
					y = p.y();
				}
				if(m2.get(p) == Raster.getNoDataValue()){
					bw.write(" "+Raster.getNoDataValue());
				}else{
					bw.write(" "+m1.get(p));
				}
			}
			
			bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void test3(){
		String shape = folder+"shape_haie/S14";
		DynamicLayer<DynamicElement> layer = DynamicLayerFactory.initWithShape(shape, t);
		Matrix matrix = ArrayMatrixFactory.get().initLines(layer, t, "raster2012", 5, -1, 20, 0);
		String raster = folder+"test/S14_test3.asc";
		String raster2 = folder+"test/S14_test3_bis.asc";
		MatrixManager.exportAsciiGrid(matrix, raster);
		MatrixManager.findAndReplace(raster, raster2, "20.0", "5");
		MatrixManager.visualize(raster2);
	}
	
	public static void test4(){
		String shape = folder+"shape_haie/S14";
		DynamicLayer<DynamicElement> layer = DynamicLayerFactory.initWithShape(shape, t);
		Matrix matrix = ArrayMatrixFactory.get().initLines(layer, t, "raster2012", 5, -1, 0, 20);
		String raster = folder+"test/S14_test4.asc";
		String raster2 = folder+"test/S14_test4_bis.asc";
		MatrixManager.exportAsciiGrid(matrix, raster);
		MatrixManager.findAndReplace(raster, raster2, "20.0", "5");
		MatrixManager.visualize(raster2);
	}
	
	public static void test1(){
		//String shape = folder+"shape_parcellaire/S19";
		//DynamicLayer<DynamicElement> layer = DynamicLayerFactory.initWithShape(shape, t);
		//Matrix matrix = ArrayMatrixFactory.get().initWithCentralPoint(layer, t, "raster2012", 5);
		//String raster = folder+"test/S19_test1.asc";
		//MatrixManager.exportAsciiGrid(matrix, raster);
	}
	
	public static void test2(){
		String shape = folder+"shape_parcellaire/S19";
		DynamicLayer<DynamicElement> layer = DynamicLayerFactory.initWithShape(shape, t);
		Matrix matrix = ArrayMatrixFactory.get().initWithMajorSurface(layer, t, "raster2012", 5);
		String raster = folder+"test/S19_test2.asc";
		MatrixManager.exportAsciiGrid(matrix, raster);
	}

	public static void rasterisationParcellaire(){
		
		// parcours du dossier
		File f = new File(folder+"shape_parcellaire/");
		//String shape;
		//String name;
		//DynamicLayer<DynamicElement> layer;
		//Matrix matrix;
		//String raster;
		for(File file : f.listFiles()){
			if(file.getName().endsWith(".shp")){
				//name = file.getName().replace(".shp", "");
				//shape = folder+"shape_parcellaire/"+name;
				//layer = DynamicLayerFactory.initWithShape(shape, t);
				//matrix = ArrayMatrixFactory.get().initWithCentralPoint(layer, t, "raster2012", 5);
				//raster = folder+"raster_parcellaire/"+name+".asc";
				//MatrixManager.exportAsciiGrid(matrix, raster);
				//MatrixManager.visualizeAsciiGrid(raster);
			}
		}
	}
	
	public static void rasterisationBocage(){
		
		// parcours du dossier
		File f = new File(folder+"shape_haie/");
		String shape;
		String name;
		DynamicLayer<DynamicElement> layer;
		Matrix matrix;
		String raster;
		for(File file : f.listFiles()){
			if(file.getName().endsWith(".shp")){
				name = file.getName().replace(".shp", "");
				shape = folder+"shape_haie/"+name;
				layer = DynamicLayerFactory.initWithShape(shape, t);
				matrix = ArrayMatrixFactory.get().initLines(layer, t, "raster2012", 5, -1, 20, 0);
				raster = folder+"raster_haie/"+name+".asc";
				MatrixManager.exportAsciiGrid(matrix, raster);
				//MatrixManager.visualizeAsciiGrid(raster);
			}
		}
	}
	
	public static void fusion(){
		
		// parcours du dossier
		File f = new File(folder+"raster_haie/");
		String name;
		Matrix bocage;
		Matrix parcellaire;
		FuzionMatrixCalculation fusion;
		String raster;
		for(File file : f.listFiles()){
			if(file.getName().endsWith(".asc")){
				name = file.getName();
				bocage = ArrayMatrixFactory.get().init(folder+"raster_haie/"+name);
				parcellaire = ArrayMatrixFactory.get().init(folder+"raster_parcellaire/"+name);
				fusion = new FuzionMatrixCalculation(bocage, parcellaire);
				fusion.allRun();
				raster = folder+"raster_fusion/"+name;
				MatrixManager.exportAsciiGrid(fusion.getResult(), raster);
				//MatrixManager.visualizeAsciiGrid(raster);
			}
		}
	}
	
}
