package fr.inra.sad.bagap.chloe;

import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;

//import fr.inra.sad.bagap.apiland.core.element.manager.DynamicLayerFactory;
//import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
//import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixFactory;
//import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.ProxyMatrix;

public class Decoupeur {

	String inputAscii;
	
	String folder;
	
	int tuile;
	
	int index;
	
	int x;
	
	int y;
	
	BufferedWriter bw;
	
	int CurrentX;
	
	int nrows, ncols;
	
	StringBuffer sb;
	
	String name; // nom du fichier sans l'extension
	
	public Decoupeur(String inputAscii, String folder, int tuile, int index, int x, int y){
		this.inputAscii = inputAscii;
		this.folder = folder;
		this.tuile = tuile;
		this.index = index;
		this.x = x;
		this.y = y;
	}
	
	public void init(){
		/*
		CurrentX = 0;
		
		sb = new StringBuffer();
		
		ProxyMatrix mp = MatrixFactory.readHeader(inputAscii);
		
		File f = new File(inputAscii);
		name = folder+f.getName().replaceAll(".asc", "")+"_"+index;
		
		ncols = Math.min(mp.width() - this.x, tuile);
		nrows = Math.min(mp.height() - this.y, tuile);
		
		double myxllcorner = (mp.minX()+(mp.cellsize()*this.x));
		double myyllcorner = Math.max(mp.minY(), (mp.minY()+((mp.height()-(tuile+this.y))*mp.cellsize())));
		
		try {
			bw = new BufferedWriter(new FileWriter(name+".asc"));
			bw.write("ncols "+ncols);
			bw.newLine();
			bw.write("nrows "+nrows);
			bw.newLine();
			bw.write("xllcorner "+myxllcorner);
			bw.newLine();
			bw.write("yllcorner "+myyllcorner);
			bw.newLine();
			bw.write("cellsize "+mp.cellsize());
			bw.newLine();
			bw.write("NODATA_value "+mp.noDataValue());
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	
	public void setValue(int x, int y, double value){
		if(accept(x, y)){
			
			sb.append(value+" ");
			CurrentX++;
			
			if(CurrentX == ncols){
				CurrentX = 0;
				sb.deleteCharAt(sb.length()-1);
				//System.out.println(sb.toString());
				try{
					bw.write(sb.toString());
					bw.newLine();
					sb.setLength(0);
				}catch(IOException e){
					e.printStackTrace(); 
				}
			}
		}
	}
	
	private boolean accept(int x, int y){
		return x>=this.x && x<this.x+tuile && y>=this.y && y<this.y+tuile;
	}
	
	public void close(){
		/*
		try{
			bw.close();
		}catch(IOException e){
			e.printStackTrace(); 
		}finally{
			try {
				Tool.copy(DynamicLayerFactory.class.getResourceAsStream("lambert93.prj"), name+".prj");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		*/
	}
	
}
