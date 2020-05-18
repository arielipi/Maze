package view;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.jogamp.opengl.awt.GLJPanel;


public class MyGLJpanel extends GLJPanel {
	/**
	 * Generated Serial ID 
	 */
	private static final long serialVersionUID = 5423041566013863741L;

	public MyGLJpanel() {
		super();
		super.setOpaque(false); //Allows transparency
	}
	@Override
	public void paintComponent(Graphics g) 
    {
    	//Background image.		
        //ImageIcon img = new ImageIcon("texture/backgroundMenu.jpg").getImage();
		File imgFile = new File("src/texture/backgroundMenu.png");
		Image img = null;
		try {
			img = ImageIO.read(imgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), 0 ,0, img.getWidth(null), img.getHeight(null), null);
        this.getTopLevelAncestor().setFocusable(true);
    } 
	
}
