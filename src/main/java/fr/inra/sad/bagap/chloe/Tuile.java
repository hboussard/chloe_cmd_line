package fr.inra.sad.bagap.chloe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Tuile {

	private int index;
	
	private int minX, maxX, minY, maxY;
	
	private int tabX, tabY;
	
	private String folder;
	
	private BufferedReader reader;
	
	public Tuile(int index, int tabX, int tabY, int minX, int maxX, int minY, int maxY, String folder){
		this.index = index;
		this.tabX = tabX;
		this.tabY = tabY;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.folder = folder;
	}
	
	public boolean isYActive(int y){
		return y>=minY && y<=maxY;
	}
	
	public boolean isXActive(int x){
		return x>=minX && x<=maxX;
	}

	public int getIndex() {
		return index;
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getTabX() {
		return tabX;
	}

	public int getTabY() {
		return tabY;
	}
	
	public void openBufferedReaders() {
		try {
			reader = new BufferedReader(new FileReader(folder+"analyse/pf2000n_"+index+"_cr.csv"));
			reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeBufferedReaders() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getValues(String xy) {
		
		try {
			//System.out.println();
			String line = reader.readLine();
			//System.out.println(index+" "+xy+" "+line);
			//System.out.println(line);
			//System.out.println(xy);
			while(!line.startsWith(xy)){
				line = reader.readLine();
				//System.out.println(line);
				//System.out.println(xy);
				//System.out.println(xy+" "+line);
			}
			return line;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		throw new IllegalArgumentException();
	}
	
}
