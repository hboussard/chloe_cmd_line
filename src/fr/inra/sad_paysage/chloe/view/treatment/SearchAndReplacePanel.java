package fr.inra.sad_paysage.chloe.view.treatment;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.chloe.view.wizard.Wizard;

public class SearchAndReplacePanel extends TreatmentPanel {

	private static final long serialVersionUID = 1L;
	
	public SearchAndReplacePanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "search and replace";
	}
	
	@Override
	protected void locateComponents(){
		title.setText("Search and Replace");
		
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
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taAsciiInput, c);
		
		c.gridx = 4;
		c.gridy = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		add(bAsciiInput, c);

		c.gridx = 5;
		c.gridy = 1;
		add(bViewAsciiInput, c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lValues, c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		add(pValues, c);
		
		c.gridx = 2;
		c.gridy = 3;
		c.gridheight = 1;
		add(bCsvMap, c);
		
		c.gridx = 2;
		c.gridy = 4;
		add(bCsvApply, c);
		
		c.gridx = 3;
		c.gridy = 3;
		c.gridheight = 2;
		add(pLMap, c);
		
		c.gridx = 0;
		c.gridy = 5;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.LINE_END;
		add(lValue, c);
		
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.LINE_START;
		add(spNoData, c);
		
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
		
		for(Matrix m : inputMatrix){
			asciis.add(m.getFile());
		}
		
		values = new HashMap<Integer, Number>();
		if(oldNoData != (Integer) spNoData.getValue()){
			values.put(oldNoData, (Integer) spNoData.getValue());
		}
		if(tChanges != null){
			tChanges.getColumnModel().getColumn(2).getCellEditor().stopCellEditing();
			for(int i=0; i<tChanges.getModel().getRowCount(); i++){
				if((Boolean)tChanges.getModel().getValueAt(i, 1)){
					values.put((Integer) tChanges.getModel().getValueAt(i, 0), 
							Double.parseDouble((String) tChanges.getModel().getValueAt(i, 2)));
				}
			}
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
		importChanges(properties);
		importNoDataValue(properties);
		importOutputFolder(properties);
		importVisualizeAscii(properties);
		importMapCsv(properties);
	}
	
	@Override
	public void doExport(Properties properties){
		exportInputAscii(properties);
		exportChanges(properties);
		exportNoDataValue(properties);
		exportOutputFolder(properties);
		exportVisualizeAscii(properties);
		exportMapCsv(properties);
	}

	@Override
	public void run() {
		getController().runSearchAndReplace(asciis, (Integer) spNoData.getValue(), values, taOutputFolder.getText(), viewAsciiOutput.isSelected());
	}
}
