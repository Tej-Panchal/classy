package Views;
// This is the View
// Its only job is to display what the user sees
// It performs no calculations, but instead passes
// information entered by the user to whomever needs
// it. 

import java.awt.*;
import javax.swing.*;

public class DashboardView extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image background = Toolkit.getDefaultToolkit().createImage("assets/dashboard.jpg");
	public DashboardView(){	
	}
	@Override
	protected void paintComponent(Graphics g) {
		// Create rectangle for source image.
		super.paintComponent(g);
		g.drawImage(this.background, 0, 0, this);
	}
}