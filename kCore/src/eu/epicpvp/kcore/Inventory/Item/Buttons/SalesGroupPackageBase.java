package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.Item.Click;
import lombok.Getter;

public class SalesGroupPackageBase extends SalesPackageBase{

	@Getter
	private String group;
	
	public SalesGroupPackageBase(Click click,String group, ItemStack itemStack) {
		super(click, itemStack);
		this.group=group;
	}
}