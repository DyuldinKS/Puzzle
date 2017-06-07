import java.awt.Color;
import java.awt.Dimension;

public class Config {
	
	final static protected String name = "15Puzzle";
	
	final static Dimension frame = new Dimension(400, 400);
	
	final static protected int minHorizontalPaddings = 10;
	final static protected int minVerticalPaddings = 10;
	final static protected int borderTop = 30;

	final static private int columnAmount = 4;
	final static private int rowAmount = 4;
	final static protected Dimension field = new Dimension(columnAmount, rowAmount);
	final static protected Dimension button = new Dimension(
		(frame.width - minHorizontalPaddings) / columnAmount,
		(frame.height - minVerticalPaddings - borderTop) / rowAmount
	);

	final static protected Color emptyCellColor = new Color(240, 240, 240);
	final static protected String colorsPath = "./src/colors.txt";
	
}
