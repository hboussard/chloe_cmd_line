package fr.inra.sad.bagap.chloe.test;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

class Item {
	int value;
	
	Item(int value) {
		this.value = value;
	}
	
	public static void main(final String[] argv) {
		Comparator<Item> comparator = (x, y) -> (x.value < y.value) ? 1 : ((x.value == y.value) ? 0 : -1); // ordre décroissant
		Collection<Item> items = new TreeSet<>(comparator);
		
		items.add(new Item(1));
		items.add(new Item(5));
		items.add(new Item(2));
		items.add(new Item(4));
		items.add(new Item(3));
		
		//items.iterator().next().value = 0;
		Iterator<Item> ite = items.iterator();
		Item it = ite.next();
		ite.remove();
		it.value = 0;
		items.add(it);
		
		items.forEach(item -> System.out.println(item.value)); // 5 4 3 2 1
	}
}