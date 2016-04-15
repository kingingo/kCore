package eu.epicpvp.kcore.TreasureChest.NEW.TreasureItems;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

public class TreasureItem extends ItemStack{

	@Getter
	@Setter
	private int amount;
	@Getter
	@Setter
	private int nenner;
	
	public TreasureItem(ItemStack item,int nenner,int amount){
		super(item);
		this.nenner=nenner;
		this.amount=amount;
	}
	
}
