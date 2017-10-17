package fr.inra.sad_paysage.chloe.view.treatment;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.util.Properties;
import fr.inra.sad_paysage.chloe.view.wizard.Wizard;

public class OverlayPanel extends TreatmentPanel {

	private static final long serialVersionUID = 1L;
	
	public OverlayPanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "overlay";
	}
	
	@Override
	protected void locateComponents(){
		title.setText("Overlay");
		
		c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		add(title, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		add(lMatrix, c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 4;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(pMatrix, c);
		
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.anchor = GridBagConstraints.CENTER;
		add(bMatrixAdd, c);
		
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		add(bMatrixRemove, c);
		
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		add(bMatrixUp, c);
		
		c.gridx = 2;
		c.gridy = 4;
		c.gridwidth = 1;
		add(bMatrixDown, c);

		c.gridx = 3;
		c.gridy = 1;
		add(bViewAscii, c);
		
		c.gridx = 2;
		c.gridy = 6;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		add(bOutputFolder, c);
		
		c.gridx = 0;
		c.gridy = 6;
		c.anchor = GridBagConstraints.LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 6;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taOutputFolder, c);
		
		c.gridx = 2;
		c.gridy = 6;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		add(bOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 7;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(viewAsciiOutput, c);
	}
	
	@Override
	public boolean validateRun(List<String> list) {
		boolean validate = true;
		
		if(taOutputFolder.getText().equalsIgnoreCase("")){
			 list.add("Please choose an ascci grid output matrix file");
			 validate = false;
		}
		
		return validate;
	}
	
	@Override
	public void doImport(Properties properties) {
		importOverlayingMatrix(properties);
		importOutputFolder(properties);
		importVisualizeAscii(properties);
	}
	
	@Override
	public void doExport(Properties properties){
		exportOverlayingMatrix(properties);
		exportOutputFolder(properties);
		exportVisualizeAscii(properties);
	}

	@Override
	public void run() {
		getController().runOverlay(inputMatrix2, taOutputFolder.getText(), viewAsciiOutput.isSelected());
	}

}
