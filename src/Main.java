import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;


public class Main {
    
	public static void main(String[] args) {
		
        JFrame frame = new JFrame(Config.name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Config.frame);
//        frame.setIconImage((new ImageIcon(Config.paintIconPath)).getImage());
		frame.setLocationRelativeTo(null);  // *** this will center your app ***
		frame.add( new Puzzle() );
        frame.setVisible(true);
        
    }
	
}
