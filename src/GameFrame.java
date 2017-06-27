import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameFrame extends JFrame implements Observer{
	
	GameSettings settings;
	JDialog aboutDialog;
	
	public GameFrame(GameSettings gameSettings) {
		super(gameSettings.name);
		
		settings = gameSettings;
		settings.addObserver(this);
		setSize(settings.frameSize);
		setLocationRelativeTo(null);  // *** this will center your app ***
		setIconImage((new ImageIcon(settings.paintIconPath)).getImage());
        
		aboutDialog = new AboutDialog(this);
		aboutDialog.setLocationRelativeTo(null);
	}

	public void update(String action) {
		switch(action) {
		case "SHOW_ABOUT":
			aboutDialog.setVisible(true);
		}
		display();
	}
	
	public void display() {
		setSize(settings.frameSize);
	}
	
	class AboutDialog extends JDialog
	{
		public AboutDialog(JFrame owner)
		{
			super(owner, "About 15-Puzzle", true);
	 
			JLabel label = new JLabel(
				"<html><h1>15-puzzle</h1><hr>"
				+ "The 15-puzzle is a sliding puzzle that consists of a frame"
				+ "of numbered square tiles in random order with one tile missing."
				+ "The object of the puzzle is to place the tiles in order by making"
				+ "sliding moves that use the empty space."
			);
			add(label);
			
			setSize(260, 220);
		}
	}

	
}
