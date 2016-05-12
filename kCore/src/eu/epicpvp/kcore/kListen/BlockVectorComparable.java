package eu.epicpvp.kcore.kListen;

import java.util.Comparator;

import eu.epicpvp.kcore.MysteryBox.Templates.BlockVector;
import lombok.Getter;
import lombok.Setter;

public class BlockVectorComparable implements Comparable<BlockVectorComparable>{

	@Getter
	@Setter
	private BlockVector vector;
	@Getter
	@Setter
	private double amount;
	
	public BlockVectorComparable(BlockVector vector,double amount){
		this.vector=vector;
		this.amount=amount;
	}
	
	public int compareTo(BlockVectorComparable compare) {
		return (compare.getAmount() == this.getAmount() ? 0 : (compare.getAmount() > this.getAmount() ? -1 : 1));
	}
	
	public static Comparator<BlockVectorComparable> DESCENDING
	    = new Comparator<BlockVectorComparable>() {
	
		public int compare(BlockVectorComparable r1, BlockVectorComparable r2) {
			return (r2.getAmount() == r1.getAmount() ? 0 : (r2.getAmount() > r1.getAmount() ? 1 : -1));
		}
	
	};
	
	public static Comparator<BlockVectorComparable> ASCENDING 
                          = new Comparator<BlockVectorComparable>() {

	    public int compare(BlockVectorComparable r1, BlockVectorComparable r2) {
			return (r2.getAmount() == r1.getAmount() ? 0 : (r2.getAmount() > r1.getAmount() ? -1 : 1));
	    }

	};
}
