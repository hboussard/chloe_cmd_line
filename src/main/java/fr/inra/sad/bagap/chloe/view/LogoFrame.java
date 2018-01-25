package fr.inra.sad.bagap.chloe.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import fr.inra.sad.bagap.chloe.controller.LocalContext;

public class LogoFrame extends Frame {
	
	/** version */
	private static final long serialVersionUID = 1L;
	
	/** image */
    private Image img; 
     
    /** constructor */
    public LogoFrame() { 
        super(); 
        setSize(300,300); 
        setUndecorated(true); 
        setFocusable(false); 
        setEnabled(false); 
        setTitle("Chloe-4.0");
		setIconImage(Toolkit.getDefaultToolkit().getImage(LocalContext.get().getIcon()));
        img = this.getToolkit().createImage(LocalContext.get().getLogo());
        setLocationRelativeTo(null);
		setVisible(true);
    } 
     
    @Override
    public void paint(Graphics g){ 
        super.paint(g); 
        Dimension d = this.getSize(); 
        g.drawImage(img,0,0,d.width,d.height,this); 
    } 
 
} 
