package eu.epicpvp.kcore.GagdetShop.Gagdet;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

public class Gadget {

	@Getter
	private String name;
	@Getter
	private ItemStack item;

	public Gadget(String name,ItemStack item){
		this.name=name;
		this.item=item;
	}
	
}
