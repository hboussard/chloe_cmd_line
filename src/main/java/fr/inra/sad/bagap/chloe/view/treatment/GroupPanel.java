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

public class GroupPanel extends TreatmentPanel {

	private static final long serialVersionUID = 1L;
	
	private List<Integer> valuesClusters;
	
	private List<Double> minimumClusters;
	
	private double minimumTotal;
	
	private String typeCluster; 
	
	private double distance;
	
	public GroupPanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "group";
	}
	
	@Override
	protected void locateComponents(){
		title.setText("Group");
		
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
		c.weightx = 4;
		c.gridwidth = 4;
		add(taAsciiInput, c);
	
		c.gridx = 5;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.gridwidth = 1;
		add(bAsciiInput, c);
		
		c.gridx = 6;
		c.gridy = 1;
		add(bViewAsciiInput, c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lHabitats, c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		c.gridheight = 4;
		add(pHabitats, c);
		
		c.gridx = 2;
		c.gridy = 3;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		//c.anchor = GridBagConstraints.LINE_START;
		add(rbHabitatRook, c);
		
		c.gridx = 2;
		c.gridy = 4;
		add(rbHabitatQueen, c);
		
		c.gridx = 2;
		c.gridy = 5;
		add(rbHabitatEuclidian, c);
		
		c.gridx = 2;
		c.gridy = 6;
		add(rbHabitatFunctional, c);
		
		c.gridx = 1;
		c.gridy = 7;
		c.anchor = GridBagConstraints.LINE_END;
		add(lFrictionCluster, c);
		
		c.gridx = 2;
		c.gridy = 7;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(taFrictionCluster, c);
		
		c.gridx = 5;
		c.gridy = 7;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(bFrictionCluster, c);
		
		c.gridx = 1;
		c.gridy = 8;
		c.anchor = GridBagConstraints.LINE_END;
		add(cbGlobalMinimumSurface, c);
		
		c.gridx = 2;
		c.gridy = 8;
		c.anchor = GridBagConstraints.LINE_START;
		add(spGlobalMinimumSurface, c);
		
		c.gridx = 1;
		c.gridy = 9;
		c.anchor = GridBagConstraints.LINE_END;
		add(cbGlobalMaximumSurface, c);
		
		c.gridx = 2;
		c.gridy = 9;
		c.anchor = GridBagConstraints.LINE_START;
		add(spGlobalMaximumSurface, c);
		
		c.gridx = 0;
		c.gridy = 10;
		c.anchor = GridBagConstraints.LINE_END;
		add(lHabitatDistance, c);
		
		c.gridx = 1;
		c.gridy = 10;
		c.anchor = GridBagConstraints.LINE_START;
		add(spHabitatDistance, c);
		
		c.gridx = 0;
		c.gridy = 12;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(lComplementaries, c);
		
		c.gridx = 1;
		c.gridy = 12;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		c.gridheight = 4;
		add(pComplementaries, c);
		
		
		c.gridx = 0;
		c.gridy = 20;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 20;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridwidth = 4;
		add(taOutputFolder, c);
		
		c.gridx = 5;
		c.gridy = 20;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.gridwidth = 1;
		add(bOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 21;
		c.weightx = 0;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(viewAsciiOutput, c);
		
	}
	
	@Override
	public boolean validateRun(List<String> list) {
		boolean validate = true;
		
		valuesClusters = new ArrayList<Integer>();
		minimumClusters = new ArrayList<Double>();
		for(int r : tDistances.getSelectedRows()){
			valuesClusters.add((Integer) tDistances.getModel().getValueAt(r, 0));
			minimumClusters.add(0.0);
			//minimumClusters.add(2.0);
		}
		minimumTotal = 0.0;
		
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
		
		if(taOutputFolder.getText().equalsIgnoreCase("")){
			 list.add("Please choose an ascci grid output matrix file");
			 validate = false;
		}
		
		return validate;
	}
	
	@Override
	public void doImport(Properties properties) {
		importInputAscii(properties);
		importCluster(properties);
		importClusterType(properties);
		importClusterDistance(properties);
		importClusterFriction(properties);
		importClusterFrictionMatrix(properties);
		importOutputFolder(properties);
		importVisualizeAscii(properties);
	}
	
	@Override
	public void doExport(Properties properties){
		exportInputAscii(properties);
		exportCluster(properties);
		exportClusterType(properties);
		exportClusterDistance(properties);
		exportClusterFriction(properties);
		exportClusterFrictionMatrix(properties);
		exportOutputFolder(properties);
		exportVisualizeAscii(properties);
	}

	@Override
	public void run() {
		getController().runGroup(inputMatrix, valuesClusters, minimumClusters, minimumTotal, typeCluster, distance, clusterFriction, (clusterFrictionMatrix.size()==0)?null:clusterFrictionMatrix.iterator().next(), taOutputFolder.getText(), viewAsciiOutput.isSelected());
	}

}
