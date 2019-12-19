package fr.inra.sad.bagap.chloe.view.treatment;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import fr.inra.sad.bagap.chloe.view.wizard.Wizard;

public class CombinePanel extends TreatmentPanel {

	private static final long serialVersionUID = 1L;
	
	private List<String> names = new ArrayList<String>();
	
	private String formula;
	
	public CombinePanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "combine";
	}
	
	@Override
	protected void locateComponents(){
		title.setText("Combine");
		
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
		add(lFuzion, c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 4;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(pFuzion, c);
		
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.CENTER;
		add(bFuzion, c);
		
		c.gridx = 2;
		c.gridy = 2;
		add(bViewFuzionAscii, c);
		
		c.gridx = 2;
		c.gridy = 3;
		add(bRemoveFuzionAscii, c);
		
		c.gridx = 0;
		c.gridy = 6;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lCombination, c);
		
		c.gridx = 1;
		c.gridy = 6;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(taCombination, c);
		
		c.gridx = 0;
		c.gridy = 8;
		c.gridheight = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 8;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taOutputFolder3, c);
		
		c.gridx = 2;
		c.gridy = 8;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		add(bOutputFolder3, c);
		
		c.gridx = 1;
		c.gridy = 9;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(viewAsciiOutput3, c);
	}
	
	@Override
	public boolean validateRun(List<String> list) {
		boolean validate = true;
		
		names.clear();
		for(int i=0; i<tFuzion.getRowCount(); i++){
			names.add((String) tFuzion.getModel().getValueAt(i, 1));
		}
	
		if(taOutputFolder3.getText().equalsIgnoreCase("")){
			 list.add("Please choose an ascci grid output matrix file");
			 validate = false;
		}
		
		if(!taCombination.getText().equalsIgnoreCase("")){
			formula = taCombination.getText();
		}else{
			list.add("Please choose a formula");
			validate = false;
		}
		
		return validate;
	}
	
	@Override
	public void doImport(Properties properties) {
		importFactors(properties);
		importCombination(properties);
		importOutputFolder3(properties);
		importVisualizeAscii3(properties);
	}
		
	@Override
	public void doExport(Properties properties){
		exportFactors(properties);
		exportCombination(properties);
		exportOutputFolder3(properties);
		exportVisualizeAscii3(properties);
	}
	
	@Override
	public void run() {
		getController().runCombine(inputMatrix3, names, formula, taOutputFolder3.getText(), viewAsciiOutput3.isSelected());
	}

}
