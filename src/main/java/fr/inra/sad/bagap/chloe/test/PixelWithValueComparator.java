package fr.inra.sad.bagap.chloe.test;

import java.util.Comparator;
import java.util.Map;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public class PixelWithValueComparator implements Comparator<Pixel> {

	private Map<Pixel, Double> map;

	public PixelWithValueComparator(Map<Pixel, Double> map) {
		this.map = map;
	}

	@Override
	public int compare(Pixel p1, Pixel p2) {
		if(p1.equals(p2)){
			return 0;
		}
		/*if(!map.containsKey(p1) || !map.containsKey(p2)){
			return -1;
		}*/
		int value = map.get(p1).compareTo(map.get(p2));
		if(value < 1){
			return -1;
		}/*else if(value > 1){
			return 1;	
		}
		if((p1.x() * 152 + p1.y() * 7 + 1) > (p2.x() * 152 + p2.y() * 7 + 1)){
			return -1;
		}*/
		return 1;
	}
	
	
}