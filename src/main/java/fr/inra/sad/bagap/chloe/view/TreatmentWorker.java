package fr.inra.sad.bagap.chloe.view;

import javax.swing.SwingWorker;

public abstract class TreatmentWorker extends SwingWorker<Boolean, String> {
	
	private Ihm ihm;
	
	public TreatmentWorker(Ihm ihm) {
		super();
		this.ihm = ihm;
    }
	
	@Override
	protected void done() {
		try {
			if(isCancelled()){
				publish("CANCEL : cancelled operation");
				return;
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void publish(String text){
		ihm.publish(text);
	}
	
	/*
	public void up(int progress){
		ihm.setProgress(progress);
	}
	*/
	
}
