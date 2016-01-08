package editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

// IMAGE EDITOR

public class ImageEditor {
	
	private static int width;
	private static int height;
	
	private boolean embossed = false;
	private boolean motionBlurred = false;
	
	private static Pixel[][] image;
	
	private static Pixel[][] embossedImage;
	private static Pixel[][] blurredImage;

	
	public void readFile(String fileName) {
		// Read in file
		// Store height and width
		// set 'image' to height and width
		// fill 'image' with 'Pixels'
		//   * Each Three Values per pixel

		System.out.println("File Name: " + fileName);

		File file = new File("src/editor/" + fileName);
		// How to print out the whole path
		//System.out.println(file.getAbsolutePath());
		
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(fr);	
		Scanner sc = new Scanner(br);
		sc.useDelimiter("(\\s+)(#[^\\n]*\\n)?(\\s*)|(#[^\\n]*\\n)(\\s*)");
		
		int counter = 0;
		while(counter < 4)
		{
			++counter;
			if(counter > 1) {
				switch(counter) {
					case 2: 
						width = Integer.valueOf(sc.next());
						break;
					case 3: 
						height = Integer.valueOf(sc.next());
						break;
					default: break;
				}
			}
			else {
				sc.next();
			}
		} // End initial while loop
		
		System.out.println("Width: " + width);
		System.out.println("Height: " + height);
		
		// Initialize to size of found height
		image = new Pixel[width][height];
		embossedImage = new Pixel[width][height];
		blurredImage = new Pixel[width][height];
		
		
		// Fill the image array with all the pixel values
		fillImage(sc);
		
	}
	
	
	public void fillImage(Scanner sc) {
		// Filling the image with the pixel for each width.
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int a = sc.nextInt();
				int b = sc.nextInt();
				int c = sc.nextInt();
				// fill it with the three pixels
				Pixel newPixel = new Pixel(a, b, c);
				image[x][y] = newPixel;
			}
		}
	}
	
	
	public void grayscale() {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				image[x][y].grayscale();
			}
		}
	}
	
	public void invert() {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				image[x][y].invert();
			}
		}
	}
	
	public String getBiggestDiff(int red, int green, int blue) {
		String biggest = "";
		
		if(Math.abs(red) > Math.abs(green) && Math.abs(red) > Math.abs(blue)) {
			biggest = "red";
		}
		else if(Math.abs(green) > Math.abs(red) && Math.abs(green) > Math.abs(blue)) {
			biggest = "green";
		}
		else if(Math.abs(blue) > Math.abs(red) && Math.abs(blue) > Math.abs(green)) {
			biggest = "blue";
		}
		
		return biggest;
	}
	
	
	public void emboss() {
		embossed = true;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				
				if(x != 0 && y != 0) {
					int redDiff = image[x][y].getRed() - image[x - 1][y - 1].getRed();
					int greenDiff = image[x][y].getGreen() - image[x - 1][y - 1].getGreen();
					int blueDiff = image[x][y].getBlue() - image[x - 1][y - 1].getBlue(); 
					
					int maxDifference = 0; 
					
					String biggestDiff = getBiggestDiff(redDiff, greenDiff, blueDiff);
	
					switch(biggestDiff) {
						case "red":
							maxDifference = redDiff;
							break;
						case "green":
							maxDifference = greenDiff;
							break;
						case "blue":
							maxDifference = blueDiff;
							break;
						default:
							break;
					}
					
					int v = 128 + maxDifference;
					
					if(v < 0) v = 0;
					if(v > 255) v = 255;
					
					Pixel newPixel = new Pixel(v, v, v);
					embossedImage[x][y] = newPixel;
				}
				else {
					int defaultVal = 128;
					Pixel newPixel = new Pixel(defaultVal, defaultVal, defaultVal);
					embossedImage[x][y] = newPixel;
				}
				
			}
		}
	}
	
	public void motionBlur(int blurAmount) {
		motionBlurred = true;
		
	}
	
	
	// Printing each Pixel
	public void print() {
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				image[x][y].prettyPrint();
			}
		}
		
	}
	
	
	public void writeFile(String outfileName) {
		
		File outfile = new File("src/editor/" + outfileName);
			
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter("src/editor/" + outfileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pw.println("P3");
		pw.println("# File Created by Jake Hasler");
		pw.println(width);
		pw.println(height);

		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(embossed) {
					pw.println(embossedImage[x][y].print());
				}
				else if(motionBlurred) {
					pw.println(blurredImage[x][y].print());
				}
				else {
					pw.println(image[x][y].print());
				}
				//pw.println("hello");
			}
		}
		
		System.out.println("File \"" + outfileName + "\" has been saved.");
		pw.close();
	}
	
	public void printUsage() {
		System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
	}
	
	
	public static void main(String[] args) {
		
		// args[0] input file
		// args[1] output file
		// args[2] grayscale | invert | emboss | motionblur
		// args[3] blur amount -> 25 max
		
		ImageEditor myEditor = new ImageEditor();
		
		//String functionCall = args[2];
		String functionCall = "emboss";
		
		System.out.println("Method called: " + functionCall);
		
		// Always need to use the 'new' keyword for instantiation of objects. 
		Pixel test = new Pixel(24, 35, 24);
		
		test.prettyPrint();
		
		myEditor.readFile(args[0]);
		
		
		// Run function from command line arg
		switch(functionCall) {
			case "grayscale": 
				myEditor.grayscale(); 
				break;
			case "invert":
				myEditor.invert(); 
				break;
			case "emboss":
				myEditor.emboss(); 
				break;
			case "motionblur":
				if(args[3] != null) {
					int blurAmount = Integer.parseInt(args[3]);
					myEditor.motionBlur(blurAmount); 
				}
				else {
					System.out.println("SORRY! Incorrect Function Option");
					myEditor.printUsage();
				}
				break;
			default: 
				System.out.println("SORRY! Incorrect Function Option");
				myEditor.printUsage();
				break;
		}
		
		//myEditor.print();
		
		myEditor.writeFile(args[1]);

		
		
		
    }
    
}
