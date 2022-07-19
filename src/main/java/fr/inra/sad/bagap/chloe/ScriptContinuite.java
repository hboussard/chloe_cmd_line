package fr.inra.sad.bagap.chloe;

import java.io.IOException;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;

public class ScriptContinuite {

	private static String path; 
	
	public static void main(String[] args){
		path = "C:/Hugues/modelisation/dreal/emprise_pays_rennes/";
		retileEmprise5();
	}
	
	private static void retileEmprise1(){
		try {
			Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+"data/pays-rennes/pays-rennes.asc", true);
			Matrix m2 = MatrixManager.retile(m, 6000, 4000, 9000, 9000);
			MatrixManager.exportAsciiGridAndVisualize(m2, path+"scot/emprise1/sous_pays-rennes.asc");
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void retileEmprise2(){
		try {
			Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+"scot/emprise1/sous_pays-rennes.asc", true);
			Matrix m2 = MatrixManager.retile(m, 2000, 2000, 1500, 1500);
			MatrixManager.exportAsciiGridAndVisualize(m2, path+"scot/emprise2/sous_pays-rennes.asc");
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void retileEmprise3(){
		try {
			Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+"data/pays-rennes/pays-rennes.asc", true);
			Matrix m2 = MatrixManager.retile(m, 5500, 2600, 10500, 10500);
			MatrixManager.exportAsciiGrid(m2, path+"scot/emprise3/sous_pays-rennes.asc");
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void retileEmprise5(){
		try {
			Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+"data/pays-rennes/pays-rennes.asc", true);
			//Matrix m2 = MatrixManager.retile(m, 330653.0800000129, 382058.04000002274, 6769731.350000001, 6818341.640000005);
			//Matrix m2 = MatrixManager.retile(m, 330653.0800000129, 6769731.350000001, 10280, 9722);
			Matrix m2 = MatrixManager.retile(m, 5931, 2931, 10280, 9722);
			
			MatrixManager.exportAsciiGrid(m2, path+"scot/emprise5/sous_pays-rennes7.asc");
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
