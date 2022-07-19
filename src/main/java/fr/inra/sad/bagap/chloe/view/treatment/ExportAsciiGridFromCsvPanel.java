package fr.inra.sad.bagap.chloe.view.treatment;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.util.Properties;

import fr.inra.sad.bagap.chloe.view.wizard.Wizard;

public class ExportAsciiGridFromCsvPanel extends TreatmentPanel {
	
	private static final long serialVersionUID = 1L;

	public ExportAsciiGridFromCsvPanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "from csv";
	}

	@Override
	protected void locateComponents() {
		title.setText("Generate Ascii Grid from Csv File");
		
		c = new GridBagConstraints();
		c.insets = new Insets(5,2,5,2);
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		add(title, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		add(lCsvInput, c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taCsvInput, c);
		
		c.gridx = 4;
		c.gridy = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		add(bCsvInput, c);
		
		c.gridx = 1;
		c.gridy = 3;
		add(bSort, c);
		
		c.gridx = 0;
		c.gridy = 7;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lVariables, c);
		
		c.gridx = 1;
		c.gridy = 7;
		c.gridheight = 3;
		add(pLVariables, c);
		
		c.gridx = 2;
		c.gridy = 7;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.CENTER;
		add(bmVAll, c);
		
		c.gridx = 2;
		c.gridy = 8;
		add(bmVAdd, c);
		
		c.gridx = 2;
		c.gridy = 9;
		c.anchor = GridBagConstraints.PAGE_START;
		add(bmVRem, c);
		
		c.gridx = 3;
		c.gridy = 7;
		c.gridheight = 3;
		c.anchor = GridBagConstraints.LINE_START;
		add(pCVariables, c);
		
		c.gridx = 0;
		c.gridy = 10;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lHeader, c);
		
		c.gridx = 1;
		c.gridy = 10;
		c.anchor = GridBagConstraints.LINE_END;
		add(lncols, c);
		
		c.gridx = 2;
		c.gridy = 10;
		c.anchor = GridBagConstraints.LINE_START;
		add(spncols, c);
		
		c.gridx = 1;
		c.gridy = 11;
		c.anchor = GridBagConstraints.LINE_END;
		add(lnrows, c);
		
		c.gridx = 2;
		c.gridy = 11;
		c.anchor = GridBagConstraints.LINE_START;
		add(spnrows, c);
		
		c.gridx = 1;
		c.gridy = 12;
		c.anchor = GridBagConstraints.LINE_END;
		add(lxllcorner, c);
		
		c.gridx = 2;
		c.gridy = 12;
		c.anchor = GridBagConstraints.LINE_START;
		add(spxllcorner, c);
		
		c.gridx = 3;
		c.gridy = 12;
		c.anchor = GridBagConstraints.CENTER;
		add(bHeader, c);
		
		c.gridx = 1;
		c.gridy = 13;
		c.anchor = GridBagConstraints.LINE_END;
		add(lyllcorner, c);
		
		c.gridx = 2;
		c.gridy = 13;
		c.anchor = GridBagConstraints.LINE_START;
		add(spyllcorner, c);
		
		c.gridx = 1;
		c.gridy = 14;
		c.anchor = GridBagConstraints.LINE_END;
		add(lcellsize, c);
		
		c.gridx = 2;
		c.gridy = 14;
		c.anchor = GridBagConstraints.LINE_START;
		add(spcellsize, c);
		
		c.gridx = 1;
		c.gridy = 15;
		c.anchor = GridBagConstraints.LINE_END;
		add(lnodatavalue, c);
		
		c.gridx = 2;
		c.gridy = 15;
		c.anchor = GridBagConstraints.LINE_START;
		add(spnodatavalue, c);
		
		c.gridx = 0;
		c.gridy = 17;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 17;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 1;
		add(taOutputFolder, c);
		
		c.gridx = 4;
		c.gridy = 17;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		
		add(bOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 18;
		c.weighty = 1;
		add(viewAsciiOutput, c);
	}
	
	@Override
	public boolean validateRun(List<String> list) {
		boolean validate = true;
		
		variables.clear();
		if(tCVariables.getModel().getRowCount() == 0){
			list.add("Please choose variables to export");
			validate = false;
		}else{
			for(int r=0; r<tCVariables.getModel().getRowCount(); r++){
				variables.add((String) tCVariables.getModel().getValueAt(r, 0));
			}
		}
		
		if(taOutputFolder.getText().equalsIgnoreCase("")){
			 list.add("Please choose an output folder");
			 validate = false;
		}
		
		return validate;
	}
	
	@Override
	public void run() {
		getController().exportAsciiGridFromCsv(inputCsv, taOutputFolder.getText()+"/", variables, (Integer) spncols.getValue(), 
				(Integer) spnrows.getValue(), (Double) spxllcorner.getValue(), (Double) spyllcorner.getValue(),
				(Double) spcellsize.getValue(), (Integer) spnodatavalue.getValue(), viewAsciiOutput.isSelected());
	}

	@Override
	public void doImport(Properties properties) {
		importInputCsv(properties);
		importVariables(properties);
		importHeader(properties);
		importOutputFolder(properties);
		importVisualizeAscii(properties);
	}

	@Override
	public void doExport(Properties properties) {
		exportInputCsv(properties);
		exportVariables(properties);
		exportHeader(properties);
		exportOutputFolder(properties);
		exportVisualizeAscii(properties);
	}
	
}
