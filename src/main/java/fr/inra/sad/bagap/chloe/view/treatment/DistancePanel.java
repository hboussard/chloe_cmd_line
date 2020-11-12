package fr.inra.sad.bagap.chloe.view.treatment;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.chloe.view.wizard.Wizard;

public class DistancePanel extends TreatmentPanel {

	private static final long serialVersionUID = 1L;
	
	private String typeDistance; 
	
	private double distance;
	
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
		c.weightx = 3;
		c.gridwidth = 3;
		add(taMatrixInput, c);
		
		c.gridx = 4;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		add(bMatrixCollectionInput, c);

		c.gridx = 5;
		c.gridy = 1;
		add(bViewMatrixInput, c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lDistances, c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		c.gridheight = 4;
		add(pDistances, c);
		
		c.gridx = 2;
		c.gridy = 4;
		c.gridheight = 1;
		add(rbEuclidianDistance, c);
		
		c.gridx = 2;
		c.gridy = 5;
		add(rbFunctionalDistance, c);
		
		c.gridx = 2;
		c.gridy = 6;
		c.anchor = GridBagConstraints.LINE_END;
		add(lFrictionDistance, c);
		
		c.gridx = 3;
		c.gridy = 6;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(taFrictionDistance, c);
		
		c.gridx = 4;
		c.gridy = 6;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(bFrictionDistance, c);
		
		c.gridx = 2;
		c.gridy = 7;
		add(cbMaxDistance, c);
		
		c.gridx = 3;
		c.gridy = 7;
		add(spMaxDistance, c);
		
		c.gridx = 0;
		c.gridy = 8;
		c.anchor = GridBagConstraints.LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 8;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 3;
		c.gridwidth = 3;
		add(taOutputFolder, c);
		
		c.gridx = 4;
		c.gridy = 8;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.gridwidth = 1;
		add(bOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 9;
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
		
		if(rbEuclidianDistance.isSelected()){
			typeDistance = "euclidian";
		}else{
			typeDistance = "functional";
			if(distanceFrictionMatrix.size() == 0){
				distanceFriction = new Friction(taFrictionDistance.getText());
			}
		}
		
		distance = Raster.getNoDataValue();
		if(cbMaxDistance.isSelected()){
			distance = (double) spMaxDistance.getValue();
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
		importDistances(properties);
		importDistanceType(properties);
		importMaxDistance(properties);
		importDistanceFriction(properties);
		importDistanceFrictionMatrix(properties);
		importOutputFolder(properties);
		importVisualizeAscii(properties);
	}
	
	@Override
	public void doExport(Properties properties){
		exportInputMatrix(properties);
		exportDistances(properties);
		exportDistanceType(properties);
		exportMaxDistance(properties);
		exportDistanceFriction(properties);
		exportDistanceFrictionMatrix(properties);
		exportOutputFolder(properties);
		exportVisualizeAscii(properties);
	}

	@Override
	public void run() {
		getController().runDistance(inputMatrix, vDistances, typeDistance, distance, distanceFriction,  (distanceFrictionMatrix.size()==0)?null:distanceFrictionMatrix.iterator().next(), taOutputFolder.getText(), viewAsciiOutput.isSelected());
	}

}
