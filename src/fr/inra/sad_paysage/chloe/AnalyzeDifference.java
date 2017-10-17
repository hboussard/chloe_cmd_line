package fr.inra.sad_paysage.chloe;

import java.io.IOException;

import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.Pixel2PixelMatrixCalculation;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;

public class AnalyzeDifference extends Pixel2PixelMatrixCalculation {

	private double exact;
	
	private double total;
	
	private double sommeE, sommeT;
	
	public AnalyzeDifference(Matrix... m){
		super(m);
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		double vi = matrix().get(p);
		if(vi != Raster.getNoDataValue()){
			total++;
			sommeT += vi;
			double vt = matrix(1).get(p);
			sommeE += vt;
			if(vt == vi){
				exact++;
				return 0;
			}
			return vt-vi;
		}
		return 0;
	}

	@Override
	protected void doInit() {
		exact = 0;
		total = 0;
		sommeE = 0;
		sommeT = 0;
	}

	@Override
	protected void doClose() {
		System.out.println("nombre de valeurs exactes = "+exact+" (sur "+total+") soit un taux d'exactitude de "+(exact*100/total)+"%");
		System.out.println("somme exacte = "+sommeE+" (contre "+sommeT+") soit un taux de corrélation globale de "+(sommeE*100/sommeT)+"%");
	}
	
	public static void main(String[] args){
		try {
			Matrix bonne = JaiMatrixFactory.get().createWithAsciiGrid("C:/Hugues/temp/chloe/outputs/raster2007_SHDI_0.asc");
			Matrix test = JaiMatrixFactory.get().createWithAsciiGrid("C:/Hugues/temp/chloe/outputs/raster2007_SHDI_2355.asc");
			Matrix diff = new AnalyzeDifference(bonne, test).allRun();
			MatrixManager.exportAsciiGridAndVisualize(diff, "C:/Hugues/temp/chloe/outputs/diff.asc");
			Matrix test2 = JaiMatrixFactory.get().createWithAsciiGrid("C:/Hugues/temp/chloe/outputs/raster2007_SHDI_false.asc");
			Matrix diff2 = new AnalyzeDifference(bonne, test2).allRun();
			MatrixManager.exportAsciiGridAndVisualize(diff2, "C:/Hugues/temp/chloe/outputs/diff2.asc");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
