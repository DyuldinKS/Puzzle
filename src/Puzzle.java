import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.*;

public class Puzzle extends JPanel{
	
	private static Dice emptyCell;
	private final int w = Config.field.width;
	private final int h = Config.field.height;
	private Dice[] field;
	private List<Color> colors;
	
	
	public Puzzle() {
		super();
		
		field = new Dice[w * h];
		colors = readColors(Config.colorsPath);
		
        setLayout(new GridLayout(h, w));
        addButtons();
        
        setFocusable(true);
        addKeyListener(new KeyListener() {
    	    public void keyPressed(KeyEvent e) {
    	    	keyPressHandler( e.getKeyCode() );
    	    }
    	    public void keyReleased(KeyEvent e) {}
    	    public void keyTyped(KeyEvent e) {}
    	});
	}

	
	private static class Dice extends JButton {
		
		private Color color;
		private int position;
		
		private Dice(int index) {
			super();
			setStyle();
			this.position = index;
			
			setFocusable(false);
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(isNear(emptyCell)) {
						moveToEmptyCell();
					}
				}	
			});
		}
		
		private Dice(int pos, Color color, String text) {
			this(pos);
			setText(text);
			setColor(color);
		}
		
		private void setStyle() {
			setPreferredSize(Config.button);
			setBorderPainted(false);
			setFocusPainted(false);
			setFont(new Font("Calibri", Font.PLAIN, Config.button.height / 3));
			setForeground(new Color(250, 250, 250));
		}
		
		private boolean isNear(Dice dice) {
			int diff = Math.abs(dice.position - position);
			final int w = Config.field.width;
			return  diff == w || diff == 1 && dice.position / w == position / w;
	    }
		
		private void moveToEmptyCell() {
			String text = getText();
			Color color = getColor();
			
			setColor( emptyCell.getColor() );
			setText( emptyCell.getText() );
			
			emptyCell.setColor(color);
			emptyCell.setText(text);
			emptyCell = this;
		}
		
		private Color getColor() {
			return color;
		}

		private void setColor(Color color) {
			this.color = color;
			setBackground(color);
		}
		
		public String toString() {
			return color + ", " + position + ", " + getText();
		}

	}
	
	
	private void keyPressHandler(int keyCode) {
	
		int shift = 0;
    	switch( keyCode ) {
    	case KeyEvent.VK_LEFT:
    		shift = 1;
    		break;
    	case KeyEvent.VK_RIGHT:
    		shift = -1;
    		break;
    	case KeyEvent.VK_UP:
    		shift = w;
    		break;
    	case KeyEvent.VK_DOWN:
    		shift = -w;
    		break;
    	}
    	
    	int xPos = emptyCell.position + shift;
    	if( shift != 0 && xPos >= 0 && xPos < w * h
    		&& ( xPos / w == emptyCell.position / w || Math.abs(shift) == w )) {
    		field[ xPos ].moveToEmptyCell();
    	}	
		
	}
	
	
	private void moveDice(KeyEvent e) {
		int xPos = -1;
    	int shift = 0;
    	switch( e.getKeyCode() ) {
    	case KeyEvent.VK_LEFT:
    		shift = 1;
    		break;
    	case KeyEvent.VK_RIGHT:
    		shift = -1;
    		break;
    	case KeyEvent.VK_UP:
    		shift = w;
    		break;
    	case KeyEvent.VK_DOWN:
    		shift = -w;
    		break;
    	}
    	
    	xPos = emptyCell.position + shift;
    	if( shift != 0 && xPos >= 0 && xPos < w * h
    		&& ( xPos / w == emptyCell.position / w || Math.abs(shift) == w )) {
    		field[ xPos ].moveToEmptyCell();
    	}
	}
	
	
	private void addButtons() {
		
		int i;
		int[] seq = genShuffledSequence(w * h - 1);
		Dice dice;
		
		for(i = 0; i < seq.length; i++) {
			dice = new Dice( i, colors.get( seq[i] % colors.size() ), Integer.toString( seq[i] + 1 ) );
			field[i] = dice;
			add(dice);
		}
		
		emptyCell = new Dice(i, Config.emptyCellColor, "");
		field[i] = emptyCell;
		add(emptyCell);
		
	}
	
	
	private int[] genShuffledSequence(int length) {
		
		int i, j, temp;
		int[] seq = new int[length];
		for(i = 0; i < seq.length; i++) { seq[i] = i; }
		
		for(int n = 0; n < 100; n++) {
			i = (int)(Math.random() * (seq.length));
			j = (int)(Math.random() * (seq.length));
			temp = seq[i];
			seq[i] = seq[j];
			seq[j] = temp;
		}
		
		return seq;
		
	}
	
	
	private List<Color> readColors(String fileName) {
		
		List<Color> colors;
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			colors = stream
					.map(this::parseStringToColor)
					.collect(Collectors.toList());
		} catch (IOException e) {
			System.out.println("Colors file not found");
			e.printStackTrace();
			colors = new ArrayList<Color>();
		}
		return colors;
		
	}
	
	
	private Color parseStringToColor(String color) {
		
		String[] channels = color.replaceAll("[^0-9.,]+", "").split(",");
		int[] ch = new int[channels.length];
		
		for(int i = 0; i < channels.length; i++) {
			ch[i] = Integer.parseInt(channels[i]);
		}
		
		return new Color( ch[0], ch[1], ch[2] );
		
	}
	
}
