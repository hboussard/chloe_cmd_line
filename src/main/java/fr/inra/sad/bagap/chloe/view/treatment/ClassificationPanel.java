package fr.inra.sad.bagap.chloe.view.treatment;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import fr.inra.sad.bagap.apiland.domain.AllDomain;
import fr.inra.sad.bagap.apiland.domain.BoundedDomain;
import fr.inra.sad.bagap.apiland.domain.Domain;
import fr.inra.sad.bagap.apiland.domain.NumberDomain;
import fr.inra.sad.bagap.chloe.view.wizard.Wizard;

public class ClassificationPanel extends TreatmentPanel { 

	private static final long serialVersionUID = 1L;
	
	private Map<Domain<Double, Double>, Integer> domains;
	
	public ClassificationPanel(Wizard wizard) {
		super(wizard);
	}
	
	@Override
	public String toString(){
		return "classification";
	}
	
	@Override
	protected void locateComponents(){
		title.setText("Classification");
		
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
		//c.weightx = 1;
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
		add(lClassification, c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1;
		c.weighty = 0;
		add(pClassification, c);
		
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0;
		c.weighty = 0;
		add(bNewClassification, c);
		
		c.gridx = 2;
		c.gridy = 4;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		add(bRemoveClassification, c);
		
		c.gridx = 0;
		c.gridy = 5;
		c.anchor = GridBagConstraints.LINE_END;
		add(lOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridwidth = 3;
		add(taOutputFolder, c);
		
		c.gridx = 4;
		c.gridy = 5;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.gridwidth = 1;
		add(bOutputFolder, c);
		
		c.gridx = 1;
		c.gridy = 6;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(viewAsciiOutput, c);
	}
	
	@Override
	public boolean validateRun(List<String> list) {
		boolean validate = true;
		
		if(tClassification.getModel().getRowCount() < 1){
			list.add("Please precise at least one class");
			validate = false;
		}else{
			domains = new HashMap<Domain<Double, Double>, Integer>();
			for(int r=0; r<tClassification.getModel().getRowCount(); r++){
				Domain<Double, Double> d = getDomain((String) tClassification.getModel().getValueAt(r, 0));
				//System.out.println(tClassification.getModel().getValueAt(r, 1).getClass());
				domains.put(d, Integer.parseInt((String) tClassification.getModel().getValueAt(r, 1)));
			}
		}
		
		if(taOutputFolder.getText().equalsIgnoreCase("")){
			 list.add("Please choose an ascci grid output matrix file");
			 validate = false;
		}
		
		return validate;
	}
	
	public static Domain<Double, Double> getDomain(String value) {
		value = value.replace(" ", "");
		if(value.startsWith("[")){
			if(value.endsWith("]")){
				String[] d = value.replace("[", "").replace("]", "").replace(" ", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					double d0 = Double.parseDouble(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new BoundedDomain<Double, Double>(">=", d0, "<=", d1);
					}else{
						return new NumberDomain<Double, Double>(">=", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						double d1 =Double.parseDouble(d[1]);
						return new NumberDomain<Double, Double>("<=", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}else{ // ends with "["
				String[] d = value.replace("[", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					double d0 = Double.parseDouble(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new BoundedDomain<Double, Double>(">=", d0, "<", d1);
					}else{
						return new NumberDomain<Double, Double>(">=", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new NumberDomain<Double, Double>("<", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}
		}else{ // begins with "]"
			if(value.endsWith("]")){
				String[] d = value.replace("]", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					double d0 = Double.parseDouble(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new BoundedDomain<Double, Double>(">", d0, "<=", d1);
					}else{
						return new NumberDomain<Double, Double>(">", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new NumberDomain<Double, Double>("<=", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}else{ // ends with "["
				String[] d = value.replace("[", "").replace("]", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					double d0 = Double.parseDouble(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new BoundedDomain<Double, Double>(">", d0, "<", d1);
					}else{
						return new NumberDomain<Double, Double>(">", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new NumberDomain<Double, Double>("<", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}
		}
	}

	@Override
	public void doImport(Properties properties) {
		importInputAscii(properties);
		importDomains(properties);
		importOutputFolder(properties);
		importVisualizeAscii(properties);
	}
	
	@Override
	public void doExport(Properties properties){
		exportInputAscii(properties);
		exportDomains(properties);
		exportOutputFolder(properties);
		exportVisualizeAscii(properties);
	}

	@Override
	public void run() {
		getController().runClassification(this, inputMatrix, domains, taOutputFolder.getText(), viewAsciiOutput.isSelected());
	}

}
