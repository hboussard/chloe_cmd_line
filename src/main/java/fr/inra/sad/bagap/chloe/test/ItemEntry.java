package fr.inra.sad.bagap.chloe.test;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelManager;

public class ItemEntry {
	
	public static void main(final String[] argv) {
		
		Map<Pixel, Double> waits = new HashMap<Pixel, Double>();
		PixelWithValueComparator pComparator = new PixelWithValueComparator(waits);
		//Map<Pixel, Double> sortedWaits = new TreeMap<Pixel, Double>(pComparator);
		Set<Pixel> sortedWaits = new TreeSet<Pixel>(pComparator);
		
		/*
		Pixel p1 = PixelManager.get(1, 1);
		Pixel p2 = PixelManager.get(1, 2);
		Pixel p3 = PixelManager.get(1, 3);
		Pixel p4 = PixelManager.get(1, 4);
		Pixel p5 = PixelManager.get(1, 5);
			
		waits.put(p1, 1.0);
		waits.put(p5, 5.0);
		waits.put(p2, 2.0);
		waits.put(p4, 4.0);
		waits.put(p3, 3.0);
		
		sortedWaits.putAll(waits);
		
		System.out.println("avant changement");
		System.out.println();
		System.out.println("non triée");
		for(Entry<Pixel, Double> e : waits.entrySet()){
			System.out.println(e.getValue());
		}
		
		System.out.println();
		System.out.println("triée");
		for(Entry<Pixel, Double> e : sortedWaits.entrySet()){
			System.out.println(e.getValue());
		}
		
		sortedWaits.remove(p5);
		waits.replace(p5, 5.0, 0.0);
		sortedWaits.put(p5, 0.0);
		
		*/
		
		
		waits.put(new Pixel(1, 1), 1.0);
		waits.put(new Pixel(1, 5), 5.0);
		waits.put(new Pixel(1, 2), 1.0);
		waits.put(new Pixel(1, 4), 4.0);
		waits.put(new Pixel(1, 3), 3.0);
		
		sortedWaits.addAll(waits.keySet());
		
		System.out.println("avant changement");
		System.out.println();
		System.out.println("non triée");
		for(Entry<Pixel, Double> e : waits.entrySet()){
			System.out.println(e.getValue());
		}
		
		System.out.println();
		System.out.println("triée");
		for(Pixel p : sortedWaits){
			System.out.println(waits.get(p));
		}
		
		System.out.println();
		System.out.println("changement");
		
		sortedWaits.remove(new Pixel(1, 5));
		waits.replace(new Pixel(1, 5), 5.0, 0.0);
		sortedWaits.add(new Pixel(1, 5));
		
		
		System.out.println();
		System.out.println("après changement");
		System.out.println();
		System.out.println("non triée");
		for(Entry<Pixel, Double> e : waits.entrySet()){
			System.out.println(e.getValue());
		}
		
		System.out.println();
		System.out.println("triée");
		for(Pixel p : sortedWaits){
			System.out.println(waits.get(p));
		}
	}
}
