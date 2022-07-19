package fr.inra.sad.bagap.chloe.view.treatment;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.chloe.view.wizard.Wizard;

public class ClusterPanel extends TreatmentPanel {

	private static final long serialVersionUID = 1L;
	
	private List<Integer> vclusters;
	
	private String typeCluster; 
	
	private double distance;
	
	private double minimumTotalArea;
	
	public ClusterPanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "cluster";
	}
	
	@Override
	protected void locateComponents(){
		title.setText("Cluster");
		
		c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);
		
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
		c.gridwidth = 1;
		add(bMatrixCollectionInput, c);
		
		c.gridx = 5;
		c.gridy = 1;
		add(bViewMatrixInput, c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lClusters, c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		c.gridheight = 4;
		add(pDistances, c);
		
		c.gridx = 2;
		c.gridy = 3;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.anchor = GridBagConstraints.LINE_START;
		add(rbRook, c);
		
		c.gridx = 2;
		c.gridy = 4;
		add(rbQueen, c);
		
		c.gridx = 2;
		c.gridy = 5;
		add(rbEuclidianCluster, c);
		
		c.gridx = 3;
		c.gridy = 5;
		add(spEuclidianCluster, c);
		
		c.gridx = 2;
		c.gridy = 6;
		add(rbFunctionalCluster, c);
		
		c.gridx = 3;
		c.gridy = 6;
		add(spFunctionalCluster, c);
		
		c.gridx = 2;
		c.gridy = 7;
		c.anchor = GridBagConstraints.LINE_END;
		add(lFrictionCluster, c);
		
		c.gridx = 3;
		c.gridy = 7;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(taFrictionCluster, c);
		
		c.gridx = 4;
		c.gridy = 7;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(bFrictionCluster, c);
		
		c.gridx = 0;
		c.gridy = 8;
		c.anchor = GridBagConstraints.LINE_END;
		add(lMinimumTotalArea, c);
		
		c.gridx = 1;
		c.gridy = 8;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(spMinimumTotalArea, c);
		
		c.gridx = 0;
		c.gridy = 9;
		c.anchor = GridBagConstraints.LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 9;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridwidth = 3;
		add(taOutputFolder, c);
		
		c.gridx = 4;
		c.gridy = 9;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.gridwidth = 1;
		add(bOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 10;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(viewAsciiOutput, c);
	}
	
	@Override
	public boolean validateRun(List<String> list) {
		boolean validate = true;
		
		vclusters = new ArrayList<Integer>();
		for(int r : tDistances.getSelectedRows()){
			vclusters.add((Integer) tDistances.getModel().getValueAt(r, 0));
		}
		
		if(rbRook.isSelected()){
			typeCluster = "rook";
		}else if(rbQueen.isSelected()){
			typeCluster = "queen";
		}else if(rbEuclidianCluster.isSelected()){
			typeCluster = "euclidian";
			distance = (Double) spEuclidianCluster.getValue();
		}else{
			typeCluster = "functional";
			distance = (Double) spFunctionalCluster.getValue();
			if(clusterFrictionMatrix.size() == 0){
				clusterFriction = new Friction(taFrictionCluster.getText());
			}
		}
		
		minimumTotalArea = (Double) spMinimumTotalArea.getValue();		
		
		if(taOutputFolder.getText().equalsIgnoreCase("")){
			 list.add("Please choose an ascci grid output matrix file");
			 validate = false;
		}
		
		return validate;
	}
	
	@Override
	public void doImport(Properties properties) {
		importInputMatrix(properties);
		importCluster(properties);
		importClusterType(properties);
		importClusterDistance(properties);
		importClusterFriction(properties);
		importClusterFrictionMatrix(properties);
		importMinimumTotalArea(properties);
		importOutputFolder(properties);
		importVisualizeAscii(properties);
	}
	
	@Override
	public void doExport(Properties properties){
		exportInputMatrix(properties);
		exportCluster(properties);
		exportClusterType(properties);
		exportClusterDistance(properties);
		exportClusterFriction(properties);
		exportClusterFrictionMatrix(properties);
		exportMinimumTotalArea(properties);
		exportOutputFolder(properties);
		exportVisualizeAscii(properties);
	}

	@Override
	public void run() {
		getController().runCluster(inputMatrix, vclusters, typeCluster, distance, minimumTotalArea, clusterFriction, (clusterFrictionMatrix.size()==0)?null:clusterFrictionMatrix.iterator().next(), taOutputFolder.getText(), viewAsciiOutput.isSelected());
	}

}
