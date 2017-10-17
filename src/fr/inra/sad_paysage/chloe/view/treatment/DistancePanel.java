package fr.inra.sad_paysage.chloe.view.treatment;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import fr.inra.sad_paysage.chloe.view.wizard.Wizard;

public class DistancePanel extends TreatmentPanel {

	private static final long serialVersionUID = 1L;
	
	public DistancePanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "distance";
	}
	
	@Override
	protected void locateComponents(){
		title.setText("Distance");
		
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
		add(lAsciiInput, c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taAsciiInput, c);
		
		c.gridx = 2;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		add(bAsciiInput, c);

		c.gridx = 3;
		c.gridy = 1;
		add(bViewAsciiInput, c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lDistances, c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		add(pDistances, c);
		
		c.gridx = 0;
		c.gridy = 5;
		c.anchor = GridBagConstraints.LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taOutputFolder, c);
		
		c.gridx = 2;
		c.gridy = 5;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		add(bOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 6;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(viewAsciiOutput, c);
		
	}
	
	@Override
	public boolean validateRun(List<String> list) {
		boolean validate = true;
		
		vDistances = new HashSet<Integer>();
		for(int r : tDistances.getSelectedRows()){
			vDistances.add((Integer) tDistances.getModel().getValueAt(r, 0));
		}
		
		if(taOutputFolder.getText().equalsIgnoreCase("")){
			 list.add("Please choose an ascci grid output matrix file");
			 validate = false;
		}
		
		return validate;
	}
	
	@Override
	public void doImport(Properties properties) {
		importInputAscii(properties);
		importDistances(properties);
		importOutputFolder(properties);
		importVisualizeAscii(properties);
	}
	
	@Override
	public void doExport(Properties properties){
		exportInputAscii(properties);
		exportDistances(properties);
		exportOutputFolder(properties);
		exportVisualizeAscii(properties);
	}

	@Override
	public void run() {
		getController().runDistance(inputMatrix, vDistances, taOutputFolder.getText(), viewAsciiOutput.isSelected());
	}

}
