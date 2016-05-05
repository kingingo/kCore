package eu.epicpvp.kcore.MysteryChest.TreasureItems;

import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Permission.Group.GroupTyp;
import lombok.Getter;

public class MysteryItem extends ItemStack{

	@Getter
	private double chance;
	@Getter
	private String permission;
	@Getter
	private GroupTyp groupTyp;
	@Getter
	private String cmd;
	
	public MysteryItem(ItemStack item,double chance,String permission,GroupTyp groupTyp,String cmd){
		super(item);
		this.chance=chance;
		this.cmd=cmd;
		this.groupTyp=groupTyp;
		this.permission=permission;
	}
}
