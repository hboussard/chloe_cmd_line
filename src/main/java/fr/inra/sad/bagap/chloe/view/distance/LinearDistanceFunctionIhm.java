package fr.inra.sad.bagap.chloe.view.distance;

public class LinearDistanceFunctionIhm extends DistanceFunctionIhm {

	private double ib = 0; // borne inf�rieure
	
	private double vib = 0; // valeur borne inf�rieure
	
	private double sb = 150; // borne sup�rieure
	
	private double vsb = 1; // valeur borne sup�rieure
	
	public void setInferiorBoundary(double ib){
		this.ib = ib;
	}
	
	public void setSuperiorBoundary(double sb){
		this.sb = sb;
	}
	
	public void setValueInferiorBoundary(double vib){
		this.vib = vib;
	}
	
	public void setValueSuperiorBoundary(double vsb){
		this.vsb = vsb;
	}
	
	public double function(double x){
		double b = (ib*vsb - sb*vib) / (ib - sb);
		double a = (vsb - b) / sb;
		return a*x + b;
	}
	
}
