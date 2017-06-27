import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GameSettings implements Observable {
	
	protected String name;
	protected Dimension frameSize;
	protected int rows;
	protected int cols;
	private	ArrayList<Observer> observers;
	protected BufferedImage img;
	protected ArrayList<ImageIcon> imageChunks;
	protected ArrayList<Color> colors;
	protected String gameMode;
	protected Color defaultColor;
	protected java.net.URL paintIconPath;
//	protected static enum GameMode { IMAGE, NUMBERS };
//	public static enum ActionType { CHANGE_GAME_MODE, NEW_GAME, EXIT_GAME };
	
	public GameSettings() {
		name = "15-Puzzle";
		rows = 4;
		cols = 4;
		frameSize = new Dimension(500, 550);
		observers = new ArrayList();
		gameMode = "NUMBERS";
		defaultColor = new Color(240, 240, 240);
		paintIconPath = getClass().getResource("/images/icon.png");
		
		colors = getColors( getClass().getResourceAsStream("/colors.txt") );
		imageChunks = getImageChunks( getClass().getResourceAsStream("/images/test.jpg") );
		
	}
	
	public void addObserver(Observer o) {
		observers.add(o);
	}
	
	public void deleteObserver(Observer o) {
		int i = observers.indexOf(o);
		if(i >= 0) {
			observers.remove(i);
		}
	}
	
	public void notifyObservers(String action) {
		observers.forEach(o -> o.update(action) );
	}
	
	
	protected void setGameMode(String newMode) {
		if(gameMode != newMode) {
			if(newMode == "IMAGE") {
				if(imageChunks == null) {
					return;
				}
				frameSize = new Dimension(img.getWidth() + 8, img.getHeight() + 50);
			} else if(newMode == "NUMBERS") {
				int w = (int)frameSize.getWidth();
				int h = (int)frameSize.getHeight();
				frameSize = (w < h)? new Dimension(w, w + 50) : new Dimension(h - 50, h);
			}
			gameMode = newMode;
			notifyObservers("CHANGE_GAME_MODE");
		}
	}
	
	
	protected void restart() {
		notifyObservers("RESTART");
	}
	
	
	protected void showDialog() {
		notifyObservers("SHOW_ABOUT");
	}
	
	
	private ArrayList<ImageIcon> getImageChunks(InputStream is) {
		try {
			img = ImageIO.read(is);
			img = scaleImage(img);
			return splitImage(img);
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}
	

	private BufferedImage scaleImage(BufferedImage input) {
		int w = input.getWidth();
		int h = input.getHeight();
		double k = (w > h)
				? (double)(frameSize.getHeight() - 50) / h 
				: (double)(frameSize.getWidth() - 8) / w;
		BufferedImage output = new BufferedImage((int)(w * k), (int)(h * k), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(k, k);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		return scaleOp.filter(input, output);
	}
	
	
	private ArrayList<ImageIcon> splitImage(BufferedImage image) {
		
		int w = image.getWidth() / cols; // determines the chunk width and height
		int h = image.getHeight() / rows;
		ArrayList<ImageIcon> icons = new ArrayList(); //Image array to hold image chunks
		BufferedImage chunk;
		
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				//Initialize the image array with image chunks
				chunk = new BufferedImage(w, h, image.getType());
				// draws the image chunk
				Graphics2D gr = chunk.createGraphics();
				gr.drawImage( image, 0, 0, w, h, w * y, h * x, w * y + w, h * x + h, null );
				gr.dispose();
				icons.add( new ImageIcon(chunk) );
			}
		}
		return icons;
	}
	
	
	private ArrayList<Color> getColors(InputStream stream) {
		
		ArrayList<Color> colors;
		try ( BufferedReader reader = new BufferedReader(new InputStreamReader(stream)) ) {
			colors = (ArrayList<Color>) reader.lines()
					.map(this::parseStringToColor)
					.collect(Collectors.toList());
		} catch (IOException e) {
			System.out.println("Colors file not found. Default colors has used.");
			e.printStackTrace();
			colors = new ArrayList<Color>(
				Arrays.asList( 
						new Color(244, 67, 54), new Color(33, 150, 243), 
						new Color(76, 175, 80), new Color(255, 235, 59) 
					)
			);
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
