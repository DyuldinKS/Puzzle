import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    
	public static void main(String[] args) {
		
		GameSettings settings = new GameSettings();
		GameFrame frame = new GameFrame(settings);
		frame.setJMenuBar( new GameMenu(settings) );
		frame.add( new Field(settings) );
		frame.setVisible(true);
		
	}
}
