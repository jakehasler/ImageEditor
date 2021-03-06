package editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

// IMAGE EDITOR

public class ImageEditor {
	
	private static int width;
	private static int height;
	private static int maxColor;
	
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

		//System.out.println("File Name: " + fileName);

		File file = new File("src/editor/" + fileName);
		// File file = new File(fileName);
		//File file = new File("src/editor/" + "slctemple.ppm");
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
		while(counter < 5)
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
					case 4:
						maxColor = Integer.valueOf(sc.next());
					default: break;
				}
			}
			else {
				sc.next();
			}
		} // End initial while loop
		
//		System.out.println("Width: " + width);
//		System.out.println("Height: " + height);
//		System.out.println("Max Color: " + maxColor);
		
		// Initialize to size of found height
		image = new Pixel[height][width];
		embossedImage = new Pixel[height][width];
		blurredImage = new Pixel[height][width];
		
		
		// Fill the image array with all the pixel values
		fillImage(sc);
	
		
	}
	
	
	public void fillImage(Scanner sc) {
		// Filling the image with the pixel for each width.
		for(int x = 0; x < height; x++) {
			for(int y = 0; y < width; y++) {

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
		for(int x = 0; x < height; x++) {
			for(int y = 0; y < width; y++) {
				image[x][y].grayscale();
			}
		}
	}
	
	public void invert() {
		for(int x = 0; x < height; x++) {
			for(int y = 0; y < width; y++) {
				image[x][y].invert();
			}
		}
	}
	
	
	public void emboss() {
		embossed = true;
		for(int x = 0; x < height; x++) {
			for(int y = 0; y < width; y++) {
				
				int defaultVal = 128;
				
				if(x-1 < 0 || y-1 < 0) {
					Pixel newPixel = new Pixel(defaultVal, defaultVal, defaultVal);
					embossedImage[x][y] = newPixel;
				}
				else {
					int redDiff = image[x][y].getRed() - image[x - 1][y - 1].getRed();
					int greenDiff = image[x][y].getGreen() - image[x - 1][y - 1].getGreen();
					int blueDiff = image[x][y].getBlue() - image[x - 1][y - 1].getBlue(); 
					
					int maxDifference = 0;
					
					int bigDiff = Math.max(Math.max(Math.abs(redDiff), Math.abs(greenDiff)), Math.abs(blueDiff));
		
					
					if(bigDiff == Math.abs(redDiff)) {
						maxDifference = redDiff;
					}
					else if(bigDiff == Math.abs(greenDiff)) {
						maxDifference = greenDiff;
					}
					else if (bigDiff == Math.abs(blueDiff)) {
						maxDifference = blueDiff;
					}
					
					int v = defaultVal + maxDifference;
					
					if(v < 0) {
						v = 0;
					}
					else if(v > 255) {
						v = 255;
					}
					
					Pixel newPixel = new Pixel(v, v, v);
					embossedImage[x][y] = newPixel;
				}
				
			}
		}
	}
	
	
	public void motionBlur(int n) {
		motionBlurred = true;
		
		for(int x = 0; x < height; x++) {
			for(int y = 0; y < width; y++) {
				
				int pixelsOnRight = Math.min(n - 1, width - y - 1);
				Pixel[] valuesToRight = Arrays.copyOfRange(image[x], y + 1, y + pixelsOnRight + 1);
				
				int lastPossiblePixel = y + n;
				int endPixel;
				
				if(lastPossiblePixel >= width) {
					endPixel = width;
				}
				else { 
					endPixel = y + n;
				}
				
				int startPixel = y;
				
				int avgCount = endPixel - startPixel;
				if(endPixel == y) avgCount = width - x;
				int sumRed = 0;
				int sumGreen = 0;
				int sumBlue = 0;
				
				for(int i = startPixel; i < endPixel; i++) {
					sumRed += image[x][i].getRed();
					sumGreen += image[x][i].getGreen();
					sumBlue += image[x][i].getBlue();
				}
				
				int finRed = sumRed/avgCount;
				int finGreen = sumGreen/avgCount;
				int finBlue = sumBlue/avgCount;
				
				blurredImage[x][y] = new Pixel(finRed, finGreen, finBlue);
				
			}
		}
		
		
	}
	
	
	// Printing each Pixel
	public void print() {
		
		for(int x = 0; x < height; x++) {
			for(int y = 0; y < width; y++) {
			   System.out.println(image[x][y].print());	
			   
			}
		}

		
	}
	
	
	public void writeFile(String outfileName) {
		
		
		StringBuilder strBld = new StringBuilder();
		
		//File outfile = new File("src/editor/" + outfileName);
			
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter("src/editor/" + outfileName);
			//pw = new PrintWriter(outfileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pw.println("P3");
		pw.println("# File Created by Jake Hasler");
		pw.println(width);
		pw.println(height);
		pw.println(maxColor);

		for(int x = 0; x < height; x++) {
			for(int y = 0; y < width; y++) {
				if(embossed) {
					pw.println(embossedImage[x][y].print());
				}
				else if(motionBlurred) {
					pw.println(blurredImage[x][y].print());
				}
				else {
					pw.println(image[x][y].print());
				}

			}
		}
		
		//System.out.println("File \"" + outfileName + "\" has been saved.");
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
		
		String functionCall = args[2];
		//String functionCall = "motionblur";
		
		//System.out.println("Method called: " + functionCall);
		
		// Always need to use the 'new' keyword for instantiation of objects. 
		Pixel test = new Pixel(24, 35, 24);
		
		//test.prettyPrint();
		
		myEditor.readFile(args[0]);
		
		// if args.length
		//    if file.exists() || file.canRead()
		// public static final <- constant in java
		
		//myEditor.print();
		
		
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
		
		
		
		myEditor.writeFile(args[1]);

		
    }
    
}
