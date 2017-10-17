package fr.inra.sad_paysage.chloe;

public class NoParameterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NoParameterException(String parameter){
		super(parameter);
	}
	
}
