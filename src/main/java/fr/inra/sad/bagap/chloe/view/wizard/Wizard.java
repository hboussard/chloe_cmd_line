package fr.inra.sad.bagap.chloe.view.wizard;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.Frame;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.inra.sad.bagap.chloe.controller.Controller;
import fr.inra.sad.bagap.chloe.controller.ChloeContext;
import fr.inra.sad.bagap.chloe.view.Ihm;
import fr.inra.sad.bagap.chloe.view.TreatmentTree;
import fr.inra.sad.bagap.chloe.view.treatment.TreatmentPanel;

public class Wizard extends JPanel implements ActionListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	
	private Ihm ihm;
	
	private TreatmentTree treatmentTree;
	
    public static final Dimension WIZARD_WINDOW_SIZE = new Dimension( 450, 200 );

    private final JButton previousButton = new JButton("<< previous");
    private final JButton nextButton = new JButton("next >>");
    private final JButton runButton = new JButton("run");
    private final JButton importButton = new JButton("import");
    private final JButton exportButton = new JButton("export");
    private final JButton helpButton = new JButton("help");

    private Stack<WizardPanel> stack;
    private WizardPanel current;
    private JScrollPane currentSP;
    
    /** Creates a new wizard. */
    public Wizard(Controller controller, Ihm ihm) {
    	this.controller = controller;
    	this.ihm = ihm;
        init();
    }

    public Controller getController(){
    	return controller;
    }
    
    private void init() {
        nextButton.addActionListener(this);
        previousButton.addActionListener(this);
        runButton.addActionListener(this);
        importButton.addActionListener(this);
        exportButton.addActionListener(this);
        helpButton.addActionListener(this);

        nextButton.setEnabled(false);
        previousButton.setEnabled(false);
        runButton.setEnabled(false);
        importButton.setEnabled(true);
        exportButton.setEnabled(false);
        
        runButton.setBackground(Color.BLUE);
        runButton.setForeground(Color.WHITE);
        
        importButton.setBackground(Color.BLUE);
        importButton.setForeground(Color.WHITE);
        
        exportButton.setBackground(Color.BLUE);
        exportButton.setForeground(Color.WHITE);

        helpButton.setBackground(Color.BLUE);
        helpButton.setForeground(Color.WHITE);
        
        setLayout(new BorderLayout());

        JPanel navButtons = new JPanel();
        navButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        navButtons.add(helpButton);
        navButtons.add(previousButton);
        navButtons.add(nextButton);
        navButtons.add(runButton);

        JPanel importExportButtons = new JPanel();
        importExportButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
        importExportButtons.add(exportButton);
        importExportButtons.add(importButton);
        

        JPanel buttons = new JPanel();
        buttons.setLayout(new BorderLayout());
        buttons.add(navButtons, BorderLayout.EAST);
        buttons.add(importExportButtons, BorderLayout.CENTER);

        add(buttons, BorderLayout.SOUTH);

        setMinimumSize( WIZARD_WINDOW_SIZE );
        setPreferredSize( WIZARD_WINDOW_SIZE );
    }

    /** Start this wizard with this panel. */
    public void start(WizardPanel wp) {
        stack = new Stack<WizardPanel>();
        setPanel(wp);
        updateButtons();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String ac = ae.getActionCommand();
        if ("<< previous".equals(ac)) {
            previous();
        } else if ("next >>".equals(ac)) {
            next();
        } else if ("run".equals(ac)) {
            run();
        } else if ("import".equals(ac)) {
        	importation();
        } else if ("export".equals(ac)) {
        	exportation();
        } else if ("help".equals(ac)) {
        	help();
        }
    }
    
    @Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2 && ((TreatmentTree) e.getSource()).getLastSelectedPathComponent() != null){
    		next((TreatmentPanel) ((DefaultMutableTreeNode) ((TreatmentTree) e.getSource()).getLastSelectedPathComponent()).getUserObject());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) { 
		// do nothing 
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// do nothing
	}

	public WizardPanel getCurrent(){
		return current;
	}
	
    private void setPanel(WizardPanel wp) {
        if (current != null) {
            //remove(current);
        	remove(currentSP);
        }

        current = wp;
        if (current == null) {
            current = new NullWizardPanel();
        }
        //add(current, BorderLayout.CENTER);
        currentSP = new JScrollPane(current);
        currentSP.getVerticalScrollBar().setUnitIncrement(10);
        currentSP.getHorizontalScrollBar().setUnitIncrement(10);
        add(currentSP);
      
        setVisible(true);
        revalidate();
        updateUI();
       
        current.display();
        
    }

    private void updateButtons() {
        previousButton.setEnabled(current.hasPrevious());
        nextButton.setEnabled(current.hasNext());        
        runButton.setEnabled(current.canRun());
        exportButton.setEnabled(current.hasPrevious());
    }

    public void previous() {
    	WizardPanel wp = stack.pop();
    	setPanel(wp);
    	updateButtons();
    }

    private void next() {
        List<String> list = new ArrayList<String>();
        if (current.validateNext(list)) {
        	stack.push(current);
            WizardPanel wp = current.next(this);

            setPanel(wp);
            updateButtons();
        } else {
            showErrorMessages(list);
        }
    }
    
    private void next(TreatmentPanel panel) {
        List<String> list = new ArrayList<String>();
        if (current.validateNext(list)) {
        	stack.push(current);

            setPanel(panel);
            updateButtons();
        } else {
            showErrorMessages(list);
        }
    }
    
    public void setTreatmentTree(TreatmentTree treatmentTree){
    	this.treatmentTree = treatmentTree;
    }
    
    public TreatmentPanel next(String treatment) {
    	TreatmentPanel panel = treatmentTree.getPanel(treatment);
    	next(panel);
    	return panel;
    }

    private void run() {
    	List<String> list = new ArrayList<String>();
    	if (current.validateRun(list)) {
    		current.run();
        } else {
            showErrorMessages(list);
        }
    }
    
    private void importation() {
    	ihm.importTreatment();
    }
    
    private void exportation() {
    	ihm.exportTreatment();
    }

    private void help(){
    	if(Desktop.isDesktopSupported()){
			if(Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)){
				try {
					java.awt.Desktop.getDesktop().open(new File(ChloeContext.get().getDocumentation()));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}else{
				System.out.println("opening pdf file unsupported");
			}
		}else{
			System.out.println("pdf file unsupported");
		}
    }
    
    public void showErrorMessage(String message) {
        Window w = SwingUtilities.windowForComponent(this);
        ErrorMessageBox errorMsgBox;
        if (w instanceof Frame) {
            errorMsgBox = new ErrorMessageBox((Frame)w);
        } else if (w instanceof Dialog) {
            errorMsgBox = new ErrorMessageBox((Dialog)w);
        } else {
            errorMsgBox = new ErrorMessageBox();
        }
        errorMsgBox.showErrorMessage(message);
    }
    
    private void showErrorMessages(List<String> list) {
        Window w = SwingUtilities.windowForComponent(this);
        ErrorMessageBox errorMsgBox;
        if (w instanceof Frame) {
            errorMsgBox = new ErrorMessageBox((Frame)w);
        } else if (w instanceof Dialog) {
            errorMsgBox = new ErrorMessageBox((Dialog)w);
        } else {
            errorMsgBox = new ErrorMessageBox();
        }
        errorMsgBox.showErrorMessages(list);
    }

}
