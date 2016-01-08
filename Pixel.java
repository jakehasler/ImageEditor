package editor;

// PIXEL

public class Pixel {
	
	// Constant for Max color value.
	private static int MAX_COLOR = 255;
	
	private int red;
	private int green;
	private int blue;
	
	
	// Constructor Syntax
	public Pixel(int redVal, int greenVal, int blueVal) {
		red = redVal;
		green = greenVal;
		blue = blueVal;
	}
	
	public void invert() {
		int newRed = MAX_COLOR - red;		
		int newGreen = MAX_COLOR - green;
		int newBlue = MAX_COLOR - blue;
		
		red = newRed;
		green = newGreen;
		blue = newBlue;
	}
	
	public void grayscale() {
		
		int total = red + green + blue;
		int average = total/3;
		red = average;
		green = average;
		blue = average;
	}
	
	
	public void prettyPrint() {
		System.out.println("R:" + red + " G:" + green + " B:" + blue);
	}
	
	public String print() {
		String pixel = red + "\n" + green + "\n" + blue;
		
		return pixel;
	}
	
	public int getRed() {
		return red;
	}
	
	public int getGreen() {
		return green;
	}
	
	public int getBlue() {
		return blue;
	}
	
}
