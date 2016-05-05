package eu.epicpvp.kcore.MysteryChest.TreasureItems;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;

public class MysteryItem extends ItemStack{

	@Getter
	private double chance;
	@Getter
	private String cmd;
	
	public MysteryItem(ItemStack item,double chance,String cmd){
		super(item);
		this.chance=chance;
		this.cmd=cmd;
	}
}
