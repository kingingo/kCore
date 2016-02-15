package me.kingingo.kcore.Kit;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public abstract class Perk implements Listener{

	@Getter
	private String name;
	@Getter
	private PerkData perkData;
	@Getter
	@Setter
	private kPermission permission;
	@Getter
	@Setter
	private ItemStack item;
	
	public Perk(String name){
		this(name,UtilItem.RenameItem(new ItemStack(Material.EMERALD), "§e"+name));
	}
	
	public Perk(String name,ItemStack item){
		this.name=name;
		this.item=item;
	}
	
	public void setPerkData(PerkData perkData){
		this.perkData=perkData;
	}
	
}
