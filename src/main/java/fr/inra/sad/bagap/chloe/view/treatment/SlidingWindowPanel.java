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

public class SlidingWindowPanel extends TreatmentPanel {
	
	private static final long serialVersionUID = 1L;
	
	public SlidingWindowPanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "sliding";
	}
	
	@Override
	protected void locateComponents() {
		title.setText("Sliding Window Analysis");
		
		c = new GridBagConstraints();
		//c.insets = new Insets(5,5,5,5);
		c.insets = new Insets(5,2,5,2);
		
		c.gridx = 5;
		c.gridy = 0;
		add(lFictif, c);
		
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
		add(taMatrixInput, c);
		
		c.gridx = 4;
		c.gridy = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		add(bMatrixCollectionInput, c);
		
		c.gridx = 5;
		c.gridy = 1;
		add(bViewMatrixInput, c);
		
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
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(taFriction, c);
		
		c.gridx = 5;
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
		c.anchor = GridBagConstraints.LINE_START;
		add(spSize, c);
		
		c.gridx = 2;
		c.gridy = 4;
		add(bsAdd, c);
		
		c.gridx = 3;
		c.gridy = 4;
		c.gridheight = 2;
		add(pSize, c);
		
		c.gridx = 2;
		c.gridy = 5;
		c.anchor = GridBagConstraints.LINE_END;
		c.gridheight = 1;
		add(bsRem, c);
		
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.LINE_START;
		add(lDMax, c);
		
		// Pour Envam
		c.gridx = 0;
		c.gridy = 6;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lDistanceType, c);
		
		c.gridx = 1;
		c.gridy = 6;
		c.anchor = GridBagConstraints.LINE_START;
		add(rbThreshold, c);
		
		c.gridx = 1;
		c.gridy = 7;
		add(rbFormula, c);
		
		c.gridx = 2;
		c.gridy = 7;
		c.gridwidth = 3;
		c.gridheight = 4;
		c.fill = GridBagConstraints.BOTH;
		add(taFormula, c);
		
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.NONE;
		
		c.gridx = 5;
		c.gridy = 7;
		add(bDistanceFunction, c);
		
		c.gridx = 5;
		c.gridy = 8;
		add(bDistanceImport, c);
		
		c.gridx = 5;
		c.gridy = 9;
		add(bDistanceExport, c);
		
		c.gridx = 5;
		c.gridy = 10;
		add(bDistancePost, c);
		
		c.gridx = 2;
		c.gridy = 10;
		//add(rbUserFormula, c);
		
		c.gridx = 2;
		c.gridy = 11;
		add(rbLinearFunction, c);
		
		c.gridx = 3;
		c.gridy = 11;
		add(rbCurveFunctionType1, c);
		
		c.gridx = 4;
		c.gridy = 11;
		add(rbCurveFunctionType2, c);
		
		c.gridx = 1;
		c.gridy = 12;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lDistanceOrigin, c);
		
		c.gridx = 2;
		c.gridy = 12;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(taDistanceOrigin, c);
		
		c.gridx = 3;
		c.gridy = 12;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		add(lDistanceOriginValue, c);
		
		c.gridx = 4;
		c.gridy = 12;
		c.anchor = GridBagConstraints.LINE_START;
		add(spDistanceOriginValue, c);
		
		c.gridx = 1;
		c.gridy = 13;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lDistanceFinal, c);
		
		c.gridx = 2;
		c.gridy = 13;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(taDistanceFinal, c);
		
		c.gridx = 3;
		c.gridy = 13;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		add(lDistanceFinalValue, c);
		
		c.gridx = 4;
		c.gridy = 13;
		c.anchor = GridBagConstraints.LINE_START;
		add(spDistanceFinalValue, c);
		
		c.gridx = 3;
		c.gridy = 14;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lDistanceTilt, c);
		
		c.gridx = 4;
		c.gridy = 14;
		c.anchor = GridBagConstraints.LINE_START;
		add(spDistanceTilt, c);
		
		c.gridx = 2;
		c.gridy = 15;
		add(rbGaussianFunction, c);
		
		c.gridx = 3;
		c.gridy = 16;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lDistanceBaseValue, c);
		
		c.gridx = 4;
		c.gridy = 16;
		c.anchor = GridBagConstraints.LINE_START;
		add(spDistanceBaseValue, c);
		
		c.gridx = 1;
		c.gridy = 17;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lDistancePic, c);
		
		c.gridx = 2;
		c.gridy = 17;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(taDistancePic, c);
		
		c.gridx = 3;
		c.gridy = 17;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lDistancePicValue, c);
		
		c.gridx = 4;
		c.gridy = 17;
		c.anchor = GridBagConstraints.LINE_START;
		add(spDistancePicValue, c);
		
		c.gridx = 3;
		c.gridy = 18;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lDistanceQValue, c);
		
		c.gridx = 4;
		c.gridy = 18;
		c.anchor = GridBagConstraints.LINE_START;
		add(spDistanceQValue, c);	
				
		// fin pour envam
				
		c.gridx = 0;
		c.gridy = 19;
		c.anchor = GridBagConstraints.LINE_END;
		add(lDelta, c);
		
		c.gridx = 1;
		c.gridy = 19;
		c.anchor = GridBagConstraints.LINE_START;
		add(spDelta, c);
		
		c.gridx = 2;
		c.gridy = 19;
		add(lDeltaMeters, c);
		
		c.gridx = 3;
		c.gridy = 19;
		c.anchor = GridBagConstraints.LINE_START;
		add(cbInterpolate, c);
		
		c.gridx = 1;
		c.gridy = 20;
		c.anchor = GridBagConstraints.LINE_END;
		add(lXOrigin, c);
		
		c.gridx = 2;
		c.gridy = 20;
		c.anchor = GridBagConstraints.LINE_START;
		add(spXOrigin, c);
		
		c.gridx = 1;
		c.gridy = 21;
		c.anchor = GridBagConstraints.LINE_END;
		add(lYOrigin, c);
		
		c.gridx = 2;
		c.gridy = 21;
		c.anchor = GridBagConstraints.LINE_START;
		add(spYOrigin, c);
		
		c.gridx = 0;
		c.gridy = 22;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lFilters, c);
		
		c.gridx = 1;
		c.gridy = 22;
		add(cbF, c);
		
		c.gridx = 2;
		c.gridy = 22;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(pF, c);
		
		c.gridx = 3;
		c.gridy = 22;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(cbNF, c);
		
		c.gridx = 4;
		c.gridy = 22;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(pNF, c);
		
		c.gridx = 0;
		c.gridy = 23;
		c.anchor = GridBagConstraints.LINE_END;
		add(lMaxRate, c);
		
		c.gridx = 1;
		c.gridy = 23;
		c.anchor = GridBagConstraints.LINE_START;
		add(spMaxRate, c);
		
		c.gridx = 0;
		c.gridy = 24;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		//add(lType, c);
		
		c.gridx = 0;
		c.gridy = 25;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lMetrics, c);
		
		c.gridx = 1;
		c.gridy = 25;
		//c.anchor = GridBagConstraints.LINE_START;
		add(cbType, c);
		
		c.gridx = 2;
		c.gridy = 25;
		c.gridheight = 3;
		//c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(pLMetrics, c);
		
		c.gridx = 3;
		c.gridy = 25;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		add(bmAll, c);
		
		c.gridx = 3;
		c.gridy = 26;
		add(bmAdd, c);
		
		c.gridx = 3;
		c.gridy = 27;
		c.anchor = GridBagConstraints.PAGE_START;
		add(bmRem, c);
		
		c.gridx = 4;
		c.gridy = 25;
		c.gridheight = 3;
		//c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		add(pCMetrics, c);
		
		c.gridx = 0;
		c.gridy = 28;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 28;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taOutputFolder, c);
		
		c.gridx = 4;
		c.gridy = 28;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		add(bOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 29;
		c.weighty = 1;
		//c.gridwidth = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(exportCsv, c);
		
		c.gridx = 2;
		c.gridy = 29;
		add(exportAscii, c);
		
		c.gridx = 3;
		c.gridy = 29;
		add(viewAsciiOutput, c);
	}

	@Override
	public boolean validateRun(List<String> list) {
		boolean validate = true;
		
		if(inputMatrix.size() == 0){
			 list.add("Please choose an ascci grid input matrix file");
			 validate = false;
		}
		
		if(tSize.getModel().getRowCount() < 1){
			list.add("Please select at least one window size");
			validate = false;
		}else{
			windowSizes = new ArrayList<Integer>();
			for(int r=0; r<tSize.getModel().getRowCount(); r++){
				windowSizes.add((Integer) tSize.getModel().getValueAt(r, 0));
			}
		}
		
		if(cbShape.getSelectedItem().equals(WindowShapeType.SQUARE) && rbFormula.isSelected()){
			list.add("The weighted distance calculation is not available for the square window shape");
			validate = false;
		}
		
		if(cbShape.getSelectedItem().equals(WindowShapeType.FUNCTIONAL)){
			if(frictionMatrix.size() == 0){
				friction = new Friction(taFriction.getText());
			}
		}else{
			friction = null;
			frictionMatrix.clear();
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
		
		metrics = new TreeSet<String>();
		if(!exportCsv.isSelected() && tCMetrics.getModel().getRowCount() == 0){
			list.add("Please choose metrics to calculate");
			validate = false;
		}else{
			for(int r=0; r<tCMetrics.getModel().getRowCount(); r++){
				metrics.add((String) tCMetrics.getModel().getValueAt(r, 0));
			}
		}
		
		if(taOutputFolder.getText().equalsIgnoreCase("")){
			 list.add("Please choose an output folder");
			 validate = false;
		}
		
		if(rbFormula.isSelected()){
			distanceFunction = taFormula.getText();
			if(distanceFunction.equalsIgnoreCase("")){
				list.add("Please set a formula for the distance function");
				validate = false;
			}
		}else{
			distanceFunction = null;
		}
		
		if((Integer) spDelta.getValue() == 1){
			spXOrigin.setValue(0);
			spYOrigin.setValue(0);
		}
		
		return validate;
	}
	
	@Override
	public void doImport(Properties properties) {
		importInputMatrix(properties);
		importShape(properties);
		importFriction(properties);
		importFrictionMatrix(properties);
		importWindowSizes(properties);
		importMaximumNoValueRate(properties);
		importDeltaDisplacement(properties);
		importXOrigin(properties);
		importYOrigin(properties);
		importInterpolation(properties);
		importFilters(properties);
		importUnfilters(properties);
		importMetrics(properties);
		importOutputFolder(properties);
		importExportCsv(properties);
		importExportAscii(properties);
		importVisualizeAscii(properties);
		importDistanceFunction(properties);
	}

	@Override
	public void doExport(Properties properties){
		exportInputMatrix(properties);
		exportShape(properties);
		exportFriction(properties);
		exportFrictionMatrix(properties);
		exportWindowSizes(properties);
		exportMaximumNoValueRate(properties);
		exportDeltaDisplacement(properties);
		exportXOrigin(properties);
		exportYOrigin(properties);
		exportDeltaDisplacement(properties);
		exportInterpolation(properties);
		exportFilters(properties);
		exportUnfilters(properties);
		exportMetrics(properties);
		exportOutputFolder(properties);
		exportExportCsv(properties);
		exportExportAscii(properties);
		exportVisualizeAscii(properties);
		exportDistanceFunction(properties);
	}
	
	@Override
	public void run() {
		getController().runSlidingWindow(inputMatrix, 
				(WindowShapeType) cbShape.getSelectedItem(), friction,  (frictionMatrix.size()==0)?null:frictionMatrix.iterator().next(), 
				windowSizes, distanceFunction, (Integer) spDelta.getValue(), (Integer) spXOrigin.getValue(), (Integer) spYOrigin.getValue(), cbInterpolate.isSelected(), 1.0-(((Integer) spMaxRate.getValue())/100.0), metrics, taOutputFolder.getText(), 
				exportAscii.isSelected()?viewAsciiOutput.isSelected():false, exportCsv.isSelected(), exportAscii.isSelected(), filters, unfilters);
	}

	
}
