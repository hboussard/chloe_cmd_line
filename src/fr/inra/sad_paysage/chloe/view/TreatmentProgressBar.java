package fr.inra.sad_paysage.chloe.view;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class TreatmentProgressBar extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JTextArea textArea;
	
    private JProgressBar progressBar;
    
    private int progress;
	
	public TreatmentProgressBar(JFrame frame){
		super(frame);
		textArea = new JTextArea(10, 50);
        textArea.setEnabled(false);
		progressBar = new JProgressBar();
		JPanel content = new JPanel(new BorderLayout());
		content.add(new JScrollPane(textArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		JPanel south = new JPanel(new BorderLayout());
		south.add(progressBar);
		content.add(south, BorderLayout.SOUTH);
		setContentPane(content);
		setLocation((frame.getWidth() - getWidth()) / 2, (frame.getHeight() - getHeight()) / 2);
		pack();
//		init();
//		addWindowListener(new WindowAdapter(){
//			@Override
//			public void windowClosing(WindowEvent e) {
//				TreatmentProgressBar.this.treatment.abort();
//			}			
//		});
		//setVisible(true);
	}

	protected JTextArea getTextArea() {
		return textArea;
	}

	protected JProgressBar getProgressBar() {
		return progressBar;
	}	
	
	public void setProgress(int progress) {
		if(this.progress != progress){
			this.progress = progress; 
			progressBar.setValue(progress);
			setTitle("("+progress+"%)");
		}
		
	}
	
	public void publish(String text){
		textArea.append(text+"\n");
	}

}

