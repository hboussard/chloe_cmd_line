package fr.inra.sad.bagap.chloe.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

//import fr.inra.sad.bagap.apiland.analysis.matrix.RCMDistanceCalculationTest;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;

public class ScriptTest {

	private static String path = "";
	
	public static void main(String[] args){
		diffusion();
	}
	
	private static void diffusion(){
		/*try {
			Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+"oso.asc", false);
			Matrix frictionMatrix = JaiMatrixFactory.get().createWithAsciiGridOld(path+"oso.asc", false);
			
			Set<Integer> values = new HashSet<Integer>();
			values.add(1);
			
			RCMDistanceCalculationTest rcm = new RCMDistanceCalculationTest(m, frictionMatrix, values, -1);
			Matrix mDistance = rcm.allRun();
			
			MatrixManager.exportAsciiGrid(mDistance, path+"export_test.asc");
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}*/
	}
	
}
