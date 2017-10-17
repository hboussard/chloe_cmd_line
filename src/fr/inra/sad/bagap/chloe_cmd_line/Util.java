package fr.inra.sad.bagap.chloe_cmd_line;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShapeType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class Util {

	public static Set<String> getSetString(String[] tab){
		if(tab != null){
			Set<String> set = new TreeSet<String>();
			for(String s : tab){
				set.add(s);
			}
			return set;
		}
		return null;
	}
	
	public static Matrix getMatrix(String s){
		if(s != null && !s.equalsIgnoreCase("")){
			try {
				return JaiMatrixFactory.get().createWithAsciiGridOld(s, false);
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
			throw new IllegalArgumentException(s+" is not a matrix");
		}
		return null;
	}
	
	public static WindowShapeType getShape(String s){
		return WindowShapeType.get(s);
	}

	public static int getInteger(String s) {
		if(s != null && !s.equalsIgnoreCase("")){
			return Integer.parseInt(s);
		}
		return -1;
	}

	public static boolean getBoolean(String s) {
		if(s != null && !s.equalsIgnoreCase("")){
			return Boolean.parseBoolean(s);
		}
		return Boolean.FALSE;
	}

	public static double getDouble(String s) {
		if(s != null && !s.equalsIgnoreCase("")){
			return Double.parseDouble(s);
		}
		return Double.NaN;
	}

	public static Set<Integer> getSetInteger(String[] tab) {
		Set<Integer> set = new TreeSet<Integer>();
		if(tab != null){
			for(String f : tab){
				set.add(Integer.parseInt(f));
			}
		}
		return set;
	}

	public static Friction getFriction(String s) {
		if(s != null && !s.equalsIgnoreCase("")){
			return new Friction(s);	
		}
		return null;	
	}

	public static List<Integer> getListInteger(String[] tab) {
		List<Integer> list = new ArrayList<Integer>();
		if(tab != null){
			for(String f : tab){
				list.add(Integer.parseInt(f));
			}
		}
		return list;
	}
	
}
