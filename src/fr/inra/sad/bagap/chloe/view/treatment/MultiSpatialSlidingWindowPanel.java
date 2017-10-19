package fr.inra.sad.bagap.chloe.view.treatment;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShapeType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.chloe.view.wizard.Wizard;

public class MultiSpatialSlidingWindowPanel extends TreatmentPanel {
	
	private static final long serialVersionUID = 1L;
	
	public MultiSpatialSlidingWindowPanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "multi-spatial sliding";
	}
	
	@Override
	protected void locateComponents() {
		title.setText("Multi-Spatial Sliding Window Analysis");
		
		c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		add(title, c);
	
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_END;
		add(lShape, c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_START;
		add(cbShape, c);
		
		c.gridx = 2;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_END;
		add(lFriction, c);
		
		c.gridx = 3;
		c.gridy = 3;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(taFriction, c);
		
		c.gridx = 6;
		c.gridy = 3;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(bFriction, c);
		
		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lSize, c);
		
		c.gridx = 1;
		c.gridy = 4;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(spSize, c);
		
		c.gridx = 2;
		c.gridy = 4;
		add(bsAdd, c);
		
		c.gridx = 3;
		c.gridy = 4;
		add(pSize, c);
		
		c.gridx = 4;
		c.gridy = 4;
		add(bsRem, c);
		
		c.gridx = 0;
		c.gridy = 5;
		c.anchor = GridBagConstraints.LINE_END;
		add(lDelta, c);
		
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.LINE_START;
		add(spDelta, c);
		
		c.gridx = 2;
		c.gridy = 5;
		c.anchor = GridBagConstraints.LINE_START;
		add(lDeltaMeters, c);
		
		c.gridx = 3;
		c.gridy = 5;
		c.anchor = GridBagConstraints.LINE_START;
		add(cbInterpolate, c);
		
		c.gridx = 0;
		c.gridy = 6;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lFilters, c);
		
		c.gridx = 1;
		c.gridy = 6;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(cbF, c);
		
		c.gridx = 2;
		c.gridy = 6;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(pF, c);
		
		c.gridx = 3;
		c.gridy = 6;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(cbNF, c);
		
		c.gridx = 4;
		c.gridy = 6;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(pNF, c);
		
		c.gridx = 0;
		c.gridy = 7;
		c.anchor = GridBagConstraints.LINE_END;
		add(lType, c);
		
		c.gridx = 1;
		c.gridy = 7;
		c.anchor = GridBagConstraints.LINE_START;
		add(cbType, c);
		
		c.gridx = 0;
		c.gridy = 8;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lMetrics, c);
		
		c.gridx = 1;
		c.gridy = 8;
		c.gridheight = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(pLMetrics, c);
		
		c.gridx = 3;
		c.gridy = 8;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		add(bmAll, c);
		
		c.gridx = 3;
		c.gridy = 9;
		add(bmAdd, c);
		
		c.gridx = 3;
		c.gridy = 10;
		c.anchor = GridBagConstraints.PAGE_START;
		add(bmRem, c);
		
		c.gridx = 4;
		c.gridy = 8;
		c.gridheight = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		add(pCMetrics, c);
		
		c.gridx = 0;
		c.gridy = 11;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lMSMetrics, c);
		
		c.gridx = 1;
		c.gridy = 11;
		c.gridheight = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(pLMSMetrics, c);
		
		c.gridx = 3;
		c.gridy = 11;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		add(bmsmAll, c);
		
		c.gridx = 3;
		c.gridy = 12;
		add(bmsmAdd, c);
		
		c.gridx = 3;
		c.gridy = 13;
		c.anchor = GridBagConstraints.PAGE_START;
		add(bmsmRem, c);
		
		c.gridx = 4;
		c.gridy = 11;
		c.gridheight = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		add(pCMSMetrics, c);
		
		c.gridx = 0;
		c.gridy = 14;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 14;
		c.gridwidth = 5;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taOutputFolder, c);
		
		c.gridx = 6;
		c.gridy = 14;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		add(bOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 15;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(exportCsv, c);
		
		c.gridx = 2;
		c.gridy = 15;
		add(exportAscii, c);
		
		c.gridx = 3;
		c.gridy = 15;
		add(viewAsciiOutput, c);
	}
	
	@Override
	public boolean validateRun(List<String> list) {
		boolean validate = true;
		
		if(inputMatrix.size() == 0){
			list.add("Please choose an ascci grid input matrix file");
			validate = false;
		}
		
		if(cbShape.getSelectedItem().equals(WindowShapeType.FUNCTIONAL)){
			if(frictionMatrix == null){
				friction = new Friction(taFriction.getText());
			}
		}else{
			friction = null;
		}
		
		if(tSize.getModel().getRowCount() < 2){
			list.add("Please select at least two window sizes");
			validate = false;
		}else{
			windowSizes = new ArrayList<Integer>();
			for(int r=0; r<tSize.getModel().getRowCount(); r++){
				windowSizes.add((Integer) tSize.getModel().getValueAt(r, 0));
			}
		}
		
		filters = new HashSet<Integer>();
		if(cbF.isSelected()){
			for(int r : tF.getSelectedRows()){
				filters.add((Integer) tF.getModel().getValueAt(r, 0));
			}
		}
		
		unfilters = new HashSet<Integer>();
		if(cbNF.isSelected()){
			for(int r : tNF.getSelectedRows()){
				unfilters.add((Integer) tNF.getModel().getValueAt(r, 0));
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
			for(int r=0; r<tCMSMetrics.getModel().getRowCount(); r++){
				metrics.add((String) tCMSMetrics.getModel().getValueAt(r, 0));
			}
		}
		
		if(taOutputFolder.getText().equalsIgnoreCase("")){
			 list.add("Please choose an output folder");
			 validate = false;
		}
		
		return validate;
	}
	
	@Override
	public void doImport(Properties properties) {
		importInputAscii(properties);
		importProcessType(properties);
		importShape(properties);
		importWindowSizes(properties);
		importDeltaDisplacement(properties);
		importFilters(properties);
		importUnfilters(properties);
		importMetrics(properties);
		importMultiScalesMetrics(properties);
		importOutputFolder(properties);
		importExportCsv(properties);
		importExportAscii(properties);
		importVisualizeAscii(properties);
	}
	
	@Override
	public void doExport(Properties properties){
		exportInputAscii(properties);
		exportProcessType(properties);
		exportShape(properties);
		exportWindowSizes(properties);
		exportDeltaDisplacement(properties);
		exportFilters(properties);
		exportUnfilters(properties);
		exportMetrics(properties);
		exportMultiScalesMetrics(properties);
		exportOutputFolder(properties);
		exportExportCsv(properties);
		exportExportAscii(properties);
		exportVisualizeAscii(properties);
	}
	
	@Override
	public void run() {
		/*
		getController().runMultiSpatialSlidingWindow(inputMatrix, 
				(String) cbType.getSelectedItem(), (WindowShapeType) cbShape.getSelectedItem(), friction, frictionMatrix, 
				windowSizes, (Integer) spDelta.getValue(), cbInterpolate.isSelected(), metrics, taOutputFolder.getText(), 
				viewAsciiOutput.isSelected(), exportCsv.isSelected(), exportAscii.isSelected(), filters, unfilters);
				*/
	}

}
