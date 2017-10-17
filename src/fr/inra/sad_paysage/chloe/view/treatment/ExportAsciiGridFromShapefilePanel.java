package fr.inra.sad_paysage.chloe.view.treatment;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import fr.inra.sad_paysage.chloe.view.wizard.Wizard;

public class ExportAsciiGridFromShapefilePanel extends TreatmentPanel {
	
	private static final long serialVersionUID = 1L;

	public ExportAsciiGridFromShapefilePanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "from shapefile";
	}

	@Override
	protected void locateComponents() {
		title.setText("Generate Ascii Grid from Shapefile");
		
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
		add(lShapeInput, c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 5;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taShapeInput, c);
		
		c.gridx = 6;
		c.gridy = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		add(bShapeInput, c);
	
		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lAttribute, c);
		
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		c.gridheight = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(pAttribute, c);
		
		c.gridx = 3;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lCorrespondance, c);
		
		c.gridx = 4;
		c.gridy = 4;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		add(taCorrespondance, c);
		
		c.gridx = 6;
		c.gridy = 4;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		add(bCorrespondance, c);
		
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lCellsize, c);
		
		c.gridx = 1;
		c.gridy = 7;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(spCellsize, c);
		
		c.gridx = 2;
		c.gridy = 7;
		add(bsSFAdd, c);
		
		c.gridx = 3;
		c.gridy = 7;
		add(pCellsize, c);
		
		c.gridx = 4;
		c.gridy = 7;
		add(bsSFRem, c);
		
		c.gridx = 0;
		c.gridy = 8;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lEnvelope, c);
		
		c.gridx = 1;
		c.gridy = 8;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(cbEnvelope, c);
		
		c.gridx = 2;
		c.gridy = 8;
		add(lMinX, c);
		
		c.gridx = 3;
		c.gridy = 8;
		add(spMinX, c);
		
		c.gridx = 4;
		c.gridy = 8;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lMaxX, c);
		
		c.gridx = 5;
		c.gridy = 8;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(spMaxX, c);
		
		c.gridx = 2;
		c.gridy = 9;
		add(lMinY, c);
		
		c.gridx = 3;
		c.gridy = 9;
		add(spMinY, c);
		
		c.gridx = 4;
		c.gridy = 9;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lMaxY, c);
		
		c.gridx = 5;
		c.gridy = 9;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(spMaxY, c);
		
		c.gridx = 6;
		c.gridy = 8;
		add(bImportEnvelope, c);
		
		c.gridx = 6;
		c.gridy = 9;
		add(bExportEnvelope, c);
	
		c.gridx = 0;
		c.gridy = 13;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 13;
		c.gridwidth = 5;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taOutputFolder, c);
		
		c.gridx = 6;
		c.gridy = 13;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		add(bOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 14;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(viewAsciiOutput, c);
		
	}

	@Override
	public boolean validateRun(List<String> list) {
		boolean validate = true;
		
		attribute = (String) tAttribute.getModel().getValueAt(tAttribute.getSelectedRow(), 0);
		
		cellsizes = new TreeSet<Double>();
		for(int r=0; r<tCellsize.getModel().getRowCount(); r++){
			cellsizes.add((Double) tCellsize.getModel().getValueAt(r, 0));
		}
		
		return validate;
	}
	
	@Override
	public void doImport(Properties properties) {
		importInputShapefile(properties);
		importLookupTable(properties);
		importAttribute(properties);
		importCellSizes(properties);
		importEnvelope(properties);
		importOutputFolder(properties);
		importVisualizeAscii(properties);
	}
	
	@Override
	public void doExport(Properties properties){
		exportInputShapefile(properties);
		exportLookupTable(properties);
		exportAttribute(properties);
		exportCellSizes(properties);
		exportEnvelope(properties);
		exportOutputFolder(properties);
		exportVisualizeAscii(properties);
	}

	@Override
	public void run() {
		if(cbEnvelope.isSelected()){
			getController().exportAsciiGridFromShapefile(shapes, attribute, taCorrespondance.getText(), cellsizes, taOutputFolder.getText(), 
					viewAsciiOutput.isSelected(),(Double) spMinX.getValue(), (Double) spMaxX.getValue(), (Double) spMinY.getValue(), (Double) spMaxY.getValue());
		}else{
			getController().exportAsciiGridFromShapefile(shapes, attribute, taCorrespondance.getText(), cellsizes, taOutputFolder.getText(), viewAsciiOutput.isSelected(),
					null, null, null, null);
		}
	}
	
}
