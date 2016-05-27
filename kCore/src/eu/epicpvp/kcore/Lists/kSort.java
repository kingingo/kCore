package eu.epicpvp.kcore.Lists;

import java.util.Comparator;

import lombok.Getter;
import lombok.Setter;

public class kSort implements Comparable<kSort>{

	@Getter
	@Setter
	private String name;
	@Getter
	@Setter
	private int obj;
	
	public kSort(String name,int obj){
		this.name=name;
		this.obj=obj;
	}
	
	public int compareTo(kSort compareFruit) {
		int compareQuantity = ((kSort) compareFruit).getObj(); 
		return compareQuantity - this.getObj();
	}
	
	public static Comparator<kSort> DESCENDING
	    = new Comparator<kSort>() {
	
		public int compare(kSort r1, kSort r2) {
			return r2.getObj() - r1.getObj();
		}
	
	};
	
	public static Comparator<kSort> ASCENDING 
                          = new Comparator<kSort>() {

	    public int compare(kSort r1, kSort r2) {
			return r1.getObj() - r2.getObj();
	    }

	};
}
