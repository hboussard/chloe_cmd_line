package fr.inra.sad_paysage.chloe.view;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.JDesktopPane;

public class DesktopPaintPanel extends JDesktopPane {

	private static final long serialVersionUID = 1L;
	
	private Image image;
	
	public DesktopPaintPanel(String image){
		super();
		try {
			this.image = javax.imageio.ImageIO.read(new File(image));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}

}
