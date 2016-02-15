package me.kingingo.kcore.kListen;

import java.util.Comparator;

import lombok.Getter;
import lombok.Setter;

public class kRank implements Comparable<kRank>{

	@Getter
	@Setter
	private String player;
	@Getter
	@Setter
	private int obj;
	
	public kRank(String player,int obj){
		this.player=player;
		this.obj=obj;
	}
	
	public int compareTo(kRank compareFruit) {
		int compareQuantity = ((kRank) compareFruit).getObj(); 
		return compareQuantity - this.getObj();
	}
	
	public static Comparator<kRank> DESCENDING
	    = new Comparator<kRank>() {
	
		public int compare(kRank r1, kRank r2) {
			return r2.getObj() - r1.getObj();
		}
	
	};
	
	public static Comparator<kRank> ASCENDING 
                          = new Comparator<kRank>() {

	    public int compare(kRank r1, kRank r2) {
			return r1.getObj() - r2.getObj();
	    }

	};
}
