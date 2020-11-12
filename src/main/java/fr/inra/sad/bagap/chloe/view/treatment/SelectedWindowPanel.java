package fr.inra.sad.bagap.chloe.view.treatment;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShapeType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.chloe.view.wizard.Wizard;

public class SelectedWindowPanel extends TreatmentPanel {
	
	private static final long serialVersionUID = 1L;

	public SelectedWindowPanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "selected";
	}
	
	@Override
	protected void locateComponents() {
		title.setText("Selected Window Analysis");
		
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
		
		// fin pour Envam
		
		c.gridx = 0;
		c.gridy = 20;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lppSelection, c);
		
		c.gridx = 1;
		c.gridy = 20;
		c.anchor = GridBagConstraints.LINE_END;
		add(rbPixel, c);
		
		c.gridx = 2;
		c.gridy = 20;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(taPixel, c);
		
		c.gridx = 5;
		c.gridy = 20;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(bPixel, c);
		
		c.gridx = 1;
		c.gridy = 21;
		c.anchor = GridBagConstraints.LINE_END;
		add(rbPoint, c);
		
		c.gridx = 2;
		c.gridy = 21;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(taPoint, c);
		
		c.gridx = 5;
		c.gridy = 21;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(bPoint, c);
		
		c.gridx = 1;
		c.gridy = 22;
		c.anchor = GridBagConstraints.LINE_END;
		add(rbGPixel, c);
		
		c.gridx = 2;
		c.gridy = 22;
		add(lNPixel, c);
		
		c.gridx = 3;
		c.gridy = 22;
		c.anchor = GridBagConstraints.LINE_START;
		add(spNPixel, c);
		
		c.gridx = 5;
		c.gridy = 22;
		c.anchor = GridBagConstraints.LINE_START;
		add(bRunPixel, c);
		
		c.gridx = 2;
		c.gridy = 23;
		c.anchor = GridBagConstraints.LINE_END;
		add(lConstraint, c);
	
		c.gridx = 3;
		c.gridy = 23;
		c.anchor = GridBagConstraints.LINE_START;
		add(cbMinDistance, c);
		
		c.gridx = 4;
		c.gridy = 23;
		add(spMinDistance, c);
		
		c.gridx = 3;
		c.gridy = 24;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(cbCI, c);
		
		c.gridx = 3;
		c.gridy = 25;
		add(pCI, c);
		
		c.gridx = 4;
		c.gridy = 24;
		add(cbCNI, c);
		
		c.gridx = 4;
		c.gridy = 25;
		add(pCNI, c);
		
		c.gridx = 5;
		c.gridy = 23;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(bExportPixel, c);
		
		c.gridx = 5;
		c.gridy = 24;
		add(bExportPoint, c);
		
		c.gridx = 0;
		c.gridy = 26;
		c.anchor = GridBagConstraints.LINE_END;
		add(lMaxRate, c);
		
		c.gridx = 1;
		c.gridy = 26;
		c.anchor = GridBagConstraints.LINE_START;
		add(spMaxRate, c);
		
		c.gridx = 0;
		c.gridy = 27;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		//add(lType, c);
		
		c.gridx = 0;
		c.gridy = 28;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lMetrics, c);
		
		c.gridx = 1;
		c.gridy = 28;
		c.anchor = GridBagConstraints.LINE_START;
		add(cbType, c);
		
		c.gridx = 2;
		c.gridy = 28;
		c.gridheight = 3;
		//c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(pLMetrics, c);
		
		c.gridx = 3;
		c.gridy = 28;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		add(bmAll, c);
		
		c.gridx = 3;
		c.gridy = 29;
		add(bmAdd, c);
		
		c.gridx = 3;
		c.gridy = 30;
		c.anchor = GridBagConstraints.PAGE_START;
		add(bmRem, c);
		
		c.gridx = 4;
		c.gridy = 28;
		c.gridheight = 3;
		//c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		add(pCMetrics, c);
		
		c.gridx = 0;
		c.gridy = 31;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 31;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(taOutputFolder, c);
		
		c.gridx = 4;
		c.gridy = 31;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		add(bOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 32;
		c.weighty = 1;
		//c.gridwidth = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(exportCsv, c);
		
		c.gridx = 2;
		c.gridy = 32;
		add(exportAscii, c);
		
		c.gridx = 3;
		c.gridy = 32;
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
		
		if(cbShape.getSelectedItem().equals(WindowShapeType.FUNCTIONAL)){
			if(frictionMatrix.size() == 0){
				friction = new Friction(taFriction.getText());
			}
		}else{
			friction = null;
			frictionMatrix.clear();
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
		
		if(rbFormula.isSelected()){
			distanceFunction = taFormula.getText();
			if(distanceFunction.equalsIgnoreCase("")){
				list.add("Please set a formula for the distance function");
				validate = false;
			}
		}else{
			distanceFunction = "";
		}
		
		return validate;
	}
	
	@Override
	public void doImport(Properties properties) {
		importInputMatrix(properties);
		importShape(properties);
		importWindowSizes(properties);
		importMaximumNoValueRate(properties);
		importPixels(properties);
		importPoints(properties);
		importGeneratedPixels(properties);
		importMinimumDistance(properties);
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
		exportWindowSizes(properties);
		exportMaximumNoValueRate(properties);
		exportPixels(properties);
		exportPoints(properties);
		exportGeneratedPixels(properties);
		exportMinimumDistance(properties);
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
		getController().runSelectedWindow(inputMatrix, 
				1.0-(((Integer) spMaxRate.getValue())/100.0), (WindowShapeType) cbShape.getSelectedItem(), 
				friction, (frictionMatrix.size()==0)?null:frictionMatrix.iterator().next(), windowSizes, distanceFunction, pixels, points, metrics, taOutputFolder.getText(), 
				exportAscii.isSelected() && viewAsciiOutput.isSelected(), exportCsv.isSelected(), exportAscii.isSelected());	
	}

}
