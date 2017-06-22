import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class GameMenu extends JMenuBar implements Observer {
	
	GameSettings settings;

	public GameMenu(GameSettings gameSettings) {
		super();
		settings = gameSettings;
		settings.addObserver(this);
		add( createGameMenu() );
		add( createSettingsMenu() );
	}
	
	
	private JMenuItem createJMenuItem(String name, ActionListener listener) {
		JMenuItem item = new JMenuItem(name);
		item.addActionListener(listener);
		return item;
	}
	
	
	private JMenu createGameMenu() {
		JMenu game = new JMenu("Game");
		game.add( createJMenuItem("New", e -> settings.restart()) );
		game.add( createJMenuItem("Exit", e -> System.exit(0)) );
		return game;
	}
	
	
	private JMenu createSettingsMenu() {
		JMenu opts = new JMenu("Settings");
		opts.add( createJMenuItem("Image", e -> settings.setGameMode("IMAGE")) );
		opts.add( createJMenuItem("Numbers", e -> settings.setGameMode("NUMBERS")) );
		return opts;
	}


	public void update(String action) {
		// TODO Auto-generated method stub
		
	}

}
