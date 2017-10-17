package fr.inra.sad_paysage.chloe.view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;

import fr.inra.sad_paysage.chloe.controller.Controller;
import fr.inra.sad_paysage.chloe.controller.LocalContext;
import fr.inra.sad_paysage.chloe.view.treatment.TreatmentPanel;
import fr.inra.sad_paysage.chloe.view.wizard.Wizard;

public class Ihm {

	private Controller controller;
	
	private JFrame frame;
	
	private Wizard wizard;
	
	private TreatmentProgressBar progressBar;
	
	@SuppressWarnings("unused")
	private ServerSocket socket;
	
	public Ihm(ServerSocket ss){
		this.socket = ss;
	}
	
	public Ihm(){	}

	public void setController(Controller controller){
		this.controller = controller;
	}
	
	public JFrame getFrame() {
		if (frame == null) {
			frame = new JFrame();
			
			// application du look and feel Nimbus
			for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels() ){
				//System.out.println(laf.getName());
				
				if ("Nimbus".equals(laf.getName())) {
					try {
						UIManager.setLookAndFeel(laf.getClassName());	
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			
			//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			frame.setMinimumSize(new Dimension(1400, 850));
			frame.setContentPane(getPanel());
			frame.setTitle("Chloe2012");
			frame.addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosing(WindowEvent e) {
					controller.close();
					System.exit(0);
				}
			});
			
			frame.setIconImage(Toolkit.getDefaultToolkit().getImage(LocalContext.get().getIcon()));
			
			progressBar = new TreatmentProgressBar(frame);
			
			frame.setVisible(true);
		}
		return frame;
	}

	public Container getPanel() {
		if (wizard == null) {
			wizard = new Wizard(controller, this);
			wizard.start(new ChooseTreatmentPanel(wizard));
		}
		return wizard;
	}
	
	public void start(){
		frame.setEnabled(false);
		progressBar.setVisible(true);
	}
	
	public void resetProgressBar(){
		frame.setEnabled(true);
		progressBar.dispose();
		progressBar.setProgress(0);
	}
	
	public void reset(){
		resetProgressBar();
		wizard.previous();
	}
	
	public void setProgress(int progress) {
		progressBar.setProgress(progress);
	}
	
	public void publish(String text){
		progressBar.publish(text);
	}
	
	public void message(String message){
		wizard.showErrorMessage(message);
	}

	public void importTreatment() {
		JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
		if(fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
			LocalContext.get().setRepData(fc.getSelectedFile().toString());
			
			String file = fc.getSelectedFile().toString();
			
			try{
				Properties properties = new Properties();
				FileInputStream in = new FileInputStream(file);
				properties.load(in);
				in.close();
				
				String treatment = properties.getProperty("treatment");
				TreatmentPanel panel;
				if(wizard.getCurrent().toString().equals("choose treatment")){
					panel = wizard.next(treatment);
				}else if(wizard.getCurrent().toString().equals(treatment)){
					panel = (TreatmentPanel) wizard.getCurrent();
				}else{
					wizard.previous();
					panel = wizard.next(treatment);
				}
				
				panel.importTreatment(file);
					
			}catch(FileNotFoundException ex){
				ex.printStackTrace();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
	}

	public void exportTreatment() {
		JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
		if(fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
			LocalContext.get().setRepData(fc.getSelectedFile().toString());
			
			String file = fc.getSelectedFile().toString();
			
			if(!file.endsWith(".properties")){
				file += ".properties";
			}
			
			((TreatmentPanel) wizard.getCurrent()).exportTreatment(file);
		}
	}

}
