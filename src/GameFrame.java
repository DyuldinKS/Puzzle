import javax.swing.JFrame;

public class GameFrame extends JFrame implements Observer{
	
	GameSettings settings;
	
	public GameFrame(GameSettings gameSettings) {
		super(gameSettings.name);
		settings = gameSettings;
		settings.addObserver(this);
		setSize(settings.frameSize);
		setLocationRelativeTo(null);  // *** this will center your app ***
	}

	public void update(String action) {
		if(action == "CHANGE_GAME_MODE") {
			setSize(settings.frameSize);
		}
		// TODO Auto-generated method stub
		
	}
	
	

	
	
}
