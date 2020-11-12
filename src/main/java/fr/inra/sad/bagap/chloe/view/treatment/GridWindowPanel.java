package fr.inra.sad.bagap.chloe.view.treatment;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import fr.inra.sad.bagap.chloe.view.wizard.Wizard;

public class GridWindowPanel extends TreatmentPanel {
	
	private static final long serialVersionUID = 1L;

	public GridWindowPanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "grid";
	}

	@Override
	protected void locateComponents() {
		title.setText("Grid Window Analysis");
		
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
		c.gridwidth = 5;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taMatrixInput, c);
		
		c.gridx = 6;
		c.gridy = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		add(bMatrixCollectionInput, c);
		
		c.gridx = 7;
		c.gridy = 1;
		add(bViewMatrixInput, c);
	
		c.gridx = 0;
		c.gridy = 5;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lGSize, c);
		
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(spGSize, c);
		
		c.gridx = 2;
		c.gridy = 5;
		add(bsgAdd, c);
		
		c.gridx = 3;
		c.gridy = 5;
		add(pGSize, c);
		
		c.gridx = 4;
		c.gridy = 5;
		add(bsgRem, c);
		
		c.gridx = 0;
		c.gridy = 6;
		c.anchor = GridBagConstraints.LINE_END;
		add(lType, c);
		
		c.gridx = 1;
		c.gridy = 6;
		c.anchor = GridBagConstraints.LINE_START;
		add(cbType, c);
		
		c.gridx = 3;
		c.gridy = 6;
		c.anchor = GridBagConstraints.LINE_END;
		add(lMaxRate, c);
		
		c.gridx = 4;
		c.gridy = 6;
		c.anchor = GridBagConstraints.LINE_START;
		add(spMaxRate, c);
		
		c.gridx = 0;
		c.gridy = 7;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lMetrics, c);
		
		c.gridx = 1;
		c.gridy = 7;
		c.gridheight = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(pLMetrics, c);
		
		c.gridx = 3;
		c.gridy = 7;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		add(bmAll, c);
		
		c.gridx = 3;
		c.gridy = 8;
		add(bmAdd, c);
		
		c.gridx = 3;
		c.gridy = 9;
		c.anchor = GridBagConstraints.PAGE_START;
		add(bmRem, c);
		
		c.gridx = 4;
		c.gridy = 7;
		c.gridheight = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		add(pCMetrics, c);
		
		c.gridx = 0;
		c.gridy = 10;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 10;
		c.gridwidth = 5;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taOutputFolder, c);
		
		c.gridx = 6;
		c.gridy = 10;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		add(bOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 11;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(exportCsv, c);
		
		c.gridx = 2;
		c.gridy = 11;
		add(exportAscii, c);
		
		c.gridx = 3;
		c.gridy = 11;
		add(viewAsciiOutput, c);
	}
	
	@Override
	public boolean validateRun(List<String> list) {
		boolean validate = true;
		
		if(inputMatrix.size() == 0){
			 list.add("Please choose an ascci grid input matrix file");
			 validate = false;
		}
		
		if(tGSize.getModel().getRowCount() < 1){
			list.add("Please select at least one grid size");
			validate = false;
		}else{
			gridSizes = new ArrayList<Integer>();
			for(int r=0; r<tGSize.getModel().getRowCount(); r++){
				gridSizes.add((Integer) tGSize.getModel().getValueAt(r, 0));
			}
		}
		
		if(tCMetrics.getModel().getRowCount() == 0){
			list.add("Please choose metrics to calculate");
			 validate = false;
		}else{
			metrics = new TreeSet<String>();
			for(int r=0; r<tCMetrics.getModel().getRowCount(); r++){
				metrics.add((String) tCMetrics.getModel().getValueAt(r, 0));
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
		importInputMatrix(properties);
		importGridSizes(properties);
		importMaximumNoValueRate(properties);
		importMetrics(properties);
		importOutputFolder(properties);
		importExportCsv(properties);
		importExportAscii(properties);
		importVisualizeAscii(properties);
	}
	
	@Override
	public void doExport(Properties properties){
		exportInputMatrix(properties);
		exportGridSizes(properties);
		exportMaximumNoValueRate(properties);
		exportMetrics(properties);
		exportOutputFolder(properties);
		exportExportCsv(properties);
		exportExportAscii(properties);
		exportVisualizeAscii(properties);
	}
	
	@Override
	public void run() {
		getController().runGridWindow(inputMatrix, gridSizes, 1.0-(((Integer) spMaxRate.getValue())/100.0), 
				metrics, taOutputFolder.getText(), viewAsciiOutput.isSelected(), exportCsv.isSelected(), exportAscii.isSelected());
	}

}

