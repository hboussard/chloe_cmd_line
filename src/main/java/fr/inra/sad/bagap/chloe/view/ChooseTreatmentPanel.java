package fr.inra.sad.bagap.chloe.view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.inra.sad.bagap.chloe.controller.ChloeContext;
import fr.inra.sad.bagap.chloe.view.treatment.TreatmentPanel;
import fr.inra.sad.bagap.chloe.view.wizard.Wizard;
import fr.inra.sad.bagap.chloe.view.wizard.WizardPanel;

public class ChooseTreatmentPanel extends WizardPanel {

	private static final long serialVersionUID = 1L;
	
	private JInternalFrame treatments;
	
	private TreatmentTree treatmentTree;
	
	private Wizard wizard;
	
	private JDesktopPane desktop;
	
	public ChooseTreatmentPanel(Wizard wizard){
		this.wizard = wizard;
	}
	
	@Override
	public String toString(){
		return "choose treatment";
	}
	
	@Override
	public void display() {
		setLayout(new BorderLayout());
		add(getTreatments(), BorderLayout.WEST);
		add(getDesktop(), BorderLayout.CENTER);
	}
	
	public JInternalFrame getTreatments() {
		if(treatments == null){
			treatments = new JInternalFrame("treatment",true,false,false,false);
			JScrollPane spView = new JScrollPane();
			spView.setViewportView(geTreatmentTree());
			treatments.setContentPane(spView);
			treatments.setBounds(
					0,
					0, 
					new Double(0.25*getDesktop().getBounds().width).intValue(),
					getDesktop().getBounds().height);
			treatments.setVisible(true);
		}
		return treatments;
	}
	
	public TreatmentTree geTreatmentTree(){
		if(treatmentTree == null){
			treatmentTree = new TreatmentTree(wizard);
			wizard .setTreatmentTree(treatmentTree);
		}
		return treatmentTree;
	}

	public JDesktopPane getDesktop() {
		if (desktop == null) {
			desktop = new DesktopPaintPanel(ChloeContext.get().getLogo());
		}
		return desktop;
	}
	

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public boolean validateNext(List<String> list) {
		if(geTreatmentTree().getSelectionCount() == 0
				|| !(((DefaultMutableTreeNode) geTreatmentTree().getLastSelectedPathComponent()).isLeaf())){
			list.add("you must choose a treatment !");
			return false;
		}
		return true;
	}

	@Override
	public WizardPanel next(Wizard wizard) {
		return (TreatmentPanel) ((DefaultMutableTreeNode) geTreatmentTree().getLastSelectedPathComponent()).getUserObject();
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}

	@Override
	public boolean canRun() {
		return false;
	}

	@Override
	public boolean validateRun(List<String> list) {
		return false;
	}

	@Override
	public void run() {	}
	
}
