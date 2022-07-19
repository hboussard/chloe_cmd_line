package fr.inra.sad.bagap.chloe.view.wizard;

import java.util.List;

public class NullWizardPanel extends WizardPanel {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void display() {}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public boolean validateNext(List<String> list) {
		return true;
	}

	@Override
	public WizardPanel next(Wizard wizard) {
		return null;
	}

	@Override
	public boolean hasPrevious() {
		return true;
	}

	@Override
	public boolean canRun() {
		return false;
	}

	@Override
	public boolean validateRun(List<String> list) {
		return false;
	}

	@Override
	public void run() {}

}
