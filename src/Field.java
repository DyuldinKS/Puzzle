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
import java.awt.image.BufferedImage;
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

import javax.imageio.ImageIO;
import javax.swing.*;

public class Field extends JPanel implements Observer{
	
	private static Dice emptyCell;
	private int cols;
	private int rows;
	private ArrayList<Dice> field;
	private GameSettings settings;
	private List<Color> colors;
	private String mode;
	
	public Field(GameSettings s) {
		super();
		settings = s;
		rows = s.rows;
		cols = s.cols;
		mode = s.gameMode;
		colors = s.colors;
		
		s.addObserver(this);
		
		field = new ArrayList(rows * cols);
        setLayout(new GridLayout(rows, cols));
        create();
        shuffle();
        display();
        
        setFocusable(true);
        addKeyListener(new KeyListener() {
    	    public void keyPressed(KeyEvent e) {
    	    	keyPressHandler( e.getKeyCode() );
    	    }
    	    public void keyReleased(KeyEvent e) {}
    	    public void keyTyped(KeyEvent e) {}
    	});
	}

	
	private class Dice extends JButton {
		
		private Color color;
		private int position;
		private int value;
		
		private Dice(int pos, int val) {
			super();
			setStyle();
			setColor(settings.defaultColor); // default color
			position = pos;
			value = val;
			
			setFocusable(false);
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(isNear(emptyCell)) {
						moveToEmptyCell();
					}
				}	
			});
		}
		
		private void setStyle() {
			setBorderPainted(false);
			setFocusPainted(false);
			setFont(new Font("Calibri", Font.PLAIN, 40 ));
			setForeground(new Color(250, 250, 250));
		}
		
		private boolean isNear(Dice dice) {
			int diff = Math.abs(dice.getPosition() - position);
			final int w = cols;
			return  diff == w || diff == 1 && dice.getPosition() / w == position / w;
	    }
		
		private void moveToEmptyCell() {
			int val = emptyCell.value;
			emptyCell.value = this.value;
			this.value = val;
			emptyCell = this;
			display();
		}
		
		private Color getColor() {
			return color;
		}
		
		private void setColor(Color color) {
			this.color = color;
			setBackground(color);
		}
		
		private int getPosition() {
			return position;
		}
		
		public String toString() {
			return color + ", " + position + ", " + getText();
		}

	}
	
	
	private void create() {
		Dice dice;
		for(int i = 0; i < rows * cols; i++) {
			dice = new Dice(i, i);
			field.add(dice);
			add(dice);
		}
	}
	
	
	private void shuffle() {
		int[] seq = genShuffledSequence(rows * cols);
		for(int i = 0; i < seq.length; i++) {
			field.get(i).value = seq[i];
			if(seq[i] == 0) {
				emptyCell = field.get(i);
			}
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
    		shift = cols;
    		break;
    	case KeyEvent.VK_DOWN:
    		shift = -cols;
    		break;
    	}
    	
    	int xPos = emptyCell.getPosition() + shift;
    	if( shift != 0 && xPos >= 0 && xPos < cols * rows
    		&& ( xPos / cols == emptyCell.getPosition() / cols || Math.abs(shift) == cols )) {
    		field.get(xPos).moveToEmptyCell();
    	}	
		
	}
	
	
	private int[] genShuffledSequence(int length) {
		
		int i, j, temp;
		int[] seq = new int[length];
		for(i = 0; i < seq.length; i++) { seq[i] = i; }
		
		for(int n = 0; n < 100; n++) {
			i = (int)(Math.random() * length);
			j = (int)(Math.random() * length);
			temp = seq[i];
			seq[i] = seq[j];
			seq[j] = temp;
		}
		
		return seq;
		
	}


	public void update(String action) {
		switch(action) {
		case "CHANGE_GAME_MODE":
			mode = settings.gameMode;
			display();
			return;
		case "RESTART":
			shuffle();
			display();
		}
	}
	
	
	protected void display() {
		
		emptyCell.setColor(settings.defaultColor);
		emptyCell.setText("");
		emptyCell.setIcon(null);
		
		switch (mode) {
		case "IMAGE":
			if(settings.imageChunks != null) {
				field.forEach(this::displayBGImage);
				return;
			}
		case "NUMBERS":
			if(settings.colors != null) {
				field.forEach(this::displayNumberWithBGColor);
				return;
			}
		default:
			field.forEach(this::displayNumber);
		}
		
	}
	
	
	private void displayBGImage(Dice dice) {
		if(dice != emptyCell) {
			dice.setColor(null);
			dice.setText("");
			dice.setIcon( settings.imageChunks.get(dice.value - 1) );
		}
	}
	
	private void displayNumberWithBGColor(Dice dice) {
		if(dice != emptyCell) {
			dice.setIcon(null);
			dice.setColor( colors.get( (dice.value - 1) % colors.size() ) );
			dice.setText( Integer.toString(dice.value) );
		}
	}
	
	private void displayNumber(Dice dice) {
		if(dice.value > 0) {
			dice.setIcon(null);
			dice.setColor(null);
			dice.setText( Integer.toString(dice.value) );
		}
	}
	
	
}
