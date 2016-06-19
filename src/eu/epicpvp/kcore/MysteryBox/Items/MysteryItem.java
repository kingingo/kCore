package eu.epicpvp.kcore.MysteryBox.Items;

import org.bukkit.inventory.ItemStack;

import dev.wolveringer.bukkit.permissions.GroupTyp;
import lombok.Getter;

public class MysteryItem extends ItemStack{

	@Getter
	private double chance;
	@Getter
	private int sharps;
	@Getter
	private String permission;
	@Getter
	private GroupTyp groupTyp;
	@Getter
	private String cmd;
	
	public MysteryItem(ItemStack item,int sharps,double chance,String permission,GroupTyp groupTyp,String cmd){
		super(item);
		this.sharps=sharps;
		this.chance=chance;
		this.cmd=cmd;
		this.groupTyp=groupTyp;
		this.permission=permission;
	}
}
