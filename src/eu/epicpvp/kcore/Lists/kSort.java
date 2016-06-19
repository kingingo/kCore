package eu.epicpvp.kcore.Lists;

import java.util.Comparator;

import lombok.Getter;
import lombok.Setter;

public class kSort<T> implements Comparable<kSort<T>>{

	@Getter
	@Setter
	private T object;
	@Getter
	@Setter
	private int value;
	
	public kSort(T object,int value){
		this.object=object;
		this.value=value;
	}
	
	public int compareTo(kSort<T> compareFruit) {
		int compareQuantity = ((kSort<T>) compareFruit).getValue(); 
		return compareQuantity - this.getValue();
	}
	
	public static Comparator<kSort<?>> DESCENDING
	    = new Comparator<kSort<?>>() {
	
		public int compare(kSort<?> r1, kSort<?> r2) {
			return r2.getValue() - r1.getValue();
		}
	
	};
	
	public static Comparator<kSort<?>> ASCENDING 
                          = new Comparator<kSort<?>>() {

	    public int compare(kSort<?> r1, kSort<?> r2) {
			return r1.getValue() - r2.getValue();
	    }

	};
}
