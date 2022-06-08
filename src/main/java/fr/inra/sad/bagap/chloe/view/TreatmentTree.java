package fr.inra.sad.bagap.chloe.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import fr.inra.sad.bagap.chloe.controller.ChloeContext;
import fr.inra.sad.bagap.chloe.view.treatment.*;
import fr.inra.sad.bagap.chloe.view.wizard.Wizard;

public class TreatmentTree extends JTree{

	private static final long serialVersionUID = 1L;
	
	private Wizard wizard;
	
	public static final int r = 255, g = 255, b = 255;
	//public static final int r = 245, g = 245, b = 255;
	
	public TreatmentTree(Wizard wizard){
		super();
		this.wizard = wizard;
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setRootVisible(false);
		loadTree();
		((DefaultTreeModel)getModel()).reload();
		expandAll();
		addMouseListener(this.wizard);
		
		setBackground(new Color(r, g , b));
		setFont(new Font("Verdana", Font.BOLD, 13));
		
		setCellRenderer(new DefaultTreeCellRenderer(){
			private static final long serialVersionUID = 1L;
			
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, 
                    boolean selected, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {
				
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
				Icon icone = null;
				if(node.getUserObject() instanceof TreatmentPanel){
	  				icone = new ImageIcon(ChloeContext.get().getRepImages() + "treatment.png");
	  				//icone = new ImageIcon(LocalContext.get().getRepImages() + "chloe_icon.jpg");
	  			}else{
	  				icone = new ImageIcon(ChloeContext.get().getRepImages() + "group.png");
	  			}
				
				setBackgroundNonSelectionColor(new Color(r, g , b));
				
	  			setOpenIcon(icone);
	        	setLeafIcon(icone);
	        	setClosedIcon(icone);
				
	        	super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus );
	        	
				return this;
			}
		});
	}
	
	public void expandAll() {  
	    for (int i=0; i<getRowCount(); i++) {
	      expandRow(i);
	    }
	}
	
	public TreatmentPanel getPanel(String treatment){
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)getModel().getRoot();
		DefaultMutableTreeNode block, node;
		int index = -1;
		for(int i=0; i<root.getChildCount(); i++){
			block = (DefaultMutableTreeNode) root.getChildAt(i);
			index++;
			for(int j=0; j<block.getChildCount(); j++){
				index++;
				node = (DefaultMutableTreeNode) block.getChildAt(j);
				if(node.getUserObject().toString().equalsIgnoreCase(treatment)){
					setSelectionRow(index);
					return (TreatmentPanel) node.getUserObject();
				}
			}
		}
		throw new IllegalArgumentException(treatment);
	}
	
	private void loadTree(){
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)getModel().getRoot();
		root.removeAllChildren();
		DefaultMutableTreeNode node;
		
		node = new DefaultMutableTreeNode("generate ascii grid");
		node.add(new DefaultMutableTreeNode(new ExportAsciiGridFromCsvPanel(wizard)));
		node.add(new DefaultMutableTreeNode(new ExportAsciiGridFromShapefilePanel(wizard)));
		root.add(node);
		node = new DefaultMutableTreeNode("util");
		node.add(new DefaultMutableTreeNode(new SearchAndReplacePanel(wizard)));
		node.add(new DefaultMutableTreeNode(new OverlayPanel(wizard)));
		node.add(new DefaultMutableTreeNode(new ClassificationPanel(wizard)));
		node.add(new DefaultMutableTreeNode(new CombinePanel(wizard)));
		node.add(new DefaultMutableTreeNode(new FilterPanel(wizard)));
		root.add(node);
		node = new DefaultMutableTreeNode("connectivity");
		node.add(new DefaultMutableTreeNode(new DistancePanel(wizard)));
		node.add(new DefaultMutableTreeNode(new ClusterPanel(wizard)));
		root.add(node);
		node = new DefaultMutableTreeNode("landscape metrics");
		node.add(new DefaultMutableTreeNode(new MapWindowPanel(wizard)));
		node.add(new DefaultMutableTreeNode(new GridWindowPanel(wizard)));
		node.add(new DefaultMutableTreeNode(new SlidingWindowPanel(wizard)));
		node.add(new DefaultMutableTreeNode(new SelectedWindowPanel(wizard)));
		root.add(node);
	}

}
