package fr.inra.sad.bagap.chloe.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import fr.inra.sad.bagap.apiland.analysis.matrix.MatrixCalculation;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.InterpolateLinearSplineAsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.FuzionMatrixCalculation;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.Pixel2PixelMatrixCalculation;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.basic.ValidValueMetric;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysisBuilder;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.FunctionalWindowWithMap;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.CenteredWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;

public class Script {

	public static String path = "c://Hugues/agents/colette/Donnees_COlette/raster/";
	
	public static String file1 = "c://Hugues/agents/jacques/colette/raster/OcSol_2013_500m_0005.asc";
	public static String file2 = "c://Hugues/agents/jacques/colette/raster/OcSol_2013_500m_5_test.asc";
	
	public static void main(String[] args){
		
	}
	
	

	private static void addition(){
		try {
			
			Matrix m1 = JaiMatrixFactory.get().createWithAsciiGridOld(path+"Boundary_Armorique_2013_0005.asc", false);
			Matrix m2 = JaiMatrixFactory.get().createWithAsciiGridOld(path+"OcSol_2013_500m_5.asc", false);
			
			Pixel2PixelMatrixCalculation ppm = new Pixel2PixelMatrixCalculation(m1, m2){
				@Override
				protected double treatPixel(Pixel p) {
					for(Matrix m : wholeMatrix()){
						double v = m.get(p);
						if(v != Raster.getNoDataValue()){
							return v;
						}
					}
					return Raster.getNoDataValue();
				}
			};
			Matrix m = ppm.allRun();
			MatrixManager.exportAsciiGrid(m, path+"raster/ocsol_boundary_5.asc");
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void read(String file){
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file2));
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line, sep;
			String[] s;
			
			bw.write(br.readLine());
			bw.newLine();
			bw.write(br.readLine());
			bw.newLine();
			bw.write(br.readLine());
			bw.newLine();
			bw.write(br.readLine());
			bw.newLine();
			bw.write(br.readLine());
			bw.newLine();
			bw.write(br.readLine());
			bw.newLine();
			
			
			int county = 0;
			while(br.ready()){
				int countx = 0;
				line = br.readLine();
				
				sep = " ";
				s = line.split(sep);
				for(String ss : s){
					if((ss.equals("-1") || ss.equals("1") || ss.equals("2") || ss.equals("3") || ss.equals("4") || ss.equals("5") || ss.equals("6") || ss.equals("7"))){
						bw.write(" "+ss);
						countx++;
					}else{
						bw.write(" 100");
						countx++;
					}
					
									
				}
				System.out.println(countx);
				county++;
				bw.newLine();
			}
			System.out.println(county);
			br.close();
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public static void fuzion(int m){	
		try {
			String path = "c://Hugues/agents/stephanie/agriconnect/rasterisation/"+m+"m/";
			Matrix bocage = JaiMatrixFactory.get().createWithAsciiGrid(path+"2012_haies_merge_00"+m+".asc");
			Matrix parcellaire = JaiMatrixFactory.get().createWithAsciiGrid(path+"2012_merge_00"+m+".asc");
			MatrixCalculation fuzion = new FuzionMatrixCalculation(bocage, parcellaire);
			fuzion.allRun();
			String output = path+"2012_parcellaire_00"+m+".asc";
			MatrixManager.exportAsciiGrid(fuzion.getResult(), output);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}














